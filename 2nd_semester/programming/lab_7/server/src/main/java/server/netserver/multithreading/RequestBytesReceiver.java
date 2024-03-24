package server.netserver.multithreading;

import common.ByteManager;
import server.netserver.ServerManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RequestBytesReceiver {
    private final ByteManager byteManager;
    private final DatagramSocket datagramSocket = ServerManager.getInstance().getDatagramSocket();
    private final InetAddress clientAddress;
    private final int clientPort;
    private byte[][] requestBytesArrays;
    private int requestBytesArraysLength;
    private boolean isSetRequestBytesArraysLength = false;
    private int requestBytesIndex = 0;
    private volatile boolean isRequestReadyToDeserialize = false;

    public RequestBytesReceiver(InetAddress clientAddress, int clientPort) {
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
        this.byteManager = new ByteManager();
    }

    public boolean isThisClient(InetAddress clientAddress, int clientPort) {
        return this.clientAddress.equals(clientAddress) && this.clientPort == clientPort;
    }

    public boolean getIsRequestReadyToDeserialize() {
        return isRequestReadyToDeserialize;
    }

    public byte[] getRequestBytes() {
        return byteManager.joinToByteArray(requestBytesArrays);
    }

    private synchronized void makeRequestReadyToDeserialize() {
        isRequestReadyToDeserialize = true;
        notifyAll();
    }


    public void solveDatagram(DatagramPacket datagramPacket) throws IOException {
        byte[] buffer = datagramPacket.getData();
        if (byteManager.containsOnlyZeroesFrom(buffer, ByteManager.getIntSize())) {
            requestBytesArraysLength = byteManager.byteArrayToInt(buffer);
            byte[] lengthBuffer = byteManager.intToByteArray(requestBytesArraysLength);
            isSetRequestBytesArraysLength = true;
            requestBytesArrays = new byte[requestBytesArraysLength][ByteManager.getBufferSize()];
            DatagramPacket lengthPacket = new DatagramPacket(lengthBuffer, lengthBuffer.length, clientAddress, clientPort);
            datagramSocket.send(lengthPacket);
        } else if (isSetRequestBytesArraysLength) {
            int arrayIndex = byteManager.byteArrayToInt(buffer);
            if (arrayIndex < requestBytesArraysLength && !isRequestReadyToDeserialize) {
                if (arrayIndex == requestBytesIndex) {
                    requestBytesArrays[requestBytesIndex] = buffer;
                    requestBytesIndex++;
                }
                byte[] indexBuffer = byteManager.intToByteArray(requestBytesIndex);
                DatagramPacket indexPacket = new DatagramPacket(indexBuffer, indexBuffer.length, clientAddress, clientPort);
                datagramSocket.send(indexPacket);
                if (arrayIndex == requestBytesArraysLength - 1) {
                    makeRequestReadyToDeserialize();
                }
            }
        } else {
            byte[] zeroes = new byte[ByteManager.getBufferSize()];
            DatagramPacket zeroesPacket = new DatagramPacket(zeroes, zeroes.length, clientAddress, clientPort);
            datagramSocket.send(zeroesPacket);
        }
    }

}
