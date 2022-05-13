package TA_Report_Tool.Filters;

import java.util.HashSet;

import TA_Report_Tool.Data.ColumnProperties;

import static TA_Report_Tool.Tools.check.*;

public class CheckInCheckOutFilter {
	private boolean separateCheckInAndCheckOut = false;
	private HashSet<String> checkIn;
	private HashSet<String> checkOut;

	public CheckInCheckOutFilter() {
		checkIn = new HashSet<>();
		checkOut = new HashSet<>();
		reset();
	}

	public void reset() {
		this.separateCheckInAndCheckOut = false;
		this.checkIn.clear();
		this.checkOut.clear();
	}

	public <T> void addCheckIn(T string) {
		if (isFalse(this.separateCheckInAndCheckOut)) {
			this.separateCheckInAndCheckOut = true;
		}

		this.checkIn.add(String.valueOf(string));
	}

	public <T> void removeCheckIn(T string) {
		if (isFalse(this.separateCheckInAndCheckOut)) {
			return;
		}

		this.checkIn.remove(String.valueOf(string));
		
		if (this.checkIn.isEmpty()) {
			this.separateCheckInAndCheckOut = false;
		}
	}

	public <T> void addCheckOut(T string) {
		if (isFalse(this.separateCheckInAndCheckOut)) {
			this.separateCheckInAndCheckOut = true;
		}

		this.checkOut.add(String.valueOf(string));
	}

	public <T> void removeCheckOut(T string) {
		if (isFalse(this.separateCheckInAndCheckOut)) {
			return;
		}

		this.checkOut.remove(String.valueOf(string));
		
		if (this.checkOut.isEmpty()) {
			this.separateCheckInAndCheckOut = false;
		}
	}

	/**
	 * Method used to return true if there are entries in CheckIn list And/Or
	 * CheckOut list. These entries will be used for TA (first in, last out) If
	 * method returns false, all events will be check in or check out. TA will be
	 * calculated based on the first and last event in that day.
	 * 
	 * @return
	 */
	public boolean areFiltersSeparated() {
		return this.separateCheckInAndCheckOut;
	}

	public <T> boolean isThisCheckIn(T string) {
		if (isFalse(this.separateCheckInAndCheckOut)) {
			return false;
		}

		if (this.checkIn.contains(String.valueOf(string))) {
			return true;
		}
		return false;
	}

	public <T> boolean isThisCheckOut(T string) {
		if (isFalse(this.separateCheckInAndCheckOut)) {
			return false;
		}

		if (this.checkOut.contains(String.valueOf(string))) {
			return true;
		}
		return false;
	}

	private ColumnProperties column;

	public void attachColumn(ColumnProperties column) {
		this.column = column;
	}

	public ColumnProperties getAttachedColumn() {
		return this.column;
	}
}
