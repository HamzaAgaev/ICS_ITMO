package server.netserver;

import common.ByteManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Deprecated
public class RequestReceiver {
    public static DatagramPacket receiveDatagram(DatagramSocket datagramSocket) throws IOException {
        ByteManager byteManager = new ByteManager();
        byte[] buffer = new byte[ByteManager.getIntSize()];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(datagramPacket);
        InetAddress clientAddress = datagramPacket.getAddress();
        int clientPort = datagramPacket.getPort();
        int requestEntityBytesArraysLength = byteManager.byteArrayToInt(buffer);
        buffer = byteManager.intToByteArray(requestEntityBytesArraysLength);
        datagramPacket = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        byte[][] requestEntityBytesArrays = new byte[requestEntityBytesArraysLength][];
        datagramSocket.send(datagramPacket);
        for (int i = 0; i < requestEntityBytesArraysLength; ) {
            buffer = new byte[ByteManager.getBufferSize()];
            datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);
            int arrayNumber = byteManager.byteArrayToInt(buffer);
            if (arrayNumber == i) {
                requestEntityBytesArrays[i] = buffer;
                i++;
            }
            buffer = new byte[ByteManager.getIntSize()];
            byte[] needNumberInArray = byteManager.intToByteArray(i);
            System.arraycopy(needNumberInArray, 0, buffer, 0, ByteManager.getIntSize());
            datagramPacket = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
            datagramSocket.send(datagramPacket);
        }

        byte[] requestEntityBytes = byteManager.joinToByteArray(requestEntityBytesArrays);
        datagramPacket = new DatagramPacket(requestEntityBytes, requestEntityBytes.length, clientAddress, clientPort);

        return datagramPacket;
    }
}
