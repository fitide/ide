package org.ide.FileExplorerController.File_tree;

import org.ide.FileExplorerController.Exceptions.DirAlreadyExistException;
import org.ide.FileExplorerController.Exceptions.FileAlreadyExistException;
import org.ide.FileExplorerController.Exceptions.NoNodeFoundException;
import org.ide.FileExplorerController.Exceptions.WrongTypeOfNodeException;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.FileExplorerController.Node.FEFile;
import org.ide.FileExplorerController.Node.Node;

import java.io.File;
import java.nio.file.Path;
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

        Directory dir = new Directory(curDir.getName());

        try {
            for (File inFile : Objects.requireNonNull(curDir.listFiles())) {
                if (inFile.isFile()) {
                    FEFile file = new FEFile(inFile.getName());
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

    static public Directory getDir(Directory tree, Path path) throws WrongTypeOfNodeException, NoNodeFoundException {
        Node node = null;
        try {
            node = getNode(tree, path);

            if (node instanceof Directory) {
                return (Directory) node;
            }
            else {
                throw new WrongTypeOfNodeException("Node has instance of File while required Directory");
            }
        } catch (NoNodeFoundException e) {
            throw new NoNodeFoundException(e.getMessage());
        }
    }

    static public FEFile getFile(Directory tree, Path path) throws NoNodeFoundException, WrongTypeOfNodeException {
        Node node = null;
        try {
            node = getNode(tree, path);

            if (node instanceof FEFile) {
                return (FEFile) node;
            }
            else {
                throw new WrongTypeOfNodeException("Node has instance of Directory while required File");
            }
        } catch (NoNodeFoundException e) {
            throw new NoNodeFoundException(e.getMessage());
        }
    }

    static private Node getNode(Node node, Path path) throws NoNodeFoundException {
        for (int i = 0; i < path.getNameCount(); i++) {
            String curName = path.getName(i).toString();
            if (node.name.equals(curName)) {
                if (i == path.getNameCount() - 1) {
                    return node;
                }
                else {
                    if (node instanceof Directory) {
                        Directory dir = (Directory) node;
                        Node ans = searchInDir(dir, path, i);
                        if (ans != null) return ans;
                    }
                }
            }
        }
        throw new NoNodeFoundException("No node Found with fileName" + path.getFileName().toString());
    }

    static private Node searchInDir(Directory dir, Path path, int it) {
        for (int j = 0; j < dir.getFilesCnt(); j++) {
            Node found = getNode(dir.getFile(j), path, it + 1);
            if (found != null) return found;
        }
        for (int j = 0; j < dir.getDirsCnt(); j++) {
            Node found = getNode(dir.getDir(j), path, it + 1);
            if (found != null) return found;
        }
        return null;
    }


    static private Node getNode(Node node, Path path, int it) {
        if (node.name.equals(path.getName(it).toString())) {
            if (it == path.getNameCount() - 1) {
                return node;
            }

            if (node instanceof FEFile) {
                return null;
            }

            Directory dir = (Directory) node;
            return searchInDir(dir, path, it);
        }

        return null;
    }
}
