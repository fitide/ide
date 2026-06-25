package org.ide.WebWorker.Roles;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.ide.IdeControllerWebInt;
import org.ide.WebWorker.Connection.ConnectionRequest;
import org.ide.WebWorker.Connection.ConnectionResponse;
import org.ide.WebWorker.ResponseCodes.ConnectionCode;
import org.ide.WebWorker.FileSystem.Copy.CopyServerRequest;
import org.ide.WebWorker.FileSystem.Copy.CopyServerResponse;
import org.ide.WebWorker.FileSystem.Create.CreateServerRequest;
import org.ide.WebWorker.FileSystem.Create.CreateServerResponse;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerRequest;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerResponse;
import org.ide.WebWorker.FileSystem.FileSystemComponents.Directory;
import org.ide.WebWorker.FileSystem.FileSystemComponents.File;
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
import org.ide.WebWorker.Positions.PositionsTable;
import org.ide.WebWorker.ResponseCodes.*;
import org.ide.WebWorker.Text.Changing.ChangeTextServerRequest;
import org.ide.WebWorker.Text.Changing.ChangeTextServerResponse;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerRequest;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerResponse;
import org.ide.WebWorker.Text.Inserting.InsertTextServerRequest;
import org.ide.WebWorker.Text.Inserting.InsertTextServerResponse;
import org.ide.WebWorker.User.*;
import org.ide.WebWorker.Workers.IDEWebWorkerGrpc;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Leader extends Follower{
    private final Map<User, IDEWebWorkerGrpc.IDEWebWorkerBlockingStub> programmersStub;
    private int lastClientNumber = -1;
    private final int port;
    private final IdeControllerWebInt ideController;
    private final Map<String, String> nameToHost = new HashMap<>();

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
        nameToHost.put(request.getUser().getName(), request.getUser().getHost());
        super.setUserFilePosition(request, responseObserver);
    }

    @Override
    public boolean onConnection(ConnectionRequest request, StreamObserver<ConnectionResponse> responseObserver) {
        var stub = IDEWebWorkerGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(request.getHost(), port).usePlaintext().build());

        this.programmersStub.put(
                User.newBuilder().setHost(request.getHost()).setName(request.getName()).build(),
                stub);

        String rootDir = ideController.getProjectRoot().getFileName().toString();
        responseObserver.onNext(ConnectionResponse.newBuilder()
                .setCode(ConnectionCode.Connection_Code_OK)
                .setDirectory(rootDir)
                .build());
        responseObserver.onCompleted();

        var requestToClientsBuilder = UpdateProgrammersRequest.newBuilder();
        for (var programmer : programmers.keySet()) {
            requestToClientsBuilder.addProgrammers(programmers.get(programmer),
                    Programmer.newBuilder().setHost(programmer.getHost()).setName(programmer.getName()).build());
        }
        var requestToClients = requestToClientsBuilder.build();

        new Thread(() -> {
            for (var p : programmersStub.values()) {
                try { p.updateUsers(requestToClients); } catch (Exception ignored) {}
            }
        }).start();

        return true;
    }

    @Override
    public void updatePositions(UsersClient request, StreamObserver<Empty> responseObserver) {
        var now = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build();
        var usersPositionsRequestBuilder = UsersClient.newBuilder().setTime(timestamp);
        for (var name : positionsTable.usersPositions.keySet()) {
            var userPosition = positionsTable.usersPositions.get(name);
            if (userPosition.file == null) continue;
            var user = User.newBuilder().setHost(this.nameToHost.get(name)).setName(name).build();
            usersPositionsRequestBuilder.addUserFiles(UserFile.newBuilder().setFile(userPosition.file).setUser(user).setTime(timestamp).build());

            if (userPosition.cursorPosition != null) {
                usersPositionsRequestBuilder.addUserCursors(
                        UserCursor.newBuilder().setUser(user).setTime(timestamp).setCursorPosition(userPosition.cursorPosition));
            }
            else if (userPosition.highlightedPosition != null) {
                usersPositionsRequestBuilder.addUserHighlighteds(
                        UserHighlighted.newBuilder().setHighlightedPosition(userPosition.highlightedPosition).setUser(user).setTime(timestamp));
            }
        }

        for (var programmer : programmersStub.values()) {
            programmer.updatePositions(usersPositionsRequestBuilder.build());
        }
    }

    @Override
    public void updateUsers(UpdateProgrammersRequest request, StreamObserver<UpdateProgrammersResponse> responseObserver) {
        super.updateUsers(request, responseObserver);
    }

    @Override
    public boolean insertText(InsertTextServerRequest request, StreamObserver<InsertTextServerResponse> responseObserver) {
        var res = ideController.insertText(request.getFilePath(), request.getText(), request.getPosition(), request.getUser());

        if (res) {
            responseObserver.onNext(InsertTextServerResponse.newBuilder().setCode(InsertTextCode.Insert_Text_Code_OK).build());
            responseObserver.onCompleted();
            for (var programmer : programmersStub.values()) {
                try {
                    programmer.insertText(request);
                } catch (Exception ignored) {
                    // peer unreachable — keep delivering to the others
                }
            }
        }

        return res;
    }

    @Override
    public boolean deleteText(DeleteTextServerRequest request, StreamObserver<DeleteTextServerResponse> responseObserver) {
        var res = ideController.deleteText(request.getFilePath(), request.getTextToDelete(), request.getPosition(), request.getUser());

        if (res) {
            responseObserver.onNext(DeleteTextServerResponse.newBuilder().setCode(DeleteTextCode.Delete_Text_Code_OK).build());
            responseObserver.onCompleted();
            for (var programmer : programmersStub.values()) {
                try {
                    programmer.deleteText(request);
                } catch (Exception ignored) {
                    // peer unreachable — keep delivering to the others
                }
            }
        }

        return res;
    }

    @Override
    public boolean changeText(ChangeTextServerRequest request, StreamObserver<ChangeTextServerResponse> responseObserver) {
        var res = ideController.changeText(request.getFilePath(), request.getTextToDelete(), request.getTextToInsert(),
                request.getPosition(), request.getUser());

        if (res) {
            responseObserver.onNext(ChangeTextServerResponse.newBuilder().setCode(ChangeCode.Change_Code_OK).build());
            responseObserver.onCompleted();
            for (var programmer : programmersStub.values()) {
                try {
                    programmer.changeText(request);
                } catch (Exception ignored) {
                    // peer unreachable — keep delivering to the others
                }
            }
        }

        return res;
    }

    @Override
    public void shareDir(DirectoryRequest request, StreamObserver<DirectoryResponse> responseObserver) throws Exception {
        var dir = ideController.getDirData(request.getDirectoryRelativePath());

        var directoryBuilder = Directory.newBuilder();
        directoryBuilder.setRelativeDirPath(request.getDirectoryRelativePath());
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
    public void shareFile(FileRequest request, StreamObserver<FileResponse> responseObserver) throws Exception {
        byte[] bytes = ideController.getFileContent(request.getFileRelativePath(), request.getIsInitingProject());

        var fileBuilder = File.newBuilder()
                .setRelativeFilePath(request.getFileRelativePath())
                .setContent(com.google.protobuf.ByteString.copyFrom(bytes));

        responseObserver.onNext(FileResponse.newBuilder().setFile(fileBuilder).build());
        responseObserver.onCompleted();
    }

    @Override
    public void connect() {
        // лидер не коннектится ни к кому
    }

    @Override
    public PositionsTable getPositionsTable() {
        return positionsTable;
    }

    @Override
    public void getPositions(Empty request, StreamObserver<UsersClient> responseObserver) {
        var now = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build();
        var builder = UsersClient.newBuilder().setTime(timestamp);
        for (var name : positionsTable.usersPositions.keySet()) {
            var userPosition = positionsTable.usersPositions.get(name);
            if (userPosition.file == null) continue;
            var user = User.newBuilder().setHost(this.nameToHost.get(name)).setName(name).build();
            builder.addUserFiles(UserFile.newBuilder().setFile(userPosition.file).setUser(user).setTime(timestamp).build());
            if (userPosition.cursorPosition != null) {
                builder.addUserCursors(UserCursor.newBuilder().setUser(user).setTime(timestamp).setCursorPosition(userPosition.cursorPosition));
            } else if (userPosition.highlightedPosition != null) {
                builder.addUserHighlighteds(UserHighlighted.newBuilder().setHighlightedPosition(userPosition.highlightedPosition).setUser(user).setTime(timestamp));
            }
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private void onError(IDEWebWorkerGrpc.IDEWebWorkerBlockingStub stub) {
        stub.shareDir(DirectoryRequest.newBuilder().getDefaultInstanceForType());
    };

}
