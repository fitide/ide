package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.KeyWord;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;
import org.ide.PluginController.PluginInterface.Tag;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public abstract class AInternalCodeNode {
    public Plugin plugin;
    public String id;
    public Path pathToModule;
    public LinkTreePosition wholePos = new LinkTreePosition();
    public List<String> keyWordsNames;
    public List<KeyWord> keyWords;
    public boolean isDef = false;
    public AInternalCodeNode definition = null;

    public HashMap<String, AInternalCodeNode> childs = new HashMap<>();


    public String name;
    public LinkTreePosition namePosition;

    AInternalCodeNode(Plugin plugin, Path path, ParseTree tree) {
        this.plugin = plugin;
        this.pathToModule = path;
        Position position = plugin.getBounds(tree);
        this.wholePos.rowS = position.rowS;
        this.wholePos.colS = position.colS;
        this.wholePos.rowE = position.rowE;
        this.wholePos.colE = position.colE;
        Tag[] tags = plugin.getTagsOfNode(tree);
        for (Tag tag : tags) {
            if (tag == Tag.Declaration) {
                isDef = false;
                break;
            }
            if (tag ==Tag.Definition) {
                isDef = true;
                break;
            }
        }
    }

    public void setPathToFile(Path pathToFile) {

        this.pathToModule = pathToFile;
    }


    public void setId(String id) {
        if (id != null) {
            this.pathToModule = pathToModule.subpath(0, this.pathToModule.getNameCount() - 1);
        }

        this.id = id;
        this.pathToModule = Paths.get(pathToModule.toString(), id);
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

    public abstract AInternalCodeNode getDeclaration(Path path);

    public abstract AInternalCodeNode getDefinition(Path path);

    public abstract void updateCurNode(AInternalCodeNode node);

    public abstract Path[] getPathsToSearchDeclaration(Path pathToModule);

    public abstract Path[] getPathsToSearchDefinition(Path pathToModule);

    public abstract Path[] getPathsToSearchDeclaration(Position position);

    public abstract Path[] getPathsToSearchDefinition(Position position);

}
