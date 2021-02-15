package us.malfeasant.swinemeeper;

import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Timer extends AnimationTimer {
	private static long NANOFACTOR = 1000000000L;
	private long last;
	private long remainder;
	private final IntegerProperty timeProp;
	
	public Timer() {
		timeProp = new SimpleIntegerProperty(0);
	}
	
	@Override
	public void handle(long now) {
		if (last > 0) {
			remainder += now - last;	// add current interval
			last = now;
		} else {	// first run since started
			last = now;
			return;	// do nothing else this iteration
		}
		timeProp.set((int) (timeProp.get() + remainder / NANOFACTOR));	// should only ever be 0 or 1
		remainder %= NANOFACTOR;
	}
	
	public IntegerProperty timeProperty() {
		return timeProp;
	}
	
	@Override
	public void stop() {
		super.stop();
		last = 0;	// this allows the timer to stop and be restarted, f.e. minimizing the window...
	}
	public void reset() {
		timeProp.set(0);
		remainder = 0;
	}
}
