package com.emb.patientservice.service;

import com.emb.patientservice.PatientMapper;
import com.emb.patientservice.dto.PatientRequestDTO;
import com.emb.patientservice.dto.PatientResponseDTO;
import com.emb.patientservice.exception.EmailAlreadyExistsException;
import com.emb.patientservice.exception.PatientNotExistsException;
import com.emb.patientservice.exception.PatientNotFoundException;
import com.emb.patientservice.model.Patient;
import com.emb.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        String email = patientRequestDTO.getEmail();
        if (patientRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format("A patient with the email [%s] already exists", email));
        }
        Patient patient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));

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
