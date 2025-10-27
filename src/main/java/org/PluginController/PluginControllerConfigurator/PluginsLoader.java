package org.PluginController.PluginControllerConfigurator;

import org.PluginController.Exceptions.NotInstanceOfPluginException;
import org.PluginController.PluginInterface.Plugin;
import org.PluginController.PluginNode.PluginNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;

public class PluginsLoader {

    public static Plugin loadPlugin(String name, Path pathToDirWithPlugin) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NotInstanceOfPluginException {
        ClassLoader classLoader = PluginNode.class.getClassLoader();
        Class<?> clazz = classLoader.loadClass(pathToDirWithPlugin.toString() + File.separator + name);
        Object plobj = clazz.getDeclaredConstructor().newInstance();
        if (plobj instanceof Plugin) {
            return (Plugin) plobj;
        } else {
            throw new NotInstanceOfPluginException("File " + pathToDirWithPlugin.toString() + File.separator + name + " is not a Plugin implementaion");
        }
    }

    public static List<PluginNode> loadPluginsFromConfig(Path pathToConfig) throws FileNotFoundException {
        File conf = new File(pathToConfig.toString());
        if (!conf.exists()) throw new FileNotFoundException(pathToConfig.toString() + " not found");


    }
}
