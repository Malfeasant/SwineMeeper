package us.malfeasant.swinemeeper;

public enum GameState {
	// represents state of game
	READY(true, "🙂"),	// gameboard is built, but waiting for first click
	RUNNING(true, "😐"),	// board has been clicked on, so timers are running
	WON(false, "😁"),	// all non-mine cells uncovered
	LOST(false, "😢");	// hit a mine
	private GameState(boolean a, String t) {
		allow = a;
		butText = t;
	}
	private final boolean allow;
	private final String butText;
	@Override
	public String toString() {
		return butText;
	}
	public boolean allowClicks() {
		return allow;
	}
}
