package server.netserver.multithreading;

import common.ByteManager;
import common.netentities.ResponseEntity;
import server.netserver.ResponseEntitySerializer;
import server.netserver.ServerManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;

public class ResponseSendTask extends RecursiveAction {
    private final ByteManager byteManager;
    private final Future<ResponseEntity<?>> responseEntityFuture;
    private final DatagramSocket datagramSocket = ServerManager.getInstance().getDatagramSocket();
    private final InetAddress clientAddress;
    private final int clientPort;

    private volatile boolean isResponseSend = false;

    public ResponseSendTask(Future<ResponseEntity<?>> responseEntityFuture, InetAddress clientAddress, int clientPort) {
        this.responseEntityFuture = responseEntityFuture;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
        this.byteManager = new ByteManager();
    }

    public boolean getIsResponseSend() {
        return isResponseSend;
    }

    @Override
    protected void compute() {
        try {
            ResponseEntity responseEntity = responseEntityFuture.get();
            byte[] responseBytes = new ResponseEntitySerializer().getBytes(responseEntity);
            byte[][] responseBytesArrays = byteManager.divideToByteArrays(responseBytes);
            int responseBytesArraysLength = responseBytesArrays.length;
            byte[] lengthBuffer = new byte[ByteManager.getBufferSize()];
            System.arraycopy(byteManager.intToByteArray(responseBytesArraysLength), 0, lengthBuffer, 0, ByteManager.getIntSize());
            DatagramPacket lengthPacket = new DatagramPacket(lengthBuffer, lengthBuffer.length, clientAddress, clientPort);
            datagramSocket.send(lengthPacket);  // synchonize
            for (int i = 0; i < responseBytesArraysLength; i++) {
                DatagramPacket responsePartPacket = new DatagramPacket(responseBytesArrays[i], responseBytesArrays[i].length, clientAddress, clientPort);
                datagramSocket.send(responsePartPacket);    // synchonize
            }
        } catch (Exception ignored) {
            System.out.println(ignored);
        }
        setResponseSend();
    }

    private synchronized void setResponseSend() {
        isResponseSend = true;
        notifyAll();
    }
}
