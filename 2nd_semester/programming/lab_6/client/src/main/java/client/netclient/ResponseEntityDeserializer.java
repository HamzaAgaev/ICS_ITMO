package client.netclient;

import common.netentities.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class ResponseEntityDeserializer<T extends Serializable> {
    public ResponseEntity<T> getResponseEntity(byte[] responseEntityBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(responseEntityBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ResponseEntity<T> responseEntity = (ResponseEntity<T>) objectInputStream.readObject();
        objectInputStream.close();
        return responseEntity;
    }
}
