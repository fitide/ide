package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.KeyWord;
import org.ide.LinkTreeController.Tree.Nodes.CodeStrForColour;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.util.List;

public abstract class AInternalCodeNode {
    public Plugin plugin;
    public String id;
    public Path pathToFile;
    public int column;
    public int row;
    public int columnEnd;
    public int rowEnd;
    public List<String> keyWordsNames;
    public List<KeyWord> keyWords;
    public boolean isDef = false;

    public String name;
    public int nameRowS, nameColS, nameRowE, nameColE;

    AInternalCodeNode(Plugin plugin, Path path, ParseTree tree) {
        this.plugin = plugin;
        this.pathToFile = path;
        Position position = plugin.getBounds(tree);
        this.row = position.rowS;
        this.rowEnd = position.colE;
        this.column = position.colS;
        this.columnEnd = position.colE;

    }

    public void setPathToFile(Path pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRowEnd(int rowEnd) {
        this.rowEnd = rowEnd;
    }

    public void setColumnEnd(int columnEnd) {
        this.columnEnd = columnEnd;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void getCommonHints(String prefix, List<String> hints) {
        for (String keyWord : keyWordsNames) {
            if (keyWord.startsWith(prefix)) hints.add(keyWord);
        }
    }

    public abstract void getHint(String prefix, List<String> hints);

    public abstract void getHighlightning(List<CodeStrForColour> list);

    public abstract Path searchForDeclaration(Path pathToNode, String name);

    public abstract Path searchForDefinition(Path pathToNode, String name);

    public abstract void updateTree(Path pathToModule, ParseTree parseTree);
}
