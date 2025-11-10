package com.project.back_end.controllers;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.UserService;

import jakarta.validation.Valid;

import com.project.back_end.DTO.Login;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("${api.path}patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private UserService service;
    public PatientController(PatientService patientService, UserService service) {
        this.patientService = patientService;
        this.service = service;
    } 


@GetMapping("/{token}")
public ResponseEntity<?> getPatient(
        @PathVariable("token") String token) {
    String role = "patient";
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.getBody().get("status").equals("Valid token")) {
    ResponseEntity<Patient> patientResponse =patientService.getPatientDetails(token);
    return patientResponse;
    } else if (validationResponse.getBody().get("status").equals("Expired token")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token. Access denied.");
    }
}


@PostMapping
public ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient) {
   Boolean exists = service.validatePatient(patient);
    if (exists) {
         return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient with the given email or username already exists.");
    }
    int creationResult = patientService.createPatient(patient);
    if (creationResult == 1) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Patient created successfully.");
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the patient.");
    }
    
}


@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Login login) {
    ResponseEntity<Map<String, String>> loginResponse = service.validatePatientLogin(login);
    if (loginResponse.getStatusCode().equals(HttpStatus.OK)) {
        return ResponseEntity.ok(loginResponse.getBody());
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse.getBody().get("status"));
    }
}

@GetMapping("/appointment/{patientId}/{token}")
public ResponseEntity<?> getPatientAppointment(
        @PathVariable("patientId") Long patientId,
        @PathVariable("token") String token){
    String role = "patient";
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.getBody().get("status").equals("Valid token")) {
    ResponseEntity<?> appointmentResponse =patientService.getPatientAppointment(patientId,token);
    return appointmentResponse;
    } else if (validationResponse.getBody().get("status").equals("Expired token")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token. Access denied.");
    }
}
@GetMapping("/filter/{condition}/{name}/{token}")
public ResponseEntity<?> filterPatientAppointments(
        @PathVariable("condition") String condition,
        @PathVariable("name") String name,
        @PathVariable("token") String token){
    String role = "patient";
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
    if (validationResponse.getBody().get("status").equals("Valid token")) {
    ResponseEntity<?> filterResponse =service.filterPatient(condition,name,token);
    return filterResponse;
    } else if (validationResponse.getBody().get("status").equals("Expired token")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token. Access denied.");
    }
}







}


