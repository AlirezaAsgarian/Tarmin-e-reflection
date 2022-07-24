package org.example;

import java.util.Objects;

public class Car extends Vehicle{
        public double capacity;
        public Car() {
            super();
        }
        public Car(String name, int price, double capacity) {
            this.price = price;
            this.capacity = capacity;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Car car = (Car) o;
            return Double.compare(car.capacity, capacity) == 0;
        }
        @Override public int hashCode() {
            return Objects.hash(capacity);
        }
    }
