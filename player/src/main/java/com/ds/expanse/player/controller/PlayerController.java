package com.ds.expanse.player.controller;

import com.ds.expanse.player.component.PlayerSearch;
import com.ds.expanse.player.model.Player;
import com.ds.expanse.player.component.PlayerRegistrar;
import com.ds.expanse.player.repository.PlayerRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(path="/player")
public class PlayerController {
    @Autowired
    private PlayerRegistrar playerRegistrar;

    @Autowired
    private PlayerRestRepository playerRestRepository;

    @Autowired
    private PlayerSearch playerSearch;

    /**
     * Registers the Player.
     * @param player The player to register.
     * @return The new Player URI response entity.
     */
    @RequestMapping(path="/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Player player) {
       return playerRegistrar
                .register(player)
                .map(registeredPlayer -> {
                    final URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{name}")
                            .buildAndExpand(registeredPlayer.getName())
                            .toUri();

                    return ResponseEntity.created(location);
                }).orElseGet(() -> ResponseEntity.badRequest()).build();
    }

    /**
     * Saves the Player.
     * @param player The Player entity to save.
     * @return The Player response entity.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Player> savePlayer(@RequestBody Player player) {
        return playerRestRepository
                .findByName(player.getName())
                .map( foundPlayer -> {
                    foundPlayer.setPosition(player.getPosition());
                    playerRestRepository.save(foundPlayer);

                    return ResponseEntity.ok(foundPlayer);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Search for the Player by criteria
     * @param searchCriteria The parameters for a Player.
     * @return The Player response entity.
     */
    @GetMapping
    public ResponseEntity<?> search(@RequestParam Map<String,String> searchCriteria) {

        // Build search criteria
        Player player = new Player();
        player.setId(searchCriteria.get("id"));
        player.setName(searchCriteria.get("name"));

        // Do the search.
        return playerSearch
                .findPlayerBy(player)
                .map(foundPlayer -> ResponseEntity.ok(foundPlayer))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
