package org.ide.FileExplorerController.Node;

import org.ide.FileExplorerController.Exceptions.DirAlreadyExistException;
import org.ide.FileExplorerController.Exceptions.FileAlreadyExistException;
import org.ide.Tools.PathTools;


import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Directory extends Node {
    private final List<Directory> dirs = new ArrayList<>();
    final private List<FEFile> files = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    public void addFile(FEFile file) throws FileAlreadyExistException {
        if (file == null) throw new NullPointerException("FEFile add null pointer");

        for (FEFile inFile : files) {
            if (Objects.equals(inFile.name, file.name)) {
                throw new FileAlreadyExistException("File already exist: " + file.name);
            }
        }

        files.add(file);
    }

    public void addDir(Directory dir) throws DirAlreadyExistException {
        if (dir == null) throw new NullPointerException("Directory add null pointer");

        for (Directory inDir : dirs) {
            if (Objects.equals(inDir.name, dir.name)) {
                throw new DirAlreadyExistException("File already exist: " + dir.name);
            }
        }

        dirs.add(dir);
    }

    public int getDirsCnt() {
        return dirs.size();
    }

    public int getFilesCnt() {
        return files.size();
    }

    public Directory getDir(int it) {
        if (it >= dirs.size()) {
            throw new RuntimeException("Index out of range for dirs");
        }

        return dirs.get(it);
    }

    public FEFile getFile(int it) {
        if (it >= files.size()) {
            throw new RuntimeException("Index out of range for Files");
        }

        return files.get(it);
    }

    public void deleteFile(String name) {
        for (int i = 0; i < files.size(); i++) {
            if(files.get(i).name.equals(name)) {
                files.remove(i);
                return;
            }
        }
    }

    public void deleteDir(String name) {
        for (int i = 0; i < dirs.size(); i++) {
            if(dirs.get(i).name.equals(name)) {
                dirs.remove(i);
                return;
            }
        }
    }


    @Override
    protected String toString(int level) {
        StringBuilder builder = new StringBuilder();
        builder.repeat("  ", level).append(this.name).append(":\n");
        for (var dir : dirs) {
            builder.append(dir.toString(level + 1));
        }
        for (var file : files) {
            builder.append(file.toString(level + 1));
        }
        return builder.toString();
    }

    public Directory findDir(String relativePath) throws FileNotFoundException {
        return findDir(Paths.get(relativePath));
    }

    public Directory findDir(Path relativePath) throws FileNotFoundException {
        if ((relativePath.getRoot().toString()).equals(this.name) && relativePath.getNameCount() == 1) return this;
        var newPath = PathTools.deleteRoot(relativePath);
        if (newPath.getNameCount() == 0) throw new FileNotFoundException();

        for (int i = 0; i < this.getDirsCnt(); i++) {
            var dir = getDir(i);
            if (dir.name.equals(PathTools.getRootStr(newPath))) return dir.findDir(relativePath);
        }

        throw new FileNotFoundException();
    }

    public FEFile findFile(String relativePath) throws FileNotFoundException {
        return findFile(Paths.get(relativePath));
    }

    public FEFile findFile(Path relativePath) throws FileNotFoundException {
        var dir = findDir(PathTools.deleteLast(relativePath));

        for (int i = 0; i < dir.getFilesCnt(); i++) {
            var file = dir.getFile(i);
            if (file.name.equals(PathTools.getRootStr(relativePath))) return file;
        }

        throw new FileNotFoundException();
    }
}
