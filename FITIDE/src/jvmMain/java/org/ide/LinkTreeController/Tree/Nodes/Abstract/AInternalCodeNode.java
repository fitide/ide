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
import java.util.ArrayList;
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
    public LinkTreePosition namePosition = new LinkTreePosition();

    public AInternalCodeNode(Plugin plugin, Path path, ParseTree tree) {
        setCommon(plugin, path, tree);
    }

    public AInternalCodeNode(Plugin plugin, Path path, ParseTree tree, String name) {
        setCommon(plugin, path, tree);
        this.name = name;
        Position namePos = plugin.getNamePositionOfModule(tree);
        this.namePosition.rowS = namePos.rowS;
        this.namePosition.colS = namePos.colS;
        this.namePosition.rowE = namePos.rowE;
        this.namePosition.colE = namePos.colE;
    }

    protected void setCommon(Plugin plugin, Path path, ParseTree tree) {
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

        setKeyWords(tree);

        this.setChilds();
    }

    protected void setKeyWords(ParseTree tree) {
        List<ParseTree> keyWordsOfModule = plugin.getKeyWordsOfModule(tree);
        List<KeyWord> keyWordList = new ArrayList<>();
        for (ParseTree key : keyWordsOfModule) {
            keyWordList.add(new KeyWord(plugin, pathToModule, key));
        }
        this.keyWords = keyWordList;
    }

    protected abstract void setChilds();



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

    public void getHint(String prefix, List<String> hints) {}

    public abstract void getHighlightning(List<CodeStrForColour> list);

    public Path searchForDeclaration(Path pathToNode, String name) {
        return null;
    };

    public Path searchForDefinition(Path pathToNode, String name) {
        return null;
    }

    public void updateTree(Path pathToModule, ParseTree parseTree) {}

    public AInternalCodeNode getDeclaration(Path path) {
        return null;
    };

    public AInternalCodeNode getDefinition(Path path) {
        return null;
    }

    public void updateCurNode(AInternalCodeNode node) {
        this.keyWords = node.keyWords;
        this.namePosition = node.namePosition;
        this.name = node.name;
        this.wholePos = node.wholePos;
        this.childs = node.childs;
        this.pathToModule = node.pathToModule;
        this.isDef = node.isDef;
        this.plugin = node.plugin;
        this.keyWordsNames = node.keyWordsNames;
        this.definition = node.definition;
        this.id = node.id;
    }

    public Path[] getPathsToSearchDeclaration(Path pathToModule) {
        return null;
    }

    public Path[] getPathsToSearchDefinition(Path pathToModule) {
        return null;
    }

    public Path[] getPathsToSearchDeclaration(Position position) {
        return null;
    }

    public Path[] getPathsToSearchDefinition(Position position) {
        return null;
    }

}
