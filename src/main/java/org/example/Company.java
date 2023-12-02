package org.example;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private List<Developer> developers;

    public Company(List<Developer> developers) {
        this.developers = new ArrayList<>();
    }

    public void addDeveloper(Developer developer) {
        developers.add(developer);
    }

}
