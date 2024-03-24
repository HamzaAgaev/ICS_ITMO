package server.netserver;

import common.ByteManager;
//import common.netentities.RequestEntity;
//import common.netentities.ResponseEntity;
import server.collection.CollectionManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerManager {
    private static ServerManager instance = new ServerManager();
    private ServerManager() {}
    public static ServerManager getInstance() {
        return instance;
    }
    private final int serverPort = 8050;
    private DatagramSocket datagramSocket;
    private boolean isRunning = false;
    private List<ClientMessageManager> clientMessageManagers;

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public void run(String filename) {
        Boolean canSetFile = CollectionManager.getInstance().tryToSetCollectionFilename(filename).getValue();
        Boolean canRun = CollectionManager.getInstance().run().getValue();
        if (!canSetFile || !canRun) {
            System.out.println("Cannot run Collection Manager (Collection File doesn't exist).");
            return;
        }

        try {
            datagramSocket = new DatagramSocket(serverPort);
            isRunning = true;
            clientMessageManagers = new ArrayList<>();
        } catch (SocketException socketException) {
            System.out.println("Cannot create UDP Datagram Socket.");
            return;
        }
        try {
            while (isRunning) {
                byte[] buffer = new byte[ByteManager.getBufferSize()];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                InetAddress clientAddress = datagramPacket.getAddress();
                int clientPort = datagramPacket.getPort();

                Optional<ClientMessageManager> optionalCMM = clientMessageManagers.stream().filter(cmm -> cmm.isThisClient(clientAddress, clientPort)).findFirst();

                ClientMessageManager currentCMM;
                if (optionalCMM.isPresent()) {
                    currentCMM = optionalCMM.get();
                } else {
                    currentCMM = new ClientMessageManager(clientAddress, clientPort);
                    clientMessageManagers.add(currentCMM);
                }

                currentCMM.solveDatagram(datagramPacket);
                if (currentCMM.getIsRequestReadyToDeserialize()) {
                    currentCMM.deserializeRequestEntity();
                }

                if (currentCMM.getIsRequestDeserialized()) {
                    System.out.println(currentCMM.getRequestEntity());
                    currentCMM.solveResponseEntity();
                }

                if (currentCMM.getIsResponseSolved()) {
                    currentCMM.sendResponse();
                }

                if (currentCMM.getIsResponseSend()) {
                    clientMessageManagers.remove(currentCMM);
                }
                // ввод команд либо без него (доделать при помощи многопоточности в лабе 7)

            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        datagramSocket.close();
        isRunning = false;
    }
}
