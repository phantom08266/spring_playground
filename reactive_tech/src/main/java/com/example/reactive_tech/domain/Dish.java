package com.example.reactive_tech.domain;

public class Dish {
    private String description;
    private boolean delivered = false;

    public Dish(String description) {
        this.description = description;
    }

    public static Dish deliver(Dish dish) {
        // 함수형에서 선호하는 방식. 객체를 변경하지 않고 새로운 객체를 생성해서 상태를 변경한다.
        Dish deliveredDish = new Dish(dish.description);
        deliveredDish.delivered = true;
        return deliveredDish;
    }

    public String getDiscription() {
        return description;
    }

    public void setDiscription(String description) {
        this.description = description;
    }

    public boolean isDelivered() {
        return delivered;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "description='" + description + '\'' +
                ", delivered=" + delivered +
                '}';
    }
}
