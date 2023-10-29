package com.nwboxed.simplespring.api;

import com.nwboxed.simplespring.model.Car;
import com.nwboxed.simplespring.model.ResourceNotFoundException;
import com.nwboxed.simplespring.services.CarService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(value="api/cars")
public class CarController extends AbstractController {

    @Autowired
    private CarService carService;

    @PostMapping(
            consumes =  {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary =" Create a new car and returns the location of the created car in the response header.")
    public void create(
            @RequestBody Car car,
            HttpServletRequest request,
            HttpServletResponse response) {
        Car createdCar = carService.createCar(car);
        response.setHeader(HttpHeaders.LOCATION, request.getRequestURL().append("/").append(createdCar.getId()).toString());
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary =" Create a new car and returns the location of the created car in the response header.")
    public void update(
            @RequestBody Car car,
            HttpServletRequest request,
            HttpServletResponse response) {
        checkResourceFound(carService.getCar(car.getId()));
        Car createdCar = carService.updateCar(car);
        response.setHeader(HttpHeaders.LOCATION, request.getRequestURL().append("/").append(createdCar.getId()).toString());
    }

    @PatchMapping("")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.partialUpdate(car));
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public Car get(@PathVariable("id") String id) {
        return checkResourceFound(carService.getCar(id));
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    List<Car> list() {
        return carService.getAllCars();
    }

    @DeleteMapping (value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        checkResourceFound(carService.getCar(id));
        carService.deleteCar(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public
    @ResponseBody
    String handleResourceNotFoundException(ResourceNotFoundException exception) {
        return exception.getMessage();
    }

}
