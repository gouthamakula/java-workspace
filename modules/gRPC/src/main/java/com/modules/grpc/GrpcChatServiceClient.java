package com.modules.grpc;

import com.misc.grpc.services.ChatMessage;
import com.misc.grpc.services.ChatMessageFromServer;
import com.misc.grpc.services.ChatServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GrpcChatServiceClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        ChatServiceGrpc.ChatServiceStub stub = ChatServiceGrpc.newStub(channel);
        StreamObserver<ChatMessage> response = stub.chat(new StreamObserver<ChatMessageFromServer>() {
            @Override
            public void onNext(ChatMessageFromServer chatMessageFromServer) {
                System.out.println("Message : " + chatMessageFromServer.getMessage());
                System.out.println("Timestamp : " + chatMessageFromServer.getTimeStamp());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });
        Thread.sleep(1000);
        response.onNext(ChatMessage.newBuilder().setMessage("Hello").build());
        Thread.sleep(5000);
    }

}
