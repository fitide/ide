package org.ide.PluginController;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.PluginController.Exceptions.ConfAlreadyExistException;
import org.ide.PluginController.Exceptions.DirectoryForLangAlreadyExistException;
import org.ide.PluginController.Exceptions.LangALreadyAddedException;
import org.ide.PluginController.Exceptions.NotInstanceOfPluginException;
import org.ide.PluginController.LangsInfo.Lang;
import org.ide.PluginController.PluginControllerConfigurator.PluginsLoader;
import org.ide.PluginController.PluginControllerConfigurator.PluginsSaver;
import org.ide.PluginController.PluginInterface.Tag;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PluginController {
    private final Map<String, Lang> langs;
    private final File IDEConfigDir;

    public PluginController(String IDEDir)
            throws NotInstanceOfPluginException, ConfAlreadyExistException, IOException,
            ParseException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {

        this.IDEConfigDir = new File(IDEDir + File.separator + "conf");
        if (!IDEConfigDir.exists()) {
            if (IDEConfigDir.mkdir()) throw new RuntimeException("Unable to create config");
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
            throws LangALreadyAddedException, IOException, NotInstanceOfPluginException,
            ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, DirectoryForLangAlreadyExistException {

        if (langs.containsKey(langStr)) {
            throw new LangALreadyAddedException(langStr + " already added");
        }

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
            throws ConfAlreadyExistException {

        Lang lang = langs.get(langStr);
        lang.addConf(name, pathToCompiler, args);
    }

    public void updateConfParam(String langStr, String confName, String newName, Path pathToCompile, String[] newArgs) {
        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newName, pathToCompile, newArgs);
    }

    public void updateConfParam(String langStr, String confName, String newName, String[] newArgs) {
        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newName, newArgs);
    }

    public void updateConfParam(String langStr, String confName, String newName, Path pathToCompile) {
        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newName, pathToCompile);
    }

    public void updateConfParam(String langStr, String confName, String[] newArgs, Path pathToCompile) {
        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newArgs, pathToCompile);
    }

    public void updateConfParam(String langStr, String confName, Path pathToCompile) {
        Lang lang = langs.get(langStr);
        lang.updateParams(confName, pathToCompile);
    }

    public void updateConfParam(String langStr, String confName, String[] newArgs) {
        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newArgs);
    }

    public void updateConfParam(String langStr, String confName, String newName) {
        Lang lang = langs.get(langStr);
        lang.updateParams(confName, newName);
    }

    public ParseTree getTree(String langStr, File file) {
        Lang lang = langs.get(langStr);
        return lang.getFileParseTree(file);
    }

    public Tag[] getTags(String langStr, ParseTree tree) {
        Lang lang = langs.get(langStr);
        return lang.getTagsOfNode(tree);
    }

    public String getCompileString(String langStr, String confName) {
        Lang lang = langs.get(langStr);
        return lang.getCompileString(confName);
    }
}
