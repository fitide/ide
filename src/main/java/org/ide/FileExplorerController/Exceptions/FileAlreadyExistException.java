package org.ide.FileExplorerController.Exceptions;

public class FileAlreadyExistException extends Exception {
    public FileAlreadyExistException(String message) {
        super(message);
    }
}
