package server.netserver;

import common.ByteManager;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Deprecated
public class ClientMessageManager {
    private final DatagramSocket datagramSocket = ServerManager.getInstance().getDatagramSocket();
    private final InetAddress clientAddress;
    private final int clientPort;
    private final ByteManager byteManager;
    private byte[][] requestBytesArrays;
    private int requestBytesArraysLength;
    private boolean isSetRequestBytesArraysLength = false;
    private int requestBytesIndex = 0;
    private boolean isRequestReadyToDeserialize = false;
    private RequestEntity requestEntity = null;
    private boolean isRequestDeserialized = false;
    private ResponseEntity<?> responseEntity = null;
    private boolean isResponseSolved = false;
    private boolean isResponseSend = false;

    public ClientMessageManager(InetAddress clientAddress, int clientPort) {
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

    public boolean getIsRequestDeserialized() {
        return isRequestDeserialized;
    }

    public RequestEntity getRequestEntity() {
        return requestEntity;
    }

    public boolean getIsResponseSolved() {
        return isResponseSolved;
    }

    public ResponseEntity<?> getResponseEntity() {
        return responseEntity;
    }

    public boolean getIsResponseSend() {
        return isResponseSend;
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
                    isRequestReadyToDeserialize = true;
                }
            }
        } else {
            byte[] zeroes = new byte[ByteManager.getBufferSize()];
            DatagramPacket zeroesPacket = new DatagramPacket(zeroes, zeroes.length, clientAddress, clientPort);
            datagramSocket.send(zeroesPacket);
        }
    }

    public void deserializeRequestEntity() throws IOException, ClassNotFoundException {
        if (isRequestReadyToDeserialize) {
            byte[] requestBytes = byteManager.joinToByteArray(requestBytesArrays);
            requestEntity = new RequestEntityDeserializer().getRequestEntity(requestBytes);
            isRequestDeserialized = true;
        }
    }

    public void solveResponseEntity() {
        if (isRequestDeserialized) {
            responseEntity = new CommandDelegator().runCommand(requestEntity);
            isResponseSolved = true;
        }
    }

    // без подтвреждения доставки всех пакетов
    public void sendResponse() throws IOException {
        if (isResponseSolved) {
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
            isResponseSend = true;
        }
    }
}
