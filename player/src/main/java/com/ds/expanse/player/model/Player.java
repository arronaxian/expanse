package com.ds.expanse.player.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document(collection = "players")
public class Player {
    @Id @Getter @Setter String id;
    @Getter @Setter String name;
    @Getter @Setter PlayerPosition position;
}
