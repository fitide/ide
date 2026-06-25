package org.ide.PluginController.PluginInterface;

import java.util.List;

public class ExternalConstruction {
    public List<String> keyWords;
    public String name;

    public ExternalConstruction(List<String> keyWords, String name) {
        this.keyWords = keyWords;
        this.name = name;
    }
}
