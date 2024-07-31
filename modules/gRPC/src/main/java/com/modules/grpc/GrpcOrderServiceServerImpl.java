package com.modules.grpc;

import com.misc.grpc.services.OrderDetailsRequest;
import com.misc.grpc.services.OrderServiceGrpc;
import com.misc.grpc.services.ProceedTransactionResponse;
import com.misc.grpc.services.TransactionType;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class GrpcOrderServiceServerImpl extends OrderServiceGrpc.OrderServiceImplBase {
    @Override
    public void isProceedTransaction(
            OrderDetailsRequest request,
            StreamObserver<ProceedTransactionResponse> responseObserver) {

        if(request.getTxnType() == TransactionType.QR_PAYMENT) {
            responseObserver.onError(Status.INTERNAL.withDescription("QR Payment not supported").asException());
        } else {
            ProceedTransactionResponse response = ProceedTransactionResponse.newBuilder()
                    .setProceedTransaction(true)
                    .setTxnId("123_txn")
                    .build();
            System.out.println("Request received coupons: " + request.getCoupons(0));
            System.out.println("Request received products: " + request.getProductsOrDefault("1", "None"));
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
