package org.ide.PluginController.PluginControllerConfigurator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PluginsSaver {

    public void saveToConfig(Path pathToConfDir, String confName, Path pathToPlugin) throws IOException {
        File confDir = new File(pathToConfDir.toString());
        if (!confDir.exists()) {
            if (!confDir.mkdir()) {
                throw new RuntimeException("Unable to create conf dir");
            }
        }

        File conf = new File(pathToConfDir.toString() + File.separator + confName);
        if (!conf.exists()) {
            conf.createNewFile();
        }


    }
}
