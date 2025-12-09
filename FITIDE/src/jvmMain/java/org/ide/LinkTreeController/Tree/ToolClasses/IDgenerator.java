package org.ide.LinkTreeController.Tree.ToolClasses;

import java.util.UUID;

public class IDgenerator {

    public static String genID() {
        return UUID.randomUUID().toString();
    }
}
