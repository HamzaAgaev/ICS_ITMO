package client.netclient;

import client.exceptions.SendingException;
import common.ByteManager;
import common.Sleeper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ResponseReceiver {
    public static byte[] receiveBytes(DatagramChannel datagramChannel) throws IOException, SendingException {
        ByteManager byteManager = new ByteManager();
        byte[] lengthBuffer = new byte[ByteManager.getBufferSize()];
        ByteBuffer byteLengthBuffer = ByteBuffer.wrap(lengthBuffer);
        for (int i = 0; i < 4; i++) {
            Sleeper.tryToSleep(80 + 1000 * i);
            if (datagramChannel.receive(byteLengthBuffer) != null) {
                break;
            } else if (i == 4 - 1) {
                throw new SendingException("Cannot receive a count of Response Packets from server.");
            }
        }

        int responseBytesArraysLength = byteManager.byteArrayToInt(lengthBuffer);
        byte[][] responseBytesArrays = new byte[responseBytesArraysLength][ByteManager.getBufferSize()];
        boolean[] isReceivedPackets = new boolean[responseBytesArraysLength];
        int triesToReceive = 0;
        int receivedCount = 0;

        while (triesToReceive < 4 && receivedCount < responseBytesArraysLength) {
            byte[] responsePartBuffer = new byte[ByteManager.getBufferSize()];
            ByteBuffer byteResponsePartBuffer = ByteBuffer.wrap(responsePartBuffer);
            Sleeper.tryToSleep(10 + triesToReceive * 100);
            if (datagramChannel.receive(byteResponsePartBuffer) != null) {
                int index = byteManager.byteArrayToInt(responsePartBuffer);
                if (!isReceivedPackets[index]) {
                    responseBytesArrays[index] = responsePartBuffer;
                    receivedCount++;
                    isReceivedPackets[index] = true;
                }
                triesToReceive = 0;
            } else {
                triesToReceive++;
            }

            if (triesToReceive == 4) {
                throw new SendingException("Cannot receive full Response from server.");
            }
        }

        byte[] responseBytes = byteManager.joinToByteArray(responseBytesArrays);
        return responseBytes;
    }
}
