package org.ide.WebWorker.MessageManager;

public class FileChangingMessage implements Comparable<FileChangingMessage> {

    public Integer colS;
    public Integer colE;


    @Override
    public int compareTo(FileChangingMessage o) {
        return this.colS.compareTo(o.colE);
    }
}
