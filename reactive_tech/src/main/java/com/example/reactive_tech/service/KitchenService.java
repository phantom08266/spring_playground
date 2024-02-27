package com.example.reactive_tech.service;

import com.example.reactive_tech.domain.Dish;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class KitchenService {

    private Random picker = new Random();
    private List<Dish> menu = Arrays.asList(
            new Dish("Sesame chicken"),
            new Dish("Lo mein noodles, plain"),
            new Dish("Sweet & sour beef"));


    public Flux<Dish> getDishes() {
        return Flux.<Dish> generate(sink -> sink.next(randomDis()))
                .delayElements(Duration.ofMillis(250));
    }

    private Dish randomDis() {
        return menu.get(picker.nextInt(menu.size()));
    }
}
