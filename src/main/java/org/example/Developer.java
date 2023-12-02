package org.example;

import java.math.BigDecimal;

class Developer {
    private String name;
    private String specialty;
    private BigDecimal salary;

    public Developer(String name, String specialty, BigDecimal salary) {
        this.name = name;
        this.specialty = specialty;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Developer: " + "\n" + "name: " + name + "\n" + "specialty: " + specialty + "\n" + "salary: " + salary + "\n" + '}';
    }
}