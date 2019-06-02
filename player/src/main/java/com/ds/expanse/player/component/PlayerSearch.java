package com.ds.expanse.player.component;

import com.ds.expanse.player.model.Player;
import com.ds.expanse.player.repository.PlayerRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerSearch {
    @Autowired
    PlayerRestRepository playerRestRepository;

    public Optional<?> findPlayerBy(Player player) {
        if ( player.getId() != null ) {
            return Optional.ofNullable(playerRestRepository.findById(player.getId()));
        } else if ( player.getName() != null ) {
            return Optional.ofNullable(playerRestRepository.findByName(player.getName()));
        } else {
            return Optional.empty();
        }
    }
}
