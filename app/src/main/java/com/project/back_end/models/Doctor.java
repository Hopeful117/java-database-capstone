package com.project.back_end.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Generated;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Doctor {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long doctor_id;

@NotNull(message = "Doctor  first name cannot be null")
@Size(min = 3, max = 100, message = "Doctor name must be between 3 and 100 characters")
private String first_name;

@NotNull(message = "Doctor  last name cannot be null")
@Size(min = 3, max = 100, message = "Doctor name must be between 3 and 100 characters")
private String last_name;

@NotNull(message = "Specialty cannot be null")  
@Size(min = 3, max = 150, message = "Specialty must be between 3 and 150 characters")
private String specialty;

@NotNull(message = "Email cannot be null")
@Email(message = "Email should be valid")
@Size(max = 255, message = "Email cannot exceed 255 characters")
private String email;

@NotNull(message = "Password cannot be null")
@Size(min=8,max=255, message = "Password must be between 8 and 255 characters")
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
private String password;

@NotNull(message = "Phone number cannot be null")
@Pattern(regexp="\\d{10}", message="Phone number must be 10 digits")
private String phone;

@ElementCollection(fetch = FetchType.EAGER)
@CollectionTable(
    name = "doctor_available_times",
    joinColumns = @JoinColumn(name = "doctor_id")  
)
@Column(name = "available_times") 
private List<String> availableTimes;


 
protected void setId(Long id) {
    this.doctor_id = id;
}
public Long getId() {
    return doctor_id;
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
public String getSpecialty() {
    return specialty;
}
public void setSpecialty(String specialty) {
    this.specialty = specialty;

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
public List<String> getAvailableTimes() {
    return availableTimes;
}
public void setAvailableTimes(List<String> availableTimes) {
    this.availableTimes = availableTimes;
}




}

