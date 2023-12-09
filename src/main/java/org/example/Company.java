package org.example;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private List<Developer> developers;
    private Developer deletedDeveloper;

    public Company() {
        developers = new ArrayList<>();
    }

    public Developer getDeletedDeveloper() {
        return deletedDeveloper;
    }

    public void deleteDeveloper(int index) throws IndexOutOfBoundsException {
        if (index >= 0 && index < developers.size()) {
            deletedDeveloper = developers.remove(index);
        }
    }

    public void addDeveloper(Developer developer) {
        developers.add(developer);
    }
}
