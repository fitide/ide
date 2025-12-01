package org.ide.FileExplorerController;

import org.apache.logging.log4j.Logger;
import org.ide.FileExplorerController.Exceptions.*;
import org.ide.FileExplorerController.File_tree.FileTreeBuilder;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.FileExplorerController.Node.FEFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.logging.log4j.LogManager;

public class FileExplorerController {
    private Directory workingDir;
    private final Logger logger;
    private final File recycleBin;
    private int binIt;

    public FileExplorerController(String pathToDir) throws UnnableToCreateFileException {
        this(pathToDir, LogManager.getLogger(FileExplorerController.class));
    }

    public FileExplorerController(String pathToDir, Logger logger) throws UnnableToCreateFileException {
        FileTreeBuilder builder = new FileTreeBuilder();
        workingDir = builder.getTree(pathToDir);
        this.logger = logger;
        if (logger != null) logger.debug("File tree:\n" + workingDir.toString());


        File recycleBin = new File(pathToDir + File.separator + ".ide" + File.separator + "recycle_bin");
        if (!recycleBin.exists()) {
            if (!recycleBin.mkdirs()) {
                throw new UnnableToCreateFileException("Unable to create file in conf");
            }
        }
        this.recycleBin = recycleBin;

        for (File bin : Objects.requireNonNull(recycleBin.listFiles())) {
            try {
                int binIt = Integer.parseInt(bin.getName());
                this.binIt = Math.max(binIt + 1, this.binIt);
            } catch (NumberFormatException ignored) {
            }

        }
    }

    Directory getFileTree() {
        return workingDir;
    }

    public Directory updateTree(String pathToDir) {
        if (logger != null) logger.info("update tree request");
        FileTreeBuilder builder = new FileTreeBuilder();
        workingDir = builder.getTree(pathToDir);
        if (logger != null) logger.debug("Tree updated:\n" + workingDir.toString());
        return workingDir;
    }

    public Directory getTreeCopy() {
        if (logger != null) logger.info("Tree copy request");

        if (workingDir == null) return workingDir;

        Directory cdir = new Directory(workingDir.name);
        if (logger != null) logger.debug("TreeDir copied = " + cdir.name + "\n");
        copyTree(workingDir, cdir);
        return cdir;
    }

    private void copyTree(Directory source, Directory target) {

        try {
            for (int i = 0; i < source.getFilesCnt(); i++) {
                target.addFile(new FEFile(source.getFile(i).name));
                if (logger != null) logger.debug("TreeFile copied = " + target.name);
            }
            for (int i = 0; i < source.getDirsCnt(); i++) {
                Directory ccdir = new Directory(source.getDir(i).name);
                target.addDir(ccdir);
                if (logger != null) logger.debug("TreeDir copied = " + ccdir.name);
                copyTree(source.getDir(i), ccdir);

            }

        } catch (FileAlreadyExistException | DirAlreadyExistException e) {
            throw new RuntimeException(e);
        }

    }

    public void createFile(Path pathToDir, String name) throws WrongTypeOfNodeException,
            NoNodeFoundException, FileAlreadyExistException, IOException {
        if (logger != null) logger.info("Create File request");
        if (logger != null) logger.debug("Path to dir = " + pathToDir.toString());
        if (logger != null) logger.debug("File name = " + name);

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
        if (logger != null) logger.info("Create dir request");
        if (logger != null) logger.debug("Path for root dir: " + pathToRootDir.toString());
        if (logger != null) logger.debug("New name = " + name);

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
        if (logger != null) logger.info("Delete file request");
        if (logger != null) logger.debug("Path to file for deleting: " + pathToFile.toString());

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
        if (logger != null) logger.info("Delete directory request");
        if (logger != null) logger.debug("Path to dir for deleting: " + pathToDir.toString());

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
        if (logger != null) logger.debug("Deleted dir: " + rootDir.getName());
    }

    public void saveChangesToFile(Path pathToFile, List<String> strs)
            throws UnnableToWriteInFileException, IOException {
        if (logger != null) logger.info("Save changes to file request");
        if (logger != null) logger.debug("Path to file: " + pathToFile.toString());
        if (logger != null) logger.debug("Cnt strings for saving: " + strs.size());

        File file = new File(pathToFile.toString());
        if (!file.canWrite()) {
            throw new UnnableToWriteInFileException("Unable to write in file with name "
                    + pathToFile.getFileName().toString());
        }

        FileWriter writer = new FileWriter(file, false);

        writer.write("");

        for (String str : strs) {
            writer.append(str).append("\n");
        }

        writer.flush();
        writer.close();
    }

    public List<String> openFile(Path pathToFile) throws UnnableToReadFileException, FileNotFoundException {
        if (logger != null) logger.info("Open file request");
        if (logger != null) logger.debug("Path to file: " + pathToFile.toString());

        File file = new File(pathToFile.toString());
        if (!file.canRead()) {
            throw new UnnableToReadFileException("");
        }
        Scanner scanner = new Scanner(file);
        ArrayList<String> strsList = new ArrayList<>();
        while(scanner.hasNextLine()) {
            strsList.addLast(scanner.nextLine());
        }
        return strsList;
    }

    public void renameFile(Path pathToFile, String newName) throws NoNodeFoundException, WrongTypeOfNodeException, UnnableToRenameException {
        if (logger != null) logger.info("Rename file request");
        if (logger != null) logger.debug("Path to file: " + pathToFile.toString());
        if (logger != null) logger.debug("New name: " + newName);

        tryRename(pathToFile, newName);

        FEFile fileNode = FileTreeBuilder.getFile(workingDir, pathToFile);
        fileNode.name = newName;

    }

    public void renameDirectory(Path pathToDir, String newName)
            throws UnnableToRenameException, NoNodeFoundException, WrongTypeOfNodeException {
        if (logger != null) logger.info("Rename dir request");
        if (logger != null) logger.debug("Path to file: " + pathToDir.toString());
        if (logger != null) logger.debug("New name: " + newName);

        tryRename(pathToDir, newName);

        Directory directory = FileTreeBuilder.getDir(workingDir, pathToDir);
        directory.name = newName;

    }

    private void tryRename(Path pathToFile, String newName) throws UnnableToRenameException {
        File prevFile = new File(pathToFile.toString());
        File newFile = new File(pathToFile.subpath(0, pathToFile.getNameCount() - 1) + File.separator + newName);
        if (!prevFile.renameTo(newFile)) {
            throw new UnnableToRenameException("");
        }
    }

    public void moveFile(Path pathToFile, Path pathToNewDir)
            throws NoNodeFoundException, UnnableToDeleteException, WrongTypeOfNodeException,
            IOException, FileAlreadyExistException, UnnableToWriteInFileException, UnnableToReadFileException {
        if (logger != null) logger.info("Move file request");
        if (logger != null) logger.debug("Path to file: " + pathToFile.toString());
        if (logger != null) logger.debug("Path to new dir: " + pathToNewDir.toString());

        copyFile(pathToFile, pathToNewDir);

        deleteFile(pathToFile);
    }

    public void copyFile(Path pathToFile, Path pathToNewDir) throws IOException, FileAlreadyExistException,
            NoNodeFoundException, WrongTypeOfNodeException, UnnableToReadFileException, UnnableToWriteInFileException {
        if (logger != null) logger.info("Move dir request");
        if (logger != null) logger.debug("Path to file: " + pathToFile.toString());
        if (logger != null) logger.debug("Path to dir where copy: " + pathToNewDir.toString());

        File sourceFile = new File(pathToFile.toString());
        if (!sourceFile.isFile()) {
            throw new WrongTypeOfNodeException("Wrong type to copy; Required File");
        }

        File targetFile = new File(pathToNewDir.toString() + File.separator + pathToFile.getFileName().toString());
        if (!targetFile.exists()) {
            createFile(pathToNewDir, targetFile.getName());
        }

        saveChangesToFile(targetFile.toPath(), openFile(pathToFile));
    }

    public void moveDir(Path pathToDir, Path pathToNewDir)
            throws NoNodeFoundException, UnnableToDeleteException, WrongTypeOfNodeException,
            FileAlreadyExistException, DirAlreadyExistException, IOException, CopyDirIntoitsInsideException, UnnableToWriteInFileException, UnnableToReadFileException {

        if (logger != null) logger.info("Move dir request");
        if (logger != null) logger.debug("Path to dir to move: " + pathToDir.toString());
        if (logger != null) logger.debug("Path to dir where move: " + pathToNewDir.toString());

        _copyDir(pathToDir, pathToNewDir);

        deleteDirectory(pathToDir);
    }

    public void copyDir(Path pathToDir, Path pathToNewDir)
            throws WrongTypeOfNodeException, FileAlreadyExistException, NoNodeFoundException,
            DirAlreadyExistException, IOException, CopyDirIntoitsInsideException, UnnableToWriteInFileException, UnnableToReadFileException {
        if (logger != null) logger.info("Copy dir request");
        if (logger != null) logger.debug("Path to dir to copy: " + pathToDir.toString());
        if (logger != null) logger.debug("Path to dir where copy: " + pathToNewDir.toString());

        if (pathToNewDir.startsWith(pathToDir)) {
            throw new CopyDirIntoitsInsideException("");
        }

        _copyDir(pathToDir, pathToNewDir);
    }

    private void _copyDir(Path pathToDir, Path pathToNewDir)
            throws WrongTypeOfNodeException, FileAlreadyExistException, NoNodeFoundException,
            DirAlreadyExistException, IOException, UnnableToWriteInFileException, UnnableToReadFileException {
        File dir = new File(pathToDir.toString());
        if (!dir.isDirectory()) {
            throw new WrongTypeOfNodeException("Wrong type to copy; Required Directory");
        }

        copyRecursively(dir, pathToNewDir);
    }

    private void copyRecursively(File dir, Path path)
            throws FileAlreadyExistException, NoNodeFoundException, IOException, WrongTypeOfNodeException, DirAlreadyExistException, UnnableToWriteInFileException, UnnableToReadFileException {

        createDir(path, dir.getName());
        Path newPath = Paths.get(path.toString(), dir.getName());
        if (logger != null) logger.debug("Copied dir: " + newPath.toString());

        for (File children : Objects.requireNonNull(dir.listFiles())) {
            if (children.isDirectory()) {
                copyRecursively(children, newPath);
            }
            else if (children.isFile()) {
                copyFile(Paths.get(children.getAbsolutePath()), newPath);
            }
        }
    }

    public void safeDeleteFile(Path pathToFile)
            throws WrongTypeOfNodeException, IOException, UnnableToCreateFileException,
            FileAlreadyExistException, NoNodeFoundException, UnnableToWriteInFileException,
            UnnableToDeleteException, UnnableToReadFileException {

        checkForAbleForDeleting(pathToFile);
        File bin = createNewBin();

        moveFile(pathToFile, bin.toPath());
        deleteFile(pathToFile);
    }

    public void safeDeleteDir(Path pathToDir)
            throws UnnableToCreateFileException, IOException, WrongTypeOfNodeException,
            FileAlreadyExistException, NoNodeFoundException, CopyDirIntoitsInsideException,
            DirAlreadyExistException, UnnableToWriteInFileException, UnnableToDeleteException,
            UnnableToReadFileException {

        checkForAbleForDeleting(pathToDir);
        File bin = createNewBin();

        moveDir(pathToDir, bin.toPath());
        deleteDirectory(pathToDir);
    }

    private void checkForAbleForDeleting(Path pathToFile)
            throws WrongTypeOfNodeException, IOException {
        File file = new File(pathToFile.toString());
        if (!file.isFile()) {
            throw new WrongTypeOfNodeException("Trying to delete not a file while required file");
        }
        if (!file.exists()) {
            throw new FileNotFoundException("Trying to delete file that does not exist");
        }
    }

    private File createNewBin()
            throws IOException, UnnableToCreateFileException {
        File newBin = new File(this.recycleBin.getPath() + File.separator + binIt);
        binIt++;

        if (!newBin.createNewFile()) {
            throw new UnnableToCreateFileException("Unable to create new bin");
        }
        return newBin;
    }

    public void restoreLast(Path whereSave)
            throws NoFilesInBinException, FileAlreadyExistException, NoNodeFoundException,
            CopyDirIntoitsInsideException, DirAlreadyExistException, UnnableToWriteInFileException,
            UnnableToDeleteException, IOException, UnnableToReadFileException, WrongTypeOfNodeException {
        int maxIt = -1;
        for (File bin : Objects.requireNonNull(recycleBin.listFiles())) {
            try {
                maxIt = Math.max(maxIt, Integer.parseInt(bin.getName()));
            } catch (NumberFormatException ignored) {
            }
        }

        if (maxIt == -1) throw new NoFilesInBinException("There is no bins");

        Path pathToBinToRestore = Paths.get(recycleBin.getAbsolutePath(), String.valueOf(maxIt));
        File bin = new File(pathToBinToRestore.toString());

        restore(bin, whereSave);

        deleteDirectory(pathToBinToRestore);
    }

    private void restore(File bin, Path whereSave)
            throws FileAlreadyExistException, NoNodeFoundException, CopyDirIntoitsInsideException,
            DirAlreadyExistException, UnnableToWriteInFileException, UnnableToDeleteException, IOException,
            UnnableToReadFileException, WrongTypeOfNodeException {

        for (File file : bin.listFiles()) {
            if (file.isFile()) {
                moveFile(file.getAbsoluteFile().toPath(), whereSave);
            }
            else if (file.isDirectory()) {
                moveDir(file.getAbsoluteFile().toPath(), whereSave);
            }
        }
    }
}
