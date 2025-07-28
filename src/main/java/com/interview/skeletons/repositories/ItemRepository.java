package com.interview.skeletons.repositories;

import com.interview.skeletons.models.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
}
