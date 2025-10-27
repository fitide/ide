package org.ide.FileExplorerController.Exceptions;

public class DirAlreadyExistException extends Exception {
    public DirAlreadyExistException(String message) {
        super(message);
    }
}
