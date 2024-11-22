package ua.com.javarush.gnew.entity.island;

import ua.com.javarush.gnew.entity.Animal;
import ua.com.javarush.gnew.entity.plant.Grass;

import java.util.ArrayList;
import java.util.List;

import static java.awt.AWTEventMulticaster.remove;

public class Cell {

    private final List<Animal> animals = new ArrayList<>();
    private final List<Grass> grass = new ArrayList<>();


    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void addGrass(Grass grassObject) {
        grass.add(grassObject);
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Grass> getGrass() {
        return grass;
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }
}
