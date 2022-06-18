package TA_Report_Tool.Data;

import static TA_Report_Tool.Tools.check.isFalse;
import static TA_Report_Tool.Tools.check.isNotNull;
import static TA_Report_Tool.Tools.check.isNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class timeCheckCollection {
	private ArrayList<LocalTime> checkInTime;
	private ArrayList<LocalTime> checkOutTime;
	private LocalDate dayInUse;
	private TimeInterval workingScheduleForDayOfWeek;

	public timeCheckCollection(LocalDate day, TimeInterval workingScheduleForDay) {
		this.checkInTime = new ArrayList<>();
		this.checkOutTime = new ArrayList<>();
		this.dayInUse = day;
		this.workingScheduleForDayOfWeek = workingScheduleForDay;
	}

	public void addCheckInTime(LocalTime timeEntry) {
		this.checkInTime.add(timeEntry);
	}

	public void addCheckOutTime(LocalTime timeEntry) {
		this.checkOutTime.add(timeEntry);
	}

	public void addTimeEntry(LocalTime timeEntry) {
		this.addCheckInTime(timeEntry);
	}

	private ArrayList<TimeInterval> fullyWorkedIntervals = null;
	float workedHours = 0;
	float overtimeHours = 0;

	public Float getWorkingHours() {
		if (isNull(this.fullyWorkedIntervals)) {
			computeWorkedIntervals();
		}
		return this.workedHours;
	}

	public Float getOvertimeHours() {
		if (isNull(this.fullyWorkedIntervals)) {
			computeWorkedIntervals();
		}
		return this.overtimeHours;
	}

	private float getTimeIntervalWorkingHours(TimeInterval x) {
		LocalTime tempStart = null;
		LocalTime tempEnd = null;
		TimeInterval tempInt = x;

		if (this.workingScheduleForDayOfWeek.getStart().equals(this.workingScheduleForDayOfWeek.getEnd())) {
			return (float) 0;
			// during no working hours (Start = End), any worked hour is overtime
		}

		if (isNull(x)) {
			return (float) 0;
		}

		if (intervalBeforeWorkingSchedule(tempInt) || intervalAfterWorkingSchedule(tempInt)) {
			return (float) 0;
		}

		if (tempInt.contains(this.workingScheduleForDayOfWeek.getStart())) {
			tempStart = this.workingScheduleForDayOfWeek.getStart();
		}

		if (this.workingScheduleForDayOfWeek.contains(tempInt.getStart())) {
			tempStart = tempInt.getStart();
		}

		if (tempInt.contains(this.workingScheduleForDayOfWeek.getEnd())) {
			tempEnd = this.workingScheduleForDayOfWeek.getEnd();
		}

		if (this.workingScheduleForDayOfWeek.contains(tempInt.getEnd())) {
			tempEnd = tempInt.getEnd();
		}

		return ((float) tempStart.until(tempEnd, ChronoUnit.SECONDS)) / 3600;
	}

	private boolean intervalAfterWorkingSchedule(TimeInterval tempInt) {
		return (tempInt.getStart().compareTo(this.workingScheduleForDayOfWeek.getEnd()) > 0)
				&& (tempInt.getEnd().compareTo(this.workingScheduleForDayOfWeek.getEnd()) > 0);
	}

	private boolean intervalBeforeWorkingSchedule(TimeInterval tempInt) {
		return (tempInt.getStart().compareTo(this.workingScheduleForDayOfWeek.getStart()) < 0)
				&& (tempInt.getEnd().compareTo(this.workingScheduleForDayOfWeek.getStart()) < 0);
	}

	private float getTimeIntervalOvertimeHours(TimeInterval x) {
		LocalTime tempStart1 = null;
		LocalTime tempStart2 = null;
		LocalTime tempEnd1 = null;
		LocalTime tempEnd2 = null;
		TimeInterval tempInt = x;

		if (this.workingScheduleForDayOfWeek.getStart().equals(this.workingScheduleForDayOfWeek.getEnd())) {
			return ((float) tempInt.getStart().until(tempInt.getEnd(), ChronoUnit.SECONDS)) / 3600;
		}

		if (isNull(x)) {
			return (float) 0;
		}

		if (intervalBeforeWorkingSchedule(tempInt) || intervalAfterWorkingSchedule(tempInt)) {
			return ((float) tempInt.getStart().until(tempInt.getEnd(), ChronoUnit.SECONDS)) / 3600;
		}

		if (tempInt.contains(this.workingScheduleForDayOfWeek.getStart())) {
			tempStart1 = tempInt.getStart();
			tempEnd1 = this.workingScheduleForDayOfWeek.getStart();
		}

		if (tempInt.contains(this.workingScheduleForDayOfWeek.getEnd())) {
			tempStart2 = this.workingScheduleForDayOfWeek.getEnd();
			tempEnd2 = tempInt.getEnd();
		}

		float time1 = 0;
		float time2 = 0;

		if (isNotNull(tempStart1) && isNotNull(tempEnd1)) {
			time1 = ((float) tempStart1.until(tempEnd1, ChronoUnit.SECONDS)) / 3600;
		}

		if (isNotNull(tempStart2) && isNotNull(tempEnd2)) {
			time2 = ((float) tempStart2.until(tempEnd2, ChronoUnit.SECONDS)) / 3600;
		}

		return time1 + time2;
	}

	private LocalTime temptStart = null;
	private LocalTime temptEnd = null;

	private void computeWorkedIntervals() {
		this.fullyWorkedIntervals = new ArrayList<>();
		ArrayList<LocalTime> allChecks = new ArrayList<>();
		boolean separateChecks = false;

		if (isSingleCheckPoint()) {
			if (onlyCheckInCotainsData()) {
				copyLocalTimeFromTo(this.checkInTime, allChecks);
				separateChecks = false;
			}
			if (onlyCheckOutContainsData()) {
				copyLocalTimeFromTo(this.checkOutTime, allChecks);
				separateChecks = false;
			}
		}
		if (isInOutCheckPoint()) {
			copyLocalTimeFromTo(this.checkInTime, allChecks);
			copyLocalTimeFromTo(this.checkOutTime, allChecks);
			separateChecks = true;
		}

		Collections.sort(allChecks);

		if (allChecks.size() == 1) {
			this.fullyWorkedIntervals.add(workingScheduleForDayOfWeek);
			return;
		}

		int i = 0;

		if (isFalse(separateChecks)) {
			this.fullyWorkedIntervals.add(new TimeInterval(allChecks.get(0), allChecks.get(allChecks.size() - 1)));
		}

		while ((i < allChecks.size()) && separateChecks) {
			int offset = checkLocalTime(i, allChecks);
			i = i + offset;

			if (isNotNull(temptStart) && isNotNull(temptEnd)) {
				this.fullyWorkedIntervals.add(new TimeInterval(temptStart, temptEnd));
				temptStart = null;
				temptEnd = null;
			}
		}

		for (TimeInterval x : this.fullyWorkedIntervals) {
			this.workedHours = this.workedHours + getTimeIntervalWorkingHours(x);
			this.overtimeHours = this.overtimeHours + getTimeIntervalOvertimeHours(x);
		}
	}

	private int checkLocalTime(int currentPos, ArrayList<LocalTime> allChecks) {
		LocalTime localTimeToBeChecked = allChecks.get(currentPos);

		if (isNull(this.temptStart) && isCheckOut(localTimeToBeChecked)) {
			return 1;
		}

		if (isNull(this.temptStart) && isCheckIn(localTimeToBeChecked)) {
			this.temptStart = localTimeToBeChecked;
			return 1;
		}

		if (isNotNull(this.temptStart) && isCheckOut(localTimeToBeChecked)) {
			int j = currentPos;
			if ((j + 1) < allChecks.size()) {
				while (((j + 1) < allChecks.size()) && (isCheckOut(allChecks.get(j + 1)))) {
					j = j + 1;
				}
			}
			this.temptEnd = allChecks.get(j);
			return 1 + (j - currentPos);
		}

		if (isNotNull(this.temptStart) && isCheckIn(localTimeToBeChecked)) {
			return 1;
		}
		return 0;
	}

	private boolean isCheckOut(LocalTime localTimeToBeChecked) {
		if (this.checkOutTime.contains(localTimeToBeChecked)) {
			return true;
		}
		return false;
	}

	private boolean isCheckIn(LocalTime localTimeToBeChecked) {
		if (this.checkInTime.contains(localTimeToBeChecked)) {
			return true;
		}
		return false;
	}

	private void copyLocalTimeFromTo(ArrayList<LocalTime> checkListInUse, ArrayList<LocalTime> allChecks) {
		for (LocalTime x : checkListInUse) {
			allChecks.add(x);
		}
	}

	private boolean onlyCheckOutContainsData() {
		if (this.checkOutTime.isEmpty()) {
			return false;
		}
		return this.checkInTime.isEmpty();
	}

	private boolean onlyCheckInCotainsData() {
		if (this.checkInTime.isEmpty()) {
			return false;
		}
		return this.checkOutTime.isEmpty();
	}

	private boolean isInOutCheckPoint() {
		return isFalse(onlyCheckInCotainsData()) && isFalse(onlyCheckOutContainsData());
	}

	private boolean isSingleCheckPoint() {
		return onlyCheckInCotainsData() || onlyCheckOutContainsData();
	}

}
