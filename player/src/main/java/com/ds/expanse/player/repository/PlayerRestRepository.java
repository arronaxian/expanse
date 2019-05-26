package com.ds.expanse.player.repository;

import com.ds.expanse.player.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface PlayerRestRepository extends MongoRepository<Player, String> {
    Optional<Player> findByName(@Param("name") String name);
}
