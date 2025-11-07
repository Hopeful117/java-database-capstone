package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;
@Service
public class DoctorService {

@Autowired
private DoctorRepository doctorRepository;
@Autowired
private AppointmentRepository appointmentRepository;
@Autowired
private TokenService tokenService;

public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, TokenService tokenService) {
    this.doctorRepository = doctorRepository;
    this.appointmentRepository = appointmentRepository;
    this.tokenService = tokenService;
}

@Transactional
public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atTime(23, 59, 59);
    List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay);
    Set<LocalDateTime> bookedSlots = appointments.stream()
            .map(Appointment::getAppointmentTime)
            .collect(Collectors.toSet());

    List<String> allSlots = Arrays.asList("09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM", "03:00 PM");
    List<String> availableSlots = allSlots.stream()
            .filter(slot -> !bookedSlots.contains(slot))
            .collect(Collectors.toList());

    return availableSlots;
}
@Modifying
@Transactional
public int saveDoctor(Doctor doctor) {
    try {
        Doctor existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
        if (existingDoctor != null) {
            return -1; // Conflict: Doctor with the same email already exists
        }
        doctorRepository.save(doctor);
        return 1; // Success
    } catch (Exception e) {
        return 0; // Internal Error
    }
}

@Modifying
@Transactional
public int updateDoctor(Doctor doctor) {
    try {
        Doctor existingDoctor = doctorRepository.findById(doctor.getId()).orElse(null);
        if (existingDoctor == null) {
            return -1; // Doctor not found
        }
        doctorRepository.save(doctor);
        return 1; // Success
    } catch (Exception e) {
        return 0; // Internal Error
    }
}
@Transactional
public List<Doctor> getDoctors() {
    return doctorRepository.findAll();
}
@Modifying
@Transactional
public int deleteDoctor(Long doctorId) {
    try {
        Doctor existingDoctor = doctorRepository.findById(doctorId).orElse(null);
        if (existingDoctor == null) {
            return -1; // Doctor not found
        }
        doctorRepository.deleteById(doctorId);
        return 1; // Success
    } catch (Exception e) {
        return 0; // Internal Error
    }
}
@Transactional
public String validateDoctor(String email, String password) {
    Doctor doctor = doctorRepository.findByEmail(email);
    if (doctor == null) {
        return "Doctor not found.";
    }
    if (!doctor.getPassword().equals(password)) {
        return "Invalid password.";
    }
    String token = tokenService.generateToken(doctor.getEmail());
    return token;
}
@Transactional
public List<Doctor> findDoctorByName(String name) {
    List<Doctor> doctors = doctorRepository.findByNameLike(name);
    return doctors;
}
@Transactional
public List<Doctor> filterDoctorsByNameSpecilityandTime(String name, String specialty, String time) {
    List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    List <Doctor> filteredByTime=filterDoctorsByTime(doctors, time);
    return filteredByTime;
   
}

@Transactional
public List <Doctor> filterDoctorByTime(String time){
    List<Doctor> doctors=doctorRepository.findAll();
    List <Doctor> filteredByTime=filterDoctorsByTime(doctors, time);
    return filteredByTime;
}
@Transactional
public List <Doctor> filterDoctorByNameAndTime(String name,String time){
    List<Doctor> doctors=doctorRepository.findByNameLike(name);
    List <Doctor> filteredByTime=filterDoctorsByTime(doctors, time);
    return filteredByTime;


}
@Transactional
public List <Doctor> filterDoctorByNameAndSpecility(String name,String specialty){
    List<Doctor> doctors=doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    return doctors;
}
@Transactional
public List <Doctor> filterDoctorByTimeAndSpecility(String specialty,String time){
    List<Doctor> doctors=doctorRepository.findBySpecialtyIgnoreCase(specialty);
    List <Doctor>filteredByTime=filterDoctorsByTime(doctors, time);
    return filteredByTime;
}
@Transactional
public List <Doctor> filterDoctorBySpecility(String specialty){
    List<Doctor> doctors=doctorRepository.findBySpecialtyIgnoreCase(specialty);
    return doctors;
}
private List <Doctor>filterDoctorsByTime(List<Doctor> doctors, String time) {
    doctors.stream().filter(doctor -> {
        List<String> availableTimes = doctor.getAvailableTimes();
        if (time.equalsIgnoreCase("AM")) {
            return availableTimes.stream().anyMatch(t -> t.endsWith("AM"));
        } else if (time.equalsIgnoreCase("PM")) {
            return availableTimes.stream().anyMatch(t -> t.endsWith("PM"));
        }
        return false;
    }).collect(Collectors.toList());
    return doctors;
    




}

   
}
