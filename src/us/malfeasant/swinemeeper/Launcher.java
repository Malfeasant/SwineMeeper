package us.malfeasant.swinemeeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
		Difficulty diff = Persist.loadDifficulty();
		Button go = new Button("Go");
		go.setOnAction(e -> newGame());
		BorderPane pane = new BorderPane();
		pane.setCenter(go);
		pane.setLeft(timeLabel);
		pane.setRight(mineLabel);
		pane.setBottom(gameGrid);
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("SwineMeeper");
		primaryStage.show();
		go.fire();
	}
	
	private void newGame() {
		gameGrid.getChildren().clear();
		Difficulty diff = Persist.loadDifficulty();
		for (int y = 0; y < diff.getHeight(); ++y) {
			for (int x = 0; x < diff.getWidth(); ++x) {
				gameGrid.add(new Button(" "), x, y);
			}
		}
		
	}
}
