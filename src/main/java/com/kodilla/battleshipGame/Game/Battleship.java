package com.kodilla.battleshipGame.Game;

public class Battleship {
    enum Ship {
        ONE_MAST_SHIP(ONE_MAST_SHIP_SIZE), TWO_MAST_SHIP(TWO_MAST_SHIP_SIZE),
        THREE_MAST_SHIP(THREE_MAST_SHIP_SIZE), FOUR_MAST_SHIP(FOUR_MAST_SHIP_SIZE);

        private int size;

        Ship(int size) {
            this.size = size;
        }


        public int getSize() {
            return size;
        }
    }

    enum Masts {
        ONE,
        TWO,
        THREE,
        FOUR
    }

    private static final int ONE_MAST_SHIP_SIZE = 1;
    private static final int TWO_MAST_SHIP_SIZE = 2;
    private static final int THREE_MAST_SHIP_SIZE = 3;
    private static final int FOUR_MAST_SHIP_SIZE = 4;

    private int shipSize;
    private int hitpoints;
    private String shipName;


    public Battleship(Ship ship) {
        this.shipSize = ship.getSize();
        this.hitpoints = ship.getSize();
        this.shipName = ship.name();
    }

    public int getShipSize() {
        return shipSize;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public String getShipName() {
        return shipName;
    }

    void wasShoot() {
        if (isNotSinked()) {
            hitpoints--;
        }
    }

    public boolean isNotSinked() {
        return hitpoints > 0;
    }
}