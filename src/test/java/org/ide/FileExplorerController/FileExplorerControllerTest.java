package org.ide.FileExplorerController;

import org.ide.FileExplorerController.Exceptions.*;
import org.ide.FileExplorerController.File_tree.FileTreeBuilder;
import org.ide.FileExplorerController.Node.Directory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
class FileExplorerControllerTest {

    String pathToTestDir = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "source";

    @Test
    void getFileTree() {
        FileExplorerController controller = new FileExplorerController(pathToTestDir);
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
    void testCreateDeleteFile() {
        FileExplorerController controller = new FileExplorerController(pathToTestDir);
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
        FileExplorerController controller = new FileExplorerController(pathToTestDir);
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
}