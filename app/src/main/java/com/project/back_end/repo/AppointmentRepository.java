package com.project.back_end.repo;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Appointment;

import jakarta.transaction.Transactional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  // 🔹 Récupérer les rendez-vous d’un docteur entre deux dates
  @Query("""
      SELECT a FROM Appointment a 
      LEFT JOIN FETCH a.doctor d 
      LEFT JOIN FETCH d.availableTimes 
      WHERE d.doctor_id = :doctorId 
      AND a.appointmentTime BETWEEN :start AND :end
      """)
  List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
      @Param("doctorId") Long doctorId,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end
  );

  // 🔹 Filtrer par docteur, nom de patient et plage horaire
  @Query("""
      SELECT a FROM Appointment a 
      LEFT JOIN FETCH a.doctor d 
      LEFT JOIN FETCH a.patient p 
      WHERE d.doctor_id = :doctorId 
      AND LOWER(p.last_name) LIKE LOWER(CONCAT('%', :patientName, '%')) 
      AND a.appointmentTime BETWEEN :start AND :end
      """)
  List<Appointment> findByDoctorIdAndPatientNameContainingIgnoreCaseAndAppointmentTimeBetween(
      @Param("doctorId") Long doctorId,
      @Param("patientName") String patientName,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end
  );

  // 🔹 Supprimer tous les rendez-vous d’un docteur
  @Modifying
  @Transactional
  @Query("DELETE FROM Appointment a WHERE a.doctor.doctor_id = :doctorId")
  void deleteAllByDoctorId(@Param("doctorId") Long doctorId);

  // 🔹 Récupérer les rendez-vous d’un patient
  @Query("SELECT a FROM Appointment a WHERE a.patient.patient_id = :patientId")
  List<Appointment> findByPatient_Id(@Param("patientId") Long patientId);

  // 🔹 Récupérer les rendez-vous d’un patient selon le statut
  @Query("""
      SELECT a FROM Appointment a 
      WHERE a.patient.patient_id = :patientId 
      AND a.status = :status 
      ORDER BY a.appointmentTime ASC
      """)
  List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
      @Param("patientId") Long patientId,
      @Param("status") Appointment.Status status
  );

  // 🔹 Filtrer par nom du docteur et patient
  @Query("""
      SELECT a FROM Appointment a 
      WHERE LOWER(a.doctor.last_name) LIKE LOWER(CONCAT('%', :doctorName, '%')) 
      AND a.patient.patient_id = :patientId
      """)
  List<Appointment> filterByDoctorNameAndPatientId(
      @Param("doctorName") String doctorName,
      @Param("patientId") Long patientId
  );

  // 🔹 Filtrer par nom du docteur, patient et statut
  @Query("""
      SELECT a FROM Appointment a 
      WHERE LOWER(a.doctor.last_name) LIKE LOWER(CONCAT('%', :doctorName, '%')) 
      AND a.patient.patient_id = :patientId 
      AND a.status = :status
      """)
  List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
      @Param("doctorName") String doctorName,
      @Param("patientId") Long patientId,
      @Param("status") Appointment.Status status
  );

  
 
}
