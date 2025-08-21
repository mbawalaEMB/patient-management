package com.emb.patientservice.service;

import com.emb.billing.BillingRequest;
import com.emb.billing.BillingResponse;
import com.emb.billing.BillingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PatientBillingService {

    private static final Logger log = LoggerFactory.getLogger(PatientBillingService.class);

    @GrpcClient("billing-service")
    private BillingServiceGrpc.BillingServiceBlockingStub stub;

    public String processPatientsPayment(UUID patientId, double amount) {
        BillingRequest billingRequest = BillingRequest.newBuilder()
                .setPatientId(patientId.toString())
                .setName("John Doe")
                .setEmail("john.doe@example.com")
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
