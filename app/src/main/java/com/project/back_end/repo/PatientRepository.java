package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.email = :email")
    Patient findByEmail(String email);

    @Query("SELECT p FROM Patient p WHERE p.email = :email OR p.phone = :phone")
    Patient findByEmailOrPhone(String email, String phone);
   

}

