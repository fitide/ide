package org.ide.WebWorker;

import io.grpc.ServerBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.ide.IdeControllerWebInt;
import org.ide.WebWorker.Roles.Leader;
import org.ide.WebWorker.Roles.ServersRoles;
import org.ide.WebWorker.ServerClient.IDEWebWorkerClient;
import org.ide.WebWorker.ServerClient.IDEWebWorkerServer;

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
    }


    public WebController(IdeControllerWebInt ideController, String name, String host, String identificationString) throws Exception {
        this.ideController = ideController;
        this.name = name;
        this.host = host;
        curLeader = decodeCodeToConnect(identificationString);
        this.server = new IDEWebWorkerServer(ServersRoles.Follower, ideController, curLeader, port, host, name);
        ServerBuilder.forPort(port).addService(server).build().start();
        this.client = new IDEWebWorkerClient();
        client.updateServer(curLeader, port);
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
                        } catch (InterruptedException e) {
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
}
