package com.ds.expanse.command.component.command;

import com.ds.expanse.command.component.adapter.CommandProcessEngineAdapter;
import com.ds.expanse.command.model.spi.cartograph.MapGrid;
import com.ds.expanse.command.model.spi.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class DefaultContext implements Context {
    @Getter private String command;

    /**
     * The sourcePlayer is an unvalidated entity and should not be trusted.
     */
    @Getter @Setter private Player sourcePlayer;

    @Getter @Setter private CommandProcessEngineAdapter adapter;

    /**
     * The Player is a validated entity and can be trusted.
     */
    @Getter @Setter private Player player;

    @Getter @Setter private MapGrid mapGrid;

    @Getter private CommandResult result;

    @Getter private String userName;

    @Getter @Setter private Map<String, Object> results = new HashMap<>();

    public DefaultContext(String command) {
        this.command = command;
    }

    public DefaultContext(String userName, String command) {
        this.userName = userName;
        this.command = command;
    }

    public final static Optional<DefaultContext> fromContext(Context context) {
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
