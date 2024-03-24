package server.netserver;

import common.netentities.RequestEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;

public class RequestEntityDeserializer {
    public RequestEntity getRequestEntity(byte[] buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        RequestEntity requestEntity = (RequestEntity) objectInputStream.readObject();
        objectInputStream.close();
        return requestEntity;
    }
}
