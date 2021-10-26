package org.example.sample.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.sample.server.service.GreetingService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GreetingServer {
    private static final Logger logger = Logger.getLogger(GreetingServer.class.getName());

    private final int port;
    private final Server server;

    public GreetingServer(int port) {
        this(ServerBuilder.forPort(port), port);
    }

    public GreetingServer(ServerBuilder serverBuilder, int port) {
        this.port = port;
        GreetingService greetingService = new GreetingService();
        server = serverBuilder.addService(greetingService).build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.err.println("shut down gRPC server because of JVM shuts down");
                try {
                    GreetingServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("server shut down");
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String PORT_KEY = "port";
        String port = System.getProperty(PORT_KEY);

        if (port == null) {
            port = System.getenv(PORT_KEY);
            if (port == null) {
                port = "9000";
            }
        }
        GreetingServer server = new GreetingServer(Integer.valueOf(port));
        server.start();
        server.blockUntilShutdown();
    }
}
