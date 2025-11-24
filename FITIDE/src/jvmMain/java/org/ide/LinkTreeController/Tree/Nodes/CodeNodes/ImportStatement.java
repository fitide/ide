package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.Tools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImportStatement extends AInternalCodeNode {


    public ImportStatement(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        this.childs = new HashMap<>();
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);

        CodeStrForColour code = new CodeStrForColour();
        code.pos = this.namePosition.clone();
        code.tag = LinkTreeCodeTag.importStatement;
        list.add(code);
    }

    @Override
    public void updateTree(ParseTree parseTree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, parseTree);
        this.updateCurNode(node);
    }

    @Override
    public List<Path> getPathsToSearchDeclaration(Path pathToModule) {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get(pathToFile.toString(), this.name));
        paths.add(Paths.get(this.name));
        return paths;
    }

    @Override
    public List<Path> getPathsToSearchDefinition(Path pathToModule) {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get(pathToFile.toString(), this.name));
        paths.add(Paths.get(this.name));
        return paths;
    }

    @Override
    public List<Path> getPathsToSearchDeclaration(Position position) {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get(pathToFile.toString(), this.name));
        paths.add(Paths.get(this.name));
        return paths;
    }

    @Override
    public List<Path> getPathsToSearchDefinition(Position position) {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get(pathToFile.toString(), this.name));
        paths.add(Paths.get(this.name));
        return paths;
    }

    @Override
    protected List<Path> getPaths(Position position) {
        return List.of();
    }

}
