package com.kodilla.battleshipGame;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Random;

public class Game extends Application {
    Image imageback = new Image("file:src/main/resources/waterBackground.jpg");
    Image win = new Image("file:src//main/resources/WIN.jpg");
    Image lose = new Image("file:src//main/resources/LOSE3.jpg");
    ImageView winner = new ImageView(win);
    ImageView loser = new ImageView(lose);
    private int turnCount = 0;
    private Board enemyBoard;
    private Board playerBoard;
    private boolean gameRunning = false;
    private boolean enemyTurn;
    private Random random = new Random();
    private Battleship currentPlayerShip;
    private int oneMastShips = 4;
    private int twoMastShips = 3;
    private int threeMastShips = 2;
    private int fourMastShips = 1;
    private int totalships = oneMastShips + twoMastShips + threeMastShips + fourMastShips;
    private LinkedList<Battleship> playerShips  = new LinkedList<>();
    private LinkedList<Battleship> enemyShips  = new LinkedList<>();
    private Label turnCounter;
    private Label gameResult;
    private boolean winnerFound = false;
    private Label rules;
    private boolean polish = false;
    private LinkedList<Board.Cell> nextEnemyShots = new LinkedList<>();


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createGame(primaryStage));

        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private Parent createGame(Stage stage) {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100,
                true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(imageback,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        turnCounter = new Label("Turn: " + turnCount);
        if (polish) {
            turnCounter.setText("Tura: " + turnCount);
        }
        winner.setFitHeight(200);
        winner.setFitWidth(200);
        loser.setFitHeight(200);
        loser.setFitWidth(200);

        addShips(playerShips);
        currentPlayerShip = getNextShip(playerShips);
        addShips(enemyShips);

        BorderPane pane = new BorderPane();
        pane.setBackground(background);
        pane.setPrefSize(800, 640);

        Button Pl = new Button("Polski");
        Pl.setOnAction(event -> {
            polish = true;
            startNewGame(stage);
            System.out.println(rules);
        });
        if (polish) {
            Pl.setText("English");
            Pl.setOnAction((event -> {
                polish = false;
                startNewGame(stage);
            }));
        }

        Button rotateBtn = new Button("Rotate Ship");
        if (polish) {
            rotateBtn.setText("Obr\u00F3\u0107 statek");
        }
        rotateBtn.setOnAction(event -> {
            rotateShip(currentPlayerShip);
        });

        Button changeShipBtn = new Button("next Ship");
        if (polish) {
            changeShipBtn.setText("zmie\u0144 statek");
        }
        changeShipBtn.setOnAction(event -> {
            int returnedShip = currentPlayerShip.size;
            if (playerShips.size() > 0) {
                while (returnedShip == currentPlayerShip.size) {
                    playerShips.add(currentPlayerShip);
                    currentPlayerShip = getNextShip(playerShips);
                }
            }
        });

        /*Button fourShipBtn = new Button("Size 4");
        changeShipBtn.setOnAction(event -> {
            boolean shipAvailable = lookForShip(4, playerShips);
            if (shipAvailable) {
                while (currentPlayerShip.size != 4) {
                    playerShips.add(currentPlayerShip);
                    currentPlayerShip = getNextShip(playerShips);
                }
            }
        });

        Button threeShipBtn = new Button("Size 3");
        changeShipBtn.setOnAction(event -> {
            if (lookForShip(3, playerShips)) {
                while (currentPlayerShip.size != 3){
                    playerShips.add(currentPlayerShip);
                    currentPlayerShip = getNextShip(playerShips);
                }
            }
        });

        Button twoShipBtn = new Button("Size 2");
        changeShipBtn.setOnAction(event -> {
            if (lookForShip(2, playerShips)) {
                while (currentPlayerShip.size != 2){
                    playerShips.add(currentPlayerShip);
                    currentPlayerShip = getNextShip(playerShips);
                }
            }
        });

        Button oneShipBtn = new Button("Size 1");
        changeShipBtn.setOnAction(event -> {
            if (lookForShip(1, playerShips)) {
                while (currentPlayerShip.size != 1){
                    playerShips.add(currentPlayerShip);
                    currentPlayerShip = getNextShip(playerShips);
                }
            }
        });*/

        Button newGameBtn = new Button("Start new game");
        if (polish) {
            newGameBtn.setText("Nowa gra");
        }
        newGameBtn.setOnAction(event -> {
            startNewGame(stage);
        });

        Button exitBtn = new Button("Exit game");
        if (polish) {
            exitBtn.setText("Wyjd\u017A z gry");
        }
        exitBtn.setOnAction(event -> {
            exitGame();
        });

        Button randomizeShipBtn = new Button("Place ships randomly");
        if (polish) {
            randomizeShipBtn.setText("Rozmie\u015B\u0107 statki losowo");
        }
        randomizeShipBtn.setOnAction(event -> {
            setShipsRandomly(playerBoard, playerShips);
        });

        FlowPane bottomButtons = new FlowPane();
        bottomButtons.getChildren().addAll(newGameBtn, changeShipBtn, /*oneShipBtn,
                twoShipBtn, threeShipBtn, fourShipBtn,*/ randomizeShipBtn, exitBtn,
                rotateBtn, Pl);

        pane.setTop(turnCounter);

        pane.setBottom(bottomButtons);

        rules = new Label("1. Ships may not touch. Not even corners." +
                "\n 3. If ship is hit the player shoots again");

        if (polish) {
            rules.setText("1. Statki nie mog\u0105 si\u0119 styka\u0107 nawet naro\u017Cnikami.\n" +
                    "2. Za trafienie statku przys\u0142uguje dodatkowy ruch.");
        }
        rules.setWrapText(true);
        rules.setTextFill(Color.WHITE);
        rules.setTextAlignment(TextAlignment.LEFT);
        rules.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));
        gameResult = new Label("game running");
        gameResult.setPrefWidth(250);
        if (polish) {
            gameResult.setText("gra w toku");
        }
        gameResult.setAlignment(Pos.CENTER);
        VBox rightpane = new VBox(10, rules, gameResult);
        rightpane.setAlignment(Pos.CENTER);
        rightpane.setPrefWidth(200);
        rightpane.setMaxWidth(200);
        rightpane.setMaxHeight(640);
        pane.setRight(rightpane);

        Label rules2 = new Label(" Orange means hit but not sinked." +
                "\n While Red means sinked" +
                "\n And Black means missed"        );

        if (polish) {
            rules2.setText(" Pomaranczowy oznacza trafiony." +
                    "\n Czerwony zatopiony" +
                    "\n A czarny Pud\u0142o"  );
        }
        rules2.setWrapText(true);
        rules2.setTextFill(Color.WHITE);
        rules2.setTextAlignment(TextAlignment.LEFT);
        rules2.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));

        VBox leftpane = new VBox(10, rules2);
        leftpane.setAlignment(Pos.CENTER);
        leftpane.setPrefWidth(200);
        leftpane.setMaxWidth(200);
        leftpane.setMaxHeight(640);
        pane.setLeft(leftpane);

        enemyBoard = new Board(false, enemyBoardClickHandler());
        playerBoard = new Board(true, playerBoardClickHandler(),
                playerBoardEnteredHandler(), playerBoardExitedHandler());

        VBox gameBoards = new VBox(25, enemyBoard, playerBoard);

        gameBoards.setAlignment(Pos.CENTER);

        pane.setCenter(gameBoards);
        if (winnerFound) {
            gameBoards = null;
            pane.setCenter(gameResult);
        }

        return pane;
    }

    private boolean lookForShip(int i, LinkedList<Battleship> playerShips) {
        boolean shipAvailable = false;
        for (Battleship ship : playerShips) {
            if (ship.size == i) {
                shipAvailable = true;
            }
        }
        return shipAvailable;
    }

    public void resetSetting() {
        playerShips.clear();
        enemyShips.clear();
        oneMastShips = 4;
        twoMastShips = 3;
        threeMastShips = 2;
        fourMastShips = 1;
        turnCount = 0;
        totalships = oneMastShips + twoMastShips + threeMastShips + fourMastShips;
        gameRunning = false;
        winnerFound = false;
        nextEnemyShots.clear();
    }

    private EventHandler<? super MouseEvent> playerBoardExitedHandler() {
        return event -> {
          if (gameRunning) {
              return;
          }
          playerBoard.removeHiglightFromCellsToSetShipOn();
        };
    }

    private EventHandler<? super MouseEvent> playerBoardEnteredHandler() {
        return event -> {
            if (gameRunning) {
                return;
            }
            Board.Cell currentCell = (Board.Cell) event.getSource();
            playerBoard.highlitCellsTosetShipOn(currentPlayerShip, currentCell);
        };
    }

    private EventHandler<? super MouseEvent> playerBoardClickHandler() {
        return event -> {
          if (gameRunning)
              return;

            if (event.getButton() == MouseButton.SECONDARY) {
                currentPlayerShip.rotate();
                return;
            }
            Board.Cell cell = (Board.Cell) event.getSource();

            boolean shipSetProperly = playerBoard.placeShip(currentPlayerShip, cell.columns, cell.rows);

            if (shipSetProperly) {
                playerBoard.placeShip(currentPlayerShip, cell.columns, cell.rows);
                if (playerShips.size() == 0) {
                    startGame();
                }
                currentPlayerShip = getNextShip(playerShips);
            }
        };
    }

    private Battleship getNextShip(LinkedList<Battleship> ships) {
        return ships.pollFirst();
    }

    public EventHandler<MouseEvent> enemyBoardClickHandler() {
        return event -> {
            if (!gameRunning) {
                return;
            }

            Board.Cell cell = (Board.Cell) event.getSource();
            if(cell.wasAimed)
                return;

            enemyTurn = !cell.shoot();

            if(enemyBoard.shipcount == 0) {
                gameRunning = false;
                winnerFound = true;
                gameResult.setGraphic(winner);
            }

            if (enemyTurn && gameRunning)
                enemyMove();
        };
    }

    private void enemyMove() {
        while (enemyTurn) {
            int col = random.nextInt(10);
            int row = random.nextInt(10);
            Board.Cell cell;

            if (nextEnemyShots.isEmpty()){
            cell = playerBoard.getCell(col, row);
            } else {
            cell = nextEnemyShots.poll();
            }

            if (cell.wasAimed)
                continue;


            if (cell.ship != null) {
               if (!cell.ship.isNotSinked()) {
                   nextEnemyShots.clear();
               }

                if (isValidPoint(col + 1, row + 1)) {
                    playerBoard.getCell(cell.columns + 1, cell.rows + 1).wasAimed = true;
                    nextEnemyShots.add(playerBoard.getCell(cell.columns + 1, cell.rows));
                    nextEnemyShots.add(playerBoard.getCell(cell.columns, cell.rows + 1));
                }

                if (isValidPoint(col + 1, row - 1)) {
                    playerBoard.getCell(cell.columns + 1, cell.rows - 1).wasAimed = true;
                    nextEnemyShots.add(playerBoard.getCell(cell.columns + 1, cell.rows));
                    nextEnemyShots.add(playerBoard.getCell(cell.columns , cell.rows - 1));
                }

                if (isValidPoint(col - 1, row + 1)) {
                    playerBoard.getCell(cell.columns - 1, cell.rows + 1).wasAimed = true;
                    nextEnemyShots.add(playerBoard.getCell(cell.columns - 1, cell.rows));
                    nextEnemyShots.add(playerBoard.getCell(cell.columns, cell.rows + 1));
                }

                if (isValidPoint(col - 1, row - 1)) {
                    playerBoard.getCell(cell.columns - 1, cell.rows - 1).wasAimed = true;
                    nextEnemyShots.add(playerBoard.getCell(cell.columns - 1, cell.rows));
                    nextEnemyShots.add(playerBoard.getCell(cell.columns , cell.rows - 1));
                }

            }

            enemyTurn = cell.shoot();

            if (playerBoard.shipcount == 0) {
                winnerFound = true;
                gameRunning = false;
                gameResult.setGraphic(loser);
            }
        }
        turnCount++;
        turnCounter.setText("Turn: " + turnCount);
        if (polish) {
            turnCounter.setText("Tura: " + turnCount);
        }
    }

    private void exitGame() {
        System.exit(0);
    }

    private boolean isValidPoint(double columns, double rows) {
        return columns >= 0 && columns < 10 && rows >= 0 && rows < 10;
    }

    private void startGame() {
        gameRunning = true;
        setShipsRandomly(enemyBoard, enemyShips);

    }

    private void startNewGame(Stage stage){
        resetSetting();
        Scene scene = new Scene(createGame(stage));
        stage.setTitle("Battleship");
        if (polish) {
            stage.setTitle("Statki");
        }
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

    }

    private void setShipsRandomly(Board board, LinkedList<Battleship> ships) {
        totalships = ships.size();
        Battleship currentship;
        if (ships == playerShips) {
            currentship = currentPlayerShip;
        } else {
            currentship = getNextShip(ships);
        }

        while (totalships > 0) {
            int col = random.nextInt(10);
            int row = random.nextInt(10);
            if (random.nextBoolean()) {
                currentship.rotate();
            }
            boolean shipSetProperly = board.placeShip(currentship, col, row);

            if (shipSetProperly) {
                board.placeShip(currentship, col, row);
                if (ships.size() == 0) {
                    if (board == playerBoard) {
                        startGame();
                    }
                }
                currentship = getNextShip(ships);
            }
        }
    }

    private void rotateShip(Battleship ship) {
        if(!gameRunning) {
            ship.rotate();
        }
    }

    private void addShips (LinkedList<Battleship> ships){
        for (int i = 0; i < fourMastShips; i++) {
            ships.add(new Battleship(4, true));
        }

        for (int i = 0; i < threeMastShips; i++) {
            ships.add(new Battleship(3, true));
        }

        for (int i = 0; i < twoMastShips; i++) {
            ships.add(new Battleship(2, true));
        }

        for (int i = 0; i < oneMastShips; i++) {
            ships.add(new Battleship(1, true));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
