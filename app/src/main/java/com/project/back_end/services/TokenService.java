package com.project.back_end.services;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;

@Component
public class TokenService {
@Autowired
private AdminRepository adminRepository;
@Autowired
private DoctorRepository doctorRepository;
@Autowired
private PatientRepository patientRepository;
@Value("${jwt.secret}")
private String jwtSecret;
public TokenService(AdminRepository adminRepository, DoctorRepository doctorRepository,
        PatientRepository patientRepository) {
    this.adminRepository = adminRepository;
    this.doctorRepository = doctorRepository;
    this.patientRepository = patientRepository;
}
public SecretKey getSigningKey() {
    String secretKey = jwtSecret;
    return Keys.hmacShaKeyFor(secretKey.getBytes());
}
public String generateToken(String identifier) {
    SecretKey key = getSigningKey();
    String jwt = Jwts.builder()
            .subject(identifier)
            .issuedAt(new java.util.Date())
            .expiration(new java.util.Date(System.currentTimeMillis() + 604800000L)) // 7 days
            .signWith(key)
            .compact();
    return jwt;
}

public String extractEMail(String token) {
    SecretKey key = getSigningKey();
    return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
}
public boolean validateToken(String token, String user) {
    try {
        String email = extractEMail(token);
        String userLower = user.toLowerCase();
        switch (userLower) {
            case "admin":
                return adminRepository.findByUsername(userLower) != null;
            case "doctor":
                return doctorRepository.findByEmail(email) != null;
            case "patient":
                return patientRepository.findByEmail(email) != null;
            default:
                return false;
        }
    } catch (Exception e) {
        return false;
    }



}
}
