package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.UserService;

import jakarta.validation.Valid;




@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {
@Autowired
private DoctorService doctorService;
@Autowired
private UserService service;
public DoctorController(DoctorService doctorService, UserService service) {
    this.doctorService = doctorService;
    this.service = service;
}



@GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
public ResponseEntity<?> getDoctorAvailability(
        @PathVariable("user") String user,
        @PathVariable("doctorId") Long doctorId,
        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @PathVariable("token") String token) {
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, user);
    if (!validationResponse.getBody().get("status").equals("Valid token")) {
        return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody().get("message"));
    }
    List <String> isAvailable = doctorService.getDoctorAvailability(doctorId, date);
    String availabilityMessage = isAvailable.isEmpty() ? "Doctor is not available on " + date.toString()
            : "Doctor is available on " + date.toString() + " at: " + String.join(", ", isAvailable);
    return ResponseEntity.ok(availabilityMessage);
}

@GetMapping
public ResponseEntity<?> getDoctor() {
    List<?> doctors = doctorService.getDoctors();
    return ResponseEntity.ok(Map.of("doctors", doctors));
}

@PostMapping("/{token}")
public ResponseEntity<?> saveDoctor(
        @RequestBody @Valid Doctor doctor,
        @PathVariable ("token") String token) {
    String role = "admin";
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
    if(!validationResponse.getStatusCode().equals(HttpStatus.OK)) {
        return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody().get("message"));
    }
  
    
    int saveResult = doctorService.saveDoctor(doctor);
    if (saveResult == 1) {
        return ResponseEntity.ok("Doctor registered successfully.");
    } else if (saveResult == -1) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor with the same email already exists.");
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the doctor.");
    }
}

@PostMapping("/login")
public ResponseEntity<?> doctorLogin(@RequestBody @Valid Login login) {
   String email = login.getEmail();
   String password = login.getPassword();
   String token = doctorService.validateDoctor(email, password);
    if (token.equals("Doctor not found.")) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(token);
    } else if (token.equals("Invalid password.")) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token);
    } else {
         return ResponseEntity.ok(Map.of("message", "Login successful.", "token", token));
    }
}

@PutMapping("/{token}")
public ResponseEntity<?> updateDoctor(
        @RequestBody @Valid Doctor doctor,
        @PathVariable("token") String token) {
    String role = "admin";
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
    if (!validationResponse.getBody().get("status").equals("Valid token")) {
        return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody().get("message"));
    }
    int updateResult = doctorService.updateDoctor(doctor);
    if (updateResult == 1) {
        return ResponseEntity.ok("Doctor updated successfully.");
    } else if (updateResult == -1) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the doctor.");
    }
}


@DeleteMapping("/{doctorId}/{token}")
public ResponseEntity<?> deleteDoctor(
        @PathVariable("doctorId") Long doctorId,
        @PathVariable("token") String token) {
    String role = "admin";
    ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
   if(!validationResponse.getStatusCode().equals(HttpStatus.OK)) {
       return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody().get("message"));
   }
    int deleteResult = doctorService.deleteDoctor(doctorId);
    if (deleteResult == 1) {
        return ResponseEntity.ok("Doctor deleted successfully.");
    } else if (deleteResult == -1) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the doctor.");
    }
}


@GetMapping("/filter/{name}/{time}/{speciality}")
public ResponseEntity<?> filter(
        @PathVariable("name") String name,
        @PathVariable("time") String time,
        @PathVariable("speciality") String speciality) {
    Map<String,Object> filteredDoctors = service.filterDoctor(name, speciality, time);
    return ResponseEntity.ok(filteredDoctors);
}



}
