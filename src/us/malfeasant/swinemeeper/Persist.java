package us.malfeasant.swinemeeper;

import java.util.prefs.Preferences;

public class Persist {
	private static Preferences prefs = Preferences.userNodeForPackage(Persist.class);
	
	public static Difficulty loadDifficulty() {
		Difficulty d = Difficulty.valueOf(prefs.get(Difficulty.class.getSimpleName(), Difficulty.EASY.name()));
		return d;
	}
	public static void storeDifficulty(Difficulty d) {
		prefs.put(Difficulty.class.getSimpleName(), d.name());
		// This is convoluted- we have difficulty from persistent storage- if it's any of the presets, just return the
		// dimensions (Triple) from that, wrapped in Optional- if custom, then this returns an empty option, so go
		// a step further and ask the user for the dimensions- if that fails (user closes the box) fall back to
		// whatever it was set to previously...
		Triple t = d.getTriple().orElseGet(() -> {
			return CustomDialog.build().showAndWait().orElse(getDimensions(d));
		});
		setStuff(t.width, t.height, t.mines);
	}
	
	private static void setStuff(int width, int height, int mines) {
		prefs.putInt("Width", width);
		prefs.putInt("Height", height);
		prefs.putInt("Mines", mines);
	}
	
	public static Triple getDimensions(Difficulty d) {
		return d.getTriple().orElse(new Triple(getWidth(), getHeight(), getMines()));
	}
	private static int getWidth() {
		return prefs.getInt("Width", 5);
	}
	private static int getHeight() {
		return prefs.getInt("Height", 5);
	}
	private static int getMines() {
		return prefs.getInt("Mines", 5);
	}
}
