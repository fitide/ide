package org.ide.LinkTreeController.cdmTest;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.FileExplorerController.Exceptions.UnnableToCreateFileException;
import org.ide.FileExplorerController.FileExplorerController;
import org.ide.LinkTreeController.LinkTreeController;
import org.ide.LinkTreeController.LinkTreeControllerImpl;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Func;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.LinkTreeController.cdmTest.cdm.CdmPlugin;
import org.ide.LinkTreeController.cdmTest.cdm.CdmPluginTest;
import org.ide.PluginController.PluginInterface.Plugin;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkTreeControllerCdmTest {
    static LinkTreeController controller;
    static FileExplorerController FEController;
    static Plugin plugin;

    @BeforeAll
    static void setUp() {
        controller = new LinkTreeControllerImpl();
        try {
            FEController = new FileExplorerController("C:\\Users\\user\\IdeaProjects\\ide\\FITIDE\\src\\jvmTest\\java\\org\\ide\\LinkTreeController\\cdmTest\\cdm\\testFiles");
        } catch (UnnableToCreateFileException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
        controller.setFilesAndDirectoriesData(FEController.getTreeCopy());
        plugin = new CdmPluginTest();
    }

    @Test
    void simpleHighlightining() {
        controller.initFiles(plugin, List.of());
        ParseTree tree = plugin.getFileParseTree(new File(
                "C:\\Users\\user\\IdeaProjects\\ide\\FITIDE\\src\\jvmTest\\java\\org\\ide" +
                        "\\LinkTreeController\\cdmTest\\cdm\\testFiles\\simple.asm"));
        controller.updateTree(plugin, List.of(new Pair<>(Paths.get("testFiles", "simple.asm"), tree)));
        var syn = controller.getSyntaxHighlightning(Paths.get("testFiles", "simple.asm"));
        Assert.assertEquals(syn.size(), 4);
    }

    @Test
    void simpleDec() {
        controller.initFiles(plugin, List.of());
        ParseTree tree = plugin.getFileParseTree(new File(
                "C:\\Users\\user\\IdeaProjects\\ide\\FITIDE\\src\\jvmTest\\java\\org\\ide" +
                        "\\LinkTreeController\\cdmTest\\cdm\\testFiles\\simple.asm"));
        controller.updateTree(plugin, List.of(new Pair<>(Paths.get("testFiles", "simple.asm"), tree)));
        var linkTree = controller.getTree();
        var file = (CommonFile) linkTree.getFileNode(Paths.get("testFiles", "simple.asm"));
        for (var node : file.codeNodes.values()) {
            if (node instanceof Func) {
                Assert.assertEquals(node.declaration, linkTree.getExternalFunctions().get("add"));
            }
        }
    }
}