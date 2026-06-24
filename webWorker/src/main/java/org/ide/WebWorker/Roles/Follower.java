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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Follower implements Role {
    protected final PositionsTable positionsTable;
    private String curMain;
    private final Object fileSystemLock = new Object();
    private final IdeControllerWebInt ideController;
    protected final Map<User, Integer> programmers = new HashMap<>();

    private User me;
    private final String host;
    private final String name;
    private final int port;
    private IDEWebWorkerGrpc.IDEWebWorkerBlockingStub stub;

    private LocalTime lastTimeUpdated;

    private String rootDir;

    public Follower(String curMain, IdeControllerWebInt ideController, String host, int port, String name) {
        this.positionsTable = new PositionsTable();
        this.curMain = curMain;
        this.ideController = ideController;
        this.host = host;
        this.name = name;
        this.port = port;
        this.stub = IDEWebWorkerGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(curMain, port).usePlaintext().build());
    }

    public void connect() {
        rootDir = stub.onConnection(ConnectionRequest.newBuilder().setHost(host).setName(name).build()).getDirectory();
        getDirectory(rootDir);
        ideController.reloadPluginsAfterSync();
    }

    @Override
    public boolean copy(CopyServerRequest request, StreamObserver<CopyServerResponse> responseObserver) {
        synchronized (fileSystemLock) {
            var fileType = request.getFileType();
            try {
                switch (fileType) {
                    case REGULAR -> ideController.copyFile(Paths.get(request.getRelativeFilePath()), Paths.get(request.getPathToCopy()));
                    case DIRECTORY -> ideController.copyDir(Paths.get(request.getRelativeFilePath()), Paths.get(request.getPathToCopy()));
                    default -> {
                        responseObserver.onNext(CopyServerResponse.newBuilder().setCode(CopyCode.UNRECOGNIZED).build());
                        return false;
                    }
                }
            } catch (Exception e) {
                responseObserver.onNext(CopyServerResponse.newBuilder().setCode(CopyCode.Copy_Code_FAILED).setError(e.getMessage()).build());
                return false;
            }

            responseObserver.onNext(CopyServerResponse.newBuilder().setCode(CopyCode.Copy_Code_OK).build());
        }

        responseObserver.onCompleted();
        return true;
    }

    @Override
    public boolean create(CreateServerRequest request, StreamObserver<CreateServerResponse> responseObserver) {
        synchronized (fileSystemLock) {
            var fileType = request.getFileType();
            try {
                Path base = ideController.getProjectRoot().resolve(request.getRelativeFilePath());
                switch (fileType) {
                    case REGULAR   -> ideController.createFile(base, request.getName());
                    case DIRECTORY -> ideController.createDir(base, request.getName());
                    default -> {
                        responseObserver.onNext(CreateServerResponse.newBuilder().setCode(CreateFileCode.UNRECOGNIZED).build());
                        return false;
                    }
                }
            } catch (Exception e) {
                responseObserver.onNext(CreateServerResponse.newBuilder().setCode(CreateFileCode.Create_File_Code_FAILED).setError(e.getMessage()).build());
                return false;
            }

            responseObserver.onNext(CreateServerResponse.newBuilder().setCode(CreateFileCode.Create_File_Code_OK).build());
        }

        responseObserver.onCompleted();
        return true;
    }

    @Override
    public boolean delete(DeleteServerRequest request, StreamObserver<DeleteServerResponse> responseObserver) {
        synchronized (fileSystemLock) {
            var fileType = request.getFileType();
            try {
                Path base = ideController.getProjectRoot().resolve(request.getRelativeFilePath());
                switch (fileType) {
                    case REGULAR -> ideController.deleteFile(base);
                    case DIRECTORY -> ideController.deleteDir(base);
                    default -> {
                        responseObserver.onNext(DeleteServerResponse.newBuilder().setCode(DeleteFileCode.UNRECOGNIZED).build());
                        return false;
                    }
                }
            } catch (Exception e) {
                responseObserver.onNext(DeleteServerResponse.newBuilder().setCode(DeleteFileCode.Delete_File_Code_FAILED).setError(e.getMessage()).build());
                return false;
            }

            responseObserver.onNext(DeleteServerResponse.newBuilder().setCode(DeleteFileCode.Delete_File_Code_OK).build());
        }

        responseObserver.onCompleted();
        return true;
    }

    @Override
    public boolean move(MoveServerRequest request, StreamObserver<MoveServerResponse> responseObserver) {
        synchronized (fileSystemLock) {
            var fileType = request.getFileType();
            try {
                switch (fileType) {
                    case REGULAR -> ideController.moveFile(Paths.get(request.getRelativeFilePath()), Paths.get(request.getPathToMove()));
                    case DIRECTORY -> ideController.moveFile(Paths.get(request.getRelativeFilePath()), Paths.get(request.getPathToMove()));
                    default -> {
                        responseObserver.onNext(MoveServerResponse.newBuilder().setCode(MoveCode.UNRECOGNIZED).build());
                        return false;
                    }
                }
            } catch (Exception e) {
                responseObserver.onNext(MoveServerResponse.newBuilder().setCode(MoveCode.Move_Code_FAILED).setError(e.getMessage()).build());
                return false;
            }

            responseObserver.onNext(MoveServerResponse.newBuilder().setCode(MoveCode.Move_Code_OK).build());
        }

        responseObserver.onCompleted();
        return true;
    }

    @Override
    public boolean rename(RenameServerRequest request, StreamObserver<RenameServerResponse> responseObserver) {
        synchronized (fileSystemLock) {
            var fileType = request.getFileType();
            try {
                Path base = ideController.getProjectRoot().resolve(request.getRelativeFilePath());
                switch (fileType) {
                    case REGULAR   -> ideController.renameFile(base, request.getNewName());
                    case DIRECTORY -> ideController.renameDir(base, request.getNewName());
                    default -> {
                        responseObserver.onNext(RenameServerResponse.newBuilder().setCode(RenameCode.UNRECOGNIZED).build());
                        return false;
                    }
                }
            } catch (Exception e) {
                responseObserver.onNext(RenameServerResponse.newBuilder().setCode(RenameCode.Rename_Code_FAILED).setError(e.getMessage()).build());
                return false;
            }

            responseObserver.onNext(RenameServerResponse.newBuilder().setCode(RenameCode.Rename_Code_OK).build());
        }

        responseObserver.onCompleted();
        return true;
    }

    @Override
    public void setUserCursor(UserCursorServer request, StreamObserver<Empty> responseObserver) {
        setUserCursorClient(request.getUserCursor());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private void setUserCursorClient(UserCursor cursor) {
        this.positionsTable.updateCursor(cursor.getUser().getName(), cursor);
    }

    @Override
    public void setUserHighlighted(UserHighlightedServer request, StreamObserver<Empty> responseObserver) {
        setUserHighlightedClient(request.getUserCursor());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private void setUserHighlightedClient(UserHighlighted userHighlighted) {
        this.positionsTable.updateHighlited(userHighlighted.getUser().getName(), userHighlighted);
    }

    @Override
    public void setUserFilePosition(UserFile request, StreamObserver<Empty> responseObserver) {
        this.setUserFilePositionClient(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private void setUserFilePositionClient(UserFile userFile) {
        this.positionsTable.updateFile(userFile.getUser().getName(), userFile);
    }

    @Override
    public boolean onConnection(ConnectionRequest request, StreamObserver<ConnectionResponse> responseObserver) {
        responseObserver.onNext(ConnectionResponse.newBuilder().setCode(ConnectionCode.Connection_Code_Request_To_Client)
                .setError(curMain).build());

        responseObserver.onCompleted();
        return false;
    }

    @Override
    public void updatePositions(UsersClient request, StreamObserver<Empty> responseObserver) {
        lastTimeUpdated = (Instant.ofEpochSecond(request.getTime().getSeconds())).atZone(ZoneId.systemDefault()).toLocalTime();
        for (var file : request.getUserFilesList()) {
            setUserFilePositionClient(file);
        }
        for (var cursor : request.getUserCursorsList()) {
            setUserCursorClient(cursor);
        }
        for (var highligthed : request.getUserHighlightedsList()) {
            setUserHighlightedClient(highligthed);
        }
    }

    @Override
    public void updateUsers(UpdateProgrammersRequest request, StreamObserver<UpdateProgrammersResponse> responseObserver) {
        for (var programmer : request.getProgrammersList()) {
            var user = User.newBuilder().
                    setHost(programmer.getHost()).setName(programmer.getName()).build();
            if (Objects.equals(programmer.getHost(), host)) {
                me = user;
            }
            programmers.put(user, programmer.getClientNumber());
        }
    }

    @Override
    public boolean insertText(InsertTextServerRequest request, StreamObserver<InsertTextServerResponse> responseObserver) {
        return ideController.insertText(request.getFilePath(), request.getText(), request.getPosition(), request.getUser());
    }

    @Override
    public boolean deleteText(DeleteTextServerRequest request, StreamObserver<DeleteTextServerResponse> responseObserver) {
        return ideController.deleteText(request.getFilePath(), request.getTextToDelete(), request.getPosition(), request.getUser());
    }

    @Override
    public boolean changeText(ChangeTextServerRequest request, StreamObserver<ChangeTextServerResponse> responseObserver) {
        return ideController.changeText(request.getFilePath(), request.getTextToDelete(), request.getTextToInsert(),
                request.getPosition(), request.getUser());
    }

    @Override
    public void shareDir(DirectoryRequest request, StreamObserver<DirectoryResponse> responseObserver) throws Exception {
        getDirectory(rootDir);
    }

    @Override
    public void shareFile(FileRequest request, StreamObserver<FileResponse> responseObserver) throws Exception {
        getDirectory(rootDir);
    }

    @Override
    public PositionsTable getPositionsTable() {
        return positionsTable;
    }

    @Override
    public void getPositions(Empty request, StreamObserver<UsersClient> responseObserver) {
        responseObserver.onNext(UsersClient.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public User getNextLeader() {
        User minUser = null;
        for (var programmer : programmers.keySet()) {
            if (minUser == null || programmers.get(minUser) > programmers.get(programmer)) {
                minUser = programmer;
            }
        }

        return minUser;
    }

    @Override
    public void updateLeader() {
        var newLeader = getNextLeader();
        this.curMain = newLeader.getHost();
        this.stub = IDEWebWorkerGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(newLeader.getHost(), port).usePlaintext().build());
    }

    @Override
    public LocalTime getLastTimeUpdated() {
        return lastTimeUpdated;
    }

    private void getDirectory(String directory) {
        var dir = stub.shareDir(DirectoryRequest.newBuilder().setDirectoryRelativePath(directory).build()).getDirectory();
        ideController.setDir(dir);
        for (var file : dir.getInboundsList()) {
            if (file.getType() == FileType.REGULAR) {
                getFile(file.getRelativePath(), true);
            } else if (file.getType() == FileType.DIRECTORY) {
                getDirectory(file.getRelativePath());
            }
        }

    }

    private void getFile(String filePath, boolean isInitingProject) {
        var file = stub.shareFile(FileRequest.newBuilder().setFileRelativePath(filePath).setIsInitingProject(isInitingProject).build());
        ideController.setFile(file.getFile());
    }


    @Override
    public void updateFile(String filePath) {
        getFile(filePath, false);
    }

    public String getCurMain() {
        return curMain;
    }
}
