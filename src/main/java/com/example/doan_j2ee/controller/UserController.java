package com.example.doan_j2ee.controller;

import com.example.doan_j2ee.dto.UserDTO;
import com.example.doan_j2ee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // THAY THẾ: từ javax.validation.Valid sang jakarta.validation.Valid

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) 
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- 1. CREATE (THÊM/ĐĂNG KÝ) ---
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO request) {
        try {
            UserDTO newUser = userService.registerNewUser(request);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- 2. READ ALL (HIỆN TẤT CẢ) ---
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // --- 3. READ ONE (HIỆN THEO ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.findUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- 4. UPDATE (SỬA) ---
    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO request) throws IllegalArgumentException {
        try {
            UserDTO updatedUser = userService.updateUser(request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
             return ResponseEntity.notFound().build();
        }
    }

    // --- 5. DELETE (XÓA) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}