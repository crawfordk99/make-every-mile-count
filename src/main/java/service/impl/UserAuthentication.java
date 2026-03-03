package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import service.api.UserService;
import repository.UserRepository;
import entity.UserEntity;

@Service
public class UserAuthentication implements UserService {

    @Autowired
    private final UserRepository _userRepository;

    @Autowired
    private final BCryptPasswordEncoder _passwordEncoder;

    public UserAuthentication(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(String email, String password) throws Exception {
        if (_userRepository.existsByEmail(email) == true) {
            throw new Exception("User already exists");
        }
        String passwordHash = _passwordEncoder.encode(password);
        _userRepository.save(new UserEntity(email, passwordHash));
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        UserEntity userEntity = _userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
            return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPasswordHash())
                .roles("USER") // You can customize roles as needed
                .build();
        
    }
}