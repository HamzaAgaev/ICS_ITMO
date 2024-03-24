package server.netserver.multithreading;

import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import server.netserver.CommandDelegator;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class RequestExecuteTask implements Callable<ResponseEntity<?>> {
    private final Future<RequestEntity> requestEntityFuture;

    public RequestExecuteTask(Future<RequestEntity> requestEntityFuture) {
        this.requestEntityFuture = requestEntityFuture;
    }

    @Override
    public ResponseEntity<?> call() {
        ResponseEntity<?> responseEntity = null;
        try {
            RequestEntity requestEntity = requestEntityFuture.get();
            responseEntity = new CommandDelegator().runCommand(requestEntity);
        } catch (Exception ignored) {}
        return responseEntity;
    }
}
