package us.malfeasant.swinemeeper;

import java.util.Optional;
import java.util.prefs.Preferences;

import javafx.scene.control.Dialog;

public class Persist {
	private static Preferences prefs = Preferences.userNodeForPackage(Persist.class);
	
	public static Difficulty loadDifficulty() {
		Difficulty d = Difficulty.valueOf(prefs.get(Difficulty.class.getSimpleName(), Difficulty.EASY.name()));
		return d;
	}
	public static void storeDifficulty(Difficulty d) {
		prefs.put(Difficulty.class.getSimpleName(), d.name());
		if (d == Difficulty.CUSTOM) {
			Dialog<Triple> dialog = CustomDialog.build();
			Optional<Triple> result = dialog.showAndWait();
			result.ifPresent(triple -> {
				setStuff(triple.width, triple.height, triple.mines);
			});
		} else {
			Triple t = d.getTriple();
			setStuff(t.width, t.height, t.mines);
		}
	}
	
	private static void setStuff(int width, int height, int mines) {
		prefs.putInt("Width", width);
		prefs.putInt("Height", height);
		prefs.putInt("Mines", mines);
	}
	
	public static int getWidth() {
		return prefs.getInt("Width", 5);
	}
	public static int getHeight() {
		return prefs.getInt("Height", 5);
	}
	public static int getMines() {
		return prefs.getInt("Mines", 5);
	}
}
