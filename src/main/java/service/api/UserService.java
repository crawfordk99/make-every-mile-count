package service.api;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    void registerUser(String email, String password) throws Exception;
    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;
}
