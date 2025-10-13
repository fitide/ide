package org.ide.File_explorer.Node;

import org.ide.File_explorer.Exceptions.DirAlreadyExistException;
import org.ide.File_explorer.Exceptions.FileAlreadyExistException;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Directory extends Node {
    private final Set<Directory> dirs = new HashSet<>();
    final private Set<FEFile> files = new HashSet<>();

    public Directory(String name, String path) {
        super(name, path);
    }

    public void addFile(FEFile file) throws FileAlreadyExistException {
        if (file == null) throw new NullPointerException("FEFile add null pointer");

        for (FEFile inFile : files) {
            if (Objects.equals(inFile.name, file.name)) {
                throw new FileAlreadyExistException("File already exist: " + file.path + File.separator + file.name);
            }
        }

        files.add(file);
    }

    public void addDir(Directory dir) throws DirAlreadyExistException {
        if (dir == null) throw new NullPointerException("Directory add null pointer");

        for (Directory inDir : dirs) {
            if (Objects.equals(inDir.name, dir.name)) {
                throw new DirAlreadyExistException("File already exist: " + dir.path + File.separator + dir.name);
            }
        }

        dirs.add(dir);
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
}
