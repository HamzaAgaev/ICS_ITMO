package server.netserver.multithreading;

import common.netentities.RequestEntity;
import server.netserver.RequestEntityDeserializer;

import java.util.concurrent.RecursiveTask;

public class RequestReadTask extends RecursiveTask<RequestEntity> {
    private final RequestBytesReceiver requestBytesReceiver;

    public RequestReadTask(RequestBytesReceiver requestBytesReceiver) {
        this.requestBytesReceiver = requestBytesReceiver;
    }

    @Override
    protected RequestEntity compute() {
        RequestEntity requestEntity = null;
        synchronized (requestBytesReceiver) {
            try {
                while (!requestBytesReceiver.getIsRequestReadyToDeserialize()) requestBytesReceiver.wait();
                byte[] requestBytes = requestBytesReceiver.getRequestBytes();
                requestEntity = new RequestEntityDeserializer().getRequestEntity(requestBytes);
            } catch (Exception ignored) {}
        }
        return requestEntity;
    }
}
