package server.netserver;

import common.ByteManager;
//import common.netentities.RequestEntity;
//import common.netentities.ResponseEntity;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import server.collection.CollectionManager;
import server.database.DatabaseManager;
import server.netserver.multithreading.RequestBytesReceiver;
import server.netserver.multithreading.RequestExecuteTask;
import server.netserver.multithreading.RequestReadTask;
import server.netserver.multithreading.ResponseSendTask;
import server.users.UserManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class ServerManager {
    private static ServerManager instance = new ServerManager();
    private ServerManager() {}
    public static ServerManager getInstance() {
        return instance;
    }
    private final int serverPort = 8050;
    private DatagramSocket datagramSocket;
    private boolean isRunning = false;
//    private List<ClientMessageManager> clientMessageManagers;
    private List<RequestBytesReceiver> requestBytesReceivers;
//    private boolean isMultiThreadingWork = true;

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    private ForkJoinPool forkJoinPool = new ForkJoinPool();
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);

    public void run() {
        boolean canRunDatabase = DatabaseManager.getInstance().getIsRunning();
        try {
            canRunDatabase = DatabaseManager.getInstance().run();
        } catch (Exception ignored) {
        } finally {
            if (!canRunDatabase) {
                System.out.println("Cannot run Database.");
                return;
            }
        }
        boolean canRunCollection = CollectionManager.getInstance().run();
        if (!canRunCollection) {
            System.out.println("Cannot run Collection Manager.");
            return;
        }

        boolean canRunUsers = UserManager.getInstance().run();
        if (!canRunUsers) {
            System.out.println("Cannot run User Manager.");
            return;
        }

        try {
            datagramSocket = new DatagramSocket(serverPort);
//            clientMessageManagers = new ArrayList<>();
            requestBytesReceivers = new ArrayList<>();
            isRunning = true;
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
                workInMultiThread(clientAddress, clientPort, datagramPacket);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        datagramSocket.close();
        isRunning = false;
    }

    public void workInMultiThread(InetAddress clientAddress, int clientPort, DatagramPacket datagramPacket) throws IOException {
        Optional<RequestBytesReceiver> optionalRequestBytesReceiver = requestBytesReceivers.stream().filter(orbr -> orbr.isThisClient(clientAddress, clientPort)).findFirst();
        RequestBytesReceiver currentRequestBytesReceiver;
        if (optionalRequestBytesReceiver.isPresent()) {
            currentRequestBytesReceiver = optionalRequestBytesReceiver.get();
        } else {
            currentRequestBytesReceiver = new RequestBytesReceiver(clientAddress, clientPort);
            requestBytesReceivers.add(currentRequestBytesReceiver);
            Future<RequestEntity> requestEntityFuture = forkJoinPool.submit(new RequestReadTask(currentRequestBytesReceiver));
            Future<ResponseEntity<?>> responseEntityFuture = fixedThreadPool.submit(new RequestExecuteTask(requestEntityFuture));
            ResponseSendTask responseSendTask = new ResponseSendTask(responseEntityFuture, clientAddress, clientPort);
            forkJoinPool.execute(responseSendTask);
            new Thread(
                () -> {
                    synchronized (responseSendTask) {
                        try {
                            while (!responseSendTask.getIsResponseSend()) responseSendTask.wait();
                        } catch (InterruptedException ignored) {}
                    }
                    synchronized (requestBytesReceivers) {
                        requestBytesReceivers.remove(currentRequestBytesReceiver);
                    }
                }
            ).start();
        }

        currentRequestBytesReceiver.solveDatagram(datagramPacket);
    }

//    public void workInSingleThread(InetAddress clientAddress, int clientPort, DatagramPacket datagramPacket) throws IOException, ClassNotFoundException {
//        Optional<ClientMessageManager> optionalCMM = clientMessageManagers.stream().filter(cmm -> cmm.isThisClient(clientAddress, clientPort)).findFirst();
//
//        ClientMessageManager currentCMM;
//        if (optionalCMM.isPresent()) {
//            currentCMM = optionalCMM.get();
//        } else {
//            currentCMM = new ClientMessageManager(clientAddress, clientPort);
//            clientMessageManagers.add(currentCMM);
//        }
//
//        currentCMM.solveDatagram(datagramPacket);
//        if (currentCMM.getIsRequestReadyToDeserialize()) {
//            currentCMM.deserializeRequestEntity();
//        }
//
//        if (currentCMM.getIsRequestDeserialized()) {
//            System.out.println(currentCMM.getRequestEntity());
//            currentCMM.solveResponseEntity();
//        }
//
//        if (currentCMM.getIsResponseSolved()) {
//            currentCMM.sendResponse();
//        }
//
//        if (currentCMM.getIsResponseSend()) {
//            clientMessageManagers.remove(currentCMM);
//        }
//    }
}
