package com.emb.patientservice.service;

import com.emb.billing.BillingRequest;
import com.emb.billing.BillingResponse;
import com.emb.billing.BillingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This service will perform remote procedure calls to the Billing Service.
 * The BillingServiceGrpc was created by running "mvn clean install".
 * The generated classes, are found in the target folder.
 * Contract: billing_service.proto
 * */
@Service
public class BillingGrpcClientService {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcClientService.class);

    @GrpcClient("billing-service")
    private BillingServiceGrpc.BillingServiceBlockingStub stub;

//    public BillingGrpcClientService(
//            @Value("${BILLING_SERVICE_ADDRESS:localhost}") String serverAddress,
//            @Value("${BILLING_SERVICE_GRPC_PORT:9001}") int serverPort) {
//
//        log.info("Connecting to Billing Service GRPC service at {}:{}",
//                serverAddress, serverPort);
//
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,
//                serverPort).usePlaintext().build();
//
//        stub = BillingServiceGrpc.newBlockingStub(channel);
//    }

    public String createBillingAccount(UUID patientId, String name, String email) {
        BillingRequest billingRequest = BillingRequest.newBuilder()
                .setPatientId(patientId.toString())
                .setName(name)
                .setEmail(email)
                .build();

        BillingResponse billingResponse = stub.createBillingAccount(billingRequest);

        if(billingResponse != null) {
            log.info("processPatientsPayment: BillingResponse received {}", billingResponse.toString());
            return billingResponse.getStatus();
        }
        else {
            log.info("processPatientsPayment: BillingResponse is null");
            return "";
        }
    }
}
