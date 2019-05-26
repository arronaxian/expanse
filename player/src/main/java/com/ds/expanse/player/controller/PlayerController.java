package com.ds.expanse.player.controller;

import com.ds.expanse.player.model.Player;
import com.ds.expanse.player.component.PlayerRegistrar;
import com.ds.expanse.player.repository.PlayerRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path="/player")
public class PlayerController {
    @Autowired
    private PlayerRegistrar playerRegistrar;

    @Autowired
    private PlayerRestRepository playerRestRepository;

    @RequestMapping(path="/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Player player) {
        ResponseEntity re = playerRegistrar
                .register(player)
                .map(registeredPlayer -> {
                    final URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{name}")
                            .buildAndExpand(registeredPlayer.getName())
                            .toUri();

                    return ResponseEntity.created(location);
                }).orElseGet(() -> ResponseEntity.badRequest()).build();

        System.out.println("POST /player/register " + player.getName() + " " + re.getStatusCode());

        return re;

    }

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

    @RequestMapping(path="/{name}", method = RequestMethod.GET)
    public ResponseEntity<Player> getPlayer(@PathVariable("name") String name) {
        return playerRestRepository
                .findByName(name)
                .map(foundPlayer -> ResponseEntity.ok(foundPlayer))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
