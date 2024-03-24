package client.netclient;

import client.exceptions.SendingException;
import common.ByteManager;
import common.Sleeper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class RequestSender {
    public static void sendBytes(byte[] requestEntityBytes, InetSocketAddress serverAddress, DatagramChannel datagramChannel) throws IOException, SendingException {
        ByteManager byteManager = new ByteManager();
        byte[][] requestEntityBytesArrays = byteManager.divideToByteArrays(requestEntityBytes);
        int requestEntityBytesArraysLength = requestEntityBytesArrays.length;
        ByteBuffer byteLengthBuffer = ByteBuffer.wrap(byteManager.intToByteArray(requestEntityBytesArraysLength));
        datagramChannel.send(byteLengthBuffer, serverAddress);
        byteLengthBuffer = ByteBuffer.allocate(ByteManager.getIntSize());
        boolean canConnect = tryToReceiveBytes(datagramChannel, byteLengthBuffer);
        if (!canConnect) {
            throw new SendingException("Cannot connect to the server.");
        }
        if (byteLengthBuffer.getInt() == requestEntityBytesArraysLength) {
            for (int i = 0; i < requestEntityBytesArrays.length; ) {
                ByteBuffer byteRequestPartBuffer = ByteBuffer.wrap(requestEntityBytesArrays[i]);
                datagramChannel.send(byteRequestPartBuffer, serverAddress);
                ByteBuffer bytePartIndexBuffer = ByteBuffer.allocate(ByteManager.getIntSize());
                canConnect = tryToReceiveBytes(datagramChannel, bytePartIndexBuffer);
                if (!canConnect) {
                    throw new SendingException("Client was connected to server, but now there is no connection.");
                }
                int arrayNumber = bytePartIndexBuffer.getInt();
                if (arrayNumber == i) {
                    i++;
                } else {
                    i = arrayNumber;
                }
            }
        }
    }

    private static boolean tryToReceiveBytes(DatagramChannel datagramChannel, ByteBuffer byteBuffer) throws IOException {
        for (int i = 0; i < 4; i++) {
            Sleeper.tryToSleep(10 + i*50);
            if (datagramChannel.receive(byteBuffer) != null) {
                byteBuffer.flip();
                return true;
            }
        }
        return false;
    }
}
