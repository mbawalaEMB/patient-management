package com.emb.billingservice.grpc;

import com.emb.billing.BillingRequest;
import com.emb.billing.BillingResponse;
import com.emb.billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest billingRequest, StreamObserver<BillingResponse> responseObserver) {
        log.info("createBillingAccount request received {}", billingRequest.toString());
        // TODO BusinessLogic with Database connectivity
        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("123")
                .setStatus("ACTIVE")
                .build();

        // send response to the client
        responseObserver.onNext(billingResponse);
        /*
        * specify that we’ve finished dealing with the RPC; otherwise, the connection will be hung
        * , and the client will just wait for more information to come in
        * */
        responseObserver.onCompleted();
    }
}
