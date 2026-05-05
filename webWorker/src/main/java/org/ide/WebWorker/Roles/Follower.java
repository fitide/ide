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
import org.ide.WebWorker.FileSystem.Move.MoveServerRequest;
import org.ide.WebWorker.FileSystem.Move.MoveServerResponse;
import org.ide.WebWorker.FileSystem.Rename.RenameServerRequest;
import org.ide.WebWorker.FileSystem.Rename.RenameServerResponse;
import org.ide.WebWorker.MainSelecting.UpdateProgrammersRequest;
import org.ide.WebWorker.MainSelecting.UpdateProgrammersResponse;
import org.ide.WebWorker.Positions.PositionsTable;
import org.ide.WebWorker.Text.Changing.ChangeTextServerRequest;
import org.ide.WebWorker.Text.Changing.ChangeTextServerResponse;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerRequest;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerResponse;
import org.ide.WebWorker.Text.Inserting.InsertTextServerRequest;
import org.ide.WebWorker.Text.Inserting.InsertTextServerResponse;
import org.ide.WebWorker.User.UserCursorServer;
import org.ide.WebWorker.User.UserFile;
import org.ide.WebWorker.User.UserHighlightedServer;
import org.ide.WebWorker.User.UsersClient;

public class Follower implements Role {
    private PositionsTable positionsTable;


    Follower(PositionsTable table) {
        this.positionsTable = table;
    }


    @Override
    public void copy(CopyServerRequest request, StreamObserver<CopyServerResponse> responseObserver) {

    }

    @Override
    public void create(CreateServerRequest request, StreamObserver<CreateServerResponse> responseObserver) {

    }

    @Override
    public void delete(DeleteServerRequest request, StreamObserver<DeleteServerResponse> responseObserver) {

    }

    @Override
    public void move(MoveServerRequest request, StreamObserver<MoveServerResponse> responseObserver) {

    }

    @Override
    public void rename(RenameServerRequest request, StreamObserver<RenameServerResponse> responseObserver) {

    }

    @Override
    public void setUserCursor(UserCursorServer request, StreamObserver<Empty> responseObserver) {
        positionsTable.updateCursor(request.);
    }

    @Override
    public void setUserHighlighted(UserHighlightedServer request, StreamObserver<Empty> responseObserver) {

    }

    @Override
    public void setUserFilePosition(UserFile request, StreamObserver<Empty> responseObserver) {

    }

    @Override
    public void onConnection(ConnectionRequest request, StreamObserver<ConnectionResponse> responseObserver) {
        return;
    }

    @Override
    public void updatePositions(UsersClient request, StreamObserver<Empty> responseObserver) {

    }

    @Override
    public void updateUsers(UpdateProgrammersRequest request, StreamObserver<UpdateProgrammersResponse> responseObserver) {

    }

    @Override
    public void insertText(InsertTextServerRequest request, StreamObserver<InsertTextServerResponse> responseObserver) {

    }

    @Override
    public void deleteText(DeleteTextServerRequest request, StreamObserver<DeleteTextServerResponse> responseObserver) {

    }

    @Override
    public void changeText(ChangeTextServerRequest request, StreamObserver<ChangeTextServerResponse> responseObserver) {

    }
}
