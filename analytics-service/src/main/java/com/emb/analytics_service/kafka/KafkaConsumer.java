package com.emb.analytics_service.kafka;

import com.emb.patient.events.PatientEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void ConsumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // TODO Add event to database and other business logic
            logger.info("PatientEvent {} was successfully received", patientEvent.getEventType());
            logger.info("{PatientId={},PatientName={},PatientEmail={}}"
                    , patientEvent.getPatientId()
                    , patientEvent.getName(), patientEvent.getEmail());

        } catch (InvalidProtocolBufferException e) {
            logger.error("Error during deserialization of PatientEvent {}", e.getMessage());
        }
    }
}
