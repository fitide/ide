package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;

import java.nio.file.Path;
import java.util.List;

public class While extends AInternalCodeNode {


    public While(List<KeyWord> keyWords, List<String> keyWordsNames, Path path, int row, int column, int columnEnd, int rowEnd) {
        super(keyWords, keyWordsNames, path, row, column, columnEnd, rowEnd);
    }

    public While(List<String> keyWordsNames) {
        super(keyWordsNames);
    }
}
