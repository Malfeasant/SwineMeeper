package us.malfeasant.swinemeeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launcher extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	private final GridPane gameGrid = new GridPane();
	private final Label timeLabel = new Label("000");
	private final Label mineLabel = new Label("000");
	@Override
	public void start(Stage primaryStage) throws Exception {
		Button go = new Button("Go");
		go.setOnAction(e -> newGame());
		HBox topBox = new HBox(timeLabel, go, mineLabel);
		VBox vbox = new VBox(topBox, gameGrid);
		BorderPane pane = new BorderPane(vbox);
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SwineMeeper");
		primaryStage.show();
		go.fire();
	}
	
	private void newGame() {
		gameGrid.getChildren().clear();
		Difficulty diff = Persist.loadDifficulty();
		Cell[][] cells = new Cell[diff.getWidth()][diff.getHeight()];
		
		// Top left corner
		Cell c = new Cell();
		cells[0][0] = c;
		gameGrid.add(c.getButton(), 0, 0);
		
		// Top row
		for (int x = 1; x < diff.getWidth(); ++x) {
			c = new Cell();
			gameGrid.add(c.getButton(), x, 0);
			cells[x][0] = c;
			c.setNeighbor(Direction.WEST, cells[x-1][0]);
		}
		
		// Remaining rows
		for (int y = 1; y < diff.getHeight(); ++y) {
			// Left cell
			c = new Cell();
			cells[0][y] = c;
			gameGrid.add(c.getButton(), 0, y);
			c.setNeighbor(Direction.NORTH, cells[0][y-1]);
			
			// Remaining cells
			for (int x = 1; x < diff.getWidth(); ++x) {
				c = new Cell();
				c.setNeighbor(Direction.NORTHWEST, cells[x-1][y-1]);
				gameGrid.add(c.getButton(), x, y);
				cells[x][y] = c;
			}
		}
	}
}
