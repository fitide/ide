package org.ide.PluginController;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.PluginController.Exceptions.*;
import org.ide.PluginController.LangsInfo.Lang;
import org.ide.PluginController.LangsInfo.LangConf;
import org.ide.PluginController.PluginControllerConfigurator.PluginsLoader;
import org.ide.PluginController.PluginControllerConfigurator.PluginsSaver;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Tag;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PluginController {
    private final Map<String, Lang> langs;
    private final File IDEConfigDir;

    public PluginController(String IDEDir)
            throws NotInstanceOfPluginException, ConfAlreadyExistException, IOException,
            ParseException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {

        this.IDEConfigDir = new File(IDEDir + File.separator + ".ide" + File.separator + "conf");
        if (!IDEConfigDir.exists()) {
            if (!IDEConfigDir.mkdir()) throw new RuntimeException("Unable to create config");
        }
        langs = new HashMap<>();
        List<Lang> langsList = PluginsLoader.loadLangsFromConfig(Paths.get(IDEConfigDir.getAbsolutePath(), "plugins.json"));
        for (var lang : langsList) {
            langs.put(lang.language, lang);
        }
    }

    public void saveConfig() throws IOException {
        PluginsSaver.saveToConfig(IDEConfigDir.toPath(), "plugins", langs);
    }

    public void addLang(String langStr, Path pathToPluginDir, String[] extensions)
            throws IOException, NotInstanceOfPluginException,
            ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, DirectoryForLangAlreadyExistException, LangDoesntExistException {

        checkLang(langStr);

        Path newLangPath = Paths.get(IDEConfigDir.getAbsolutePath(), langStr);
        if (!newLangPath.toFile().exists())
            throw new DirectoryForLangAlreadyExistException(newLangPath.getFileName().toString() + " already exist");

        copyDir(pathToPluginDir, newLangPath);
        Lang lang = new Lang(langStr, newLangPath, extensions);
    }

    private void copyDir(Path source, Path target) throws IOException {

        File targetDir = new File(target.toUri());
        if (!targetDir.mkdir()) throw new RuntimeException("Unable to create dir");

        for (File children : Objects.requireNonNull(targetDir.listFiles())) {
            Path sourceChildrenPath = Paths.get(source.toString(), children.getName());

            if (children.isDirectory()) {
                copyDir(sourceChildrenPath, children.toPath());
            }
            else if (children.isFile()) {
                copyFile(sourceChildrenPath, children.toPath());
            }
        }

    }

    private void copyFile(Path source, Path target) throws IOException {
        Files.copy(source, target);
    }

    public void addConf(String langStr, String name, String[] args, Path pathToCompiler)
            throws ConfAlreadyExistException, LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        lang.addConf(name, pathToCompiler, args);
    }

    public void updateConfParam(String langStr, String confName, String newName, Path pathToCompile, String[] newArgs)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newName, pathToCompile, newArgs);
    }

    public void updateConfParam(String langStr, String confName, String newName, String[] newArgs)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newName, newArgs);
    }

    public void updateConfParam(String langStr, String confName, String newName, Path pathToCompile)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newName, pathToCompile);
    }

    public void updateConfParam(String langStr, String confName, String[] newArgs, Path pathToCompile)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newArgs, pathToCompile);
    }

    public void updateConfParam(String langStr, String confName, Path pathToCompile)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        lang.updateParams(confName, pathToCompile);
    }

    public void updateConfParam(String langStr, String confName, String[] newArgs)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newArgs);
    }

    public void updateConfParam(String langStr, String confName, String newName)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newName);
    }

    public ParseTree getTree(String langStr, File file)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        return lang.getFileParseTree(file);
    }

    public Tag[] getTags(String langStr, ParseTree tree)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        return lang.getTagsOfNode(tree);
    }

    public String getCompileString(String langStr, String confName)
            throws LangDoesntExistException {
        checkLang(langStr);

        Lang lang = langs.get(langStr);
        return lang.getCompileString(confName);
    }

    public void deleteLan(String lang) throws LangDoesntExistException {
        checkLang(lang);

        langs.remove(lang);
    }

    public void deleteConfig(String lang, String confName)
            throws LangDoesntExistException, ConfigDoestntExistException {
        checkLang(lang);

        langs.get(lang).deleteConfig(confName);
    }

    public Plugin getPluginByExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }

        String finalExtension = extension;
        for (Lang lang : langs.values()) {
            for (String ext : lang.filesExtensions) {
                if (ext.equals(finalExtension)) {
                    return lang.getPlugin();
                }
            }
        }

        return null;
    }

    public String getLangNameByExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }

        for (Lang lang : langs.values()) {
            for (String ext : lang.filesExtensions) {
                if (ext.equals(extension)) {
                    return lang.language;
                }
            }
        }

        return null;
    }

    public Map<String, LangConf> getLangsConfs(String lang) {
        if (!langs.containsKey(lang)) {
            return Map.of();
        }

        return langs.get(lang).confs;
    }

    private void checkLang(String lang) throws LangDoesntExistException {
        if (!langs.containsKey(lang)) throw new LangDoesntExistException(lang + " does not exist");
    }
}
