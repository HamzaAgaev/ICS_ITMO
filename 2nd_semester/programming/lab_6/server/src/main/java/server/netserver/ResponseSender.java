package server.netserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Deprecated
public class ResponseSender {
    public static void sendDatagram(DatagramSocket datagramSocket, DatagramPacket responseDatagramPacket) throws IOException {
        datagramSocket.send(responseDatagramPacket);
    }
}
