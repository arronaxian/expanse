package com.ds.expanse.command.controller;

import com.ds.expanse.command.model.spi.player.Player;
import com.ds.expanse.command.service.CommandControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;

/**
 * Command REST controller.
 */
@RestController
@RequestMapping(path="/command")
public class CommandController {
    @Autowired
    private CommandControllerService service;

    /**
     * Gets the Player for the current User.
     * @param principal The Principal entity.
     * @return A Player entity.
     */
    @GetMapping("/player")
    public ResponseEntity<Player> getPlayer(Principal principal) {
        return service.getPlayer(getUsernameOrThrow(principal))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Changes the Player's position relative to the Player's current position.
     *
     * @param principal The Principal entity.
     * @param playerName The name of the player_find.
     * @param heading The direction of travel.
     * @param units Optional units of travel (default is 1).
     * @return A PlayerPosition entity.
     *
     * @throws AccessDeniedException if not logged in.
     */
    @PutMapping(path="/player/{name}/position")
    public ResponseEntity<?> changePlayerPosition(Principal principal,
                                                  @PathVariable(name="name", required = true) String playerName,
                                                  @RequestParam(name="heading", required = true) String heading,
                                                  @RequestParam(name="units", defaultValue = "1") int units) {
        return service.changePlayerPosition(getUsernameOrThrow(principal), playerName, heading, units)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Registers a Player to the current User.
     *
     * @param player The player_find to be registered.
     * @return A Player entity.
     */
    @PostMapping(path="/player/register")
    public ResponseEntity<Player> registerPlayer(Principal principal, @RequestBody(required = true) Player player) {
        final Optional<Player> opt = service.registerPlayer(getUsernameOrThrow(principal), player);
        if ( opt.isPresent() ) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path="/player/{name}")
    public ResponseEntity<?> getPlayer(Principal principal, @PathVariable(name="name", required = false) String playerName) {
        return service.getPlayer(principal.getName(), playerName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Gets the username from the Principal.
     * @param principal The principal entity.
     * @return The user name
     *
     * @throws AccessDeniedException is thrown if no principal available.
     */
    private String getUsernameOrThrow(Principal principal) {
        if ( principal == null ) {
            throw new AccessDeniedException("Please login.");
        } else {
            return principal.getName();
        }
    }
}
