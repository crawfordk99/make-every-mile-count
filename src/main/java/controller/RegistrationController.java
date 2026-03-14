package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entity.UserEntity;
import repository.UserRepository;

@RestController
public class RegistrationController {

    @Autowired
    private final UserRepository _userRepository;

    @Autowired
    private final BCryptPasswordEncoder _passwordEncoder;

    public RegistrationController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity userEntity) throws Exception {
        // System.out.println("Registration attempt for email: " + userEntity.getUsername());

    try {
        // 1. Check if user already exists
        if (_userRepository.findByEmail(userEntity.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        // 2. Encode password
        userEntity.setPassword(_passwordEncoder.encode(userEntity.getPassword()));

        // 3. Save
        UserEntity savedUser = _userRepository.save(userEntity);
        // System.out.println("User saved with ID: " + savedUser.getUserId());

        return ResponseEntity.ok("User registered successfully");
    } catch (Exception e) {
        // e.printStackTrace(); // This will show database errors in your IDE console
        return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
    }
    }
}
