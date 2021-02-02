package us.malfeasant.swinemeeper;

import java.util.ArrayList;
import java.util.Collections;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
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
	private final Label timeLabel = new Label();
	private final Label mineLabel = new Label();
	private Stage stage;	// need this to resize window after adding/removing children- is there a better way?
	private GameState state;	// tracks state of game- can be READY (gameboard is built, but waiting for first click),
	// RUNNING (board has been clicked on, so timers are running), WON (all non-mine cells uncovered), LOST (hit a mine)
	private ArrayList<Cell> cells;
	private int uncovered;	// number of uncovered mines- when this reaches total cells - mines, game must be won
	private int goal;	// number of non-mined cells
	
	private final Timer timer = new Timer();
	private final IntegerProperty mineProp = new SimpleIntegerProperty();
	
	private final Button go = new Button("Go");	// has to be here so it can be "clicked" from event handler
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
//		Button go = new Button("Go");
		go.setOnAction(e -> newGame());
		
		timeLabel.textProperty().bind(timer.timeProperty().asString("%03d"));
		mineLabel.textProperty().bind(mineProp.asString("%03d"));
		
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
		topGrid.add(mineLabel, 0, 0);
		topGrid.add(go, 1, 0);
		topGrid.add(timeLabel, 2, 0);	// oops- had these backwards
		timeLabel.setPadding(new Insets(5));	// keeps it from getting smushed to the edge
		mineLabel.setPadding(new Insets(5));
		
		MenuBar bar = MenuBuilder.build(this);
		VBox box = new VBox(bar, topGrid, gameGrid);
		// this is needed to allow the gameGrid to fill the remaining space when resized
		VBox.setVgrow(gameGrid, Priority.ALWAYS);
		
		Scene scene = new Scene(box);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SwineMeeper");
		primaryStage.show();
		go.fire();
	}
	
	void setDifficulty(Difficulty diff) {
		Persist.storeDifficulty(diff);
		if (diff == Difficulty.CUSTOM) {
			// TODO pop up a dialog for custom dimensions
		}
		go.fire();	// changing difficulty starts a new game
	}
	
	private void newGame() {
		// Start fresh
		gameGrid.getChildren().clear();
		gameGrid.getColumnConstraints().clear();
		gameGrid.getRowConstraints().clear();
		timer.reset();
		
		Difficulty diff = Persist.loadDifficulty();
		Triple t = Persist.getDimensions(diff);
		mineProp.set(t.mines);
		cells = new ArrayList<>(t.width * t.height);
		uncovered = 0;
		goal = t.height * t.width - t.mines;
		
		// allow buttons to resize to fill window:
		RowConstraints rc = new RowConstraints();
		rc.setPercentHeight(100);
		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(100);
		gameGrid.getRowConstraints().add(rc);
		gameGrid.getColumnConstraints().add(cc);
		
		// We need to set the constraint object for each row and column, though they can be the same...
		for (int i=0; i < t.height - 1; i++) {
			gameGrid.getRowConstraints().add(rc);
		}
		for (int i=0; i < t.width - 1; i++) {
			gameGrid.getColumnConstraints().add(cc);
		}
		
		// Create bomb cells first, then create remainder unbombed cells, shuffle them, then put them in place
		for (int i = 0; i < t.width * t.height; i++) {
			Cell e = new Cell(this);
			if (i < t.mines) e.setMine();
			cells.add(e);
		}
		Collections.shuffle(cells);
		
		// Top left corner
		Cell c = cells.get(0);
		gameGrid.add(c.getButton(), 0, 0);
		
		// Top row
		for (int x = 1; x < t.width; ++x) {
			c = cells.get(x);
			gameGrid.add(c.getButton(), x, 0);
			c.setNeighbor(Direction.WEST, cells.get(x-1));
		}
		
		// Remaining rows
		for (int y = 1; y < t.height; ++y) {
			// Left cell
			c = cells.get(y * t.width);
			gameGrid.add(c.getButton(), 0, y);
			c.setNeighbor(Direction.NORTH, cells.get((y-1) * t.width));
			c.setNeighbor(Direction.NORTHEAST, cells.get(1 + (y-1) * t.width));
			
			// Remaining cells
			for (int x = 1; x < t.width; ++x) {
				c = cells.get(x + y * t.width);
				c.setNeighbor(Direction.NORTH, cells.get(x + (y-1) * t.width));
				c.setNeighbor(Direction.NORTHWEST, cells.get((x-1) + (y-1) * t.width));
				c.setNeighbor(Direction.WEST, cells.get((x-1) + y * t.width));
				if (x < t.width - 1)	// don't do this on last column
					c.setNeighbor(Direction.NORTHEAST, cells.get(x + 1 + (y-1) * t.width));
				gameGrid.add(c.getButton(), x, y);
			}
		}
		
		state = GameState.READY;
		
		stage.sizeToScene();	// Resize window to fit size of gameGrid
		stage.setMinHeight(stage.getHeight());	// let it be resized, but don't let it get any smaller than this
		stage.setMinWidth(stage.getWidth());	// (shouldn't this already happen?)
	}
	
	void click(MineAction a) {	// gameboard has to know about clicks and right clicks to start timer, track flagged mines...
		if (state.allowClicks()) {
			switch (a) {
			case MARK:	// these alone do not start the game
				mineProp.set(mineProp.get() - 1);	// goes negative if more marks than mines, that's ok
				break;
			case UNMARK:
				mineProp.set(mineProp.get() + 1);
				break;
			case UNCOVER:
				state = GameState.RUNNING;
				timer.start();
				uncovered++;
				if (uncovered == goal) {
					cells.forEach(c -> c.endGame(true));
					state = GameState.WON;
					timer.stop();
				}
				break;
			case KABOOM:
				state = GameState.LOST;
				timer.stop();
				cells.forEach(c -> c.endGame(false));
				break;
			}
		}
	}
}
