package org.ide.WebWorker;

import io.grpc.ServerBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.ide.IdeControllerWebInt;
import org.ide.WebWorker.FileSystem.Copy.CopyServerRequest;
import org.ide.WebWorker.FileSystem.Create.CreateServerRequest;
import org.ide.WebWorker.FileSystem.Delete.DeleteServerRequest;
import org.ide.WebWorker.FileSystem.FileSystemComponents.FileType;
import org.ide.WebWorker.FileSystem.Move.MoveServerRequest;
import org.ide.WebWorker.FileSystem.Rename.RenameServerRequest;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.Roles.Leader;
import org.ide.WebWorker.Roles.ServersRoles;
import org.ide.WebWorker.ServerClient.IDEWebWorkerClient;
import org.ide.WebWorker.ServerClient.IDEWebWorkerServer;
import org.ide.WebWorker.Text.Changing.ChangeTextServerRequest;
import org.ide.WebWorker.Text.Deleting.DeleteTextServerRequest;
import org.ide.WebWorker.Text.Inserting.InsertTextServerRequest;
import org.ide.WebWorker.User.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.concurrent.locks.ReadWriteLock;

public class WebController {
    private final IdeControllerWebInt ideController;

    private static int skippingUpdationsLimit = 5;

    private final String name;
    private final String host;
    private final int port = 2255;
    private final IDEWebWorkerServer server;
    private final IDEWebWorkerClient client;
    private ServersRoles curRole;
    private String curLeader;
    private LocalTime lastTimeUpdated;


    /**
     * Constructor for Leader
     *
     * @param ideController
     * @param name
     * @param host
     * @throws Exception
     */
    public WebController(IdeControllerWebInt ideController, String name, String host) throws Exception {
        this.ideController = ideController;
        this.name = name;
        this.host = host;
        this.curLeader = host;
        curRole = ServersRoles.Main;
        this.server = new IDEWebWorkerServer(ServersRoles.Main, ideController, port, host, name);
        ServerBuilder.forPort(port).addService(server).build().start();
        this.client = new IDEWebWorkerClient();
        client.updateServer(host, port);
        initDemons();
    }


    /**
     * Constructor for Follower
     *
     * @param ideController
     * @param name
     * @param host
     * @param identificationString
     * @throws Exception
     */
    public WebController(IdeControllerWebInt ideController, String name, String host, String identificationString) throws Exception {
        this.ideController = ideController;
        this.name = name;
        this.host = host;
        curLeader = decodeCodeToConnect(identificationString);
        this.server = new IDEWebWorkerServer(ServersRoles.Follower, ideController, curLeader, port, host, name);
        ServerBuilder.forPort(port).addService(server).build().start();
        this.client = new IDEWebWorkerClient();
        client.updateServer(curLeader, port);
        initDemons();
    }

    private void initDemons() {
        switch (curRole) {
            case Main -> {
                var updaterDemon = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                Thread.sleep(10000);
                                server.updatePositions(null, null);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                updaterDemon.setDaemon(true);
                updaterDemon.start();
            }

            case Follower -> {
                var updaterChecker = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int cntskipped = 0;
                            while (true) {
                                Thread.sleep(10000);
                                if (server.getLastTimeUpdated() == lastTimeUpdated) {
                                    cntskipped++;

                                    if (cntskipped >= skippingUpdationsLimit) {

                                    }
                                } else {
                                    cntskipped = 0;
                                }
                                lastTimeUpdated = server.getLastTimeUpdated();
                            }


                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

        }

    }


    public String getCodeToConnect() {
        return Base64.encodeBase64String(host.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeCodeToConnect(String encoded) {
        return new String(Base64.decodeBase64(encoded), StandardCharsets.UTF_8);
    }

    private void setLeader() throws Exception {
        var newLeader = server.setLeader();
        if (newLeader == null) {
            becomeLeader();
            return;
        }

        client.updateServer(newLeader.getHost(), port);
    }

    private void becomeLeader() throws Exception {
        this.server.becomeLeader();
        curRole = ServersRoles.Main;
        initDemons();
    };

    public void copy(String filePath, String copyTo, FileType type) {
        client.copy(filePath, copyTo, type);
    }

    public void create(String path, FileType type, String name) {
        client.create(path, type, name);
    }

    public void delete(String path, FileType type) {
        client.delete(path, type);
    }

    public void move(String relativePath, String pathToCopy, FileType type) {
        client.move(relativePath, pathToCopy, type);
    }

    public void rename(String relativePath, String newName, FileType type) {
        client.rename(relativePath, newName, type);
    }

    public void setUserCursor(UserCursor userCursor) {
        client.setUserCursor(userCursor);
    }

    public void setUserHighlighted(UserHighlighted userHighlighted) {
        client.setUserHighlighted(userHighlighted);
    }

    public void setUserFilePosition(UserFile file) {
        client.setUserFilePosition(file);
    }

    public void insertText(String filePath, String text, CursorPosition position) {
        client.insertText(filePath, text, position);
    }

    public void deleteText(String filePath, String textToDelete, HighlightedPosition position) {
        client.deleteText(filePath, textToDelete, position);
    }

    public void changeText(String filePath, String textToDelete, String newText, HighlightedPosition position) {
        changeText(filePath, textToDelete, newText, position);
    }
}
