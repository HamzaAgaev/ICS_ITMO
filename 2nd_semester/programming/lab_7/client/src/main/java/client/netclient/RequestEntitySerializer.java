package client.netclient;

import common.netentities.RequestEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RequestEntitySerializer {
    public byte[] getBytes(RequestEntity requestEntity) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(requestEntity);
        byte[] requestEntityBytes = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        return requestEntityBytes;
    }
}
