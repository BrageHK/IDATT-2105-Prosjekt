package edu.ntnu.idatt2105.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
@RestController
@RequestMapping("/api/vi/demo-controller")
public class DemoController {

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello world!");
    }
}
*/

@RestController
public class DemoController {

    @RequestMapping("/hello")
    public String sayHello() {
        return "Hello world!";
    }
}