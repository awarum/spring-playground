package com.nwboxed.simplespring.services;

import com.nwboxed.simplespring.model.Car;
import com.nwboxed.simplespring.model.ResourceNotFoundException;
import com.nwboxed.simplespring.repositories.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public Optional<Car> getCar(String id) {
        return carRepository.findById(id);
   }

    public void deleteCar(String id) {
        carRepository.deleteById(id);
    }

    public Car updateCar(Car car) {
        return carRepository.save(car);
    }

    public List<Car> getAllCars() {
        List<Car> result = new ArrayList<>();
        carRepository.findAll().forEach(result::add);
        return result;
    }

    public Car partialUpdate(Car updatedCar) {

        Car existingCar = carRepository
                .findById(updatedCar.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find car"));
        updateProvidedProperties(updatedCar, existingCar);
        return carRepository.save(existingCar);
    }

    private static void updateProvidedProperties(Car updatedCar, Car existingCar) {

        Optional.ofNullable(updatedCar.getType()).ifPresent(existingCar::setType);
        Optional.ofNullable(updatedCar.getColour()).ifPresent(existingCar::setColour);
    }


}
