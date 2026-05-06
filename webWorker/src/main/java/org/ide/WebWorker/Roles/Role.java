package org.ide.WebWorker.Roles;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.ide.WebWorker.Connection.ConnectionRequest;
import org.ide.WebWorker.Connection.ConnectionResponse;
import org.ide.WebWorker.FileSystem.Copy.CopyServerRequest;
import org.ide.WebWorker.FileSystem.Copy.CopyServerResponse;
import org.ide.WebWorker.FileSystem.Create.CreateServerRequest;
import org.ide.WebWorker.FileSystem.Create.CreateServerResponse;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerRequest;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerResponse;
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
import org.ide.WebWorker.Text.Changing.ChangeTextServerRequest;
import org.ide.WebWorker.Text.Changing.ChangeTextServerResponse;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerRequest;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerResponse;
import org.ide.WebWorker.Text.Inserting.InsertTextServerRequest;
import org.ide.WebWorker.Text.Inserting.InsertTextServerResponse;
import org.ide.WebWorker.User.*;

public interface Role {


    boolean copy(CopyServerRequest request, StreamObserver<CopyServerResponse> responseObserver);
    boolean create(CreateServerRequest request, StreamObserver<CreateServerResponse> responseObserver);
    boolean delete(DeleteServerRequest request, StreamObserver<DeleteServerResponse> responseObserver);
    boolean move(MoveServerRequest request, StreamObserver<MoveServerResponse> responseObserver);
    boolean rename(RenameServerRequest request, StreamObserver<RenameServerResponse> responseObserver);


    void setUserCursor(UserCursorServer request, StreamObserver<Empty> responseObserver);
    void setUserHighlighted(UserHighlightedServer request, StreamObserver<Empty> responseObserver);
    void setUserFilePosition(UserFile request, StreamObserver<Empty> responseObserver);


    boolean onConnection(ConnectionRequest request, StreamObserver<ConnectionResponse> responseObserver);
    void updatePositions(UsersClient request, StreamObserver<Empty> responseObserver);
    void updateUsers(UpdateProgrammersRequest request, StreamObserver<UpdateProgrammersResponse> responseObserver);

    boolean insertText(InsertTextServerRequest request, StreamObserver<InsertTextServerResponse> responseObserver);
    boolean deleteText(DeleteTextServerRequest request, StreamObserver<DeleteTextServerResponse> responseObserver);
    boolean changeText(ChangeTextServerRequest request, StreamObserver<ChangeTextServerResponse> responseObserver);

    void shareDir(DirectoryRequest request, StreamObserver<DirectoryResponse> responseObserver);

    void shareFile(FileRequest request, StreamObserver<FileResponse> responseObserver);

    User getNextLeader();

    void updateLeader();
}
