package com.Bookstoreproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/session")
public class SessionController {


    @GetMapping("/set")
    public ResponseEntity<String> setSession(HttpSession session) {
       session.setAttribute("user2", "John Doe");
        return ResponseEntity.ok("Session set with user: Piyush");
    }

    @GetMapping("/get")
    public ResponseEntity<String> getSession(HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok("Fetched from session: " + user);
        } else {
            return ResponseEntity.ok("No session found.");
        }
    }
}
