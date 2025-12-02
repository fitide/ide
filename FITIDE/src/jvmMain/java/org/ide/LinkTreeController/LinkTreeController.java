package org.ide.LinkTreeController;

import org.antlr.v4.runtime.misc.Pair;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;

public interface LinkTreeController {

    void setFilesAndDirectoriesData(Directory FERoot);

    void addPluginForFiles(Plugin plugin);

    void getHintsForFile(Path pathToNodule);

    void getSyntaxHighlightning(Path pathToFile);

    Pair<Path, LinkTreePosition> getDefinition();

    Pair<Path, LinkTreePosition> getDeclaration();

}
