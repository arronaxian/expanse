package com.ds.expanse.command.model.spi.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;


@Document(collection = "players")
public class Player {
    @Getter @Setter String id;
    @Getter @Setter String name;
    @Getter @Setter PlayerPosition position;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        try {
            return ((Player)other).id.equalsIgnoreCase(this.id);
        } catch ( Exception e ) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
