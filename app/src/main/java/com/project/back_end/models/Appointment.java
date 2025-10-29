package com.project.back_end.models;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.beans.Transient;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.OnDelete;
@Entity
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long appointment_id;

  @NotNull(message = "Doctor cannot be null")
  @ManyToOne
  @JoinColumn(name = "doctor_id", nullable = false)
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private Doctor doctor;
  
  @NotNull(message = "Patient cannot be null")
  @ManyToOne
  @JoinColumn(name = "patient_id", nullable = false)
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private Patient patient;
  
  @NotNull(message = "Appointment time cannot be null")
  @Future(message = "Appointment time must be in the future")
  private LocalDateTime appointmentTime;

  @NotNull(message = "Duration cannot be null")
  private Integer durationInMinutes=60;

  public enum Status {
    SCHEDULED,
    COMPLETED,
    CANCELED
  }

  @NotNull(message = "Status cannot be null")
  @Enumerated(EnumType.STRING)
  private Status status = Status.SCHEDULED;

  @ManyToOne
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private Location location;

  @Transient
  private LocalDateTime getEndTime() {
    return this.appointmentTime.plusMinutes(this.durationInMinutes);
  }
  @Transient
  private LocalDate getAppointmentDate() {
    return this.appointmentTime.toLocalDate();
  }
  @Transient
  private LocalTime getAppointmentTimeOnly() {
    return this.appointmentTime.toLocalTime();
  }


protected void setAppointment_id(Long appointment_id) {
    this.appointment_id = appointment_id;
  }

  public Long getAppointment_id() {
    return appointment_id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public LocalDateTime getAppointmentTime() {
    return appointmentTime;
  }

  public void setAppointmentTime(LocalDateTime appointmentTime) {
    this.appointmentTime = appointmentTime;
  }

  public Integer getDurationInMinutes() {
    return durationInMinutes;
  }

  public void setDurationInMinutes(Integer durationInMinutes) {
    this.durationInMinutes = durationInMinutes;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }


}

