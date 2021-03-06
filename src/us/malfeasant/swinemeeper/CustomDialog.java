package us.malfeasant.swinemeeper;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CustomDialog {
	public static Dialog<Triple> build() {
		Dialog<Triple> dialog = new Dialog<>();
		dialog.setTitle("Custom Board");
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		GridPane pane = new GridPane();
		
		Triple existing = Persist.getDimensions(Difficulty.CUSTOM);
		
		TextField height = new TextField();
		height.setText("" + existing.height);
		pane.add(new Label("Height:"), 0, 0);
		pane.add(height, 1, 0);
		Platform.runLater(() -> height.requestFocus());	// Save user the trouble of clicking the first field...
		
		TextField width = new TextField();
		width.setText("" + existing.width);
		pane.add(new Label("Width:"), 0, 1);
		pane.add(width, 1, 1);
		
		TextField mines = new TextField();
		mines.setText("" + existing.mines);
		pane.add(new Label("Mines:"), 0, 2);
		pane.add(mines, 1, 2);
		
		dialog.getDialogPane().setContent(pane);
		
		dialog.setResultConverter(clicked -> {
			if (clicked == ButtonType.OK) {
				int w = Integer.parseInt(width.getText());
				if (w < 8) w = 8;
				if (w > 64) w = 64;
				int h = Integer.parseInt(height.getText());
				if (h < 8) h = 8;
				if (h > 32) h = 32;
				int m = Integer.parseInt(mines.getText());
				int maxmine = Math.min((h-1) * (w-1), 999);
				if (m < 10) m = 10;
				if (m > maxmine) m = maxmine;
				return new Triple(w, h, m);
			}
			return null;
		});
		
		return dialog;
	}
}
