package org.ide.WebWorker.Positions;

import org.ide.WebWorker.User.UserCursor;
import org.ide.WebWorker.User.UserFile;
import org.ide.WebWorker.User.UserHighlighted;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PositionsTable {
    public Map<String, UserFullPosition> usersPositions = new ConcurrentHashMap<>();

    public void updateCursor(String user, UserCursor userCursor) {
        var userPos = getUser(user);

        synchronized (userPos) {
            var newTime = (Instant.ofEpochSecond(userCursor.getTime().getSeconds())).atZone(ZoneId.systemDefault()).toLocalTime();
            if (userPos.timeWasChanged == null || userPos.timeWasChanged.isAfter(newTime)) {
                userPos.highlightedPosition = null;
                userPos.cursorPosition = userCursor.getCursorPosition();
                userPos.timeWasChanged = newTime;
            }
        }
    }

    public void updateHighlited(String user, UserHighlighted userHighlighted) {
        var userPos = getUser(user);

        synchronized (userPos) {
            var newTime = (Instant.ofEpochSecond(userHighlighted.getTime().getSeconds())).atZone(ZoneId.systemDefault()).toLocalTime();
            if (userPos.timeWasChanged == null || userPos.timeWasChanged.isAfter(newTime)) {
                userPos.cursorPosition = null;
                userPos.highlightedPosition = userHighlighted.getHighlightedPosition();
                userPos.timeWasChanged = newTime;
            }
        }
    }

    public void updateFile(String user, UserFile userFile) {
        var userPos = getUser(user);

        synchronized (userPos) {
            var newTime = (Instant.ofEpochSecond(userFile.getTime().getSeconds())).atZone(ZoneId.systemDefault()).toLocalTime();
            if (userPos.timeWasChanged == null || userPos.timeWasChanged.isAfter(newTime)) {
                userPos.cursorPosition = null;
                userPos.highlightedPosition = null;
                userPos.file = userFile.getFile();
                userPos.timeWasChanged = newTime;
            }
        }
    }

    private UserFullPosition getUser(String user) {
        if (!usersPositions.containsKey(user)) {
            usersPositions.put(user, new UserFullPosition());
        }

        try {
            return usersPositions.get(user).clone();
        } catch (CloneNotSupportedException e) {
            return usersPositions.get(user);
        }
    }
}
