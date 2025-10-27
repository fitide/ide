package org.PluginController;

import org.PluginController.PluginNode.PluginNode;

import java.nio.file.Path;
import java.util.ArrayList;

public class PluginCntroller {
    final ArrayList<PluginNode> plugins;
    final String IDEDir;

    public PluginCntroller(String IDEDir) {
        plugins = new ArrayList<>();
        this.IDEDir = IDEDir;
    }

    public void addPlugin(String name, Path pathToDirWithPlugin, String pathToCompiler,
                          String[] compilerArgs, String[] fileExtensions) {


    }

}
