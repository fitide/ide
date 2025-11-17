package org.ide.LinkTreeController.Tree.ToolClasses;

import java.nio.file.Path;

public class Tools {

    public static String getRootStr(Path path) {
        return path.getRoot().toString();
    }

    public static Path deleteRoot(Path path) {
        return path.subpath(1, path.getNameCount());
    }

}
