package com.gamestore.restapis.repositories;

import com.gamestore.restapis.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
