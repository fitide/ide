package org.ide.WebWorker.Roles;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.ide.IdeControllerWebInt;
import org.ide.WebWorker.Connection.ConnectionRequest;
import org.ide.WebWorker.Connection.ConnectionResponse;
import org.ide.WebWorker.FileSystem.Copy.CopyServerRequest;
import org.ide.WebWorker.FileSystem.Copy.CopyServerResponse;
import org.ide.WebWorker.FileSystem.Create.CreateServerRequest;
import org.ide.WebWorker.FileSystem.Create.CreateServerResponse;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerRequest;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerResponse;
import org.ide.WebWorker.FileSystem.FileSystemComponents.Directory;
import org.ide.WebWorker.FileSystem.FileSystemComponents.File;
import org.ide.WebWorker.FileSystem.FileSystemComponents.FileType;
import org.ide.WebWorker.FileSystem.FileSystemComponents.InboundFileSystemComponent;
import org.ide.WebWorker.FileSystem.FilesGetting.DirectoryRequest;
import org.ide.WebWorker.FileSystem.FilesGetting.DirectoryResponse;
import org.ide.WebWorker.FileSystem.FilesGetting.FileRequest;
import org.ide.WebWorker.FileSystem.FilesGetting.FileResponse;
import org.ide.WebWorker.FileSystem.Move.MoveServerRequest;
import org.ide.WebWorker.FileSystem.Move.MoveServerResponse;
import org.ide.WebWorker.FileSystem.Rename.RenameServerRequest;
import org.ide.WebWorker.FileSystem.Rename.RenameServerResponse;
import org.ide.WebWorker.MainSelecting.Programmer;
import org.ide.WebWorker.MainSelecting.UpdateProgrammersRequest;
import org.ide.WebWorker.MainSelecting.UpdateProgrammersResponse;
import org.ide.WebWorker.ResponseCodes.*;
import org.ide.WebWorker.Text.Changing.ChangeTextServerRequest;
import org.ide.WebWorker.Text.Changing.ChangeTextServerResponse;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerRequest;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerResponse;
import org.ide.WebWorker.Text.Inserting.InsertTextServerRequest;
import org.ide.WebWorker.Text.Inserting.InsertTextServerResponse;
import org.ide.WebWorker.Tools.Pair;
import org.ide.WebWorker.User.*;
import org.ide.WebWorker.Workers.IDEWebWorkerGrpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Leader extends Follower{
    private final Map<User, IDEWebWorkerGrpc.IDEWebWorkerBlockingStub> programmersStub;
    private int lastClientNumber = -1;
    private final int port;
    private final IdeControllerWebInt ideController;

    public Leader(String curMain, IdeControllerWebInt ideController, Map<User, Integer> programmers, int port, String host, String name) {
        super(curMain, ideController, host, port, name);
        this.programmersStub = new ConcurrentHashMap<>();
        this.ideController = ideController;
        for (var programmer : programmers.keySet()) {
            this.programmersStub.put(programmer,
                            IDEWebWorkerGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(programmer.getHost(), port).usePlaintext().build()));
        }

        if (!programmers.isEmpty()) lastClientNumber = 0;
        else {
            for (var programmerId : programmers.values()) {
                lastClientNumber = lastClientNumber < programmerId ? programmerId + 1 : lastClientNumber;
            }
        }

        this.port = port;
    }

    @Override
    public boolean copy(CopyServerRequest request, StreamObserver<CopyServerResponse> responseObserver) {
        if (!super.copy(request, responseObserver)) return false;
        for (var programmer : programmersStub.values()) {
            var response = programmer.copy(request);

            if (response.getCode() != CopyCode.Copy_Code_OK) onError(programmer);
        }
        return true;
    }

    @Override
    public boolean create(CreateServerRequest request, StreamObserver<CreateServerResponse> responseObserver) {
        if (!super.create(request, responseObserver)) return false;
        for (var programmer : programmersStub.values()) {
            var response = programmer.create(request);

            if (response.getCode() != CreateFileCode.Create_File_Code_OK) onError(programmer);
        }
        return true;
    }

    @Override
    public boolean delete(DeleteServerRequest request, StreamObserver<DeleteServerResponse> responseObserver) {
        if (!super.delete(request, responseObserver)) return false;
        for (var programmer : programmersStub.values()) {
            var response = programmer.delete(request);

            if (response.getCode() != DeleteFileCode.Delete_File_Code_OK) onError(programmer);
        }
        return true;
    }

    @Override
    public boolean move(MoveServerRequest request, StreamObserver<MoveServerResponse> responseObserver) {
        if (!super.move(request, responseObserver)) return false;
        for (var programmer : programmersStub.values()) {
            var response = programmer.move(request);

            if (response.getCode() != MoveCode.Move_Code_OK) onError(programmer);
        }
        return true;
    }

    @Override
    public boolean rename(RenameServerRequest request, StreamObserver<RenameServerResponse> responseObserver) {
        if (!super.rename(request, responseObserver)) return false;
        for (var programmer : programmersStub.values()) {
            var response = programmer.rename(request);

            if (response.getCode() != RenameCode.Rename_Code_OK) onError(programmer);
        }
        return true;
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
    public boolean onConnection(ConnectionRequest request, StreamObserver<ConnectionResponse> responseObserver) {
        var stub = IDEWebWorkerGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(request.getHost(), port).usePlaintext().build());

        this.programmersStub.put(
                User.newBuilder().setHost(request.getHost()).setName(request.getName()).build(),
                stub);

        var requestToClientsBuilder = UpdateProgrammersRequest.newBuilder();
        for (var programmer : programmers.keySet()) {
            requestToClientsBuilder.addProgrammers(programmers.get(programmer),
                    Programmer.newBuilder().setHost(programmer.getHost()).setName(programmer.getName()).build());
        }
        var requestToClients = requestToClientsBuilder.build();

        for (var programmer : programmersStub.values()) {
            var response = programmer.updateUsers(requestToClients);
        }

        return true;
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
    public boolean insertText(InsertTextServerRequest request, StreamObserver<InsertTextServerResponse> responseObserver) {
        // TODO: implement
        return true;
    }

    @Override
    public boolean deleteText(DeleteTextServerRequest request, StreamObserver<DeleteTextServerResponse> responseObserver) {
        // TODO: implement
        return true;
    }

    @Override
    public boolean changeText(ChangeTextServerRequest request, StreamObserver<ChangeTextServerResponse> responseObserver) {
        // TODO: implement
        return true;
    }

    @Override
    public void shareDir(DirectoryRequest request, StreamObserver<DirectoryResponse> responseObserver) {
        var dir = ideController.getDirData(request.getDirectoryRelativePath());

        var directoryBuilder = Directory.newBuilder();
        int number = 0;
        for (var dirData : dir) {
            var compBuilder = InboundFileSystemComponent.newBuilder();
            compBuilder.setRelativePath(dirData.first).setType(dirData.second);
            directoryBuilder.addInbounds(number++, compBuilder.build());
        }

        responseObserver.onNext(DirectoryResponse.newBuilder().setDirectory(directoryBuilder).build());
        responseObserver.onCompleted();
    }

    @Override
    public void shareFile(FileRequest request, StreamObserver<FileResponse> responseObserver) {
        var file = ideController.getFileContent(request.getFileRelativePath());

        var fileBuilder = File.newBuilder().setRelativeFilePath(request.getFileRelativePath());
        fileBuilder.addAllContent(file);

        responseObserver.onNext(FileResponse.newBuilder().setFile(fileBuilder).build());
        responseObserver.onCompleted();
    }

    private void onError(IDEWebWorkerGrpc.IDEWebWorkerBlockingStub stub) {
        stub.shareDir(DirectoryRequest.newBuilder().getDefaultInstanceForType());
    };

}
