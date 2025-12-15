package com.furniture.auth.service;

//@Service
//public class AuthService {
//
//    @Autowired
//    private UserCredentialRepository repository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtService jwtService;
//
//    public String saveUser(UserCredential credential) {
//
//        // If role is not provided, assign USER
//        if (credential.getRole() == null || credential.getRole().isBlank()) {
//            credential.setRole("USER");
//        }
//
//        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
//        repository.save(credential);
//
//        return "user added to the system";
//    }
//
//
//    public String generateToken(String username) {
//        UserCredential user = repository.findByName(username)
//                .orElseThrow(() -> new RuntimeException("User not found: " + username));
//
//        return jwtService.generateToken(user.getName(), user.getRole());
//    }
//
//    public void validateToken(String token) {
//        jwtService.validateToken(token);
//    }
//}

import com.furniture.auth.dto.AuthRequest;
import com.furniture.auth.dto.AuthResponse;
import com.furniture.auth.entity.UserCredential;
import com.furniture.auth.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(AuthRequest request) {
        UserCredential user = UserCredential.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role("USER")
                .build();
        repository.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserCredential user = repository.findByName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getName(), user.getRole());
        return new AuthResponse(token);
    }
}

