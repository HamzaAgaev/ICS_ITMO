package server.netserver;

import common.netentities.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ResponseEntitySerializer {
    public byte[] getBytes(ResponseEntity<?> responseEntity) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream((byteArrayOutputStream));
        objectOutputStream.writeObject(responseEntity);
        byte[] responseEntityBytes = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        return responseEntityBytes;
    }
}
