package com.example.reactive_tech.domain;

import org.springframework.data.annotation.Id;

public class Item {
    private @Id String id;
    private String name;
    private double price;

    private Item() {}

    Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
