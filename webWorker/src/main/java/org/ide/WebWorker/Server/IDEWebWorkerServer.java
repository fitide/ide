package org.ide.WebWorker.Server;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
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
import org.ide.WebWorker.Roles.ServersRoles;
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
import org.ide.WebWorker.Workers.IDEWebWorkerGrpc;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class IDEWebWorkerServer extends IDEWebWorkerGrpc.IDEWebWorkerImplBase {
    private ServersRoles role;


    @Autowired
    public IDEWebWorkerServer(ServersRoles initRole) {
        super();
        this.role = initRole;
    }



    @Override
    public void copy(CopyServerRequest request, StreamObserver<CopyServerResponse> responseObserver) {
        super.copy(request, responseObserver);
    }

    @Override
    public void create(CreateServerRequest request, StreamObserver<CreateServerResponse> responseObserver) {
        super.create(request, responseObserver);
    }

    @Override
    public void delete(DeleteServerRequest request, StreamObserver<DeleteServerResponse> responseObserver) {
        super.delete(request, responseObserver);
    }

    @Override
    public void move(MoveServerRequest request, StreamObserver<MoveServerResponse> responseObserver) {
        super.move(request, responseObserver);
    }

    @Override
    public void rename(RenameServerRequest request, StreamObserver<RenameServerResponse> responseObserver) {
        super.rename(request, responseObserver);
    }

    @Override
    public void setUserCursor(UserCursorServer request, StreamObserver<Empty> responseObserver) {
        super.setUserCursor(request, responseObserver);
    }

    @Override
    public void setUserHighlighted(UserHighlightedServer request, StreamObserver<Empty> responseObserver) {
        super.setUserHighlighted(request, responseObserver);
    }

    @Override
    public void setUserFilePosition(UserFile request, StreamObserver<Empty> responseObserver) {
        super.setUserFilePosition(request, responseObserver);
    }

    @Override
    public void onConnection(ConnectionRequest request, StreamObserver<ConnectionResponse> responseObserver) {
        super.onConnection(request, responseObserver);
    }

    @Override
    public void updatePositions(UsersClient request, StreamObserver<Empty> responseObserver) {
        super.updatePositions(request, responseObserver);
    }

    @Override
    public void updateUsers(UpdateProgrammersRequest request, StreamObserver<UpdateProgrammersResponse> responseObserver) {
        super.updateUsers(request, responseObserver);
    }

    @Override
    public void insertText(InsertTextServerRequest request, StreamObserver<InsertTextServerResponse> responseObserver) {
        super.insertText(request, responseObserver);
    }

    @Override
    public void deleteText(DeleteTextServerRequest request, StreamObserver<DeleteTextServerResponse> responseObserver) {
        super.deleteText(request, responseObserver);
    }

    @Override
    public void changeText(ChangeTextServerRequest request, StreamObserver<ChangeTextServerResponse> responseObserver) {
        super.changeText(request, responseObserver);
    }
}
