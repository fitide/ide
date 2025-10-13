package org.ide.File_explorer;

import org.ide.File_explorer.FELogger.FELogger;
import org.ide.File_explorer.File_tree.FileTreeBuilder;
import org.ide.File_explorer.Node.Directory;

public class FileExplorer {
    public Directory workingDir;
    private FELogger logger;

    public FileExplorer(String pathToDir, FELogger logger) {
        FileTreeBuilder builder = new FileTreeBuilder();
        workingDir = builder.getTree(pathToDir);
        this.logger = logger;
        this.logger.print(workingDir.toString());
    }

    public Directory getFileTree() {
        return workingDir;
    }
}
