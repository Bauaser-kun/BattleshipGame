package com.kodilla.battleshipGame.Game;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Cell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends Parent {
    private final int rowsNumber = 10;
    private final int columnsNumber = 10;

    private int shipCount;
    private boolean playerBoard;
    private VBox rows = new VBox();


    public Board(EventHandler<? super MouseEvent> handler, boolean playerBoard) {
        this.playerBoard = playerBoard;
        for (int y = 0; y < rowsNumber; y++) {
            HBox row = new HBox();
            for (int x = 0; x < columnsNumber; x++) {
                Cell cell = new Cell(x, y, this);
                cell.setOnMouseClicked(handler);
                row.getChildren().add(cell);
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    public class Cell extends Rectangle {
        public int x;
        public int y;
        public Battleship ship;
        private boolean wasAimed;
        private Board board;
        private boolean isEmpty;

        public Cell(int x, int y, Board board) {
            super(20, 20);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.AQUAMARINE);
            setStroke(Color.GRAY);
        }

        public boolean isWasAimed() {
            return wasAimed;
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public boolean shoot() {
            wasAimed = true;
            setFill(Color.BLACK);

            if (ship != null ) {
                ship.wasShoot();
                setFill(Color.RED);

                if (!ship.isNotSinked()) {
                    board.shipCount--;
                }
                return true;
            }
            return false;
        }

    }
}
