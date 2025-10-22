package org.ide.FileExplorerController;

import org.ide.FileExplorerController.Exceptions.*;
import org.ide.FileExplorerController.Node.Directory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
class FileExplorerControllerTest {

    String pathToTestDir = "src" + File.separator + "test" + File.separator + "resources";
    String pathToSource = pathToTestDir + File.separator + "source";

    @Test
    void getFileTree() {
        FileExplorerController controller = new FileExplorerController(pathToSource);
        Directory rootDir = controller.getFileTree();

        assertEquals(1, rootDir.getFilesCnt());
        assertEquals("text1.txt", rootDir.getFile(0).name);

        assertEquals(1, rootDir.getDirsCnt());
        assertEquals("dir1", rootDir.getDir(0).name);

        Directory dir1 = rootDir.getDir(0);
        assertEquals(0, dir1.getFilesCnt());
        assertEquals(2, dir1.getDirsCnt());
        assertEquals("dir2", dir1.getDir(0).name);
        assertEquals("dir3", dir1.getDir(1).name);

        Directory dir3 = dir1.getDir(1);
        assertEquals(0, dir3.getDirsCnt());
        assertEquals(2, dir3.getFilesCnt());
        assertEquals("text3.txt", dir3.getFile(0).name);
        assertEquals("text4.txt", dir3.getFile(1).name);

        Directory dir2 = dir1.getDir(0);
        assertEquals(1, dir2.getFilesCnt());
        assertEquals("text2.txt", dir2.getFile(0).name);
        assertEquals(1, dir2.getDirsCnt());
        assertEquals("dir", dir2.getDir(0).name);

        Directory dir = dir2.getDir(0);
        assertEquals(0, dir.getDirsCnt());
        assertEquals(1, dir.getFilesCnt());
        assertEquals("text.txt", dir.getFile(0).name);
    }

    @Test
    void testCopyTree() {
        FileExplorerController controller = new FileExplorerController(pathToSource);
        Directory rootDir = controller.getFileTree();
        Directory cdir = controller.getTreeCopy();

        testDir(rootDir, cdir);
    }

    void testDir(Directory source, Directory target) {
        assertEquals(source.name, target.name);
        assertEquals(source.getFilesCnt(), target.getFilesCnt());
        assertEquals(source.getDirsCnt(), target.getDirsCnt());

        for (int i = 0; i < source.getFilesCnt(); i++) {
            assertEquals(source.getFile(i).name, target.getFile(i).name);
        }
        for (int i = 0; i < source.getDirsCnt(); i++) {
            testDir(source.getDir(i), target.getDir(i));
        }

    }

    @Test
    void testCreateDeleteFile() {
        FileExplorerController controller = new FileExplorerController(pathToSource);
        Directory rootDir = controller.getFileTree();

        try {
            assertEquals(1, rootDir.getFilesCnt());
            controller.createFile(Paths.get("src", "test", "resources", "source"), "textTest.txt");
            File newFile = new File(Paths.get("src", "test", "resources", "source", "textTest.txt").toString());
            assertTrue(newFile.exists());
            assertEquals(2, rootDir.getFilesCnt());
            assertEquals("textTest.txt", rootDir.getFile(1).name);

            controller.deleteFile(Paths.get("src", "test", "resources", "source", "textTest.txt"));
            assertEquals(1, rootDir.getFilesCnt());
            assertEquals("text1.txt", rootDir.getFile(0).name);

        } catch (WrongTypeOfNodeException | NoNodeFoundException | FileAlreadyExistException | IOException |
                 UnnableToDeleteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateDeleteDir() {
        FileExplorerController controller = new FileExplorerController(pathToSource);
        Directory rootDir = controller.getFileTree();

        try {
            assertEquals(1, rootDir.getDirsCnt());
            controller.createDir(Paths.get("src", "test", "resources", "source"), "dirTest");
            assertTrue(new File(Paths.get("src", "test", "resources", "source", "dirTest").toString()).exists());
            assertEquals(2, rootDir.getDirsCnt());
            assertEquals("dirTest", rootDir.getDir(1).name);

            Directory testDir = rootDir.getDir(1);
            assertEquals(0, testDir.getDirsCnt());
            assertEquals(0, testDir.getFilesCnt());
            controller.createFile(Paths.get("src", "test", "resources", "source", "dirTest"), "textTest.txt");
            controller.createDir(Paths.get("src", "test", "resources", "source", "dirTest"), "dirTest2");
            assertEquals(1, testDir.getDirsCnt());
            assertEquals(1, testDir.getFilesCnt());

            Directory testDir2 = testDir.getDir(0);
            assertEquals(0, testDir2.getFilesCnt());
            controller.createFile(Paths.get("src", "test", "resources", "source", "dirTest", "dirTest2"), "test.txt");
            assertEquals(1, testDir2.getFilesCnt());

            controller.deleteDirectory(Paths.get("src", "test", "resources", "source", "dirTest"));

            assertEquals(1, rootDir.getDirsCnt());
            assertFalse(new File(Paths.get("src", "test", "resources", "source", "dirTest").toString()).exists());
            assertFalse(new File(Paths.get("src", "test", "resources", "source", "dirTest", "textTest.txt").toString()).exists());
            assertFalse(new File(Paths.get("src", "test", "resources", "source", "dirTest", "dirTest2").toString()).exists());
            assertFalse(new File(Paths.get("src", "test", "resources", "source", "dirTest", "dirTest2", "test.txt").toString()).exists());

        } catch (NoNodeFoundException | DirAlreadyExistException | WrongTypeOfNodeException |
                 FileAlreadyExistException | IOException | UnnableToDeleteException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void testReadAndUpdateFile() {
        FileExplorerController controller = new FileExplorerController(pathToSource);
        Directory rootDir = controller.getFileTree();
        Path pathToFile = Paths.get("src", "test", "resources", "source", "text1.txt");
        try {
            String []str = controller.openFile(pathToFile);
            assertEquals(2, str.length);
            assertEquals("123", str[0]);
            assertEquals("3456", str[1]);

            controller.saveChangesToFile(pathToFile, new String[0]);
            str = controller.openFile(pathToFile);
            assertEquals(0, str.length);

            controller.saveChangesToFile(pathToFile, new String[]{"123", "3456"});
            str = controller.openFile(pathToFile);
            assertEquals(2, str.length);
            assertEquals("123", str[0]);
            assertEquals("3456", str[1]);


        } catch (UnnableToReadFileException | UnnableToWriteInFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRenaming() {
        FileExplorerController controller = new FileExplorerController(pathToSource);
        Directory rootDir = controller.getFileTree();

        Path pathToFile = Paths.get("src", "test", "resources", "source", "text1.txt");
        Path pathToDir = Paths.get("src", "test", "resources", "source", "dir1");


        try {
            assertEquals("text1.txt", rootDir.getFile(0).name);

            controller.renameFile(pathToFile, "text1new.txt");

            assertTrue(new File(Paths.get("src", "test", "resources", "source", "text1new.txt").toString()).exists());
            assertFalse(new File(pathToFile.toString()).exists());
            assertEquals("text1new.txt", rootDir.getFile(0).name);

            controller.renameFile(Paths.get("src", "test", "resources", "source", "text1new.txt"), "text1.txt");

            assertEquals("text1.txt", rootDir.getFile(0).name);
            assertTrue(new File(pathToFile.toString()).exists());
            assertFalse(new File(Paths.get("src", "test", "resources", "source", "text1new.txt").toString()).exists());



            assertEquals("dir1", rootDir.getDir(0).name);

            controller.renameDirectory(pathToDir, "dir1new");

            assertTrue(new File(Paths.get("src", "test", "resources", "source", "dir1new").toString()).exists());
            assertFalse(new File(pathToDir.toString()).exists());
            assertEquals("dir1new", rootDir.getDir(0).name);

            controller.renameDirectory(Paths.get("src", "test", "resources", "source", "dir1new"), "dir1");

            assertEquals("dir1", rootDir.getDir(0).name);
            assertTrue(new File(pathToDir.toString()).exists());
            assertFalse(new File(Paths.get("src", "test", "resources", "source", "dir1new").toString()).exists());

        } catch (NoNodeFoundException | WrongTypeOfNodeException | UnnableToRenameException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testMoveCopyFile() {
        FileExplorerController controller = new FileExplorerController(pathToTestDir);
        Directory rootDir = controller.getFileTree();

        Path pathToRootDir = Paths.get("src", "test", "resources", "source");
        Path pathToStartFile = Paths.get("src", "test", "resources", "source", "text1.txt");
        Path pathToDirToMove = Paths.get("src", "test", "resources", "target");
        Path newPathToFile = Paths.get("src", "test", "resources", "target", "text1.txt");


        try {
            controller.moveFile(pathToStartFile, pathToDirToMove);
            assertEquals(2, controller.openFile(newPathToFile).length);
            assertFalse(new File(pathToStartFile.toString()).exists());

            controller.copyFile(newPathToFile, pathToRootDir);
            assertEquals(2, controller.openFile(pathToStartFile).length);

            controller.deleteFile(newPathToFile);
            assertFalse(new File(newPathToFile.toString()).exists());

        } catch (NoNodeFoundException | UnnableToDeleteException | WrongTypeOfNodeException | IOException |
                 FileAlreadyExistException | UnnableToReadFileException | UnnableToWriteInFileException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testMovyCopyDir() {
        FileExplorerController controller = new FileExplorerController(pathToTestDir);
        Directory rootDir = controller.getFileTree();

        Path pathToDirToCopy = Paths.get("src", "test", "resources", "source", "dir1", "dir2");
        Path pathToRootDir = Paths.get("src", "test", "resources", "source", "dir1");
        Path pathToDirWhereCopy = Paths.get("src", "test", "resources", "target");
        Path newPathToDir = Paths.get("src", "test", "resources", "target", "dir2");

        try {
            controller.moveDir(pathToDirToCopy, pathToDirWhereCopy);
            assertTrue(new File(newPathToDir.toString() + File.separator + "text2.txt").exists());
            assertFalse(new File(pathToRootDir.toString() + File.separator + "dir2").exists());

            controller.copyDir(newPathToDir, pathToRootDir);
            assertTrue(new File(pathToRootDir.toString() + File.separator + "dir2").exists());

            controller.deleteDirectory(newPathToDir);
            assertFalse(new File(newPathToDir.toString()).exists());

        } catch (FileAlreadyExistException | NoNodeFoundException | CopyDirIntoitsInsideException |
                 DirAlreadyExistException | UnnableToWriteInFileException | UnnableToDeleteException | IOException |
                 UnnableToReadFileException | WrongTypeOfNodeException e) {
            throw new RuntimeException(e);
        }
    }
}