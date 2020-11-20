package com.kodilla.battleshipGame.Game;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
public VBox enemyBoardArea;
public VBox playerBoardArea;
private Board enemyBoard;
private Board playerBoard;
private boolean enemyTurn;
private boolean gameRunning;
private Battleship currentPlayerShip;

@FXML
private Label gameResult;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNewGame();
    }

    private void initializeNewGame() {
        BattleshipFactory battleshipFactory = new BattleshipFactory();
        gameResult.setText("");
        gameRunning = false;
        enemyTurn = false;
        currentPlayerShip = battleshipFactory.getNextShip();
        playerBoard = new Board(true, playerBoardClickHandler(battleshipFactory), playerBoardMouseEnteredHandler(),
                playerBoardMouseExitedHandler());
        enemyBoard = new Board(false, enemyBoardClikHandler());
        enemyBoardArea.getChildren().add(enemyBoard);
        playerBoardArea.getChildren().add(playerBoard);
    }

    private EventHandler<MouseEvent> enemyBoardClikHandler() {
        return event -> {
           if (gameRunning) {
               return;
           }
           Board.Cell currentCell = (Board.Cell) event.getSource();

           if (currentCell.wasAimed()) {
               return;
           }

           boolean wasHit = enemyBoard.getShoot(currentCell);
           enemyTurn = !wasHit;

           if (enemyBoard.getShipCount() == 0) {
               gameRunning = false;
               gameResult.setText("All enemy ships are sinked! Victory is Your!");
           }

           if (enemyTurn && gameRunning) {
               enemyMove();
           }
        };
    }

    private void enemyMove() {
        Random random = new Random();
        Board.Cell cell;

        do {
            int row = random.nextInt(10);
            int column = random.nextInt(10);
            cell = playerBoard.getCell(row, column);
            enemyTurn = playerBoard.getShoot(cell);

            if (playerBoard.getShipCount() == 0) {
                gameRunning = false;
                gameResult.setText("All your ships were sinked. You lost!");
            }
        } while (!cell.wasAimed() || enemyTurn);
    }

    private EventHandler<MouseEvent> playerBoardMouseExitedHandler() {
        return event -> {
            if (gameRunning) {
                return;
            }
            playerBoard.removeHighlightFromCellsToSetSHipOn();
        };
    }

    private EventHandler< MouseEvent> playerBoardMouseEnteredHandler() {
        return event -> {
          if (gameRunning) {
              return;
          }
          Board.Cell currentCell = (Board.Cell) event.getSource();
          playerBoard.highlightCellsToSetShipOn(currentPlayerShip, currentCell);
        };
    }

    private EventHandler<MouseEvent> playerBoardClickHandler(BattleshipFactory battleshipFactory) {
        return event -> {
            if (gameRunning) {
                return;
            }

            Board.Cell currentCell = (Board.Cell) event.getSource();

            if (event.getButton() == MouseButton.SECONDARY) {
                currentPlayerShip.rotate();
                return;
            }

            if (event.getButton() == MouseButton.PRIMARY) {
                boolean shipSetProperly = playerBoard.setShip(currentPlayerShip, currentCell);
                if (shipSetProperly) {
                    currentPlayerShip = battleshipFactory.getNextShip();
                }
            }
            if (currentPlayerShip == null) {
                startGame();
            }
        };
    }

    @FXML
    private void startNewGame() {
        enemyBoardArea.getChildren().remove(enemyBoard);
        playerBoardArea.getChildren().remove(playerBoard);
        initializeNewGame();
    }

    @FXML
    private void rotateShip() {
        if (!gameRunning) {
            currentPlayerShip.rotate();
        }
    }

    @FXML
    private void setPlayerShipsRandomly() {
        if (!gameRunning) {
            setShipsRandomly(playerBoard);
            startGame();
        }
    }

    private void startGame() {
        setShipsRandomly(enemyBoard);
    gameRunning = true;
    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }

    private void setShipsRandomly(Board board) {
        BattleshipFactory battleshipFactory = new BattleshipFactory();

        for (Battleship ship = battleshipFactory.getNextShip(); ship != null;
             ship = battleshipFactory.getNextShip()) {
            board.setShipsRandomly(ship);
        }
    }
}
