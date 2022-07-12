package GameLogic;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.*;

public class View_GUI extends Application implements Observer<Model_GameLogic, String>{

    private BorderPane board;
    private Model_GameLogic game;
    private Label gameLabel;
    private Button[][] cards;
    private String prevGameState;
    private Label score;

    public void start(Stage stage) {
        this.game = new Model_GameLogic();
        this.board = new BorderPane();
        GridPane size = new GridPane();
        GridPane top = new GridPane();
        Button two = new Button();
        Button four = new Button();
        Button six = new Button();
        Button reset = new Button();
        score = new Label();

        score.setText("Score: " + 0);
        score.setAlignment(Pos.CENTER);
        score.setStyle("-fx-font: 24 Courier;");
        size.add(score, 0, 0);

        two.setText("2x2");
        two.setStyle("-fx-font: 24 Courier;");
        two.setOnAction(actionEvent -> game.newGame(2, 2));
        two.setMinHeight(100);
        two.setMinWidth(100);
        size.add(two, 0, 1);

        four.setText("4x4");
        four.setStyle("-fx-font: 24 Courier;");
        four.setOnAction(actionEvent -> game.newGame(4, 4));
        four.setMinHeight(100);
        four.setMinWidth(100);
        size.add(four, 0, 2);

        six.setText("6x6");
        six.setStyle("-fx-font: 24 Courier;");
        six.setOnAction(actionEvent -> game.newGame(6, 6));
        six.setMinHeight(100);
        six.setMinWidth(100);
        size.add(six, 0, 3);

        reset.setText("Reset");
        reset.setStyle("-fx-font: 24 Courier;");
        reset.setOnAction(actionEvent -> game.reset());
        reset.setMinHeight(100);
        reset.setMinWidth(100);
        size.add(reset, 0, 4);

        size.setAlignment(Pos.CENTER);
        board.setRight(size);
        board.setMinWidth(1250);
        board.setMinHeight(1100);

        this.gameLabel = new Label();
        gameLabel.setText("Select a Board Size");
        gameLabel.setStyle("-fx-font: 24 Courier;");
        top.add(gameLabel, 0, 0);
        top.setAlignment(Pos.TOP_CENTER);
        board.setTop(top);

        Scene scene = new Scene(board);
        stage.setScene(scene);
        stage.show();

        this.game.addObserver(this);
    }

    public void update(Model_GameLogic game, String gameState){

        if (gameState.equals("load")){
            createFlips(game);
            gameLabel.setText("Loaded a New Game");
            score.setText("Score: " + 0);
        }
        if (gameState.equals("reset")){
            createFlips(game);
            gameLabel.setText("Reset the Game");
            score.setText("Score: " + 0);
        }
        if (gameState.equals("select1")){
            if (!prevGameState.equals("match")){
                unflipCardsSelect(game);
            }
            flipCardsSelect1(game);
        }
        if (gameState.equals("select2")){
            flipCardsSelect2(game);
        }
        if (gameState.equals("match")){
            gameLabel.setText("Congrats, You Matched :)");
            score.setText("Score: " + game.score);
        }
        if (gameState.equals("noMatch")){
            gameLabel.setText("Big Oof, You Failed to Match :(");
        }
        prevGameState = gameState;
    }

    public void flipCardsSelect1(Model_GameLogic game){
        int select1i = game.select1[0];
        int select1j = game.select1[1];
        cards[select1i][select1j].setBackground(new Background(new BackgroundFill(game.board[select1i][select1j], new CornerRadii(5), Insets.EMPTY)));
    }

    public void flipCardsSelect2(Model_GameLogic game){
        int select2i = game.select2[0];
        int select2j = game.select2[1];
        cards[select2i][select2j].setBackground(new Background(new BackgroundFill(game.board[select2i][select2j], new CornerRadii(5), Insets.EMPTY)));
    }

    public void unflipCardsSelect(Model_GameLogic game){
        int select1i = game.prevSelect1[0];
        int select1j = game.prevSelect1[1];
        int select2i = game.select2[0];
        int select2j = game.select2[1];
        cards[select1i][select1j].setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(5), Insets.EMPTY)));
        cards[select2i][select2j].setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(5), Insets.EMPTY)));
    }



    public void createFlips(Model_GameLogic game){
        cards = new Button[game.numRows][game.numCols];
        GridPane flips = new GridPane();

        for (int i = 0; i < game.numRows; i++) {
            for (int j = 0; j < game.numCols; j++) {
                Button temp = new Button();
                temp.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(5), Insets.EMPTY)));
                temp.setMinWidth(150);
                temp.setMinHeight(150);
                int finalI = i;
                int finalJ = j;
                temp.setOnAction(event -> game.select(finalI, finalJ));
                cards[i][j] = temp;
                flips.add(temp, i, j);
            }
        }

        flips.setAlignment(Pos.CENTER);
        board.setCenter(flips);
        board.setMinSize(game.numRows*200, game.numCols*200);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }

}
