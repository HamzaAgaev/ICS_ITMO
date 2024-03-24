package server;

import server.netserver.ServerManager;

public class App {
    public static void main(String[] args) {
        ServerManager serverManager = ServerManager.getInstance();
        serverManager.run();
    }
}
