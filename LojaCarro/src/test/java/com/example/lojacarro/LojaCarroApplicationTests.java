package com.example.lojacarro;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LojaCarroApplicationTests {
    @Autowired
    CarRepository carRepository;
    @Test
    void contextLoads() {
    }

    @Test
    void testSaveAndFindCar() {
        // Arrange
        Car car = new Car();
        car.setMarca("BMW");
        car.setModel("320i");
        car.setAno(2021);

        // Act
        Car savedCar = carRepository.save(car);
        Car foundCar = carRepository.findById(savedCar.getId()).orElse(null);

        // Assert
        assertNotNull(foundCar);
        assertEquals("BMW", foundCar.getMarca());
        assertEquals("320i", foundCar.getModel());
        assertEquals(2021, foundCar.getAno());
    }
    @Test
    void testDeleteCar(){
        Car car = new Car();
        car.setMarca("BMW");
        car.setModel("320i");
        car.setAno(2021);
        boolean teste;
        Car carsaved =carRepository.save(car);
        carRepository.delete(car);
        Car foundCar = carRepository.findById(carsaved.getId()).orElse(null);

        assertNull(foundCar);
        if (foundCar == null){
            System.out.println("Car deleted");
        };
    }
    @Test
    void testUpdateCar(){
        Car car = new Car();
        car.setMarca("BMW");
        car.setModel("320i");
        car.setAno(2021);
        Car savedCar = carRepository.save(car);
        car.setMarca("Porshe");
        car.setModel("Cayman");
        car.setAno(2022);
        carRepository.save(car);
        Car foundCar = carRepository.findById(savedCar.getId()).orElse(null);
        String actual = foundCar.getModel();
        String expected = "Cayman";
        Assertions.assertEquals(expected, actual);

    }
}
