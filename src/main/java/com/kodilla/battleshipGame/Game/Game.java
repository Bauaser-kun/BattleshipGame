package com.kodilla.battleshipGame.Game;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class Game extends Application {
    public VBox enemyBoardArea;
    public VBox playerBoardArea;
    private Board enemyBoard;
    private Board playerBoard;
    private boolean enemyTurn;
    private boolean gameRunning = false;
    private Battleship currentPlayerShip;
    private int totalShips;
    private final Image imageback = new Image("file:src/main/resources/waterBackground.jpg");

    private int turnCount = 0;
    private final Label turnCounter = new Label("Turn: " + turnCount);
    private Label gameResult;

    private Parent createGame(){
        BackgroundSize backgroundSize = new BackgroundSize(100, 100,
                true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        BattleshipFactory battleshipFactory = new BattleshipFactory();
        totalShips = battleshipFactory.getTotalShipsQuantity();
        currentPlayerShip = battleshipFactory.getNextShip();

        BorderPane pane = new BorderPane();
        pane.setBackground(background);
        pane.setPrefSize(800, 640);

        Button rotateBtn = new Button("Rotate Ship");
        rotateBtn.setOnAction(event -> {
            rotateShip();
        });

        Button newGameBtn = new Button("Start new game");
        newGameBtn.setOnAction(event -> {
            startGame();
        });

        Button exitBtn = new Button("Exit game");
        exitBtn.setOnAction(event -> {
            exitGame();
        });

        FlowPane bottomButtons = new FlowPane();
        bottomButtons.getChildren().addAll(newGameBtn, exitBtn, rotateBtn);

        pane.setTop(turnCounter);
        pane.setBottom(bottomButtons);

        pane.setRight(new Text("checking if this works"));
        enemyBoard = new Board(false, enemyBoardClikHandler());

        playerBoard = new Board(true, event -> {

            if (gameRunning) {
                return;
            }

            Board.Cell currentCell = (Board.Cell) event.getSource();

                boolean shipSetProperly = playerBoard.setShip(currentPlayerShip, currentCell);
                if (shipSetProperly) {
                    playerBoard.setShip(currentPlayerShip, currentCell);
                    currentPlayerShip = battleshipFactory.getNextShip();
                }


            if (currentPlayerShip == null) {
                startGame();
            }
            });

        setShipsRandomly(enemyBoard);

        VBox boards = new VBox(25, enemyBoard, playerBoard);
        boards.setAlignment(Pos.CENTER);

        pane.setCenter(boards);

        return pane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
             Scene scene = new Scene(createGame());

        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

   public EventHandler<MouseEvent> playerBoardMouseExitedHandler() {
        return event -> {
            if (gameRunning) {
                return;
            }
            playerBoard.removeHighlightFromCellsToSetSHipOn();
        };
    }

    public EventHandler< MouseEvent> playerBoardMouseEnteredHandler() {
        return event -> {
            if (gameRunning) {
                return;
            }
            Board.Cell currentCell = (Board.Cell) event.getSource();
            playerBoard.highlightCellsToSetShipOn(currentPlayerShip, currentCell);
        };
    }

    public EventHandler<MouseEvent> playerBoardClickHandler(BattleshipFactory battleshipFactory) {
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

    private EventHandler<MouseEvent> enemyBoardClikHandler() {
        return event -> {
            if (!gameRunning) {
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
        turnCount++;
    }


    private void rotateShip() {
        if (!gameRunning) {
            currentPlayerShip.rotate();
        }
    }

    private void setPlayerShipsRandomly() {
        if (!gameRunning) {
            setShipsRandomly(playerBoard);
            startGame();
        }
    }

    private void startGame() {
        setShipsRandomly(enemyBoard);
        gameRunning = true;
        turnCount = 0;
    }

    private void exitGame() {
        System.exit(0);
    }

    private void setShipsRandomly(Board board) {
        BattleshipFactory battleshipFactory = new BattleshipFactory();

        for (Battleship ship = battleshipFactory.getNextShip(); ship != null;
             ship = battleshipFactory.getNextShip()) {
            board.setShipsRandomly(ship, board);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}