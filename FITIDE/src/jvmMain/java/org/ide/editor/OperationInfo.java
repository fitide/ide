package org.ide.editor;

import org.ide.WebWorker.Positions.HighlightedPosition;

public class OperationInfo {
    public TextOperation operation;
    public HighlightedPosition position;
    public String text;
    public String newText;

    public OperationInfo(TextOperation operation, HighlightedPosition position, String text) {
        this.operation = operation;
        this.position = position;
        this.text = text;
    }

    public OperationInfo(TextOperation operation, HighlightedPosition position, String text, String newText) {
        this.operation = operation;
        this.position = position;
        this.text = text;
        this.newText = newText;
    }
}
