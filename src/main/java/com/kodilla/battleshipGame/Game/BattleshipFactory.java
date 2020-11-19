package com.kodilla.battleshipGame.Game;

import java.util.Arrays;
import java.util.LinkedList;

public class BattleshipFactory {
    private static final int ONE_MAST_SHIP_QUANTITY = 4;
    private static final int TWO_MAST_SHIP_QUANTITY = 3;
    private static final int THREE_MAST_SHIP_QUANTITY = 2;
    private static final int FOUR_MAST_SHIP_QUANTITY = 1;

    private LinkedList<Battleship> ships = new LinkedList<>();

    {
        addShips(Battleship.Masts.ONE);
        addShips(Battleship.Masts.TWO);
        addShips(Battleship.Masts.THREE);
        addShips(Battleship.Masts.FOUR);
    }

    private void addShips (Battleship.Masts masts) {
        int shipQuantity = getShips(masts);

        for (int i = 0; i < shipQuantity; i++) {
            ships.add(getBattleship(masts));
        }
    }

    private Battleship getBattleship(Battleship.Masts masts) {
        switch (masts) {
            case ONE:
                return new Battleship(Battleship.Ship.ONE_MAST_SHIP);
            case TWO:
                return new Battleship(Battleship.Ship.TWO_MAST_SHIP);
            case THREE:
                return new Battleship(Battleship.Ship.THREE_MAST_SHIP);
            case FOUR:
                return new Battleship(Battleship.Ship.FOUR_MAST_SHIP);
            default:
                return null;
        }
    }

    private static int getShips(Battleship.Masts masts) {
        switch (masts) {
            case ONE:
                return ONE_MAST_SHIP_QUANTITY;
            case TWO:
                return TWO_MAST_SHIP_QUANTITY;
            case THREE:
                return THREE_MAST_SHIP_QUANTITY;
            case FOUR:
                return FOUR_MAST_SHIP_QUANTITY;
            default:
                return 0;
        }
    }

    static int getTotalShipsQuantity() {
        return Arrays.stream(new int[]{
                getShips(Battleship.Masts.ONE),
        getShips(Battleship.Masts.TWO),
        getShips(Battleship.Masts.THREE),
        getShips(Battleship.Masts.FOUR)}, 0, 4).sum();
    }

    public Battleship getNextShip() {
        return ships.pollFirst();
    }
}
