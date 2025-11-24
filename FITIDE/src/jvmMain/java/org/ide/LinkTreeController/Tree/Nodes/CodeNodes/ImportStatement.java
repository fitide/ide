package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ImportStatement extends AInternalCodeNode {


    public ImportStatement(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
    }

    @Override
    protected void setChilds() {}

    @Override
    public void getHint(String prefix, List<String> hints) {
        return;
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        for (KeyWord keyWord : this.keyWords) {
            keyWord.getHighlightning(list);
        }

        CodeStrForColour code = new CodeStrForColour();
        code.pos = this.namePosition.clone();
        code.tag = LinkTreeCodeTag.importStatement;
        list.add(code);
    }

    @Override
    public Path[] getPathsToSearchDeclaration(Path pathToModule) {
        Path[] paths = new Path[2];
        paths[0] = Paths.get(pathToFile.toString(), this.name);
        paths[1] = Paths.get(this.name);
        return paths;
    }

    @Override
    public Path[] getPathsToSearchDefinition(Path pathToModule) {
        Path[] paths = new Path[2];
        paths[0] = Paths.get(pathToFile.toString(), this.name);
        paths[1] = Paths.get(this.name);
        return paths;
    }

    @Override
    public Path[] getPathsToSearchDeclaration(Position position) {
        return super.getPathsToSearchDeclaration(position);
    }

    @Override
    public Path[] getPathsToSearchDefinition(Position position) {
        return super.getPathsToSearchDefinition(position);
    }

}
