package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import entity.UserEntity;
import repository.UserRepository;

@Service
public class UserAuthentication implements UserDetailsService {

    @Autowired
    private final UserRepository _userRepository;

    public UserAuthentication(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this._userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = _userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
            return userEntity;
        
    }
}