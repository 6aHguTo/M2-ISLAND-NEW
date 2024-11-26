package ua.com.javarush.gnew.entity.meatEaters;

import ua.com.javarush.gnew.entity.Organism;
import ua.com.javarush.gnew.entity.chewingGrass.*;
import ua.com.javarush.gnew.entity.island.Cell;
import ua.com.javarush.gnew.entity.island.Island;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class Eagle extends MeatEaters {

    private static final double MAX_WEIGHT = 7.0;
    private static final double INITIAL_WEIGHT = 6.0;
    private static final int MOVE_DISTANCE = 3;


    public Eagle() {
        super(20, INITIAL_WEIGHT);
    }
    public void move(Cell currentCell, Island island, int currentX, int currentY) {
        int deltaX = ThreadLocalRandom.current().nextInt(-MOVE_DISTANCE, MOVE_DISTANCE + 1);
        int deltaY = ThreadLocalRandom.current().nextInt(-MOVE_DISTANCE, MOVE_DISTANCE + 1);

        int newX = currentX + deltaX;
        int newY = currentY + deltaY;

        newX = Math.max(0, Math.min(newX, island.getWidth() - 1));
        newY = Math.max(0, Math.min(newY, island.getHeight() - 1));

        if (island.getField()[newX][newY].add(this)) {
            Iterator<Organism> iterator = currentCell.getResidents().get(this.getClass()).iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(this)) {
                    iterator.remove();
                }
            }
        }
    }


    @Override
    public void checkSatiation() {
        if (getWeight() >= MAX_WEIGHT) {
            isSatiated = true;
        } else {
            isSatiated = false;
        }
    }
    public void eat(Cell cell) {

        Iterator<Organism> iterator = cell.getResidents().get(ChewingGrass.class).iterator();

        while (iterator.hasNext()) {
            Organism prey = iterator.next();

            int chanceToEat = 0;

            if (prey instanceof Rabbit) {
                chanceToEat = 90;
            }else if (prey instanceof Mouse) {
                chanceToEat = 90;
            }else if (prey instanceof Duck) {
                chanceToEat = 80;
            }

            if (ThreadLocalRandom.current().nextInt(100) < chanceToEat) {

                prey.die();
                iterator.remove();
                this.setWeight(this.getWeight() + prey.getWeight() / 2);
                break;
            }

        }
    }
}