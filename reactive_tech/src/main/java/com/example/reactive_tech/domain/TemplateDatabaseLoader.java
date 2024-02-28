package com.example.reactive_tech.domain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class TemplateDatabaseLoader {

    @Bean
    CommandLineRunner initialize(ReactiveMongoOperations mongo) {
        return args -> {
            Flux<Item> items = Flux.just(
                            new Item("item 1", 20.00),
                            new Item("item 2", 30.00))
                    .flatMap(mongo::save);

            items.then().subscribe();
        };
    }
}
