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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PluginsLoader {

    public static Plugin loadPlugin(String name, Path pathToDirWithPlugin) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NotInstanceOfPluginException {
        ClassLoader classLoader = Lang.class.getClassLoader();
        Class<?> clazz = classLoader.loadClass(pathToDirWithPlugin.toString() + File.separator + name);
        Object plobj = clazz.getDeclaredConstructor().newInstance();
        if (plobj instanceof Plugin) {
            return (Plugin) plobj;
        } else {
            throw new NotInstanceOfPluginException("File " + pathToDirWithPlugin + File.separator + name + " is not a Plugin implementaion");
        }
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
            NoSuchMethodException, InstantiationException, IllegalAccessException, ConfAlreadyExistException {

        Lang langNode = setLangParams(pathToConfig, lang, langObj);

        list.add(langNode);
    }

    private static Lang setLangParams(Path pathToConfig, String lang, JSONObject langObj)
            throws NotInstanceOfPluginException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ConfAlreadyExistException {


        String[] extensions = setExtensions(langObj);
        Path pathToPluginDir = Paths.get(pathToConfig.getParent().toString() + File.separator + lang);

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
