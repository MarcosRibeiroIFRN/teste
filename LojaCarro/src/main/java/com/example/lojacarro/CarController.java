package com.example.lojacarro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class CarController {

    @Autowired
    CarRepository carRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/cars")
    public List<Car> getCars() {
        return carRepository.findAll();
    }
    @PostMapping ("/save")
    public Car saveCar(@RequestBody Car car) {
        return carRepository.save(car);
    }
    @GetMapping("/Cars")
    public ResponseEntity<Car> findCarById(@RequestParam int id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            return ResponseEntity.ok(car.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/DeleteCar")
    public void delete(@RequestParam int id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            carRepository.delete(car.get());
        }
        else {
            System.out.println("Car Not Found");
        }

    }
    @PutMapping
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        Optional<Car> carOptional = carRepository.findById(car.getId());
        if (carOptional.isPresent()) {
            Car updatedCar = carRepository.save(car);
        }else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(car);
    }

    @PostMapping("/enviarmensagem")
    public String enviarMensagem (@RequestBody String mensagem) {
        System.out.println("MENSAGEM RMQ = "+mensagem);
        RabbitSend.escreverMensagem(mensagem);
        return "Mensagem enviada com sucesso!";
    }

    @GetMapping("/lermensagem")
    public void lerMensagem() throws IOException {
        String resposta = RabbitSend.lerMensagem();

        if (resposta == null || resposta.trim().isEmpty()) {
            System.out.println("Mensagem vazia ou nula recebida da fila RabbitMQ.");
            //return;
        }
    }
    @GetMapping("/lermensagem2")
    public void lerMensagem2() throws IOException{
        ObjectMapper om = new ObjectMapper();
        Car c = om.readValue(RabbitSend.resposta, Car.class);
        carRepository.save(c);
        System.out.println("Resposta = " + RabbitSend.resposta);
    }


}