package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.adapter.EngineAdapter;
import com.ds.expanse.command.component.adapter.SecurityAdapter;
import com.ds.expanse.command.model.spi.cartograph.MapGrid;
import com.ds.expanse.command.model.spi.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class DefaultContext implements Context {
    /**
     * The command being performed.
     */
    @Getter private String command;

    /**
     * The User provided player_find is an unvalidated entity and should not be trusted.
     */
    @Getter @Setter private Player userProvidedPlayer;

    /**
     * The Player is a validated entity and can be trusted.
     */
    @Getter @Setter private Player player;

    /**
     * The Player's Map Grid
     */
    @Getter @Setter private MapGrid mapGrid;

    /**
     * The User's name (used for login).
     */
    @Getter private String userName;

    @Getter @Setter private EngineAdapter engineAdapter;
    @Getter @Setter private SecurityAdapter securityAdapter;

    public DefaultContext(String command) {
        this.command = command;
    }

    public DefaultContext(String userName, String command) {
        this.userName = userName;
        this.command = command;
    }

    public final static Optional<DefaultContext> of(Context context) {
        try {
            return Optional.ofNullable((DefaultContext) context);
        } catch ( NullPointerException | ClassCastException empty ) {
            return Optional.empty();
        }
    }

    public String toString() {
        return command;
    }
}
