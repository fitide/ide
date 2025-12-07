package org.ide.PluginController.LangsInfo;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.PluginController.Exceptions.ConfAlreadyExistException;
import org.ide.PluginController.Exceptions.ConfigDoestntExistException;
import org.ide.PluginController.Exceptions.NotInstanceOfPluginException;
import org.ide.PluginController.PluginControllerConfigurator.PluginsLoader;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Tag;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.HashMap;

public class Lang {
    public final String language;
    public final Path pathToDirWithPlugin;
    private final Plugin plugin;
    public final String[] filesExtensions;
    public final HashMap<String, LangConf> confs;

    public Lang(String language, Path pathToDirWithPlugin, String[] filesExtensions)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NotInstanceOfPluginException {
        this.language = language;
        this.pathToDirWithPlugin = pathToDirWithPlugin;
        this.plugin = PluginsLoader.loadPlugin(language, pathToDirWithPlugin);
        this.filesExtensions = filesExtensions;
        confs = new HashMap<>();
    }

    public void addConf(String name, Path pathToCompile, String[] compileArgs) throws ConfAlreadyExistException {
        if (confs.containsKey(name)) throw new ConfAlreadyExistException(name + " conf already exist");

        LangConf conf = new LangConf(name, pathToCompile, compileArgs);
        confs.put(name, conf);
    }

    public String getCompileString(String name) {
        return confs.get(name).getCompileString();
    }

    public void updateParams(String confName, String newName, Path pathToCompile, String[] newArgs) {
        updateParams(confName, newName);
        updateParams(newName, pathToCompile);
        updateParams(newName, newArgs);
    }

    public void updateParams(String confName, String newName, String[] newArgs) {
        updateParams(confName, newName);
        updateParams(newName, newArgs);
    }

    public void updateParams(String confName, String newName, Path pathToCompile) {
        updateParams(confName, newName);
        updateParams(newName, pathToCompile);
    }

    public void updateParams(String confName, String[] newArgs, Path pathToCompile) {
        updateParams(confName, newArgs);
        updateParams(confName, pathToCompile);
    }

    public void updateParams(String confName, Path pathToCompile) {
        LangConf conf = this.confs.get(confName);
        conf.pathToCompile = pathToCompile;
    }

    public void updateParams(String confName, String[] newArgs) {
        LangConf conf = this.confs.get(confName);
        conf.compileArgs = newArgs;
    }

    public void updateParams(String confName, String newName) {
        LangConf conf = this.confs.get(confName);
        confs.remove(confName);
        conf.name = newName;
        confs.put(newName, conf);
    }

    public ParseTree getFileParseTree(File file) {
        return plugin.getFileParseTree(file);
    }

    public Tag[] getTagsOfNode(ParseTree tree) {
        return plugin.getTagsOfNode(tree);
    }

    public void deleteConfig(String confName) throws ConfigDoestntExistException {
        if (!confs.containsKey(confName)) {
            throw new ConfigDoestntExistException(confName + " does not exist");
        }

        confs.remove(confName);
    }
}
