package us.malfeasant.swinemeeper;

public enum GameState {
	READY(true), RUNNING(true), WON(false), LOST(false);
	private GameState(boolean a) {
		allow = a;
	}
	private final boolean allow;
	public boolean allowClicks() {
		return allow;
	}
}
