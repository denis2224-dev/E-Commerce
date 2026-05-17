package com.gamestore.restapis.repositories;

import com.gamestore.restapis.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {

}
