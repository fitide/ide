package org.ide.PluginController.PluginControllerConfigurator;

import org.ide.PluginController.Exceptions.ConfAlreadyExistException;
import org.ide.PluginController.Exceptions.NotInstanceOfPluginException;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.LangsInfo.Lang;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginsLoader {

    public static Plugin loadPlugin(String name, Path pathToDirWithPlugin)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException, NotInstanceOfPluginException, IOException {
        File pluginDir = pathToDirWithPlugin.toFile();
        if (!pluginDir.exists() || !pluginDir.isDirectory()) {
            throw new FileNotFoundException("Plugin directory not found: " + pathToDirWithPlugin);
        }

        File[] jarFiles = pluginDir.listFiles((dir, fileName) -> fileName.endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            throw new FileNotFoundException("No JAR file found in plugin directory: " + pathToDirWithPlugin);
        }

        File jarFile = jarFiles[0];

        URL[] urls = { jarFile.toURI().toURL() };
        URLClassLoader classLoader = new URLClassLoader(urls, Plugin.class.getClassLoader());

        Plugin plugin = findPluginClass(jarFile, classLoader);

        if (plugin == null) {
            throw new NotInstanceOfPluginException(
                    "No class implementing Plugin interface found in " + jarFile.getName()
            );
        }

        return plugin;
    }

    private static Plugin findPluginClass(File jarFile, URLClassLoader classLoader)
            throws IOException, ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {

        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.endsWith(".class") && !entryName.contains("$")) {
                    String className = entryName
                            .replace('/', '.')
                            .replace(".class", "");

                    try {
                        Class<?> clazz = classLoader.loadClass(className);

                        if (Plugin.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                            Object instance = clazz.getDeclaredConstructor().newInstance();
                            return (Plugin) instance;
                        }
                    } catch (ClassNotFoundException | NoClassDefFoundError e) {
                        continue;
                    }
                }
            }
        }

        return null;
    }

    public static List<Lang> loadLangsFromConfig(Path pathToConfig)
            throws IOException, ParseException, NotInstanceOfPluginException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, ConfAlreadyExistException {

        File conf = new File(pathToConfig.toString());
        if (!conf.exists()) throw new FileNotFoundException(pathToConfig + " not found");

        return setLangs(conf, pathToConfig);
    }

    private static List<Lang> setLangs(File conf, Path pathToConfig)
            throws IOException, ParseException, NotInstanceOfPluginException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, ConfAlreadyExistException {

        List<Lang> list  = new ArrayList<>();

        Object parsedObj = new JSONParser().parse(new FileReader(conf));
        JSONObject jsonObject = (JSONObject) parsedObj;
        JSONObject langsJson = (JSONObject) jsonObject.get("language");

        Set langs = langsJson.keySet();
        for (var langObject : langs) {
            String lang = (String) langObject;
            JSONObject langObj = (JSONObject) langsJson.get(lang);

            setLang(list, pathToConfig, lang, langObj);
        }

        return list;
    }

    private static void setLang(List list, Path pathToConfig, String lang, JSONObject langObj)
            throws NotInstanceOfPluginException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ConfAlreadyExistException, IOException {

        Lang langNode = setLangParams(pathToConfig, lang, langObj);

        list.add(langNode);
    }

    private static Lang setLangParams(Path pathToConfig, String lang, JSONObject langObj)
            throws NotInstanceOfPluginException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ConfAlreadyExistException, IOException {


        String[] extensions = setExtensions(langObj);
        Path confDir = pathToConfig.getParent();
        Path pathToPluginDir = confDir.resolve(lang);

        Lang langNode = new Lang(lang, pathToPluginDir, extensions);


        JSONArray confs = (JSONArray) langObj.get("configs");
        setConfigs(langNode, confs);

        return langNode;
    }

    private static String[] setExtensions(JSONObject langObj) {
        JSONArray extensionsJson = (JSONArray) langObj.get("extensions");
        String[] extensions = new String[extensionsJson.size()];
        for(int i = 0; i < extensionsJson.size(); i++) {
            Object argObj = extensionsJson.get(i);
            String arg = (String) argObj;
            extensions[i] = arg;
        }
        return extensions;
    }

    private static void setConfigs(Lang langNode, JSONArray confs) throws ConfAlreadyExistException {
        for(var configObj : confs) {
            JSONObject config = (JSONObject) configObj;
            String name = (String) config.get("name");
            String pathToCompiler = (String) config.get("path_to_compiler");
            JSONArray argsJson = (JSONArray) config.get("args");
            String[] args = new String[argsJson.size()];
            for (int i = 0; i < argsJson.size(); i++) {
                args[i] = (String) argsJson.get(i);
            }
            langNode.addConf(name, Paths.get(pathToCompiler), args);
        }
    }
}
