package TA_Report_Tool.Data;

import java.time.LocalTime;

public final class TimeInterval {
	private LocalTime tStart;
	private LocalTime tEnd;

	/**
	 * Stores and interval of time
	 * 
	 * @param tStart marks the interval begin
	 * @param tEnd   marks the interval end
	 */
	public TimeInterval(LocalTime tStart, LocalTime tEnd) {
		this.tStart = tStart;
		this.tEnd = tEnd;
	}

	public LocalTime getStart() {
		return this.tStart;
	}

	public LocalTime getEnd() {
		return this.tEnd;
	}

	public boolean contains(LocalTime x) {
		if (x.compareTo(tStart) < 0) {
			return false;
		}
		if (x.compareTo(tEnd) > 0) {
			return false;
		}
		return true;
	}
}