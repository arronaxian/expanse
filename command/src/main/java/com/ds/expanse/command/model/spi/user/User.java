package com.ds.expanse.command.model.spi.user;

import com.ds.expanse.command.model.spi.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class User {
    @Getter @Setter String id;
    @Getter @Setter List<Player> playerIds;

    public User(String id) {
        this.id = id;
    }

}
