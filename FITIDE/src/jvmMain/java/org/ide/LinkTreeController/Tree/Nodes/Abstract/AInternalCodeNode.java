package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.KeyWord;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AInternalCodeNode extends AInternalCodeNodeEnd{
    public Path pathToFile;
    public int column;
    public int row;
    public int columnEnd;
    public int rowEnd;
    public List<String> keyWordsNames;
    public List<KeyWord> keyWords;

    public AInternalCodeNode(List<String> keyWordsNames) {
        this(new ArrayList<>(), keyWordsNames, null, -1, -1, -1, -1);
    }

    public AInternalCodeNode(List<KeyWord> keyWords, List<String> keyWordsNames, Path path, int row, int column, int columnEnd, int rowEnd) {
        super(path, row, column);
        this.columnEnd = columnEnd;
        this.rowEnd = rowEnd;
        this.keyWordsNames = keyWordsNames;
        this.keyWords = keyWords;
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


    public void getKeyWordsOfStatement(String prefix, List<String> listOfHints) {
        for (String keyWord : this.keyWordsNames) {
            if (keyWord.startsWith(prefix)) {
                listOfHints.add(keyWord);
            }
        }
    }

}
