package us.malfeasant.swinemeeper;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launcher extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	private final GridPane gameGrid = new GridPane();
	private final Label timeLabel = new Label("000");
	private final Label mineLabel = new Label("000");
	private Stage stage;	// need this to resize window after adding/removing children- is there a better way?
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		Button go = new Button("Go");
		go.setOnAction(e -> newGame());
		
		GridPane topGrid = new GridPane();	// counters & go button
		// All this is needed to keep counters at the outside edges of the bar and go button centered:
		ColumnConstraints ccL = new ColumnConstraints();
		ccL.setPercentWidth(100);
		ccL.setHalignment(HPos.LEFT);
		ColumnConstraints ccC = new ColumnConstraints();
		ccC.setPercentWidth(100);
		ccC.setHalignment(HPos.CENTER);
		ColumnConstraints ccR = new ColumnConstraints();
		ccR.setPercentWidth(100);
		ccR.setHalignment(HPos.RIGHT);
		topGrid.getColumnConstraints().addAll(ccL, ccC, ccR);
		topGrid.add(timeLabel, 0, 0);
		topGrid.add(go, 1, 0);
		topGrid.add(mineLabel, 2, 0);
		timeLabel.setPadding(new Insets(5));	// keeps it from getting smushed to the edge
		mineLabel.setPadding(new Insets(5));
		
		VBox box = new VBox(topGrid, gameGrid);
		// this is needed to allow the gameGrid to fill the remaining space when resized
		VBox.setVgrow(gameGrid, Priority.ALWAYS);
		
		Scene scene = new Scene(box);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SwineMeeper");
		primaryStage.show();
		go.fire();
	}
	
	private void newGame() {
		// Start fresh
		gameGrid.getChildren().clear();
		gameGrid.getColumnConstraints().clear();
		gameGrid.getRowConstraints().clear();
		
		Difficulty diff = Persist.loadDifficulty();
		Cell[][] cells = new Cell[diff.getWidth()][diff.getHeight()];
		
		// allow buttons to resize to fill window:
		RowConstraints rc = new RowConstraints();
		rc.setPercentHeight(100);
		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(100);
		gameGrid.getRowConstraints().add(rc);
		gameGrid.getColumnConstraints().add(cc);
		
		// We need to set the constraint object for each row and column, though they can be the same...
		for (int i=0; i < diff.getHeight() - 1; i++) {
			gameGrid.getRowConstraints().add(rc);
		}
		for (int i=0; i < diff.getWidth() - 1; i++) {
			gameGrid.getColumnConstraints().add(cc);
		}
		
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
		stage.sizeToScene();	// Resize window to fit size of gameGrid
		stage.setMinHeight(stage.getHeight());	// let it be resized, but don't let it get any smaller than this
		stage.setMinWidth(stage.getWidth());	// (shouldn't this already happen?)
	}
}
