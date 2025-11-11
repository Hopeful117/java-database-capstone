package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserService service;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    
    public AppointmentService(AppointmentRepository appointmentRepository, UserService service, TokenService tokenService, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.service = service;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Failure
        }
    }
    @Modifying
    @Transactional
    public String updateAppointment(Long appointmentId, Appointment updatedAppointment, Long patientId) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (existingAppointment == null) {
            return "Appointment not found.";
        }
        if (!existingAppointment.getPatient().getPatientId().equals(patientId)) {
            return "You are not authorized to update this appointment.";
        }
        if (existingAppointment.getStatus().equals("COMPLETED") || existingAppointment.getStatus().equals("CANCELED")) {
            return "This appointment cannot be updated.";
        }
        boolean isDoctorAvailable = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
            updatedAppointment.getDoctor().getId(),
            updatedAppointment.getAppointmentTime().minusMinutes(30),
            updatedAppointment.getAppointmentTime().plusMinutes(30)
        ).isEmpty();
        if (!isDoctorAvailable) {
            return "The doctor is not available at the selected time.";
        }
        existingAppointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        appointmentRepository.save(existingAppointment);
        return "Appointment updated successfully.";
    }
    @Modifying
    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (existingAppointment == null) {
            return "Appointment not found.";
        }
        if (!existingAppointment.getPatient().getPatientId().equals(patientId)) {
            return "You are not authorized to cancel this appointment.";
        }
        try {
            appointmentRepository.delete(existingAppointment);
            return "Appointment cancelled successfully.";
        } catch (Exception e) {
            return "Error occurred while cancelling the appointment.";
        }
    }

    @Transactional
    public List<Appointment> getAppointments(String token, LocalDate date, String patientName) {
        String doctorEmail =tokenService.extractEMail(token) ;
        Long doctorId = doctorRepository.findByEmail(doctorEmail).getId();
        LocalDateTime startOfDay;
        LocalDateTime endOfDay;
        if (date != null) {
         startOfDay = date.atStartOfDay();
         endOfDay = date.atTime(23, 59, 59);
        }
        else {
            startOfDay = LocalDateTime.of(1900, 1, 1, 0, 0);  // très ancienne date
            endOfDay = LocalDateTime.of(3000, 1, 1, 23, 59, 59); //
        }
            
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId,startOfDay,endOfDay);
        if (patientName != null && !patientName.isEmpty()&& !patientName.equals("null")) {
            return appointments.stream()
                    .filter(appointment -> appointment.getPatient().getLast_name().toLowerCase().contains(patientName.toLowerCase()))
                    .toList();
          
        } else {
            return appointments;
        }

    }
   
}