package com.furniture.auth.controller;

import com.furniture.auth.dto.AuthRequest;
import com.furniture.auth.dto.AuthResponse;
import com.furniture.auth.dto.TokenValidationResponse;
import com.furniture.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api/v1/auth")
//@CrossOrigin
//public class AuthController {
//    @Autowired
//    private AuthService service;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @PostMapping("/register")
//    public String addNewUser(@RequestBody UserCredential user) {
//        return service.saveUser(user);
//    }
//
//    @PostMapping("/token")
//    public AuthResponse getToken(@RequestBody AuthRequest authRequest) {
//
//        AuthResponse response = new AuthResponse();
//
//        try {
//            Authentication authenticate = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authRequest.getUsername(),
//                            authRequest.getPassword()
//                    )
//            );
//            if (authenticate.isAuthenticated()) {
//                String token = service.generateToken(authRequest.getUsername());
//                response.setToken(token);
//                response.setLoginValid("yes");
//            }
//        } catch (Exception e) {
//            response.setToken("");
//            response.setLoginValid("no");
//        }
//
//        return response;
//    }
//
//    @GetMapping("/validate")
//    public String validateToken(
//            @RequestHeader(value = "Authorization", required = false) String authHeader
//    ) {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("Invalid Authorization header");
//        }
//
//        String token = authHeader.substring(7); // remove "Bearer "
//        service.validateToken(token);
//
//        return "Token is valid";
//    }
//
//
//    @GetMapping("/test")
//    public String testApi(){
//        return "Testing...";
//    }
//}
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/token")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @GetMapping("/validate")
    public TokenValidationResponse validate() {
        // If request reaches here → token already validated by filter
        return new TokenValidationResponse(true);
    }


}
