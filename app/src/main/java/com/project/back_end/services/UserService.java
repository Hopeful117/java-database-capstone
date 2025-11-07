package com.project.back_end.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import java.time.LocalDate;
import java.time.LocalTime;
@Service
public class UserService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    public UserService(TokenService tokenService, AdminRepository adminRepository, DoctorService doctorService,
            PatientService patientService, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }
    

public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
    try {
        boolean isValid = tokenService.validateToken(token, user);
        if (!isValid) {
            Map<String, String> response = Map.of("message", "Invalid or expired token");
            return ResponseEntity.status(401).body(response);
        }
        Map<String, String> response = Map.of("message", "Token is valid"); 
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, String> response = Map.of("message", "An error occurred during token validation");
        return ResponseEntity.status(500).body(response);
    }
}

public ResponseEntity <Map<String,String>> validateAdmin(Admin receivedAdmin) {
    try {
        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
        if (admin != null) {
            if (admin.getPassword().equals(receivedAdmin.getPassword())) {
                String jwtToken = tokenService.generateToken(admin.getUsername());
                Map<String, String> response = Map.of("token", jwtToken);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = Map.of("message", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }
        } else {
            Map<String, String> response = Map.of("message", "Admin not found");
            return ResponseEntity.status(401).body(response);
        }
    } catch (Exception e) {
        Map<String, String> response = Map.of("message", "An error occurred during admin validation");
        return ResponseEntity.status(500).body(response);
    }
    
}
public Map<String, Object> filterDoctor(String name, String specialty, String time) {
    Map<String, Object> response = new HashMap<>();
    try {
        // 🔹 Normalisation des paramètres : convertit "null" ou "" en null
        name = (name == null || name.equalsIgnoreCase("null") || name.isBlank()) ? null : name;
        specialty = (specialty == null || specialty.equalsIgnoreCase("null") || specialty.isBlank()) ? null : specialty;
        time = (time == null || time.equalsIgnoreCase("null") || time.isBlank()) ? null : time;

        List<Doctor> doctors;

        if (name != null && specialty != null && time != null) {
            doctors = doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
        } else if (name != null && specialty != null) {
            doctors = doctorService.filterDoctorByNameAndSpecility(name, specialty);
        } else if (name != null && time != null) {
            doctors = doctorService.filterDoctorByNameAndTime(name, time);
        } else if (specialty != null && time != null) {
            doctors = doctorService.filterDoctorByTimeAndSpecility(specialty, time);
        } else if (name != null) {
            doctors = doctorService.findDoctorByName(name);
        } else if (specialty != null) {
            doctors = doctorService.filterDoctorBySpecility(specialty);
        } else if (time != null) {
            doctors = doctorService.filterDoctorByTime(time);
        } else {
            doctors = doctorRepository.findAll();
        }

        response.put("doctors", doctors);
        return response;

    } catch (Exception e) {
        response.put("message", "An error occurred while filtering doctors");
        return response;
    }
}


public int validateAppointment(Appointment appointment) {
    try {
        Long doctorId = appointment.getDoctor().getId();
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return -1; // Doctor does not exist
        }
        LocalDate appointmentDate = appointment.getAppointmentTime().toLocalDate();
        List<String> availableSlots = doctorService.getDoctorAvailability(doctorId, appointmentDate);
        LocalTime requestedTime = appointment.getAppointmentTime().toLocalTime();
        for (String slot : availableSlots) {
            if (slot.equals(requestedTime)) {
                return 1; // Valid appointment time
            }
        }
        return 0; // Invalid appointment time
    } catch (Exception e) {
        e.printStackTrace();
        return -1; // Error occurred
    }
}
public boolean validatePatient(Patient patient) {
    try {
        String email = patient.getEmail();
        String phone = patient.getPhone();
        Patient existingPatient = patientRepository.findByEmailOrPhone(email, phone);
        if (existingPatient != null) {
            return false; // Patient with same email or phone already exists
        }
        return true; // Patient is valid
    } catch (Exception e) {
        e.printStackTrace();    
        return false; // Error occurred
    }
}
public ResponseEntity<Map<String, String>> validatePatientLogin(Login login){
    try {
        Patient patient = patientRepository.findByEmail(login.getEmail());
        if (patient == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Patient not found.");
            return ResponseEntity.status(401).body(response);
        }
        if (!patient.getPassword().equals(login.getPassword())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid password.");
            return ResponseEntity.status(401).body(response);
        }
        String token = tokenService.generateToken(patient.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        e.printStackTrace();
        Map<String, String> response = new HashMap<>();
        response.put("message", "An error occurred during patient login.");
        return ResponseEntity.status(500).body(response);
    }
}
public ResponseEntity<Map<String, Object>> filterPatient(String condition,String name,String token){
    try {
        String tokenEmail = tokenService.extractEMail(token.substring(7));
        Patient patient = patientRepository.findByEmail(tokenEmail);
        if (patient == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Patient not found.");
            return ResponseEntity.status(401).body(response);
        }
        ResponseEntity<?> serviceResp;
        if((condition != null && !condition.isEmpty()) && (name != null && !name.isEmpty())) {
            serviceResp = patientService.filterByDoctorAndCondition(patient.getPatientId(), condition, name);
        } else if (condition != null && !condition.isEmpty()) {
            serviceResp = patientService.filterByCondition(patient.getPatientId(), condition);
        } else if (name != null && !name.isEmpty()) {
            serviceResp = patientService.filterByDoctor(patient.getPatientId(), name);
        } else {
            serviceResp = patientService.getPatientAppointment(patient.getPatientId(), token);
        }

        Object body = serviceResp == null ? null : serviceResp.getBody();
        if (body instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> mapBody = (Map<String, Object>) body;
            return new ResponseEntity<>(mapBody, serviceResp.getStatusCode());
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("appointments", body);
            return new ResponseEntity<>(map, serviceResp == null ? org.springframework.http.HttpStatus.OK : serviceResp.getStatusCode());
        }

    } catch (Exception e) {
        e.printStackTrace();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "An error occurred while filtering patient appointments.");
        return ResponseEntity.status(500).body(response);   
}
}
}
