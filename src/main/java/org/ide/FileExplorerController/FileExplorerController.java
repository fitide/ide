package org.ide.FileExplorerController;

import org.ide.FileExplorerController.Exceptions.*;
import org.ide.FileExplorerController.File_tree.FileTreeBuilder;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.FileExplorerController.Node.FEFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class FileExplorerController {
    private Directory workingDir;

    public FileExplorerController(String pathToDir) {
        FileTreeBuilder builder = new FileTreeBuilder();
        workingDir = builder.getTree(pathToDir);
    }

    public Directory getFileTree() {
        return workingDir;
    }

    public Directory updateTree(String pathToDir) {
        FileTreeBuilder builder = new FileTreeBuilder();
        workingDir = builder.getTree(pathToDir);
        return workingDir;
    }

    public void createFile(Path pathToDir, String name) throws WrongTypeOfNodeException,
            NoNodeFoundException, FileAlreadyExistException, IOException {
        Directory dir = null;
        FEFile newFile;
        try {
            dir = FileTreeBuilder.getDir(workingDir, pathToDir);
            newFile = new FEFile(name);
            dir.addFile(newFile);
            File osNewFile = new File(pathToDir.toString() + File.separator + name);
            if (!osNewFile.createNewFile()) {
                throw new FileAlreadyExistException("FIle already exist with name " + name);
            }

        } catch (IOException e) {
            dir.deleteFile(name);
            throw e;
        }
    }

    public void createDir(Path pathToRootDir, String name) throws NoNodeFoundException,
            DirAlreadyExistException, WrongTypeOfNodeException {
        Directory dir;
        dir = FileTreeBuilder.getDir(workingDir, pathToRootDir);
        Directory newDir = new Directory(name);
        dir.addDir(newDir);
        File osNewDir = new File(pathToRootDir.toString() + File.separator + name);
        if (!osNewDir.mkdir()) {
            throw new DirAlreadyExistException("FIle already exist with name " + name);
        }
    }

    public void deleteFile(Path pathToFile)
            throws NoNodeFoundException, WrongTypeOfNodeException, UnnableToDeleteException {
        File file = new File(pathToFile.toString());
        if (!file.isFile()) {
            throw new WrongTypeOfNodeException("Trying to delete not a file while required file");
        }

        if (!file.delete()) {
            throw new UnnableToDeleteException("");
        }

        Directory dir = FileTreeBuilder.getDir(workingDir, pathToFile.subpath(0, pathToFile.getNameCount() - 1));
        dir.deleteFile(pathToFile.getFileName().toString());
    }


    public void deleteDirectory(Path pathToDir)
            throws NoNodeFoundException, WrongTypeOfNodeException, UnnableToDeleteException {
        File file = new File(pathToDir.toString());
        if (!file.isDirectory()) {
            throw new WrongTypeOfNodeException("Trying to delete not a file while required file");
        }

        deleteFilesRecursively(file);

        Directory dir = FileTreeBuilder.getDir(workingDir, pathToDir.subpath(0, pathToDir.getNameCount() - 1));
        dir.deleteDir(pathToDir.getFileName().toString());
    }

    private void deleteFilesRecursively(File rootDir) throws UnnableToDeleteException {
        for (File children : Objects.requireNonNull(rootDir.listFiles())) {
            if (children.isDirectory()) {
                deleteFilesRecursively(children);
            }
            else {
                if (!children.delete()) {
                    throw new UnnableToDeleteException("Unable to delete recursively");
                }
            }
        }

        if (!rootDir.delete()) {
            throw new UnnableToDeleteException("");
        }
    }

    public void saveChangesToFile(Path pathToFile, String[] strs)
            throws UnnableToWriteInFileException, IOException {
        File file = new File(pathToFile.toString());
        if (!file.canWrite()) {
            throw new UnnableToWriteInFileException("Unable to write in file with name "
                    + pathToFile.getFileName().toString());
        }

        FileWriter writer = new FileWriter(file, false);

        writer.write("");

        for (String str : strs) {
            writer.append(str);
        }

        writer.flush();
        writer.close();
    }

    public String[] openFile(Path pathToFile) throws UnnableToReadFileException, FileNotFoundException {
        File file = new File(pathToFile.toString());
        if (!file.canRead()) {
            throw new UnnableToReadFileException("");
        }
        Scanner scanner = new Scanner(file);
        ArrayList<String> strsList = new ArrayList<>();
        while(scanner.hasNextLine()) {
            strsList.addLast(scanner.nextLine());
        }
        String []strs = new String[strsList.size()];
        for (int i = 0; i < strsList.size(); i++) {
            strs[i] = strsList.get(i);
        }

        return strs;
    }

    public void renameFile(Path pathToFile, String newName) throws NoNodeFoundException, WrongTypeOfNodeException, UnnableToRenameException {
        FEFile fileNode = FileTreeBuilder.getFile(workingDir, pathToFile);
        fileNode.name = newName;

        tryRename(pathToFile, newName);

    }

    public void renameDirectory(Path pathToDir, String newName)
            throws UnnableToRenameException, NoNodeFoundException, WrongTypeOfNodeException {
        FEFile fileNode = FileTreeBuilder.getFile(workingDir, pathToDir);
        fileNode.name = newName;

        tryRename(pathToDir, newName);
    }

    private void tryRename(Path pathToFile, String newName) throws UnnableToRenameException {
        File prevFile = new File(pathToFile.toString());
        File newFile = new File(pathToFile.subpath(0, pathToFile.getNameCount() - 1) + File.separator + newName);
        if (!prevFile.renameTo(newFile)) {
            throw new UnnableToRenameException("");
        }
    }

    public void moveFile(Path pathToFile, Path pathToNewDir)
            throws NoNodeFoundException, UnnableToDeleteException, WrongTypeOfNodeException, IOException, FileAlreadyExistException {

        copyFile(pathToFile, pathToNewDir);

        deleteFile(pathToFile);

    }

    public void copyFile(Path pathToFile, Path pathToNewDir) throws IOException, FileAlreadyExistException,
            NoNodeFoundException, WrongTypeOfNodeException {
        File sourceFile = new File(pathToFile.toString());
        if (!sourceFile.isFile()) {
            throw new WrongTypeOfNodeException("Wrong type to copy; Required File");
        }

        File targetFile = new File(pathToNewDir.toString() + File.separator + pathToFile.getFileName().toString());
        if (targetFile.exists()) {
            Files.copy(sourceFile.toPath(), targetFile.toPath());

        }
        else {
            createFile(pathToNewDir, targetFile.getName());
        }

    }

    public void moveDir(Path pathToDir, Path pathToNewDir)
            throws NoNodeFoundException, UnnableToDeleteException, WrongTypeOfNodeException,
            FileAlreadyExistException, DirAlreadyExistException, IOException, CopyDirIntoitsInsideException {

        _copyDir(pathToDir, pathToNewDir);

        deleteDirectory(pathToDir);
    }

    public void copyDir(Path pathToDir, Path pathToNewDir)
            throws WrongTypeOfNodeException, FileAlreadyExistException, NoNodeFoundException,
            DirAlreadyExistException, IOException, CopyDirIntoitsInsideException {

        if (pathToNewDir.startsWith(pathToDir)) {
            throw new CopyDirIntoitsInsideException("");
        }

        _copyDir(pathToDir, pathToNewDir);
    }

    private void _copyDir(Path pathToDir, Path pathToNewDir)
            throws WrongTypeOfNodeException, FileAlreadyExistException, NoNodeFoundException,
            DirAlreadyExistException, IOException {
        File dir = new File(pathToDir.toString());
        if (!dir.isDirectory()) {
            throw new WrongTypeOfNodeException("Wrong type to copy; Required Directory");
        }

        copyRecursively(dir, pathToNewDir);
    }

    private void copyRecursively(File dir, Path path)
            throws FileAlreadyExistException, NoNodeFoundException, IOException, WrongTypeOfNodeException, DirAlreadyExistException {

        createDir(path, dir.getName());

        Path newPath = Paths.get(path.toString(), dir.getName());

        for (File children : Objects.requireNonNull(dir.listFiles())) {
            if (children.isDirectory()) {
                copyRecursively(children, newPath);
            }
            else if (children.isFile()) {
                copyFile(Paths.get(children.getAbsolutePath()), newPath);
            }
        }

    }
}
