package com.project.back_end.services;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
    try {
        Long appointmentId = prescription.getAppointmentId();
        List <Prescription> existingPrescription = prescriptionRepository.findByAppointmentId(appointmentId);
        if (existingPrescription != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Prescription already exists for this appointment");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        prescriptionRepository.save(prescription);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Prescription saved successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
        e.printStackTrace();
        Map<String, String> response = new HashMap<>();
        response.put("message", "An error occurred while saving the prescription");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
    
    
 


public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
    try {
        List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
        if (prescriptions == null || prescriptions.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No prescription found for this appointment");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("prescriptions", prescriptions);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
        e.printStackTrace();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "An error occurred while fetching the prescription");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


}
