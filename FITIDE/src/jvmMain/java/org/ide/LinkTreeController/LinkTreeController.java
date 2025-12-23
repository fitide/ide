package org.ide.LinkTreeController;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.ARoot;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface LinkTreeController {

    void setFilesAndDirectoriesData(Directory FERoot);

    void initFiles(Plugin plugin, List<Pair<Path, ParseTree>> files);

    Set<HintNode> getHintsForFile(Path pathToNodule, String prefix);

    List<CodeStrForColour> getSyntaxHighlightning(Path pathToFile);

    void updateTree(Plugin plugin, List<Pair<Path, ParseTree>> files);

    ARoot getTree();

    AInternalCodeNode goToDefinition(Path pathToFile, int row, int col);

    void updateFilePath(Path pathToDirWithFileToChange, Path pathToDirToInsert, String fileName);

    void updateFileName(Path pathToFile, String newName);
}
