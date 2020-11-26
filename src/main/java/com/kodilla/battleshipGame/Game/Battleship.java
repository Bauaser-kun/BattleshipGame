package com.kodilla.battleshipGame.Game;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Battleship extends Parent {

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
    public boolean vertical = true;


    public Battleship(Ship ship) {
        this.shipSize = ship.getSize();
        this.hitpoints = ship.getSize();
        this.shipName = ship.name();
        this.vertical = vertical;

        VBox vBox = new VBox();
        for (int i = 0; i < shipSize; i ++) {
            Rectangle square = new Rectangle(20, 20);
            square.setFill(null);
            square.setStroke(Color.WHITE);
            vBox.getChildren().add(square);
        }
        getChildren().add(vBox);

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

    public boolean isVertical() {
        return vertical;
    }

    void wasShoot() {
        if (isNotSinked()) {
            hitpoints--;
        }
    }

    public boolean isNotSinked() {
        return hitpoints > 0;
    }

    public void rotate(){
        vertical = !vertical;
    }
}