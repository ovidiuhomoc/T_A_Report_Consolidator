package TA_Report_Tool.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static TA_Report_Tool.Tools.check.*;

public class employeeWorkedPeriod {

	String empID;
	ArrayList<LocalDate> daysList;
	ArrayList<timeCheckCollection> timeCollections;
	ArrayList<Float> workingHoursCollection;
	ArrayList<Float> overtimeCollection;
	HashMap<DayOfWeek, TimeInterval> workingScheduleForWeek;

	public employeeWorkedPeriod(String empID, HashMap<DayOfWeek, TimeInterval> workingScheduleForWeek) {
		this.empID = empID;
		this.daysList = new ArrayList<>();
		this.timeCollections = new ArrayList<>();
		this.workingHoursCollection = new ArrayList<>();
		this.overtimeCollection = new ArrayList<>();
		this.workingScheduleForWeek = workingScheduleForWeek;
	}

	public void addTimeEntry(LocalDate day, LocalTime timeEntry, timeCheckType type) {
		if (isFalse(this.daysList.contains(day))) {
			initializeNewEntry(day);
		}
		addTimeEntryToDay(day, timeEntry, type);
	}

	private void initializeNewEntry(LocalDate day) {
		this.daysList.add(day);
		this.timeCollections.add(new timeCheckCollection(day, this.workingScheduleForWeek.get(day.getDayOfWeek())));
	}

	private void addTimeEntryToDay(LocalDate day, LocalTime timeEntry, timeCheckType type) {
		timeCheckCollection temp = this.timeCollections.get(this.daysList.indexOf(day));

		switch (type) {
		case noSeparateCheckEvents:
			temp.addTimeEntry(timeEntry);
			break;
		case checkIn:
			temp.addCheckInTime(timeEntry);
			break;
		case checkOut:
			temp.addCheckOutTime(timeEntry);
			break;
		}
	}

	public void computeWorkedIntervals() {
		for (timeCheckCollection x : this.timeCollections) {
			this.workingHoursCollection.add(x.getWorkingHours());
			this.overtimeCollection.add(x.getOvertimeHours());
		}
	}

	public Float getWorkingHoursForDay(LocalDate day) {
		if (isNull(day)) {
			return (float) 0;
		}
		return this.workingHoursCollection.get(this.daysList.indexOf(day));
	}

	public Float getOvertimeHoursForDay(LocalDate day) {
		if (isNull(day)) {
			return (float) 0;
		}
		return this.overtimeCollection.get(this.daysList.indexOf(day));
	}

	public String getEmpID() {
		return this.empID;
	}

	public int getRecordCount() {
		return this.daysList.size();
	}

	public LocalDate getDateOfRecord(int i) {
		if (i >= this.daysList.size()) {
			return null;
		}
		return this.daysList.get(i);
	}
}
