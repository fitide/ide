package org.ide.File_explorer.File_tree;

import org.ide.File_explorer.Exceptions.DirAlreadyExistException;
import org.ide.File_explorer.Exceptions.FileAlreadyExistException;
import org.ide.File_explorer.Node.Directory;
import org.ide.File_explorer.Node.FEFile;

import java.io.File;
import java.util.Objects;

public class FileTreeBuilder {

    public Directory getTree(String pathToWorkDir) {
        try {
            File curDir = new File(pathToWorkDir);
            return getTree(curDir);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Directory getTree(File curDir) {
        if (!curDir.isDirectory()) {
            throw new RuntimeException("tree on file");
        }

        Directory dir = new Directory(curDir.getName(), curDir.getPath());

        try {
            for (File inFile : Objects.requireNonNull(curDir.listFiles())) {
                if (inFile.isFile()) {
                    FEFile file = new FEFile(inFile.getName(), inFile.getPath());
                    dir.addFile(file);
                }
                else if (inFile.isDirectory()) {
                    Directory inDir = getTree(inFile);
                    dir.addDir(inDir);
                }
            }
        } catch (FileAlreadyExistException | NullPointerException | DirAlreadyExistException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return dir;
    }
}
