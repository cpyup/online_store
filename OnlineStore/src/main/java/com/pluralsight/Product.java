package com.pluralsight;

public record Product(String id, String name, double price) {

    @Override
    public String toString() {
        return String.format("ID: %s, Name: '%s', Price: $%.2f",id,name,price);
    }
}