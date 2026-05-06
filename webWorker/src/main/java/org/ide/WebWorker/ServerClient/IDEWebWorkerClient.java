package org.ide.WebWorker.ServerClient;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.ide.WebWorker.Connection.ConnectionRequest;
import org.ide.WebWorker.Connection.ConnectionResponse;
import org.ide.WebWorker.FileSystem.Copy.CopyServerRequest;
import org.ide.WebWorker.FileSystem.Copy.CopyServerResponse;
import org.ide.WebWorker.FileSystem.Create.CreateServerRequest;
import org.ide.WebWorker.FileSystem.Create.CreateServerResponse;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerRequest;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerResponse;
import org.ide.WebWorker.FileSystem.FileSystemComponents.FileType;
import org.ide.WebWorker.FileSystem.FilesGetting.DirectoryRequest;
import org.ide.WebWorker.FileSystem.FilesGetting.DirectoryResponse;
import org.ide.WebWorker.FileSystem.FilesGetting.FileRequest;
import org.ide.WebWorker.FileSystem.FilesGetting.FileResponse;
import org.ide.WebWorker.FileSystem.Move.MoveServerRequest;
import org.ide.WebWorker.FileSystem.Move.MoveServerResponse;
import org.ide.WebWorker.FileSystem.Rename.RenameServerRequest;
import org.ide.WebWorker.FileSystem.Rename.RenameServerResponse;
import org.ide.WebWorker.MainSelecting.UpdateProgrammersRequest;
import org.ide.WebWorker.MainSelecting.UpdateProgrammersResponse;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.Positions.Positions;
import org.ide.WebWorker.Text.Changing.ChangeTextServerRequest;
import org.ide.WebWorker.Text.Changing.ChangeTextServerResponse;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerRequest;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerResponse;
import org.ide.WebWorker.Text.Inserting.InsertTextServerRequest;
import org.ide.WebWorker.Text.Inserting.InsertTextServerResponse;
import org.ide.WebWorker.User.*;
import org.ide.WebWorker.Workers.IDEWebWorkerGrpc;

import java.time.LocalTime;

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

    public void insertText(String filePath, String text, CursorPosition position) {
        stub.insertText(InsertTextServerRequest.newBuilder().setText(text).setFilePath(filePath).setPosition(position).build());
    }

    public void deleteText(String filePath, String textToDelete, HighlightedPosition position) {
        stub.deleteText(DeleteTextServerRequest.newBuilder().setTextToDelete(textToDelete).setFilePath(filePath).setPosition(position).build());
    }

    public void changeText(String filePath, String textToDelete, String newText, HighlightedPosition position) {
        stub.changeText(ChangeTextServerRequest.newBuilder().setTextToDelete(textToDelete).setFilePath(filePath)
                .setPosition(position).setTextToInsert(newText).build());
    }
}
