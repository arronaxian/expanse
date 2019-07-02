package com.ds.expanse.command.engine;

import com.ds.expanse.command.model.spi.cartograph.MapCoordinate;
import com.ds.expanse.command.model.spi.player.Player;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameEngine {
    private static final Map<String, Player> allPlayers = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Player> allNonPlayers = Collections.synchronizedMap(new HashMap<>());

    static {
        for (int index = 1; index < 20; index++) {
            final String id = "" + index;
            allNonPlayers.computeIfAbsent("dragon" + index, (key) -> {
                Player player = new Player();
                player.setId(id);
                player.setName(key);
                player.setPosition(MathUtil.random(500), MathUtil.random(500));

                return player;
            });
        }
    }

    /**
     * Gets all players near me.
     * @param me The Player to compare.
     * @return An List of integers representing the type and position (x,y)
     *      [id1,type-1,x-position-1,y-position-1,id2,type-2,x-position-2,y-position-2,...,idn,type-n,x-position-n,y-position-n]
     */
    public List<Integer> getPlayersNearMe(Player me) {
        return allNonPlayers.values().stream()
                .filter(other -> !me.equals(other) && MathUtil.inRange(me.getPosition(), other.getPosition(), -1) )
                .flatMap(p -> Arrays.asList(Integer.parseInt(p.getId()), 1, p.getPosition().getX(), p.getPosition().getY()).stream())
                .collect(Collectors.toList());
    }

    public void addPlayer(Player player) {
        allPlayers.putIfAbsent(player.getId(), player);
    }

    public boolean canMove(MapCoordinate newPosition, char tile) {
        return true;
    }
}
