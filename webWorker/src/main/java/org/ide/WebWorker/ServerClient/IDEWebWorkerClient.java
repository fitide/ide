package org.ide.WebWorker.ServerClient;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannelBuilder;
import org.ide.WebWorker.FileSystem.Copy.CopyServerRequest;
import org.ide.WebWorker.FileSystem.Create.CreateServerRequest;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerRequest;
import org.ide.WebWorker.FileSystem.FileSystemComponents.FileType;
import org.ide.WebWorker.FileSystem.Move.MoveServerRequest;
import org.ide.WebWorker.FileSystem.Rename.RenameServerRequest;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.ResponseCodes.ChangeCode;
import org.ide.WebWorker.ResponseCodes.DeleteTextCode;
import org.ide.WebWorker.ResponseCodes.InsertTextCode;
import org.ide.WebWorker.Text.Changing.ChangeTextServerRequest;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerRequest;
import org.ide.WebWorker.Text.Inserting.InsertTextServerRequest;
import org.ide.WebWorker.User.*;
import org.ide.WebWorker.Workers.IDEWebWorkerGrpc;

public class IDEWebWorkerClient {
    private IDEWebWorkerGrpc.IDEWebWorkerBlockingStub stub;


    public void updateServer(String host, int port) {
        stub = IDEWebWorkerGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }


    public void copy(String filePath, String copyTo, FileType type) {
        stub.copy(CopyServerRequest.newBuilder().setPathToCopy(copyTo).setFileType(type).setRelativeFilePath(filePath).build());
    }

    public void create(String path, FileType type, String name) {
        stub.create(CreateServerRequest.newBuilder().setName(name).setRelativeFilePath(path).setFileType(type).build());
    }

    public void delete(String path, FileType type) {
        stub.delete(DeleteServerRequest.newBuilder().setFileType(type).setRelativeFilePath(path).build());
    }

    public void move(String relativePath, String pathToCopy, FileType type) {
        stub.move(MoveServerRequest.newBuilder().setFileType(type).setRelativeFilePath(relativePath).setPathToMove(pathToCopy).build());
    }

    public void rename(String relativePath, String newName, FileType type) {
        stub.rename(RenameServerRequest.newBuilder().setFileType(type).setRelativeFilePath(relativePath).setNewName(newName).build());
    }

    public void setUserCursor(UserCursor userCursor) {
        stub.setUserCursor(UserCursorServer.newBuilder().setUserCursor(userCursor).build());
    }

    public void setUserHighlighted(UserHighlighted userHighlighted) {
        stub.setUserHighlighted(UserHighlightedServer.newBuilder().setUserCursor(userHighlighted).build());
    }

    public void setUserFilePosition(UserFile file) {
        stub.setUserFilePosition(file);
    }

    public boolean insertText(String filePath, String text, CursorPosition position) {
        return stub.insertText(InsertTextServerRequest.newBuilder().setText(text).setFilePath(filePath).setPosition(position).build()).getCode() == InsertTextCode.Insert_Text_Code_OK;
    }

    public boolean deleteText(String filePath, String textToDelete, HighlightedPosition position) {
        return stub.deleteText(DeleteTextServerRequest.newBuilder().setTextToDelete(textToDelete).setFilePath(filePath).setPosition(position).build()).getCode() == DeleteTextCode.Delete_Text_Code_OK;
    }

    public boolean changeText(String filePath, String textToDelete, String newText, HighlightedPosition position) {
        return stub.changeText(ChangeTextServerRequest.newBuilder().setTextToDelete(textToDelete).setFilePath(filePath)
                .setPosition(position).setTextToInsert(newText).build()).getCode() == ChangeCode.Change_Code_OK;
    }

    public UsersClient getPositions() {
        return stub.getPositions(Empty.getDefaultInstance());
    }
}
