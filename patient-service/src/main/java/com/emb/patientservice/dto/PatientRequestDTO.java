package com.emb.patientservice.dto;

import com.emb.patientservice.validators.CreatePatientGroupValidator;
import com.emb.patientservice.validators.PatchPatientGroupValidator;
import com.emb.patientservice.validators.UpdatePatientGroupValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Used to create and update the Patient data")
public class PatientRequestDTO {

    @NotBlank(message = "Name is required"
            , groups = {CreatePatientGroupValidator.class, UpdatePatientGroupValidator.class})
    @Size(max = 100, message = "Name cannot exceed 100 characters"
            , groups = {CreatePatientGroupValidator.class, UpdatePatientGroupValidator.class, PatchPatientGroupValidator.class})
    @Schema(description = "Patient's full name", example = "John Doe")
    private String name;

    @NotBlank(message = "Name is required"
            , groups = {CreatePatientGroupValidator.class, UpdatePatientGroupValidator.class})
    @Email( message = "Email should be valid"
            , groups = {CreatePatientGroupValidator.class, UpdatePatientGroupValidator.class, PatchPatientGroupValidator.class})
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Address is required"
            , groups = {CreatePatientGroupValidator.class, UpdatePatientGroupValidator.class})
    @Schema(description = "Home address", example = "University Str.1, 45307 Germany")
    private String address;

    @NotBlank(message = "Date of birth is required"
            , groups = {CreatePatientGroupValidator.class, UpdatePatientGroupValidator.class})
//    @Past(groups = {CreatePatientGroupValidator.class, UpdatePatientGroupValidator.class, PatchPatientGroupValidator.class})
    @Schema(description = "Date of birth in ISO format. Required on create", example = "2000-08-09")
    private String dateOfBirth;

    @NotBlank(message = "Registered date is required", groups = {CreatePatientGroupValidator.class})
    @Schema(description = "Registration date in ISO format. Required on create.", example = "2025-08-09")
    private String registeredDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }
}
