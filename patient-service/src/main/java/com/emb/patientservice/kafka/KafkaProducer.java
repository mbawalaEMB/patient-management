package com.emb.patientservice.kafka;

import com.emb.patient.events.PatientEvent;
import com.emb.patientservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType(EventType.PATIENT_CREATED.name()) // describes particular event inside a topic
                .build();

        try {
            logger.info("Sending message to Kafka Broker.");
            kafkaTemplate.send("patient", patientEvent.toByteArray());
        } catch (Exception e) {
            logger.error("Error sending PatientCreated event: {}", patientEvent);
        }
    }
}
