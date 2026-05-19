package org.ide.WebWorker.ServerClient;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.ide.IdeControllerWebInt;
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
import org.ide.WebWorker.Positions.PositionsTable;
import org.ide.WebWorker.Roles.Follower;
import org.ide.WebWorker.Roles.Leader;
import org.ide.WebWorker.Roles.Role;
import org.ide.WebWorker.Roles.ServersRoles;
import org.ide.WebWorker.Text.Changing.ChangeTextServerRequest;
import org.ide.WebWorker.Text.Changing.ChangeTextServerResponse;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerRequest;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerResponse;
import org.ide.WebWorker.Text.Inserting.InsertTextServerRequest;
import org.ide.WebWorker.Text.Inserting.InsertTextServerResponse;
import org.ide.WebWorker.User.*;
import org.ide.WebWorker.Workers.IDEWebWorkerGrpc;

import java.net.Inet4Address;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@GrpcService
public class IDEWebWorkerServer extends IDEWebWorkerGrpc.IDEWebWorkerImplBase {
    private ServersRoles role;
    private Role roleService;
    private final IdeControllerWebInt ideController;
    private final int port;
    private final String host;
    private  final String name;
    private final ReadWriteLock roleLock = new ReentrantReadWriteLock();

    public LocalTime getLastTimeUpdated() {
        return lastTimeUpdated;
    }

    public ServersRoles getRole() {
        return role;
    }

    private LocalTime lastTimeUpdated = LocalTime.now();

    public IDEWebWorkerServer(ServersRoles initRole, IdeControllerWebInt ideController, int port, String host, String name) throws Exception {
        this(initRole, ideController, Inet4Address.getLocalHost().getHostAddress(), port, host, name);
    }

    public IDEWebWorkerServer(ServersRoles initRole, IdeControllerWebInt ideController, String curMain, int port, String host, String name) throws Exception {
        super();
        this.role = initRole;
        this.port = port;
        this.host = host;
        this.name = name;
        this.ideController = ideController;
        this.roleService = createRole(initRole, curMain, host);
    }

    private Role createRole(ServersRoles role, String curMain, String host) throws Exception {
        switch (role) {
            case Follower ->{
                return new Follower(curMain, ideController, host, port, name);
            }
            case Main -> {
                return new Leader(curMain, ideController, new HashMap<>(), port, host, name);
            }
            case null, default -> throw new Exception("Creating role exception: unknown role");
        }
    }

    interface Action {
        void execute() throws Exception;
    }

    @Override
    public void copy(CopyServerRequest request, StreamObserver<CopyServerResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.copy(request, responseObserver);});
    }

    @Override
    public void create(CreateServerRequest request, StreamObserver<CreateServerResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.create(request, responseObserver);});
    }

    @Override
    public void delete(DeleteServerRequest request, StreamObserver<DeleteServerResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.delete(request, responseObserver);});
    }

    @Override
    public void move(MoveServerRequest request, StreamObserver<MoveServerResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.move(request, responseObserver);});
    }

    @Override
    public void rename(RenameServerRequest request, StreamObserver<RenameServerResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.rename(request, responseObserver);});
    }

    @Override
    public void setUserCursor(UserCursorServer request, StreamObserver<Empty> responseObserver) {
        withLock(roleLock, () -> {roleService.setUserCursor(request, responseObserver);});
    }

    @Override
    public void setUserHighlighted(UserHighlightedServer request, StreamObserver<Empty> responseObserver) {
        withLock(roleLock, () -> {roleService.setUserHighlighted(request, responseObserver);});
    }

    @Override
    public void setUserFilePosition(UserFile request, StreamObserver<Empty> responseObserver) {
        withLock(roleLock, () -> {roleService.setUserFilePosition(request, responseObserver);});
    }

    @Override
    public void onConnection(ConnectionRequest request, StreamObserver<ConnectionResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.onConnection(request, responseObserver);});
    }

    @Override
    public void updatePositions(UsersClient request, StreamObserver<Empty> responseObserver) {
        withLock(roleLock, () -> {roleService.updatePositions(request, responseObserver);});
    }

    @Override
    public void updateUsers(UpdateProgrammersRequest request, StreamObserver<UpdateProgrammersResponse> responseObserver) {
        this.lastTimeUpdated = LocalTime.now();
        withLock(roleLock, () -> {roleService.updateUsers(request, responseObserver);});
    }

    @Override
    public void insertText(InsertTextServerRequest request, StreamObserver<InsertTextServerResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.insertText(request, responseObserver);});
    }

    @Override
    public void deleteText(DeleteTextServerRequest request, StreamObserver<DeleteTextServerResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.deleteText(request, responseObserver);});
    }

    @Override
    public void changeText(ChangeTextServerRequest request, StreamObserver<ChangeTextServerResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.changeText(request, responseObserver);});
    }

    @Override
    public void shareDir(DirectoryRequest request, StreamObserver<DirectoryResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.shareDir(request, responseObserver);});
    }

    @Override
    public void shareFile(FileRequest request, StreamObserver<FileResponse> responseObserver) {
        withLock(roleLock, () -> {roleService.shareFile(request, responseObserver);});
    }

    @Override
    public void getPositions(Empty request, StreamObserver<UsersClient> responseObserver) {
        withLock(roleLock, () -> {roleService.getPositions(request, responseObserver);});
    }

    public void applyPositions(UsersClient positions) {
        roleService.updatePositions(positions, null);
    }

    private void withLock(ReadWriteLock lock, Action action) {
        try {
            lock.readLock().lock();
            action.execute();
            lock.readLock().unlock();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        roleService.connect();
    }

    public void becomeLeader() throws Exception {
        roleLock.writeLock().lock();
        this.roleService = createRole(ServersRoles.Main, host, host);
        roleLock.writeLock().unlock();
    }

    public User setLeader() throws Exception {
        if (getNextLeader().getName().equals(name)) {
            becomeLeader();
            return null;
        }


        return getNextLeader();
    }

    public User getNextLeader() {
        return roleService.getNextLeader();
    }

    public PositionsTable getPositionsTable() {
        return roleService.getPositionsTable();
    }

    public void updateFile(String filePath) {
        if (role == ServersRoles.Follower) {
            roleService.updateFile(filePath);
        }
    }
}
