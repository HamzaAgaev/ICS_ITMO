package client.netclient;

import client.exceptions.ClientManagerException;
import client.exceptions.SendingException;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;

public class ClientManager {
    private static ClientManager instance = new ClientManager();
    private ClientManager() {}
    public static ClientManager getInstance() {
        return instance;
    }
    private DatagramChannel datagramChannel;
    private boolean isStarted = false;
    private InetSocketAddress serverAddress;

    public void start() throws ClientManagerException {
        try {
            InetAddress serverHost = InetAddress.getLocalHost();
            int serverPort = 8050;
            serverAddress = new InetSocketAddress(serverHost, serverPort);
        } catch (UnknownHostException unknownHostException) {
            throw new ClientManagerException("Cannot define server host (Localhost).");
        }
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            isStarted = true;
        } catch (IOException ioException) {
            throw new ClientManagerException("Cannot open and configure channel.");
        }
    }

    public void close() throws ClientManagerException {
        try {
            datagramChannel.close();
            isStarted = false;
        } catch (IOException ioException) {
            throw new ClientManagerException("Cannot close channel.");
        }
    }

    public <T extends Serializable> ResponseEntity<T> getResponseEntity(RequestEntity requestEntity) throws SendingException {
        if (isStarted) {
            byte[] requestEntityBytes;
            byte[] responseEntityBytes;
            try {
                requestEntityBytes = new RequestEntitySerializer().getBytes(requestEntity);
            } catch (IOException ioException) {
                throw new SendingException("Cannot Serialize Request Entity.");
            }
            try {
                RequestSender.sendBytes(requestEntityBytes, serverAddress, datagramChannel);
            } catch (IOException ioException) {
                throw new SendingException("Cannot send request bytes because there are problems with channel.");
            }
            try {
                responseEntityBytes = ResponseReceiver.receiveBytes(datagramChannel);
            } catch (IOException ioException) {
                throw new SendingException("Cannot receive response bytes because there are problems with channel.");
            }
            try {
                ResponseEntity<T> responseEntity = new ResponseEntityDeserializer<T>().getResponseEntity(responseEntityBytes);
                return responseEntity;
            } catch (IOException ioException) {
                throw new SendingException("Cannot Deserialize Response Entity.");
            } catch (ClassNotFoundException classNotFoundException) {
                throw new SendingException("Cannot find Response Entity class.");
            }

        }
        throw new SendingException("Client Manager isn't started.");
    }
}
