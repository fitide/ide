package org.ide.LinkTreeController.Tree;

import java.util.UUID;

public class IDgenerator {

    public static String genID() {
        return UUID.randomUUID().toString();
    }
}
