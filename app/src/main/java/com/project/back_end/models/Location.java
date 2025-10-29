package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long location_id;

    @NotNull(message="Location name cannot be null")
    @Size(min=3, max=100, message="Location name must be between 3 and 100 characters")
    private String name;

    @NotNull(message="Location address cannot be null")
    @Size(max=255, message="Location address cannot exceed 255 characters")
    private String address;

    @NotNull(message="Location city cannot be null")
    @Size(max=100, message="Location city cannot exceed 100 characters")
    private String city;

    @NotNull(message="Phone number cannot be null")
    @Pattern(regexp="\\d{10}", message="Phone number must be 10 digits")
    private String phone;

    protected void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }
    public Long getLocation_id() {
        return location_id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }
    
}
