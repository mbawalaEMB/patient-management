package com.emb.patientservice.service;

import com.emb.patientservice.PatientMapper;
import com.emb.patientservice.dto.PatientRequestDTO;
import com.emb.patientservice.dto.PatientResponseDTO;
import com.emb.patientservice.exception.EmailAlreadyExistsException;
import com.emb.patientservice.exception.PatientNotFoundException;
import com.emb.patientservice.kafka.KafkaProducer;
import com.emb.patientservice.model.Patient;
import com.emb.patientservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final BillingGrpcClientService billingGrpcClientService;
    private final KafkaProducer kafkaProducer;


    public PatientService(PatientRepository patientRepository, BillingGrpcClientService billingGrpcClientService
    , KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingGrpcClientService = billingGrpcClientService;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        logger.info("------------------- createPatient ----------------");
        String email = patientRequestDTO.getEmail();
        if (patientRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format("A patient with the email [%s] already exists", email));
        }
        Patient patient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));

        // Creating a billing account for this patient
        billingGrpcClientService.createBillingAccount(patient.getId(), patient.getName(), patient.getEmail());
        kafkaProducer.sendEvent(patient);

        return PatientMapper.toDto(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException(String.format("Patient with ID [%s] not found with: ", id)));

        // check if there's a patient with this email but with a different id
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException(String.format("A patient with this email [%s] already exists", patientRequestDTO.getEmail()));
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDto(updatedPatient);
    }

    public PatientResponseDTO patchPatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException(String.format("Patient with ID [%s] not found with: ", id)));

        if (patientRequestDTO.getName() != null) {
            patient.setName(patientRequestDTO.getName());
        }
        if (patientRequestDTO.getAddress() != null) {
            patient.setAddress(patientRequestDTO.getAddress());
        }
        if (patientRequestDTO.getEmail() != null) {
            patient.setEmail(patientRequestDTO.getEmail());
        }
        if (patientRequestDTO.getDateOfBirth() != null) {
            patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        }

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDto(updatedPatient);
    }

    public void deletePatient(UUID id) {
        if(patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        }
        else {
            throw new PatientNotFoundException(String.format("Patient with ID [%s] not found with: ", id));
        }
    }
}
