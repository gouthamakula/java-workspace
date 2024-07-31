package com.modules.grpc;

import com.misc.grpc.services.OrderServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(OrderServiceGrpc.bindService(new GrpcOrderServiceServerImpl()))
                .build();
        server.start();
        server.awaitTermination();
    }
}