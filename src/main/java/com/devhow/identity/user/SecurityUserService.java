package com.devhow.identity.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class SecurityUserService implements UserDetailsService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public SecurityUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByUsername(email).orElseThrow(()
                -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)))
                .securityUser();
    }

}
