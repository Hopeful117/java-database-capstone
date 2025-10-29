package com.project.back_end.models;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patient_id;

    @NotNull(message="Patient first name cannot be null")
    @Size(min=3, max=100, message="Patient name must be between 3 and 100 characters")
    private String first_name;
    @NotNull(message="Patient last name cannot be null")
    @Size(min=3, max=100, message="Patient name must be between 3 and 100 characters")
    private String last_name;

    @NotNull(message="Email cannot be null")
    @Email(message="Email should be valid")
    @Size(max=255, message="Email cannot exceed 255 characters")
    private String email;

    @NotNull(message="Password cannot be null")
    @Size(min=8,max=255, message = "Password must be between 8 and 255 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message="Phone number cannot be null")
    @Pattern(regexp="\\d{10}", message="Phone number must be 10 digits")
    private String phone;

    @NotNull(message="Address cannot be null")
    @Size(max=255, message="Address cannot exceed 255 characters")
    private String address;

    @NotNull(message="City cannot be null")
    @Size(max=100, message="City cannot exceed 100 characters")
    private String city;

    @NotNull(message="Date of birth cannot be null")
    private Date date_of_birth;

    public enum Gender {
        Male,
        Female
    }
    @NotNull(message="Gender cannot be null")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    protected void setPatientId(Long patient_id) {
        this.patient_id = patient_id;
    }
    public Long getPatientId() {
        return patient_id;
    }
    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public Date getDate_of_birth() {
        return date_of_birth;
    }
    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
  

}
