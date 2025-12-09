package org.ide.PluginController.PluginInterface;

import java.util.List;

public class ExternalFunc {
    public ExternalType type;
    public String name;
    public List<ExternalVar> args;

    public ExternalFunc(ExternalType type, String name, List<ExternalVar> args) {
        this.type = type;
        this.name = name;
        this.args = args;
    }
}
