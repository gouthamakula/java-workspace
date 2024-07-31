package com.modules.grpc;

import com.google.protobuf.Descriptors;
import com.misc.grpc.services.OrderDetailsRequest;
import com.misc.grpc.services.OrderServiceGrpc;
import com.misc.grpc.services.ProceedTransactionResponse;
import com.misc.grpc.services.TransactionType;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Arrays;
import java.util.HashMap;

public class GrpcOrderServiceClientImpl {

    public static void main(String[] args) throws Descriptors.DescriptorValidationException, InterruptedException {
        ManagedChannel channel  = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
        String[] coupons = {"COUPON1", "COUPON2"};
        HashMap<String, String> products = new HashMap<>();
        products.put("1", "product1");
        products.put("2", "product2");
        Descriptors.FieldDescriptor product = OrderDetailsRequest.getDescriptor().findFieldByName("products");
        ProceedTransactionResponse response = stub.isProceedTransaction(OrderDetailsRequest.newBuilder()
                        .addAllCoupons(Arrays.stream(coupons).toList())
                        .setTxnType(TransactionType.QR_PAYMENT)
                .build());
        System.out.println(response);
        channel.shutdown();
    }

}
