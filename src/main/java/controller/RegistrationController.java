package controller;

import org.springframework.beans.factory.annotation.Autowired;
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
        if (_userRepository.findByEmail(userEntity.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        // Implementation for user registration
       
        userEntity.setPassword(_passwordEncoder.encode(userEntity.getPassword()));
        _userRepository.save(userEntity);

        return ResponseEntity.ok("User registered successfully");
    }
}
