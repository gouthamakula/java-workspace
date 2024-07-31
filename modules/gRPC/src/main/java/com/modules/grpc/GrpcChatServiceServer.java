package com.modules.grpc;

import com.google.protobuf.Timestamp;
import com.misc.grpc.services.ChatMessage;
import com.misc.grpc.services.ChatMessageFromServer;
import com.misc.grpc.services.ChatServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.LinkedHashSet;

public class GrpcChatServiceServer extends ChatServiceGrpc.ChatServiceImplBase {
    private static LinkedHashSet<StreamObserver<ChatMessageFromServer>> observers = new LinkedHashSet<>();

    public static class ChatServiceMain {
        public static void main(String[] args) throws IOException, InterruptedException {
            Server server = ServerBuilder.forPort(8080)
                    .addService(new GrpcChatServiceServer())
                    .build();
            server.start();
            server.awaitTermination();
        }
    }

    @Override
    public StreamObserver<ChatMessage> chat(
            StreamObserver<com.misc.grpc.services.ChatMessageFromServer> responseObserver) {
        observers.add(responseObserver);
        return new StreamObserver<ChatMessage>() {
            @Override
            public void onNext(ChatMessage value) {
                // Handle incoming message
                System.out.println("Received message: " + value.getMessage());
                // Send response
                responseObserver.onNext(ChatMessageFromServer
                        .newBuilder()
                        .setMessage(ChatMessage.newBuilder()
                                .setFrom("Server")
                                .setMessage("Received your message"))
                        .setTimeStamp(Timestamp.newBuilder()
                                .setSeconds(System.currentTimeMillis() / 1000)
                                .build())
                        .build());
            }

            @Override
            public void onError(Throwable t) {
                observers.remove(responseObserver);
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
                responseObserver.onCompleted();
            }
        };
    }

}
