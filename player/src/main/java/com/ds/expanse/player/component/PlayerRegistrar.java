package com.ds.expanse.player.component;

import com.ds.expanse.commonmessenger.Sender;
import com.ds.expanse.player.model.Player;
import com.ds.expanse.player.model.PlayerPosition;
import com.ds.expanse.player.repository.PlayerRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class PlayerRegistrar {
    private final static Optional<Player> NULL_OPTIONAL_PLAYER = Optional.ofNullable(null) ;

    @Autowired
    private PlayerRestRepository playerRepository;

    @Autowired
    private Sender sender;

    public Optional<Player> register(Player player) {
        return Optional.ofNullable(
                         playerRepository.findByName(player.getName()).isPresent() ? null : readyPlayerForSaving(player)
                 );
    }

    /**
     * Readies a user supplied Player for saving.
     * @param player The Player to create.
     * @return The Player for saving.
     */
    private Player readyPlayerForSaving(Player player) {
        // remove the ID if one is passed.
        player.setId(null);

        Player savedPlayer = playerRepository.save(player);

        System.out.println("New player added " + player.getName());

        return savedPlayer;
    }
}
