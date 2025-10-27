package org.PluginController.PluginNode;

import org.PluginController.Exceptions.NotInstanceOfPluginException;
import org.PluginController.PluginControllerConfigurator.PluginsLoader;
import org.PluginController.PluginInterface.Plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PluginNode {
    final String name;
    final Path pathToDirWithPlugin;
    final Plugin plugin;
    final String[] fileExtensions;
    final String pathToCompliler;
    final String[] compilerArgs;


    PluginNode(String name, Path pathToIDE,  String[] fileExtensions, String pathToCompliler, String[] compilerArgs)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NotInstanceOfPluginException {
        this.name = name;
        this.pathToDirWithPlugin = Paths.get(pathToIDE.toString(), "conf", "plugins", name);
        this.plugin = PluginsLoader.loadPlugin(name, pathToDirWithPlugin);
        this.fileExtensions = fileExtensions;
        this.pathToCompliler = pathToCompliler;
        this.compilerArgs = compilerArgs;

    }
}
