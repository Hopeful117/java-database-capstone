package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;

import com.project.back_end.services.UserService;

import java.util.Map;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {
    
@Autowired
private PrescriptionService prescriptionService;
@Autowired
private UserService service;
@Autowired
private AppointmentService appointmentService;
public PrescriptionController(PrescriptionService prescriptionService,UserService service, AppointmentService appointmentService) {
    this.prescriptionService = prescriptionService;
    this.service = service;
    this.appointmentService = appointmentService;
}

@PostMapping("/{token}")
public ResponseEntity<?> savePrescription(
        @Valid @RequestBody Prescription prescription,
        @PathVariable("token") String token) {
    String role = "doctor";
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.getBody().get("status").equals("Valid token")) {
        prescriptionService.savePrescription(prescription);
        return ResponseEntity.status(201).body("Prescription saved successfully.");
    } else if (validationResponse.getBody().get("status").equals("Expired token")) {
        return ResponseEntity.status(401).body("Token has expired. Please log in again.");
    } else {
        return ResponseEntity.status(403).body("Invalid token. Access denied.");
    }
}
        
@GetMapping("/{appointmentId}/{token}")
public ResponseEntity<?> getPrescription(
        @PathVariable("appointmentId") Long appointmentId,
        @PathVariable("token") String token) {
    String role = "doctor";
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.getBody().get("status").equals("Valid token")) {
        ResponseEntity<Map<String,Object>> prescription = prescriptionService.getPrescription(appointmentId);
        return prescription;
    } else if (validationResponse.getBody().get("status").equals("Expired token")) {
        return ResponseEntity.status(401).body("Token has expired. Please log in again.");
    } else {
        return ResponseEntity.status(403).body("Invalid token. Access denied.");
    }
}   

// 4. Define the `getPrescription` Method:
//    - Handles HTTP GET requests to retrieve a prescription by its associated appointment ID.
//    - Accepts the appointment ID and a doctor’s token as path variables.
//    - Validates the token for the `"doctor"` role using the shared service.
//    - If the token is valid, fetches the prescription using the `PrescriptionService`.
//    - Returns the prescription details or an appropriate error message if validation fails.


}
