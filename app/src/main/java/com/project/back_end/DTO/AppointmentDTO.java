package com.project.back_end.DTO;

import com.project.back_end.models.Appointment.Status;

public class AppointmentDTO {
private Long id;
private Long doctorId; // Simplified field for Doctor ID
private String doctorName; // Simplified field for Doctor Name
private Long patientId; // Simplified field for Patient ID
private String patientName; // Simplified field for Patient Name
private String patientEmail; // Simplified field for Patient Email
private String patientPhone; // Simplified field for Patient Phone
private String patientAddress; // Simplified field for Patient Address
private java.time.LocalDateTime appointmentTime;
private Status status;
private java.time.LocalDate appointmentDate; // Custom getter field
private java.time.LocalTime appointmentTimeOnly; // Custom getter field
private java.time.LocalDateTime endTime; // Custom getter field
public AppointmentDTO(Long id, Long doctorId, String doctorName, Long patientId, String patientName, String patientEmail, String patientPhone, String patientAddress, java.time.LocalDateTime appointmentTime, Status status) {
    this.id = id;
    this.doctorId = doctorId;
    this.doctorName = doctorName;
    this.patientId = patientId;
    this.patientName = patientName;
    this.patientEmail = patientEmail;
    this.patientPhone = patientPhone;
    this.patientAddress = patientAddress;
    this.appointmentTime = appointmentTime;
    this.status = status;
    this.appointmentDate = appointmentTime.toLocalDate();
    this.appointmentTimeOnly = appointmentTime.toLocalTime();
    this.endTime = appointmentTime.plusHours(1); // Assuming a fixed duration of 1 hour
}
public Long getId() {
    return id;
}
public Long getDoctorId() {
    return doctorId;
}
public String getDoctorName() {
    return doctorName;
}
public Long getPatientId() {
    return patientId;
}
public String getPatientName() {
    return patientName;
}
public String getPatientEmail() {
    return patientEmail;
}
public String getPatientPhone() {
    return patientPhone;
}
public String getPatientAddress() {
    return patientAddress;
}
public java.time.LocalDateTime getAppointmentTime() {
    return appointmentTime;
}
public Status getStatus() {
    return status;
}
public java.time.LocalDate getAppointmentDate() {
    return appointmentDate;
}
public java.time.LocalTime getAppointmentTimeOnly() {
    return appointmentTimeOnly;
}
public java.time.LocalDateTime getEndTime() {
    return endTime;
}       
}