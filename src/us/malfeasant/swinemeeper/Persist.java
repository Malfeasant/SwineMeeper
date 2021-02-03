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
	
	static class Best {
		final String name;
		final int time;
		Best(String n, int t) {
			name = n; time = t;
		}
	}
	static Best loadBest(Difficulty diff) {
		String name = prefs.get("Name" + diff.name(), "Anonymous");
		int time = prefs.getInt("Time" + diff.name(), 999);
		return new Best(name, time);
	}
	static void clearBest() {
		for (Difficulty diff : Difficulty.values()) {
			prefs.remove("Name" + diff.name());
			prefs.remove("Time" + diff.name());
		}
	}
	static void storeBest(Difficulty diff, int time) {
		Best existing = loadBest(diff);
		if (time < existing.time) {
			String name = BestDialog.newBest(existing.name);
			prefs.put("Name" + diff.name(), name);
			prefs.putInt("Time" + diff.name(), time);
			BestDialog.showBest();
		}
	}
}
