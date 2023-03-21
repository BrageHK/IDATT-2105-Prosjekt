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
@RequestMapping("/demo-controller")
public class DemoController {

    @RequestMapping("/hello")
    public String sayHello() {
        return "Hello world!";
    }

    @RequestMapping("/hello/{name}")
    public ResponseEntity<String> sayHello(@PathVariable String name) {
        return ResponseEntity.ok("Hello " + name + "!");
    }

    @GetMapping
    public ResponseEntity<String> sayHello2() {
        return ResponseEntity.ok("Hello mister!");
    }
}