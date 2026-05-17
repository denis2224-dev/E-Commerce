package com.gamestore.restapis.repositories;

import com.gamestore.restapis.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
