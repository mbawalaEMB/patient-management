package com.emb.patientservice.controller;

import com.emb.patientservice.dto.PatientRequestDTO;
import com.emb.patientservice.dto.PatientResponseDTO;
import com.emb.patientservice.service.BillingGrpcClientService;
import com.emb.patientservice.service.PatientService;
import com.emb.patientservice.validators.CreatePatientGroupValidator;
import com.emb.patientservice.validators.PatchPatientGroupValidator;
import com.emb.patientservice.validators.UpdatePatientGroupValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "API for managing Patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService, BillingGrpcClientService billingGrpcClientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get Patients", description = "Returns a list of all registered patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {

        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @PostMapping
    @Operation(summary = "Create a Patient", description = "Registers a new patient in the system")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({CreatePatientGroupValidator.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        return new ResponseEntity<>(patientService.createPatient(patientRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Patient", description = "Replaces all patient data except registered date for the given ID")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id
            , @Validated({UpdatePatientGroupValidator.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok().body(patientService.updatePatient(id, patientRequestDTO));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a Patient", description = "Replaces some patient data for the given ID")
    public ResponseEntity<PatientResponseDTO> patchPatient(@PathVariable UUID id
            , @Validated({PatchPatientGroupValidator.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok().body(patientService.patchPatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Patient", description = "Deletes a patient with the given ID from the system")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

//    /**
//     * Testing: Triggers the remote call to the Billing Service.
//     * */
//    @PostMapping("/{patientId}/billing")
//    public ResponseEntity<String> billPatient(@PathVariable UUID patientId, @RequestParam double amount) {
//        String paymentStatus = billingGrpcClientService.createBillingAccount(patientId, amount);
//
//        return ResponseEntity.ok().body("Payment Status: " + paymentStatus);
//    }

}
