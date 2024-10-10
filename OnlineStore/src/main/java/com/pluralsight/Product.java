package com.pluralsight;

public record Product(int id, String name, double price) {

    @Override
    public String toString() {
        return "id: " + id + ", name: '" + name + "', price: " + price;
    }
}