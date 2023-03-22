package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.security.authentication.AuthenticationRequest;
import edu.ntnu.idatt2105.backend.security.authentication.AuthenticationResponse;
import edu.ntnu.idatt2105.backend.security.authentication.AuthenticationService;
import edu.ntnu.idatt2105.backend.security.authentication.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    // This is just for testing purposes
    @RequestMapping("/user/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello world!");
    }

    /*@PostMapping("/user/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(authenticationService.getUser(id));
    }*/
}
