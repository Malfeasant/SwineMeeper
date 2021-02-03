package us.malfeasant.swinemeeper;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import us.malfeasant.swinemeeper.Persist.Best;

public class BestDialog {
	public static void showBest() {
		Dialog<ButtonType> alert = new Dialog<>();
		alert.setTitle("Fastest Meepers");
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		
		Best easy = Persist.loadBest(Difficulty.EASY);
		pane.add(new Label("Beginner:"), 0, 0);
		Label easyTime = new Label("" + easy.time);
		pane.add(easyTime, 1, 0);
		Label easyName = new Label("" + easy.name);
		pane.add(easyName, 2, 0);
		
		Best med = Persist.loadBest(Difficulty.MEDIUM);
		pane.add(new Label("Intermediate:"), 0, 1);
		Label medTime = new Label("" + med.time);
		pane.add(medTime, 1, 1);
		Label medName = new Label(med.name);
		pane.add(medName , 2, 1);
		
		Best hard = Persist.loadBest(Difficulty.HARD);
		pane.add(new Label("Expert:"), 0, 2);
		Label hardTime = new Label("" + hard.time);
		pane.add(hardTime, 1, 2);
		Label hardName = new Label(hard.name);
		pane.add(hardName, 2, 2);
		
		alert.getDialogPane().setContent(pane);
		
		ButtonType reset = new ButtonType("Reset");
		alert.getDialogPane().getButtonTypes().setAll(reset, ButtonType.OK);
		
		// now, get the button that will be created for the buttontype so we can catch events on it
		// because we don't want to close the dialog when the reset button is clicked
		Button btn = (Button) alert.getDialogPane().lookupButton(reset);
		btn.addEventFilter(ActionEvent.ACTION, event -> {
			Persist.clearBest();
			// same best can be used for all 3 since it's been reset...
			Best best = Persist.loadBest(Difficulty.EASY);
			
			easyTime.setText("" + best.time);
			easyName.setText(best.name);
			
			medTime.setText("" + best.time);
			medName.setText(best.name);
			
			hardTime.setText("" + best.time);
			hardName.setText(best.name);

			event.consume();	// if we redraw in a more traditional way, will need to do this
		});
		
		alert.showAndWait();	// we don't care about the response
	}
}
