package us.malfeasant.swinemeeper;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Difficulty diff = Difficulty.loadDifficulty();
	}

}
