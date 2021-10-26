package org.example.sample.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.sample.pb.GreetingRequest;
import org.example.sample.pb.GreetingResponse;
import org.example.sample.pb.GreetingServiceGrpc;
import org.example.sample.pb.GreetingServiceGrpc.GreetingServiceBlockingStub;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GreetingClient {
    private static final Logger logger = Logger.getLogger(GreetingClient.class.getName());

    private final ManagedChannel channel;
    private final GreetingServiceBlockingStub blockingStub;
    private final GreetingServiceGrpc.GreetingServiceStub asyncStub;

    public GreetingClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        blockingStub = GreetingServiceGrpc.newBlockingStub(channel);
        asyncStub = GreetingServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void sayHello(String name) {
        GreetingRequest request = GreetingRequest.newBuilder().setName(name).build();
        GreetingResponse response = blockingStub.sayHello(request);
        logger.info("Message received from server: " + response.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
        String HOSTNAME_KEY = "hostname";
        String PORT_KEY = "port";
        String USERNAME_KEY = "username";
        String hostname = System.getProperty(HOSTNAME_KEY);
        if (hostname == null) {
            hostname = System.getenv(HOSTNAME_KEY);
            if (hostname == null) {
                hostname = "localhost";
            }
        }
        String port = System.getProperty(PORT_KEY);
        if (port == null) {
            port = System.getenv(PORT_KEY);
            if (port == null) {
                port = "9000";
            }
        }
        GreetingClient client = new GreetingClient(hostname, Integer.valueOf(port));
        String name = System.getProperty(USERNAME_KEY);
        if (name == null) {
            name = System.getenv(USERNAME_KEY);
            if (name == null) {
                name = "default user";
            }
        }
        client.sayHello(name);
        client.shutdown();
    }
}
