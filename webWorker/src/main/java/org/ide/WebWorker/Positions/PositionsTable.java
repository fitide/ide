package org.ide.WebWorker.Positions;

import java.util.HashMap;
import java.util.Map;

public class PositionsTable {
    public Map<String, UserFullPosition> usersPositions = new HashMap<>();

    public void updateCursor(String user, CursorPosition cursorPosition) {
        var userPos = getUser(user);

        userPos.highlightedPosition = null;
        userPos.cursorPosition = cursorPosition;
    }

    public void updateHighlited(String user, HighlightedPosition highlightedPosition) {
        var userPos = getUser(user);

        userPos.highlightedPosition = highlightedPosition;
        userPos.cursorPosition = null;
    }

    public void updateFile(String user, String file) {
        var userPos = getUser(user);

        userPos.file = file;
    }

    private UserFullPosition getUser(String user) {
        if (!usersPositions.containsKey(user)) {
            usersPositions.put(user, new UserFullPosition());
        }

        return usersPositions.get(user);
    }
}
