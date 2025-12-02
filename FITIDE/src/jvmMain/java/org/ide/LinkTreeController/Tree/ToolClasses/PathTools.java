package org.ide.LinkTreeController.Tree.ToolClasses;

import java.nio.file.Path;

public class PathTools {

    public static String getRootStr(Path path) {
        return path.getRoot().toString();
    }

    public static Path deleteRoot(Path path) {
        return path.subpath(1, path.getNameCount());
    }

    public static Path deleteLast(Path path) {return path.subpath(0, path.getNameCount() - 1);}

}
