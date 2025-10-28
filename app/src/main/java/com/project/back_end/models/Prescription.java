package com.project.back_end.models;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;

    @Pattern(regexp = "^RX-\\d{4}-\\d{6}$", message = "Prescription ID must follow the format RX-YYYY-NNNNNN")
    private String prescriptionId;

    @NotNull(message = "Patient ID cannot be null")
    private Long patientId;

    private PatientSnapshot patientSnapshot;

    @NotNull(message = "Doctor ID cannot be null")
    private Long doctorId;

    @NotNull(message = "Appointment ID cannot be null")
    private Long appointmentId;

    @NotNull(message = "Issued date must be provided")
    private Instant issuedAt;

    @Future(message = "Valid until date must be in the future")
    private Instant validUntil;

    @NotEmpty(message = "Prescription must include at least one medication")
    private List<@Valid Medication> medications;

    private PrescriptionNotes prescriptionNotes;

    @Valid
    private Pharmacy pharmacy;

    private List<@Valid Attachment> attachments;

    @Valid
    private Audit audit;

    private List<@NotBlank String> tags;

    @Valid
    private Metadata metadata;

    // ----- Constructors -----
    public Prescription() {}

    public Prescription(String id, String prescriptionId, Long patientId, PatientSnapshot patientSnapshot,
                        Long doctorId, Long appointmentId, Instant issuedAt, Instant validUntil,
                        List<Medication> medications, PrescriptionNotes prescriptionNotes,
                        Pharmacy pharmacy, List<Attachment> attachments, Audit audit,
                        List<String> tags, Metadata metadata) {
        this.id = id;
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.patientSnapshot = patientSnapshot;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.issuedAt = issuedAt;
        this.validUntil = validUntil;
        this.medications = medications;
        this.prescriptionNotes = prescriptionNotes;
        this.pharmacy = pharmacy;
        this.attachments = attachments;
        this.audit = audit;
        this.tags = tags;
        this.metadata = metadata;
    }

    // ----- Getters and Setters -----
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public PatientSnapshot getPatientSnapshot() { return patientSnapshot; }
    public void setPatientSnapshot(PatientSnapshot patientSnapshot) { this.patientSnapshot = patientSnapshot; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Instant getIssuedAt() { return issuedAt; }
    public void setIssuedAt(Instant issuedAt) { this.issuedAt = issuedAt; }

    public Instant getValidUntil() { return validUntil; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }

    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }

    public PrescriptionNotes getPrescriptionNotes() { return prescriptionNotes; }
    public void setPrescriptionNotes(PrescriptionNotes prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; }

    public Pharmacy getPharmacy() { return pharmacy; }
    public void setPharmacy(Pharmacy pharmacy) { this.pharmacy = pharmacy; }

    public List<Attachment> getAttachments() { return attachments; }
    public void setAttachments(List<Attachment> attachments) { this.attachments = attachments; }

    public Audit getAudit() { return audit; }
    public void setAudit(Audit audit) { this.audit = audit; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }

    // ----- Inner Classes -----

    public static class PatientSnapshot {
        @NotBlank
        private String firstName;

        @NotBlank
        private String lastName;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of birth must be in YYYY-MM-DD format")
        private String dob;

        @Pattern(regexp = "^\\+?[0-9 .-]{6,20}$", message = "Invalid phone number format")
        private String phone;

        public PatientSnapshot() {}
        public PatientSnapshot(String firstName, String lastName, String dob, String phone) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.phone = phone;
        }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getDob() { return dob; }
        public void setDob(String dob) { this.dob = dob; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }

    public static class Medication {
        @NotBlank
        private String name;

        @NotBlank
        private String dose;

        @NotBlank
        private String frequency;

        @Positive(message = "Duration must be a positive number")
        private int durationDays;

        private String instructions;

        private List<@NotBlank String> tags;

        private Boolean overTheCounter;

        public Medication() {}
        public Medication(String name, String dose, String frequency, int durationDays,
                          String instructions, List<String> tags, Boolean overTheCounter) {
            this.name = name;
            this.dose = dose;
            this.frequency = frequency;
            this.durationDays = durationDays;
            this.instructions = instructions;
            this.tags = tags;
            this.overTheCounter = overTheCounter;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDose() { return dose; }
        public void setDose(String dose) { this.dose = dose; }

        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }

        public int getDurationDays() { return durationDays; }
        public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }

        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }

        public Boolean getOverTheCounter() { return overTheCounter; }
        public void setOverTheCounter(Boolean overTheCounter) { this.overTheCounter = overTheCounter; }
    }

    public static class PrescriptionNotes {
        private String freeForm;
        private List<String> structuredWarnings;
        private List<String> icd10Codes;

        public PrescriptionNotes() {}
        public PrescriptionNotes(String freeForm, List<String> structuredWarnings, List<String> icd10Codes) {
            this.freeForm = freeForm;
            this.structuredWarnings = structuredWarnings;
            this.icd10Codes = icd10Codes;
        }

        public String getFreeForm() { return freeForm; }
        public void setFreeForm(String freeForm) { this.freeForm = freeForm; }

        public List<String> getStructuredWarnings() { return structuredWarnings; }
        public void setStructuredWarnings(List<String> structuredWarnings) { this.structuredWarnings = structuredWarnings; }

        public List<String> getIcd10Codes() { return icd10Codes; }
        public void setIcd10Codes(List<String> icd10Codes) { this.icd10Codes = icd10Codes; }
    }

    public static class Pharmacy {
        @NotBlank
        private String name;

        @NotBlank
        private String address;

        @Pattern(regexp = "^\\+?[0-9 .-]{6,20}$", message = "Invalid phone number format")
        private String phone;

        public Pharmacy() {}
        public Pharmacy(String name, String address, String phone) {
            this.name = name;
            this.address = address;
            this.phone = phone;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }

    public static class Attachment {
        @NotBlank
        private String fileId;

        @NotBlank
        private String filename;

        @NotBlank
        private String mimeType;

        @NotNull
        private Instant uploadedAt;

        private Map<String, Object> metadata;

        public Attachment() {}
        public Attachment(String fileId, String filename, String mimeType, Instant uploadedAt, Map<String, Object> metadata) {
            this.fileId = fileId;
            this.filename = filename;
            this.mimeType = mimeType;
            this.uploadedAt = uploadedAt;
            this.metadata = metadata;
        }

        public String getFileId() { return fileId; }
        public void setFileId(String fileId) { this.fileId = fileId; }

        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }

        public String getMimeType() { return mimeType; }
        public void setMimeType(String mimeType) { this.mimeType = mimeType; }

        public Instant getUploadedAt() { return uploadedAt; }
        public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }

        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    public static class Audit {
        private Map<String, Object> createdBy;

        @NotNull
        private Instant createdAt;

        private Instant lastModifiedAt;
        private Map<String, Object> modifiedBy;

        public Audit() {}
        public Audit(Map<String, Object> createdBy, Instant createdAt, Instant lastModifiedAt, Map<String, Object> modifiedBy) {
            this.createdBy = createdBy;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
            this.modifiedBy = modifiedBy;
        }

        public Map<String, Object> getCreatedBy() { return createdBy; }
        public void setCreatedBy(Map<String, Object> createdBy) { this.createdBy = createdBy; }

        public Instant getCreatedAt() { return createdAt; }
        public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

        public Instant getLastModifiedAt() { return lastModifiedAt; }
        public void setLastModifiedAt(Instant lastModifiedAt) { this.lastModifiedAt = lastModifiedAt; }

        public Map<String, Object> getModifiedBy() { return modifiedBy; }
        public void setModifiedBy(Map<String, Object> modifiedBy) { this.modifiedBy = modifiedBy; }
    }

    public static class Metadata {
        @Positive
        private int schemaVersion;

        @NotBlank
        private String source;

        @DecimalMin(value = "0.0") @DecimalMax(value = "1.0")
        private double confidenceScore;

        public Metadata() {}
        public Metadata(int schemaVersion, String source, double confidenceScore) {
            this.schemaVersion = schemaVersion;
            this.source = source;
            this.confidenceScore = confidenceScore;
        }

        public int getSchemaVersion() { return schemaVersion; }
        public void setSchemaVersion(int schemaVersion) { this.schemaVersion = schemaVersion; }

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        public double getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
    }
}