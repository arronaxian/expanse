package com.ds.expanse.user.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
public class User implements Serializable {
    @DBRef @Setter @Getter private String id;
    @Setter @Getter private String username;
    @Setter @Getter private String password;
    @Setter @Getter private String email;
    @Setter @Getter private Set<String> playerIds = new HashSet<>();
}
