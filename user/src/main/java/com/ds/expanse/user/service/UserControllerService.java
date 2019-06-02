package com.ds.expanse.user.service;

import com.ds.expanse.user.model.User;
import com.ds.expanse.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserControllerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl detailService;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Register a new User if-and-only-if the Username does not exist.
     * @param user The user to register.
     * @return
     */
    public Optional<User> register(User user) {
        if ( user.getUsername() == null || user.getUsername().trim().isEmpty() ) {
            throw new IllegalArgumentException("Invalid user name.");
        }

        Optional optional = userRepository
                .findByUsername(user.getUsername())
                .map(foundUser -> { throw new IllegalArgumentException("Invalid user name."); } )
                .or(() -> {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

                    return Optional.ofNullable(userRepository.save(user));
                });

        return optional;
    }

    /**
     * Adds a Player Id to a User
     * @param userName The name of the User
     * @param playerId The Player Id
     * @return True if successful, otherwise false.
     */
    public boolean addPlayer(String userName, String playerId) {
        return userRepository.findByUsername(userName)
                .map(foundUser -> {
                        foundUser.getPlayerIds().add(playerId);
                        return Optional.ofNullable(userRepository.save(foundUser));
                    }).isPresent();
    }

    /**
     * Gets a User's Player Ids.
     * @param userName The name of the user.
     * @return A list of Player Ids, otherwise Empty Set.
     */
    public Set<String> getPlayersIds(String userName) {
        return userRepository.findByUsername(userName)
                .map(foundUser -> foundUser.getPlayerIds()).get();
    }

}
