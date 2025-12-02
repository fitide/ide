package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.KeyWord;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;
import org.ide.PluginController.PluginInterface.Tag;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class AInternalCodeNode {
    public Plugin plugin;
    public String id;
    public Path pathToModule;
    public Path pathToFile;
    public LinkTreePosition wholePos = new LinkTreePosition();
    public List<String> keyWordsNames;
    public List<KeyWord> keyWords;
    public CodeType codeType = CodeType.Usage;
    public AInternalCodeNode definition = null;
    public Map<String, AInternalCodeNode> childs = new HashMap<>();


    public String name;
    public LinkTreePosition namePosition = new LinkTreePosition();

    public AInternalCodeNode(Plugin plugin, Path pathToFile, Path path, ParseTree tree) {
        setCommon(plugin, pathToFile, path, tree);
    }

    public AInternalCodeNode(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        setCommon(plugin, pathToFile, path, tree);
        this.name = name;
        Position namePos = plugin.getNamePositionOfModule(tree);
        this.namePosition.rowS = namePos.rowS;
        this.namePosition.colS = namePos.colS;
        this.namePosition.rowE = namePos.rowE;
        this.namePosition.colE = namePos.colE;
    }

    protected void setCommon(Plugin plugin, Path pathToFile, Path path, ParseTree tree) {
        this.plugin = plugin;
        this.pathToFile = pathToFile;
        Position position = plugin.getBounds(tree);
        this.wholePos.rowS = position.rowS;
        this.wholePos.colS = position.colS;
        this.wholePos.rowE = position.rowE;
        this.wholePos.colE = position.colE;
        Tag[] tags = plugin.getTagsOfNode(tree);
        for (Tag tag : tags) {
            if (tag == Tag.Declaration) {
                codeType = CodeType.Declaration;
            }
            if (tag == Tag.Definition) {
                codeType = CodeType.Definition;
            }
            if (tag == Tag.Usage) {
                codeType = CodeType.Usage;
            }
        }

        setKeyWords(tree);

        this.setChilds(tree);

        this.id = UUID.randomUUID().toString();
        this.pathToModule = Paths.get(path.toString(), id);
    }

    protected void setKeyWords(ParseTree tree) {
        List<ParseTree> keyWordsOfModule = plugin.getKeyWordsOfModule(tree);
        List<KeyWord> keyWordList = new ArrayList<>();
        for (ParseTree key : keyWordsOfModule) {
            keyWordList.add(new KeyWord(plugin, pathToFile, pathToModule, key, plugin.getNameOfNode(key)));
        }
        this.keyWords = keyWordList;
    }

    protected abstract void setChilds(ParseTree curNode);



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

        if (name != null && name.startsWith(prefix)) hints.add(name);
    }

    public abstract void getHint(String prefix, List<String> hints, Path pathToModule);

    public void getHighlightning(List<CodeStrForColour> list) {
        for (AInternalCodeNode node : childs.values()) {
            node.getHighlightning(list);
        }
    }

    public Path searchForDeclaration(Path pathToNode, String name) {
        return null;
    };

    public Path searchForDefinition(Path pathToNode, String name) {
        return null;
    }

    public void updateTree(Path pathToModule, ParseTree parseTree) {
        if (pathToModule.getNameCount() != 0 && this.childs.containsKey(PathTools.getRootStr(pathToModule))) {
            this.childs.get(PathTools.getRootStr(pathToModule)).updateTree(PathTools.deleteRoot(pathToModule), parseTree);
            return;
        }

        if (pathToModule.getNameCount() == 0) {
            this.updateTree(parseTree);
        }

    }

    protected abstract void updateTree(ParseTree tree);

    public AInternalCodeNode getDeclaration(Path path) {
        if (path.getNameCount() == 0 && this.codeType == CodeType.Declaration || this.codeType == CodeType.Definition) {
            return this;
        }
        if (path.getNameCount() > 0 && childs.containsKey(PathTools.getRootStr(path))) {
            childs.get(PathTools.getRootStr(path)).getDeclaration(PathTools.deleteRoot(path));
        }
        return null;
    };

    public AInternalCodeNode getDefinition(Path path) {
        if (path.getNameCount() == 0 && this.codeType == CodeType.Definition) {
            return this;
        }
        if (path.getNameCount() > 0 && childs.containsKey(PathTools.getRootStr(path))) {
            childs.get(PathTools.getRootStr(path)).getDeclaration(PathTools.deleteRoot(path));
        }
        return null;
    }

    public void updateCurNode(AInternalCodeNode node) {
        this.keyWords = node.keyWords;
        this.namePosition = node.namePosition;
        this.name = node.name;
        this.wholePos = node.wholePos;
        this.childs = node.childs;
        this.pathToModule = node.pathToModule;
        this.codeType = node.codeType;
        this.plugin = node.plugin;
        this.keyWordsNames = node.keyWordsNames;
        this.definition = node.definition;
        this.id = node.id;
    }

    public List<Path> getPathsToSearchDeclaration(Path pathToModule) {
        return getPaths(pathToModule);
    }

    public List<Path> getPathsToSearchDefinition(Path pathToModule) {
        return getPaths(pathToModule);
    }

    protected List<Path> getPaths(Path pathToModule) {
        List<Path> list = null;
        if (pathToModule.getNameCount() != 0) {
            String nextId = PathTools.getRootStr(pathToModule);
            if (this.childs.containsKey(nextId)) {
                list = childs.get(nextId).
                        getPathsToSearchDeclaration(PathTools.deleteRoot(pathToModule));
            }
        }
        else {
            list = new ArrayList<>();
        }
        list.add(this.pathToModule);
        return list;
    }

    public List<Path> getPathsToSearchDeclaration(LinkTreePosition position) {
        return getPaths(position);
    }

    public List<Path> getPathsToSearchDefinition(LinkTreePosition position) {
        return getPaths(position);
    }

    protected abstract List<Path> getPaths(LinkTreePosition position);

    public void setDefinition(Map<String, AInternalCodeNode> defInFIle, List<CommonFile> includedFiles) {
        if (defInFIle.containsKey(this.name)) this.definition = defInFIle.get(this.name);
        for (CommonFile file : includedFiles) {
            if (file.defInFIle.containsKey(this.name)) this.definition = file.defInFIle.get(name);
        }
        this.definition = null;
    }
}
