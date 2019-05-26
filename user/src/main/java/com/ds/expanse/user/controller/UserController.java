package com.ds.expanse.user.controller;

import com.ds.expanse.user.model.User;
import com.ds.expanse.user.service.UserControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    UserControllerService service;

    public UserController() {
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        try {
            return service
                    .register(user)
                    .map(registeredUser -> {
                        final URI location = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{name}")
                                .buildAndExpand(registeredUser.getUsername())
                                .toUri();

                        return ResponseEntity.created(location);
                    }).orElseGet(() -> ResponseEntity.badRequest()).build();
        } catch ( IllegalArgumentException e ) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Adds a Player Id to the User
     * @param playerId The Player ID
     * @return 200 if success, otherwise 400 bad request.
     */
    @PutMapping("/{username}/player/{id}")
    public ResponseEntity addPlayer(@PathVariable(name = "username", required = true) String userName,
                                    @PathVariable(name = "id", required = true) String playerId) {
        if (service.addPlayer(userName, playerId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("player/{id}")
    public ResponseEntity removePlayer(@PathVariable(name="id", required = true) String playerId) {
        return null;
    }
}
