package org.ide.PluginController.PluginInterface;

public class ExternalVar {
    public ExternalType Type;
    public String Name;

    public ExternalVar(ExternalType type, String name) {
        this.Type = type;
        this.Name = name;
    }
}
