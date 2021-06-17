package com.example.avalancheLabs.repositories;

import com.example.avalancheLabs.models.DataCached;
import org.springframework.data.repository.CrudRepository;

public interface DataCacheRepository extends CrudRepository<DataCached, Integer> {}
