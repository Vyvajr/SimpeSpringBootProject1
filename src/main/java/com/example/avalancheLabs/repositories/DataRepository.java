package com.example.avalancheLabs.repositories;

import com.example.avalancheLabs.models.Data;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DataRepository extends CrudRepository<Data, UUID> {}
