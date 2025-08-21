package com.emb.patientservice.repository;

import com.emb.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmail(String email);

    /**
     * Checks if there is a Patient p with p.email == email and p.id != id.
     * Helps avoid having more than one patient with the same email address.
     * */
    boolean existsByEmailAndIdNot(String email, UUID id);
}
