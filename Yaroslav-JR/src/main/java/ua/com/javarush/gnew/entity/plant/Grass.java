package ua.com.javarush.gnew.entity.plant;

import ua.com.javarush.gnew.entity.Organism;
import ua.com.javarush.gnew.entity.island.Cell;

import java.util.List;
import java.util.Random;

public class Grass extends Organism {
    public Grass() {
        super();
    }


    public void grow(Cell cell) {
        Random random = new Random();
        if (random.nextInt(100) < 50) {
            cell.addGrass(new Grass());
        }
    }
}
