package com.project.back_end.controllers;

import java.util.List;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import java.util.Map;

import javax.print.Doc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("${api.path}appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService service;
    public AppointmentController(AppointmentService appointmentService, UserService service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }



@GetMapping("/{appointmentDate}/{patientName}/{token}")
public ResponseEntity<?> getAppointments (
        @PathVariable("appointmentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
        @PathVariable("patientName") String patientName,
        @PathVariable("token") String token) {
    String role = "doctor";
    ResponseEntity<Map<String,String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.equals("Valid token")) {
        Long doctorId = Long.parseLong(validationResponse.getBody().get("id"));
        List<Appointment> appointments = appointmentService.getAppointments(token,appointmentDate, patientName);
        return ResponseEntity.ok(appointments);
    } else if (validationResponse.equals("Expired token")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token. Access denied.");
    }
}

@PostMapping("/{token}")
public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment, @PathVariable("token") String token) {
    String role = "patient";
    ResponseEntity<Map<String,String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.getStatusCode() != HttpStatus.OK) {
        return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody().get("message"));
    }
    int validationStatus = service.validateAppointment(appointment);
    if (validationStatus == 1) {
        int bookingResult = appointmentService.bookAppointment(appointment);
        if (bookingResult == 1) {
            return ResponseEntity.ok("Appointment booked successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book appointment. Please try again later.");
        }
    } else if (validationStatus == -1) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid doctor ID.");
    } else {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("The selected time slot is already taken. Please choose a different time.");
    }

    
}


@PutMapping("/{token}")
public ResponseEntity<?> updateAppointment(
        @RequestBody Appointment updatedAppointment,
        @PathVariable("token") String token) {
    String role = "patient";
    ResponseEntity<Map<String,String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.getStatusCode() != HttpStatus.OK) {
        return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody().get("message"));
    }
    Long patientId = Long.parseLong(validationResponse.getBody().get("id"));
    String updateResult = appointmentService.updateAppointment(updatedAppointment.getAppointment_id(), updatedAppointment, patientId);
    if (updateResult.equals("Appointment updated successfully.")) {
        return ResponseEntity.ok(updateResult);
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updateResult);
    }
}

@DeleteMapping("/{id}/{token}")
public ResponseEntity<?> cancelAppointment(
        @PathVariable("token") String token,
        @RequestBody Long appointmentId) {
    String role = "patient";
    ResponseEntity<Map<String,String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.getStatusCode() != HttpStatus.OK) {
        return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody().get("message"));
    }
    Long patientId = Long.parseLong(validationResponse.getBody().get("id"));
    String cancelResult = appointmentService.cancelAppointment(appointmentId, patientId);
    if (cancelResult.equals("Appointment cancelled successfully.")) {
        return ResponseEntity.ok(cancelResult);
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cancelResult);
    }
}



}
