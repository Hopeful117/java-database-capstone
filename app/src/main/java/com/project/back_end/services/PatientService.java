package com.project.back_end.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientService {
@Autowired
private PatientRepository patientRepository;
@Autowired
private AppointmentRepository appointmentRepository;
@Autowired
private TokenService tokenService;  
public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository, TokenService tokenService) {
    this.patientRepository = patientRepository;
    this.appointmentRepository = appointmentRepository;
    this.tokenService = tokenService;   
    
    
}
@Modifying
@Transactional
public int createPatient(Patient patient) {
    try {
        patientRepository.save(patient);
        return 1; // Success
    } catch (Exception e) {
        e.printStackTrace();
        return 0; // Internal Error
    }
}

@Transactional
public ResponseEntity<List<AppointmentDTO>> getPatientAppointment(Long patientId, String token) {
    if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    if (patientId == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    String tokenEmail;
    try {
        tokenEmail = tokenService.extractEMail(token.substring(7));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String patientEmail = patientRepository.findById(patientId)
            .map(Patient::getEmail)
            .orElse(null);

    if (patientEmail == null || !patientEmail.equals(tokenEmail)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
        List<Appointment> appointments = appointmentRepository.findByPatient_Id(patientId);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getAppointment_id(),
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getLast_name(),
                        appointment.getPatient().getPatientId(),
                        appointment.getPatient().getLast_name(),
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getPhone(),
                        appointment.getPatient().getAddress(),
                        appointment.getAppointmentTime(),
                        appointment.getStatus() 
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
}
@Transactional
public ResponseEntity<List<AppointmentDTO>> filterByCondition(Long patientId, String condition) {
    try {
        Appointment.Status status;
        if ("future".equalsIgnoreCase(condition)) {
            status = Appointment.Status.SCHEDULED;
        } else if ("past".equalsIgnoreCase(condition)) {
            status = Appointment.Status.COMPLETED;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
        List<Appointment> appointments = appointmentRepository.findByPatient_Id(patientId);
        List<Appointment> filteredAppointments = appointments.stream()
                .filter(appointment -> appointment.getStatus() == status)
                .collect(Collectors.toList());
        List<AppointmentDTO> appointmentDTOs = filteredAppointments.stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getAppointment_id(),
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getLast_name(),
                        appointment.getPatient().getPatientId(),
                        appointment.getPatient().getLast_name(),
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getPhone(),
                        appointment.getPatient().getAddress(),
                        appointment.getAppointmentTime(),
                        appointment.getStatus() 
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
}
            

@Transactional
public ResponseEntity<List<AppointmentDTO>> filterByDoctor(Long patientId, String doctorName) {
    try {
        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientId(doctorName, patientId);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getAppointment_id(),
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getLast_name(),
                        appointment.getPatient().getPatientId(),
                        appointment.getPatient().getLast_name(),
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getPhone(),
                        appointment.getPatient().getAddress(),
                        appointment.getAppointmentTime(),
                        appointment.getStatus() 
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
}
@Transactional
public ResponseEntity<List<AppointmentDTO>> filterByDoctorAndCondition(Long patientId, String doctorName, String condition) {
    try {
        Appointment.Status status;
        if ("future".equalsIgnoreCase(condition)) {
            status = Appointment.Status.SCHEDULED;
        } else if ("past".equalsIgnoreCase(condition)) {
            status = Appointment.Status.COMPLETED;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getAppointment_id(),
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getLast_name(),
                        appointment.getPatient().getPatientId(),
                        appointment.getPatient().getLast_name(),
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getPhone(),
                        appointment.getPatient().getAddress(),
                        appointment.getAppointmentTime(),
                        appointment.getStatus() 
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
}
@Transactional
public ResponseEntity<Patient> getPatientDetails(String token) {
    if (token == null ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    String tokenEmail;
    try {
        tokenEmail = tokenService.extractEMail(token);
        Patient patient = patientRepository.findByEmail(tokenEmail);
            if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            }
        return ResponseEntity.ok(patient);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
 
    
}
}
