package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.security.authentication.AuthenticationRequest;
import edu.ntnu.idatt2105.backend.security.authentication.AuthenticationResponse;
import edu.ntnu.idatt2105.backend.security.authentication.AuthenticationService;
import edu.ntnu.idatt2105.backend.security.authentication.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
