package org.ide.LinkTreeController.Tree.ToolClasses;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTools {

    public static String getRootStr(Path path) {
        if (path.getNameCount() > 1) return path.getRoot().toString();
        else return path.getFileName().toString();
    }

    public static Path deleteRoot(Path path) {
        if (path.getNameCount() > 1)
            return path.subpath(1, path.getNameCount());
        else return Paths.get("");
    }

    public static Path deleteLast(Path path) {return path.subpath(0, path.getNameCount() - 1);}

}
