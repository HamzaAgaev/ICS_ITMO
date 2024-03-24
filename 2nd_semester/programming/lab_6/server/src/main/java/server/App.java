package server;

import server.netserver.ServerManager;

public class App
{
    public static void main(String[] args)
    {
        try {
            String filename = args[0];
            ServerManager serverManager = ServerManager.getInstance();
            serverManager.run(filename);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.out.println("Collection Filename is empty.");
        }
    }
}
