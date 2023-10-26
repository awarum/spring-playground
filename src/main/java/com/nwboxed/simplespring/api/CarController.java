package com.nwboxed.simplespring.api;

import com.nwboxed.simplespring.model.Car;
import com.nwboxed.simplespring.model.ResourceNotFoundException;
import com.nwboxed.simplespring.services.CarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
@RestController
@RequestMapping(value="api/cars")
public class CarController extends AbstractController {

    @Autowired
    private CarService carService;

    @RequestMapping(value="",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Car car, HttpServletRequest request, HttpServletResponse response) {
        Car createdCar = carService.createCar(car);
        System.out.println(createdCar);
        response.setHeader(HttpHeaders.LOCATION, request.getRequestURL().append("/").append(createdCar.getId()).toString());
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public Car get(@PathVariable("id") String id) {
        return checkResourceFound(carService.getCar(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    List<Car> list() {
        return carService.getAllCars();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
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
