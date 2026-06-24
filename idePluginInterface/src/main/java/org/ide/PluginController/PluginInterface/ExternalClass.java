package org.ide.PluginController.PluginInterface;

import java.util.List;

public class ExternalClass extends ExternalType {
    public List<ExternalFunc> methods;
    public List<ExternalVar> vars;

    public ExternalClass(String name, List<ExternalFunc> methods, List<ExternalVar> vars) {
        super(name);
        this.methods = methods;
        this.vars = vars;
    }
}
