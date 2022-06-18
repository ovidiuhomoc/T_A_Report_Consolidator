package TA_Report_Tool.Filters;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import static TA_Report_Tool.Tools.debug.*;
import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.TimeInterval;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.tableDataNotInitialized;
import TA_Report_Tool.MainApp.Profile;
import static TA_Report_Tool.Tools.check.*;

public class TAfiltersAndSettings {

	Profile profileInUse = null;

	DateTimeFilter dateTimeFilt;
	CheckInCheckOutFilter checkInCheckOut;
	HashMap<String, StdFilter> filtersMap;
	HashMap<String, ColumnProperties> columnsAndFilterAssociation;
	HashMap<DayOfWeek, TimeInterval> workingSchedule;
	int decimals = 2;

	public TAfiltersAndSettings(Profile profile) {
		this.profileInUse = profile;
		this.dateTimeFilt = new DateTimeFilter();
		this.checkInCheckOut = new CheckInCheckOutFilter();
		this.filtersMap = new HashMap<>();
		this.columnsAndFilterAssociation = new HashMap<>();
		this.workingSchedule = new HashMap<>();
		setDefaultWorkingSchedule();
	}

	public DateTimeFilter dateTime() {
		return this.dateTimeFilt;
	}

	public CheckInCheckOutFilter inOut() {
		return this.checkInCheckOut;
	}

	public void addNewFilter(String filterName, ColumnProperties column) {
		if (isDateOrTime(column)) {
			return;
		}
		filtersMap.put(filterName, new StdFilter(column));
		columnsAndFilterAssociation.put(filterName, column);
	}

	public void removeFilter(String filterName) {
		filtersMap.remove(filterName);
		columnsAndFilterAssociation.remove(filterName);
	}

	public StdFilter getFilter(String filterName) {
		return filtersMap.get(filterName);
	}

	/**
	 * Method for getting the filter associated to a column
	 * 
	 * @param colName (String type) representing the name of the column stored in
	 *                each ColumnProperties and got from header scanning
	 * @return a filter object of type StdFilter
	 */
	public StdFilter getFilterForColumn(String colName) {
		Iterator<Entry<String, ColumnProperties>> it = columnsAndFilterAssociation.entrySet().iterator();
		debugDisplay(this.getClass().getSimpleName(), "Filtering column " + colName);

		while (it.hasNext()) {
			Map.Entry<String, ColumnProperties> mapElement = (Map.Entry<String, ColumnProperties>) it.next();
			if (mapElement.getValue().getName().equals(colName)) {

				debugDisplay(this.getClass().getSimpleName(), "Column found");
				if (isDateOrTime(mapElement.getValue())) {
					debugDisplay(this.getClass().getSimpleName(),
							"The column is a date or time column and null will be returned");
					return null;
				}

				debugDisplay(this.getClass().getSimpleName(),
						"Column is neithed date and neither time and ColumnProperties will be returned");
				return filtersMap.get(mapElement.getKey());
			}
		}
		debugDisplay(this.getClass().getSimpleName(), "Column was not found and null will be returned");
		return null;
	}

	public void setWorkingTimeForDay(DayOfWeek day, LocalTime tStart, LocalTime tEnd) {
		TimeInterval timeInvervalForThisDay = this.workingSchedule.get(day);

		if (isNotNull(tStart)) {
			this.workingSchedule.put(day, new TimeInterval(tStart, timeInvervalForThisDay.getEnd()));
		}

		if (isNotNull(tEnd)) {
			this.workingSchedule.put(day, new TimeInterval(timeInvervalForThisDay.getStart(), tEnd));
		}
	}

	public TimeInterval getWorkingScheduleForDay(DayOfWeek day) {
		return this.workingSchedule.get(day);
	}

	public HashMap<DayOfWeek, TimeInterval> getWorkingScheduleFullWeek() {
		return this.workingSchedule;
	}

	private ArrayList<ColumnProperties> extraColumnsToDisplay;

	public void addColToExtraTAcolDisplay(String column)
			throws tableDataNotInitialized, nullColumnPropertiesPassed, InterruptedException, ExecutionException,
			connectionNotInitialized, columnPropertiesDoesNotExist, dateOrTimeMissing, nullArgument {

		if (isFalse(this.profileInUse.getTableData().isInitialized())) {
			throw new ExceptionsPack.tableDataNotInitialized("Column " + column
					+ " can't be added to be displayed under TA report, before the Table Data to be read OR initialized");
		}

		ColumnProperties targetColumn = this.profileInUse.getTableHeader().getColPropertiesByColName(column);

		if (isNull(this.extraColumnsToDisplay)) {
			this.extraColumnsToDisplay = new ArrayList<>();
		}

		if (this.extraColumnsToDisplay.contains(targetColumn)) {
			return;
		}
		this.extraColumnsToDisplay.add(targetColumn);
	}

	public void removeColFromTAreportDisplay(String column)
			throws tableDataNotInitialized, nullColumnPropertiesPassed, InterruptedException, ExecutionException,
			connectionNotInitialized, columnPropertiesDoesNotExist, dateOrTimeMissing, nullArgument {
		if (isFalse(this.profileInUse.getTableData().isInitialized())) {
			throw new ExceptionsPack.tableDataNotInitialized("Column " + column
					+ " can't be added to be displayed under TA report, before the Table Data to be read OR initialized");
		}

		ColumnProperties targetColumn = this.profileInUse.getTableHeader().getColPropertiesByColName(column);

		if (isNull(this.extraColumnsToDisplay)) {
			return;
		}

		if (isFalse(this.extraColumnsToDisplay.contains(targetColumn))) {
			return;
		}

		this.extraColumnsToDisplay.remove(targetColumn);
	}

	public ArrayList<ColumnProperties> getExtraColsToDisplay() {
		if (isNull(this.extraColumnsToDisplay)) {
			return null;
		}

		if (this.extraColumnsToDisplay.isEmpty()) {
			return null;
		}
		return this.extraColumnsToDisplay;
	}

	public int getDecimalsForFloatRound() {
		return this.decimals;
	}

	public void setDecimalsForFloatRound(int decimals) {
		this.decimals = decimals;
	}

	private boolean isDateOrTime(ColumnProperties column) {
		if ((column.getMappingUnit().getType() == MappingType.Date)
				|| (column.getMappingUnit().getType() == MappingType.DateAndTime)
				|| (column.getMappingUnit().getType() == MappingType.Time)) {
			return true;
		}
		return false;
	}

	private void setDefaultWorkingSchedule() {
		this.workingSchedule.put(DayOfWeek.MONDAY, new TimeInterval(LocalTime.of(9, 0, 0), LocalTime.of(17, 0, 0)));
		this.workingSchedule.put(DayOfWeek.TUESDAY, new TimeInterval(LocalTime.of(9, 0, 0), LocalTime.of(17, 0, 0)));
		this.workingSchedule.put(DayOfWeek.WEDNESDAY, new TimeInterval(LocalTime.of(9, 0, 0), LocalTime.of(17, 0, 0)));
		this.workingSchedule.put(DayOfWeek.THURSDAY, new TimeInterval(LocalTime.of(9, 0, 0), LocalTime.of(17, 0, 0)));
		this.workingSchedule.put(DayOfWeek.FRIDAY, new TimeInterval(LocalTime.of(9, 0, 0), LocalTime.of(17, 0, 0)));
		this.workingSchedule.put(DayOfWeek.SATURDAY, new TimeInterval(LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 0)));
		this.workingSchedule.put(DayOfWeek.SUNDAY, new TimeInterval(LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 0)));
	}
}
