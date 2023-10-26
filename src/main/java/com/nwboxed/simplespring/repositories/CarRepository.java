package com.nwboxed.simplespring.repositories;

import com.nwboxed.simplespring.model.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends CrudRepository<Car, String> {
    List<Car> findByType(String type);
    Optional<Car> findById(String id);
}
