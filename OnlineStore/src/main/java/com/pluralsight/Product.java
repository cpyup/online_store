package com.pluralsight;

public record Product(String id, String name, double price) {

    @Override
    public String toString() {
        return "ID: " + id + ", Name: '" + name + "', Price: $" + price;
    }
}