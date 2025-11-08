package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.project.back_end.services.UserService;

@Controller
public class DashboardController {
   
    @Autowired
    private com.project.back_end.services.TokenService tokenService;

    @org.springframework.web.bind.annotation.GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@org.springframework.web.bind.annotation.PathVariable String token) {
        
        boolean error = tokenService.validateToken(token,"admin");
        if (error==false) {
            return "redirect:/index.html";
            
        } else {
            
            return "admin/adminDashboard";
        }
    }

    @org.springframework.web.bind.annotation.GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@org.springframework.web.bind.annotation.PathVariable String token) {
        boolean error = tokenService.validateToken(token, "doctor");
        if (error==false) {
              return "redirect:/index.";
            
        } else {
          
            return "doctor/doctorDashboard";
        }
    }
    

// 1. Set Up the MVC Controller Class:
//    - Annotate the class with `@Controller` to indicate that it serves as an MVC controller returning view names (not JSON).
//    - This class handles routing to admin and doctor dashboard pages based on token validation.


// 2. Autowire the Shared Service:
//    - Inject the common `Service` class, which provides the token validation logic used to authorize access to dashboards.


// 3. Define the `adminDashboard` Method:
//    - Handles HTTP GET requests to `/adminDashboard/{token}`.
//    - Accepts an admin's token as a path variable.
//    - Validates the token using the shared service for the `"admin"` role.
//    - If the token is valid (i.e., no errors returned), forwards the user to the `"admin/adminDashboard"` view.
//    - If invalid, redirects to the root URL, likely the login or home page.


// 4. Define the `doctorDashboard` Method:
//    - Handles HTTP GET requests to `/doctorDashboard/{token}`.
//    - Accepts a doctor's token as a path variable.
//    - Validates the token using the shared service for the `"doctor"` role.
//    - If the token is valid, forwards the user to the `"doctor/doctorDashboard"` view.
//    - If the token is invalid, redirects to the root URL.


}
