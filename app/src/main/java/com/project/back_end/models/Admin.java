package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Admin {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long admin_id;

   @NotNull(message = "Username cannot be null")
   @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
   private String username;

   @NotNull(message = "Password cannot be null")
   @Size(min=8,max=255, message = "Password must be between 8 and 255 characters")
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   private String password; 

   @NotNull(message = "Email cannot be null")
   @Size(max=255, message = "Email cannot exceed 255 characters")
   @Email(message = "Email should be valid")
   private String email;

   protected void setAdmin_id(Long admin_id) {
       this.admin_id = admin_id;
   }
   
   public void setUsername(String username) {
       this.username = username;
   }
   public String getUsername() {
         return username;
    }

    void setPassword(String password) {
         this.password = password;

    }
    public String getPassword() {
         return password;
    }
    void setEmail(String email) {
         this.email = email;
    }
    public String getEmail() {
         return email;
    }


}
