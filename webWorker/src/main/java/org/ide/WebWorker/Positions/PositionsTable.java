package org.ide.WebWorker.Positions;

import com.google.protobuf.Timestamp;
import org.ide.WebWorker.User.UserCursor;
import org.ide.WebWorker.User.UserFile;
import org.ide.WebWorker.User.UserHighlighted;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PositionsTable {
    public Map<String, UserFullPosition> usersPositions = new ConcurrentHashMap<>();

    public void updateCursor(String user, UserCursor userCursor) {
        var userPos = getOrCreate(user);

        synchronized (userPos) {
            var newTime = toLocalTime(userCursor.getTime());
            if (isNewer(userPos.timeWasChanged, newTime)) {
                userPos.highlightedPosition = null;
                userPos.cursorPosition = userCursor.getCursorPosition();
                userPos.timeWasChanged = newTime;
            }
        }
    }

    public void updateHighlited(String user, UserHighlighted userHighlighted) {
        var userPos = getOrCreate(user);

        synchronized (userPos) {
            var newTime = toLocalTime(userHighlighted.getTime());
            if (isNewer(userPos.timeWasChanged, newTime)) {
                userPos.cursorPosition = null;
                userPos.highlightedPosition = userHighlighted.getHighlightedPosition();
                userPos.timeWasChanged = newTime;
            }
        }
    }

    public void updateFile(String user, UserFile userFile) {
        var userPos = getOrCreate(user);

        synchronized (userPos) {
            var newTime = toLocalTime(userFile.getTime());
            if (isNewer(userPos.timeWasChanged, newTime)) {
                userPos.cursorPosition = null;
                userPos.highlightedPosition = null;
                userPos.file = userFile.getFile();
                userPos.timeWasChanged = newTime;
            }
        }
    }

    private UserFullPosition getOrCreate(String user) {
        return usersPositions.computeIfAbsent(user, k -> new UserFullPosition());
    }

    private boolean isNewer(LocalTime stored, LocalTime incoming) {
        return stored == null || !incoming.isBefore(stored);
    }

    private LocalTime toLocalTime(Timestamp time) {
        return Instant.ofEpochSecond(time.getSeconds(), time.getNanos())
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
    }
}