package us.malfeasant.swinemeeper;

import java.util.prefs.Preferences;

public class Persist {
	private static Preferences prefs = Preferences.userNodeForPackage(Persist.class);
	
	public static Difficulty loadDifficulty() {
		return Difficulty.valueOf(prefs.get(Difficulty.class.getSimpleName(), Difficulty.EASY.name()));
	}
	public static void storeDifficulty(Difficulty d) {
		prefs.put(Difficulty.class.getSimpleName(), d.name());
	}
	
	public static void setCustom(int width, int height, int mines) {
		prefs.putInt("CustomWidth", width);
		prefs.putInt("CustomHeight", height);
		prefs.putInt("CustomMines", mines);
	}
	
	public static int getCustomWidth() {
		return prefs.getInt("CustomWidth", 5);
	}
	public static int getCustomHeight() {
		return prefs.getInt("CustomHeight", 5);
	}
	public static int getCustomMines() {
		return prefs.getInt("CustomMines", 5);
	}
}
