package com.pluralsight;

public record Product(String id, String name, double price) {

    @Override
    public String toString() {
        return "id: " + id + ", name: '" + name + "', price: " + price;
    }
}