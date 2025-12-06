package org.ide.LinkTreeController;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.FileExplorerController.Exceptions.UnnableToCreateFileException;
import org.ide.FileExplorerController.FileExplorerController;
import org.ide.LinkTreeController.SimplePlugin.SimpleParseTree;
import org.ide.LinkTreeController.SimplePlugin.SimplePlugin;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeType;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;
import org.ide.PluginController.PluginInterface.Tag;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class LinkTreeControllerSimpleTest {
    static LinkTreeController controller;
    static FileExplorerController FEController;


    @BeforeAll
    static void setUp() {
        controller = new LinkTreeControllerImpl();
        try {
            FEController = new FileExplorerController("C:\\Users\\user\\IdeaProjects\\ide\\FITIDE\\src\\jvmTest\\java\\org\\ide\\LinkTreeController");
        } catch (UnnableToCreateFileException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
        controller.setFilesAndDirectoriesData(FEController.getTreeCopy());
        Plugin plugin = new SimplePlugin();
        SimpleParseTree.SimpleParseTreeBuilder builder = new SimpleParseTree.SimpleParseTreeBuilder();
        List<ParseTree> body = new ArrayList<>();
        body.add(builder.setBody(List.of()).setName("abcb").setType("int").setValueType(Tag.Var)
                .setTypeOfUsage(Tag.Definition).setNamePos(new Position(0, 4, 0, 5)).setFullPos(new Position(0, 0, 0, 25))
                .setTypePos(new Position(0, 0, 0, 3)).build());
        body.add(builder.setName("b").setType("double").setTypeOfUsage(Tag.Declaration).setNamePos(new Position(1, 7,1, 8))
                .setTypePos((new Position(1, 0, 1, 7))).setFullPos(new Position(1, 0, 1, 25)).build());
        body.add(builder.setName("b").setType(null).setTypeOfUsage(Tag.Usage).setNamePos(new Position(2, 7,2, 8))
                .setTypePos((new Position(1, 0, 1, 7))).setFullPos(new Position(2, 0, 2, 25)).build());
        ParseTree root = builder.setBody(body).build();
        controller.initFiles(plugin, List.of(new Pair<>(Paths.get("LinkTreeController","test.txt"), root)));
    }


    @Test
    void testSyntax() {
        var list = controller.getSyntaxHighlightning(Paths.get("LinkTreeController","test.txt"));
        for (var node : list) {
            System.out.println(node);
        }
        Assert.assertEquals(list.size(), 5);
    }

    @Test
    void testDecInFile() {
        var root = controller.getTree();
        var fileNode = root.childs.get("test.txt");
        var file = (CommonFile) fileNode;
        Assert.assertTrue(file.decInFile.containsKey("b"));
        for (var code : file.codeNodes.values()) {
            if (code.codeType == CodeType.Usage) {
                Assert.assertEquals(code.declaration, file.decInFile.get("b"));
            }
        }
    }

    @Test
    void testInternalHints() {
        var hints = controller.getHintsForFile(Paths.get("LinkTreeController", "test.txt"), "ab");
        Assert.assertEquals(hints.size(), 1);
        Assert.assertEquals(hints.iterator().next().name, "abcb");
    }
}