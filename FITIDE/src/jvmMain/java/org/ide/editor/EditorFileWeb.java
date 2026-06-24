package org.ide.editor;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;
import kotlinx.coroutines.CoroutineScope;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.ide.editor.TextFieldValueHelperKt.*;

public class EditorFileWeb implements EditorFileInt {
    private final static int lockersSeparatorStringsCount = 100;
    private final static int changingLimitCnt = 5;

    private MutableState<TextFieldValue> mutableStateValue;
    private boolean saved;
    private int currentVersion;
    private final Map<Integer, Lock> fileStringslocks = new HashMap<>();
    private List<String> fileStrings = new LinkedList<>();
    private final ReadWriteLock fileLock = new ReentrantReadWriteLock();
    private final Object stateChangerObject = new Object();
    private final Object workersObject = new Object();
    private List<TextFieldValue> versionsList;

    private volatile int currentWorking = 0;
    private volatile int cntChanged = 0;

    private volatile int changeCurPos = 0;

    public EditorFileWeb(List<String> contentLines) {
        saved = true;
        setfileStrings(contentLines);
        versionsList = new ArrayList<>();
        currentVersion = 0;
        setMutableStateValue();

        var stateChanger = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    synchronized (stateChangerObject) {
                        if (currentWorking == 0 && cntChanged > 0) {
                            fileLock.writeLock().lock();
                            setMutableStateValue();
                            fileLock.writeLock().unlock();
                            cntChanged = 0;
                            stateChangerObject.notifyAll();
                        }

                        try {
                            stateChangerObject.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        stateChanger.setDaemon(true);
        stateChanger.start();
    }

    private void setfileStrings(List<String> contentLines) {
        this.fileStrings = new LinkedList<>();
        int it = 0;
        for (var str : contentLines) {
            if (it % lockersSeparatorStringsCount == 0) {
                this.fileStringslocks.put(it, new ReentrantLock());
            }

            this.fileStrings.addLast(str);
            it++;
        }
    }

    @Override
    public String getContent() {
        System.out.println("getContentRequest");
        fileLock.readLock().lock();
        var res = mutableStateValue.getValue().getText();
        System.out.println(res);
        fileLock.readLock().unlock();
        return res;
    }

    @Override
    public void setContent(String newContent) {
        var changeList = getListFromString(newContent);
        fileLock.writeLock().lock();
        setfileStrings(changeList);
        versionsList = new ArrayList<>();
        currentVersion = 0;
        setMutableStateValue();
        fileLock.writeLock().unlock();
    }

    private List<String> getListFromString(String str) {
        var splitted = str.split("\n", -1);
        return new ArrayList<>(Arrays.asList(splitted));
    }

    @Override
    public void save() {
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    public boolean canRedo() {
        return currentVersion < versionsList.size() - 1;
    }

    public boolean canUndo() {
        return currentVersion > 0;
    }

    public void undo() {
        fileLock.writeLock().lock();
        if (currentVersion > 0) {
            currentVersion--;
            mutableStateValue.setValue(versionsList.get(currentVersion));
        }
        fileLock.writeLock().unlock();
    }

    public void redo() {
        fileLock.writeLock().lock();
        if (currentVersion < versionsList.size() - 1) {
            currentVersion++;
            mutableStateValue.setValue(versionsList.get(currentVersion));
        }
        fileLock.writeLock().unlock();
    }

    @Override
    public void onTextChanged(TextFieldValue newValue) {
        if (currentVersion < versionsList.size() - 1) {
            versionsList = new ArrayList<>(versionsList.subList(0, currentVersion + 1));
        }

        versionsList.add(newValue);
        currentVersion = versionsList.size() - 1;

        saved = false;
        this.mutableStateValue.setValue(newValue);;
    }

    @Override
    public void insertText(String text, CursorPosition position, boolean isMe) throws ChangeTextUnnavailableException {
        makeChanges(() -> {
            _insertText(text, position, isMe);
        });
    }

    private void _insertText(String text, CursorPosition position, boolean isMe) throws ChangeTextUnnavailableException {
        var changesList = getListFromString(text);
        if (changesList.getLast() == "") changesList.removeLast();

        if (isMe) changeCurPos += text.length();
        if (position.getLineNumer() >= this.fileStrings.size()) {
            var startLockPos = this.fileStrings.size();
            lockStringLockers(startLockPos, position.getLineNumer() + changesList.size());
            try {
                for (int curIt = startLockPos; curIt < position.getLineNumer(); curIt++) fileStrings.addLast("");
                for (var changeStr : changesList) {
                    fileStrings.addLast(changeStr);
                }
            } finally {
                unlockStringLockers(startLockPos, position.getLineNumer() + changesList.size());
            }
            return;
        }


        if (changesList.size() == 1 && !text.endsWith("\n")) {
            lockStringLockers(position.getLineNumer(), position.getLineNumer() + 1);
            try {
                addToStr(text, position.getLineNumer(), position.getColumnNumber());
            } finally {
                unlockStringLockers(position.getLineNumer(), position.getLineNumer() + 1);
            }
            return;
        }

        int startLock = position.getLineNumer();
        int endLock = this.fileStrings.size() + changesList.size() - 1;
        lockStringLockers(startLock, endLock);
        try {
            if (text.equals("\n")) {
                var prevStr = fileStrings.get(position.getLineNumer());
                fileStrings.set(position.getLineNumer(), prevStr.substring(0, position.getColumnNumber() < prevStr.length() ? position.getColumnNumber() : prevStr.length()));
                fileStrings.add(position.getLineNumer() + 1, position.getColumnNumber() < prevStr.length() ? prevStr.substring(position.getColumnNumber()) : "");
            }
            else if (changesList.size() == 1) {
                var strBefore = fileStrings.get(position.getLineNumer());
                fileStrings.set(position.getLineNumer(),
                        strBefore.substring(0, position.getColumnNumber() + 1) + text);
                fileStrings.add(position.getLineNumer() + 1, strBefore.substring(position.getColumnNumber()));
            }
            else {
                var strBefore = fileStrings.get(position.getLineNumer());
                var prefixStr = strBefore.substring(0, position.getColumnNumber() + 1);
                var suffixStr = strBefore.substring(position.getColumnNumber());

                fileStrings.set(position.getLineNumer(), prefixStr + changesList.getFirst());
                changesList.removeFirst();
                if (text.endsWith("\n")) {
                    changesList.addLast(suffixStr);
                } else {
                    changesList.addLast(changesList.removeLast() + suffixStr);
                }
                fileStrings.addAll(position.getLineNumer() + 1, changesList);
            }
        } finally {
            unlockStringLockers(startLock, endLock);
        }
    }

    @Override
    public void deleteText(String textToDelete, HighlightedPosition position, boolean isMe) throws ChangeTextUnnavailableException {
        makeChanges(() -> {
            _deleteText(textToDelete, position, isMe);
        });
    }

    private void _deleteText(String textToDelete, HighlightedPosition position, boolean isMe) throws ChangeTextUnnavailableException {
        var changesList = getListFromString(textToDelete);
        if (changesList.getLast().equals("")) changesList.removeLast();

        if (position.getLineStart() >= this.fileStrings.size()) {
            throw new ChangeTextUnnavailableException("Delete from farther than the end of file");
        }

        if (changesList.size() == 1 && !textToDelete.endsWith("\n")) {
            lockStringLockers(position.getLineStart(), position.getLineStart() + 1);
            try {
                checkForChanging(changesList, position);
                if (isMe) changeCurPos -= textToDelete.length();

                var strBefore = fileStrings.get(position.getLineStart());
                var newStr = strBefore.substring(0, position.getColumnStart()) +
                        strBefore.substring(position.getColumnEnd() + 1);
                fileStrings.set(position.getLineStart(), newStr);
            } finally {
                unlockStringLockers(position.getLineStart(), position.getLineStart() + 1);
            }
            return;
        }

        var lockEnd = this.fileStrings.size();
        lockStringLockers(position.getLineStart(), lockEnd);
        try {
            checkForChanging(changesList, position);
            if (isMe) changeCurPos -= textToDelete.length();

            if (textToDelete.equals("\n")) {
                if (position.getLineStart() == -1) {
                    return;
                }

                var prev = fileStrings.get(position.getLineStart());
                var suf = fileStrings.get(position.getLineStart() + 1);
                fileStrings.set(position.getLineStart(), prev + suf);
                fileStrings.remove(position.getLineStart() + 1);
            }
            else if (changesList.size() == 1) {
                var strBefore = fileStrings.get(position.getLineStart());
                fileStrings.set(position.getLineStart(),
                        strBefore.substring(0, position.getColumnStart() + 1) + fileStrings.get(position.getLineStart() + 1));
                fileStrings.remove(position.getLineStart() + 1);

            }
            else {
                var prefixStr = fileStrings.get(position.getLineStart()).substring(0, position.getColumnStart() + 1);
                String suffixStr = null;

                if (textToDelete.endsWith("\n")) {
                    suffixStr = fileStrings.get(position.getLineEnd() + 1);
                }
                else {
                    suffixStr = fileStrings.get(position.getLineEnd()).substring(position.getColumnEnd());
                }

                fileStrings.set(position.getLineStart(), prefixStr + suffixStr);

                fileStrings.removeAll(fileStrings.subList(position.getLineStart() + 1, position.getLineEnd() + 1));
            }
        } finally {
            unlockStringLockers(position.getLineStart(), lockEnd);
        }
    }

    @Override
    public void changeText(String textToDelete, String newText, HighlightedPosition position, boolean isMe) throws ChangeTextUnnavailableException {
        makeChanges(() -> {
            _deleteText(textToDelete, position, isMe);
            _insertText(newText,
                    CursorPosition.newBuilder()
                            .setLineNumer(position.getLineStart())
                            .setColumnNumber(position.getColumnStart())
                            .build(), isMe);
        });
    }

    private void checkForChanging(List<String> textToCompare, HighlightedPosition position) throws ChangeTextUnnavailableException {
        if (textToCompare.isEmpty()) return;
        if (textToCompare.size() == 1) {
            if (fileStrings.get(position.getLineStart()).length() == position.getColumnStart()) {
                var curStr =  fileStrings.get(position.getLineStart() + 1);
                if (!curStr.substring(0, Math.min(position.getColumnEnd() + 1, curStr.length())).equals(textToCompare.getFirst())) {
                    throw new ChangeTextUnnavailableException("Deleting on changed text");
                }
            }
            else {
                var curStr = fileStrings.get(position.getLineStart());
                if (!curStr.substring(position.getColumnStart(), position.getColumnEnd() + 1).equals(textToCompare.getFirst())) {
                    throw new ChangeTextUnnavailableException("Deleting on changed text");
                }
            }
        }
        else {
            var first = textToCompare.removeFirst();
            var last = textToCompare.removeLast();
            if (!fileStrings.get(position.getLineStart()).substring(position.getColumnStart()).equals(first) ||
                    !fileStrings.get(position.getLineEnd()).substring(0, position.getColumnEnd() + 1).equals(last)) {
                throw new ChangeTextUnnavailableException("Deleting on changed text");
            }

            var subListToCompare = fileStrings.subList(position.getLineStart() + 1, position.getLineEnd() - 1);
            if (!subListToCompare.equals(textToCompare)) {
                throw new ChangeTextUnnavailableException("Deleting on changed text");
            }

            textToCompare.addFirst(first);
            textToCompare.addLast(last);
        }
    }

    private void addToStr(String text, int strIndex, int start) {
        var strBefore = fileStrings.get(strIndex);
        StringBuilder builder = new StringBuilder().append(strBefore, 0, start).append(text).append(
                strBefore.substring(start));
        fileStrings.set(strIndex, builder.toString());
    }

    @Override
    public MutableState<TextFieldValue> getTextField() {
        return this.mutableStateValue;
    }

    @Override
    public OperationInfo getOperation(TextFieldValue value) {
        var curText = this.getContent();
        var newText = value.getText();
        int start = 0;
        int end1 = curText.length() - 1;
        int end2 = newText.length() - 1;
        while(start < curText.length() && start < newText.length() - 1 && curText.charAt(start) == newText.charAt(start)) start++;

        while(end1 >= 0 && curText.charAt(end1) == newText.charAt(end2) && end1 > start && end2 > start) {
            end1--;
            end2--;
        }

        if (start == curText.length() && end2 > end1) {
            var startPos = getPosition(curText, curText.length());
            return new OperationInfo(TextOperation.Insert, HighlightedPosition.newBuilder()
                    .setColumnStart(startPos.getColumnNumber()).setLineStart(startPos.getLineNumer()).build(), newText.substring(start, end2 + 1));
        }


        var difLen = end1 - start;
        var startPos = getPosition(curText, start);
        var endPos = end1 >= 0 ? getPosition(curText, end1 > start ? end1 - 1 : start) : getPosition(curText, 0);
        if (difLen == 1 && curText.charAt(start) == '\n') {
            startPos = CursorPosition.newBuilder().setLineNumer(startPos.getLineNumer() - 1).build();
            endPos = CursorPosition.newBuilder().setLineNumer(endPos.getLineNumer() - 1).build();
        }

        var positions = HighlightedPosition.newBuilder()
                .setColumnStart(startPos.getColumnNumber()).setLineStart(startPos.getLineNumer())
                .setColumnEnd(endPos.getColumnNumber()).setLineEnd(endPos.getLineNumer()).build();

        if (start == end1 && start == end2) {
            return new OperationInfo(TextOperation.Insert, positions, "");
        }

        if (start >= end2 && start - end2 + 1 == curText.length() - newText.length()) return new OperationInfo(TextOperation.Delete, positions, curText.substring(start, end1));
        else if (difLen == 0) return new OperationInfo(TextOperation.Insert, positions, newText.substring(start, end2));
        else return new OperationInfo(TextOperation.Changing, positions, curText.substring(start, end1), newText.substring(start, end2));
    }

    private CursorPosition getPosition(String value, int it) {
        int col = 0;
        int line = 0;
        int curIt = 0;
        while(curIt != it && curIt < this.mutableStateValue.getValue().getText().length()) {
            if (value.charAt(curIt) == '\n') {
                col = -1;
                line += 1;
            }
            col++;
            curIt++;
        }
        return CursorPosition.newBuilder().setColumnNumber(col).setLineNumer(line).build();
    }

    private void setMutableStateValue() {
        StringBuilder builder = new StringBuilder();
        for (var str : fileStrings) {
            builder.append(str).append("\n");
        }

        if (mutableStateValue != null) {
            var posChange = this.changeCurPos;
            UIUpdater.INSTANCE.runOnMain(() -> {
                System.out.println("runOnMain action: thread=" + Thread.currentThread().getName());
                int oldCursorPosition = getSelection(versionsList.getLast());
                int newCursorPosition = oldCursorPosition + posChange;
                mutableStateValue.setValue(getTextFieldValue(builder.toString(), newCursorPosition));
                return null;
            });
        }
        else {
            mutableStateValue = getMutableStateTextFieldValue(builder.toString());
        }

        changeCurPos = 0;
        versionsList.add(mutableStateValue.getValue());
        currentVersion = versionsList.size() - 1;
    }

    private void workerStart() {
        synchronized (stateChangerObject) {
            while (true) {
                if (cntChanged + currentWorking + 1 < changingLimitCnt) {
                    currentWorking += 1;
                    cntChanged += 1;
                    break;
                } else {
                    try {
                        stateChangerObject.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void workerEnd() {
        synchronized (stateChangerObject) {
            currentWorking -= 1;
            stateChangerObject.notifyAll();
        }
    }

    private interface WorkerAction {
        void changeFile() throws ChangeTextUnnavailableException;
    }

    private void makeChanges(WorkerAction action) throws ChangeTextUnnavailableException {
        workerStart();
        try {
            action.changeFile();
        } finally {
            workerEnd();
        }
    }

    private void lockStringLockers(int start, int end) {
        for (start = start / lockersSeparatorStringsCount * lockersSeparatorStringsCount;
            start < end; start += lockersSeparatorStringsCount) {
            if (!this.fileStringslocks.containsKey(start)) {
                synchronized (fileStrings) {
                    if (!fileStringslocks.containsKey(start)) {
                        Lock newLock = new ReentrantLock();
                        fileStringslocks.put(start, newLock);
                    }
                }
            }
            this.fileStringslocks.get(start).lock();
        }
    }

    private void unlockStringLockers(int start, int end) {
        for (start = start / lockersSeparatorStringsCount * lockersSeparatorStringsCount;
             start < end; start += lockersSeparatorStringsCount) {

            this.fileStringslocks.get(start).unlock();
        }
    }
}
