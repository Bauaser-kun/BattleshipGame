package com.kodilla.battleshipGame.Game;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.List;

public class Board extends Parent {
    private final int rowsNumber = 10;
    private final int columnsNumber = 10;

    public int shipCount;
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

    public boolean setShip (Battleship ship, Cell firstCell) {
        int shipSize = ship.getShipSize();
        int x = (int)firstCell.getColumns();
        int y = (int)firstCell.getRows();

        if (!canSetShip(ship, firstCell)) {
            return false;
        }

    if (ship.vertical) {
        for (int i = y; i < y + shipSize; i++) {
            Cell occupied = getCell(x, i);
            occupied.ship = ship;
        }
    } else {
        for (int i = x; i < x + shipSize; x++) {
            Cell occupied = getCell(i, y);
            occupied.ship = ship;
        }
    }
return true;
    }

    public boolean canSetShip(Battleship ship, Cell firstCell) {
        int shipSize = ship.getShipSize();

        for (int i = 0; i < shipSize; i++) {
            if (!isValidCoordinate(firstCell.getColumns() + i, firstCell.getRows() + i)) {
                return false;
            }
        }

            for  (int i = 0; i < shipSize; i++) {
                if (ship.isVertical()) {
                    Cell currentCell = getCell(firstCell.getColumns(), firstCell.getRows() + i);
                    if (currentCell.ship != null) {
                        return false;
                    }
                } else {
                    Cell currentCell = getCell((firstCell.getColumns() + i), firstCell.getRows());
                    if (currentCell.ship != null) {
                        return false;
                    }
                }
            }

            for (Cell neighbor : getNeighbors(firstCell.getColumns(), firstCell.getRows())) {
                if (neighbor.ship != null) {
                    return false;
                }
            }
            return true;
        }

        private Cell[] getNeighbors (int column, int row) {
            Point2D[] points = new Point2D[] {
                    new Point2D(column - 1, row),
                    new Point2D(column + 1, row),
                    new Point2D(column, row - 1),
                    new Point2D(column, row + 1)
            };

            List<Cell> neighbors = new ArrayList<Cell>();

            for (Point2D p : points) {
                if (isValidPoint(p)) {
                    neighbors.add(getCell((int)p.getX(), (int)p.getY()));
                }
            }
            return neighbors.toArray(new Cell[0]);
        }

    private boolean isValidPoint(Point2D point) {
        return isValidCoordinate(point.getX(), point.getY());
    }

    private boolean isValidCoordinate(double x, double y){
        return x >= 0 && x < columnsNumber && y >= 0 && y < rowsNumber;
    }

    public class Cell extends Rectangle {
        public int columns;
        public int rows;
        public Battleship ship;
        private boolean wasAimed;
        private Board board;
        private boolean isEmpty;


        public int getColumns() {
            return columns;
        }


        public int getRows() {
            return rows;
        }

        public Battleship getShip() {
            return ship;
        }

        public Cell(int columns, int rows, Board board) {
            super(20, 20);
            this.columns = columns;
            this.rows = rows;
            this.board = board;
            setFill(Color.AQUAMARINE);
            setStroke(Color.GRAY);
        }

        public boolean isWasAimed() {
            return wasAimed;
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
