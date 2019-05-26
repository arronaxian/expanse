package com.ds.expanse.user.service;

import com.ds.expanse.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lookup the application user and build the security user.
        return userRepository
                .findByUsername(username)
                .map(foundUser -> new User(foundUser.getUsername(), foundUser.getPassword(), emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}