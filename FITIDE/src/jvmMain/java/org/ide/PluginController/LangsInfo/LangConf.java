package org.ide.PluginController.LangsInfo;

import java.nio.file.Path;

public class LangConf {
    public String name;
    public Path pathToCompile;
    public String[] compileArgs;

    LangConf(String name, Path pathToCompile, String[] compileArgs) {
        this.name = name;
        this.pathToCompile = pathToCompile;
        this.compileArgs = compileArgs;
    }

    String getCompileString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.pathToCompile.toString()).append(" ");
        for (String arg : this.compileArgs) {
            builder.append(arg);
        }
        return builder.toString();
    }
}
