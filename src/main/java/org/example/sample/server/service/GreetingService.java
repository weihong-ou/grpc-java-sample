package org.example.sample.server.service;

import io.grpc.stub.StreamObserver;
import org.example.sample.pb.GreetingRequest;
import org.example.sample.pb.GreetingResponse;
import org.example.sample.pb.GreetingServiceGrpc;

import java.util.logging.Logger;

public class GreetingService extends GreetingServiceGrpc.GreetingServiceImplBase {

    private static final Logger logger = Logger.getLogger(GreetingService.class.getName());

    private final String HOST_NAME = "hostname";

    @Override
    public void sayHello(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        String name = request.getName();
        logger.info("Get request from " + name);
        String hostName = System.getProperty(HOST_NAME);

        if (hostName == null) {
            hostName = System.getenv(HOST_NAME);
            if (hostName == null) {
                hostName = "Default";
            }
        }
        String responseMsg = "Hello " + name + " from " + hostName;

        GreetingResponse response = GreetingResponse.newBuilder().setMessage(responseMsg).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
