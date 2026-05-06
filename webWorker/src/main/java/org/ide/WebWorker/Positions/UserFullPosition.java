package org.ide.WebWorker.Positions;


import java.time.LocalTime;

public class UserFullPosition {
    public String file;
    public CursorPosition cursorPosition;
    public HighlightedPosition highlightedPosition;
    public LocalTime timeWasChanged;

    public void setTime(LocalTime time) {
        this.timeWasChanged = time;
    }


    @Override
    public UserFullPosition clone() throws CloneNotSupportedException {
        UserFullPosition userFullPosition = (UserFullPosition) super.clone();
        var copied = new UserFullPosition();
        copied.file = this.file;
        copied.cursorPosition = cursorPosition;
        copied.timeWasChanged = timeWasChanged;
        copied.highlightedPosition = highlightedPosition;
        return copied;
    }
}
