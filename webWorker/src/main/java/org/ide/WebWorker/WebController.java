package org.ide.WebWorker;

import io.grpc.ServerBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.ide.IdeControllerWebInt;
import org.ide.WebWorker.FileSystem.FileSystemComponents.FileType;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.Positions.PositionsTable;
import org.ide.WebWorker.Roles.ServersRoles;
import org.ide.WebWorker.ServerClient.IDEWebWorkerClient;
import org.ide.WebWorker.ServerClient.IDEWebWorkerServer;
import org.ide.WebWorker.User.*;

import com.google.protobuf.Timestamp;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final ExecutorService positionSender = Executors.newSingleThreadExecutor(r -> {
        var t = new Thread(r, "position-sender");
        t.setDaemon(true);
        return t;
    });

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
        curRole = ServersRoles.Follower;
        this.server = new IDEWebWorkerServer(ServersRoles.Follower, ideController, curLeader, port, host, name);
        ServerBuilder.forPort(port).addService(server).build().start();
        this.client = new IDEWebWorkerClient();
        client.updateServer(curLeader, port);
        server.connect();
        initDemons();
    }

    private void initDemons() {
        switch (curRole) {
            case Main -> {
                var updaterDemon = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(10000);
                                server.updatePositions(null, null);
                            } catch (InterruptedException e) {
                                return;
                            } catch (Exception ignored) {
                                // клиент временно недоступен — продолжаем
                            }
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
                                Thread.sleep(500);

                                try {
                                    var positions = client.getPositions();
                                    server.applyPositions(positions);
                                } catch (Exception e) {
                                    // соединение потеряно, ждём следующей итерации
                                }

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
                updaterChecker.setDaemon(true);
                updaterChecker.start();
            }

        }

    }

    public PositionsTable getPositionsTable() {
        return server.getPositionsTable();
    }

    public static String getLocalIp() throws Exception {
        try (var socket = new java.net.DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 80);
            return socket.getLocalAddress().getHostAddress();
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

    public User getMe() {
        return User.newBuilder().setHost(host).setName(name).build();
    }

    public String getMyName() {
        return name;
    }

    private Timestamp nowTimestamp() {
        var now = Instant.now();
        return Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build();
    }

    public void sendCursor(int line, int column) {
        var cursor = UserCursor.newBuilder()
                .setUser(getMe())
                .setCursorPosition(CursorPosition.newBuilder().setLineNumer(line).setColumnNumber(column).build())
                .setTime(nowTimestamp())
                .build();
        positionSender.submit(() -> {
            try {
                client.setUserCursor(cursor);
            } catch (Exception ignored) {
            }
        });
    }

    public void sendHighlight(int startLine, int startColumn, int endLine, int endColumn) {
        var highlighted = UserHighlighted.newBuilder()
                .setUser(getMe())
                .setHighlightedPosition(HighlightedPosition.newBuilder()
                        .setLineStart(startLine).setColumnStart(startColumn)
                        .setLineEnd(endLine).setColumnEnd(endColumn).build())
                .setTime(nowTimestamp())
                .build();
        positionSender.submit(() -> {
            try {
                client.setUserHighlighted(highlighted);
            } catch (Exception ignored) {
            }
        });
    }

    public void sendFileOpened(String relativePath) {
        var userFile = UserFile.newBuilder()
                .setUser(getMe())
                .setFile(relativePath)
                .setTime(nowTimestamp())
                .build();
        positionSender.submit(() -> {
            try {
                client.setUserFilePosition(userFile);
            } catch (Exception ignored) {
            }
        });
    }

    public void insertText(String filePath, String text, CursorPosition position) {
        client.insertText(filePath, text, position, host);
    }

    public void deleteText(String filePath, String textToDelete, HighlightedPosition position) {
        client.deleteText(filePath, textToDelete, position, host);
    }

    public void changeText(String filePath, String textToDelete, String newText, HighlightedPosition position) {
        client.changeText(filePath, textToDelete, newText, position, host);
    }

    public void updateFile(String filePath) {
        server.updateFile(filePath);
    }

    public boolean isMe(String host) {
        return this.host.equals(host);
    }
}
