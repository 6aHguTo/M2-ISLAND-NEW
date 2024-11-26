package ua.com.javarush.gnew.config;

import ua.com.javarush.gnew.entity.Animal;
import ua.com.javarush.gnew.entity.Organism;
import ua.com.javarush.gnew.entity.chewingGrass.*;
import ua.com.javarush.gnew.entity.island.Cell;
import ua.com.javarush.gnew.entity.island.Island;
import ua.com.javarush.gnew.entity.meatEaters.*;
import ua.com.javarush.gnew.entity.plant.Grass;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Loader {

    private static Loader INSTANCE;
    Context context = Context.getINSTANCE();

    private Loader() {
    }

    public static synchronized Loader getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Loader();
        }
        return INSTANCE;
    }

    public Context init() {
        initIsland(5, 5);
        populateIslandWithAnimals();
        return context;
    }

    public void initIsland(int width, int height) {
        Island island = new Island(width, height);
        Cell[][] cells = island.getField();


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ConcurrentHashMap<Class<? extends Organism>, Set<Organism>> residents = new ConcurrentHashMap<>();
                cells[i][j] = new Cell(residents);
            }
        }
        this.context.setIsland(island);
    }


    private void populateIslandWithAnimals() {
        Island island = context.getIsland();
        Cell[][] cells = island.getField();


        for (int i = 0; i < island.getWidth(); i++) {
            for (int j = 0; j < island.getHeight(); j++) {
                Cell cell = cells[i][j];
                addAnimalsToCell(cell);
            }
        }
    }


    private void addAnimalsToCell(Cell cell) {
        addAnimalsOfType(cell, Grass.class);

        addAnimalsOfType(cell, Wolf.class);
        addAnimalsOfType(cell, Bear.class);
        addAnimalsOfType(cell, Eagle.class);
        addAnimalsOfType(cell, Fox.class);
        addAnimalsOfType(cell, Python.class);

        addAnimalsOfType(cell, Sheep.class);
        addAnimalsOfType(cell, Duck.class);
        addAnimalsOfType(cell, Boar.class);
        addAnimalsOfType(cell, Buffalo.class);
        addAnimalsOfType(cell, Caterpillar.class);
        addAnimalsOfType(cell, Deer.class);
        addAnimalsOfType(cell, Goat.class);
        addAnimalsOfType(cell, Horse.class);
        addAnimalsOfType(cell, Mouse.class);
        addAnimalsOfType(cell, Rabbit.class);

    }


    private <T extends Organism> void addAnimalsOfType(Cell cell, Class<T> animalType) {
        try {

            Organism sampleAnimal = animalType.getDeclaredConstructor().newInstance();
            int maxResidents = sampleAnimal.getMaxCellResidents();


            int numberOfAnimals = ThreadLocalRandom.current().nextInt(0, maxResidents + 1);

            for (int i = 0; i < numberOfAnimals; i++) {
                Organism animal = animalType.getDeclaredConstructor().newInstance();
                cell.add(animal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processAnimals(Island island, int startX, int endX) {
        for (int x = startX; x < endX; x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                Cell cell = island.getField()[x][y];
                synchronized (cell) {

                    ConcurrentHashMap<Class<? extends Organism>, Set<Organism>> snapshot = new ConcurrentHashMap<>(cell.getResidents());

                    int finalX = x;
                    int finalY = y;
                    snapshot.forEach((type, organisms) -> {
                        Iterator<Organism> iterator = organisms.iterator();
                        while (iterator.hasNext()) {
                            Organism organism = iterator.next();
                            if (organism instanceof Animal) {
                                Animal animal = (Animal) organism;
                                animal.eat(cell);
                                animal.reproduce(cell);
                                animal.move(cell, island, finalX, finalY);
                            }
                        }
                    });
                }
            }
        }
    }

}
