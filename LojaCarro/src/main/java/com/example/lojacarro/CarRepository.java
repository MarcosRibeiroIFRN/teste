package com.example.lojacarro;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    // Já vem com findById(Integer id) por padrão do JpaRepository
    // Não precisa redeclarar a menos que queira comportamento diferente

    List<Car> findByMarca(String marca);  // Mais útil que findBySomeProperty
    List<Car> findByAno(int ano);
}
