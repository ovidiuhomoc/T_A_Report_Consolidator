package TA_Report_Tool.Filters;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static TA_Report_Tool.Tools.debug.*;
import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.TimeInterval;
import TA_Report_Tool.MainApp.Profile;
import static TA_Report_Tool.Tools.check.*;

public class TAfiltersAndSettings {

	Profile correspondingProfile = null;

	DateTimeFilter dateTimeFilt;
	CheckInCheckOutFilter checkInCheckOut;
	HashMap<String, StdFilter> filtersMap;
	HashMap<String, ColumnProperties> columnsAndFilterAssociation;
	TimeInterval WorkingTime;

	public TAfiltersAndSettings(Profile profile) {
		this.correspondingProfile = profile;
		this.dateTimeFilt = new DateTimeFilter();
		this.checkInCheckOut = new CheckInCheckOutFilter();
		this.filtersMap = new HashMap<>();
		this.columnsAndFilterAssociation = new HashMap<>();

		this.WorkingTime = new TimeInterval(LocalTime.of(0, 0), LocalTime.of(23, 59));
	}

	public DateTimeFilter dateTime() {
		return this.dateTimeFilt;
	}

	public CheckInCheckOutFilter inOut() {
		return this.checkInCheckOut;
	}

	public void attachInOutToColumn(ColumnProperties column) {
		this.checkInCheckOut.attachColumn(column);
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
		display(this.getClass().getSimpleName(), "Filtering column " + colName);

		while (it.hasNext()) {
			Map.Entry<String, ColumnProperties> mapElement = (Map.Entry<String, ColumnProperties>) it.next();
			if (mapElement.getValue().getName().equals(colName)) {

				display(this.getClass().getSimpleName(), "Column found");
				if (isDateOrTime(mapElement.getValue())) {
					display(this.getClass().getSimpleName(),
							"The column is a date or time column and null will be returned");
					return null;
				}

				display(this.getClass().getSimpleName(),
						"Column is neithed date and neither time and ColumnProperties will be returned");
				return filtersMap.get(mapElement.getKey());
			}
		}
		display(this.getClass().getSimpleName(), "Column was not found and null will be returned");
		return null;
	}

	public void setWorkingTime(LocalTime tStart, LocalTime tEnd) {
		if (isNotNull(tStart)) {
			this.WorkingTime = new TimeInterval(tStart, this.WorkingTime.getEnd());
		}

		if (isNotNull(tEnd)) {
			this.WorkingTime = new TimeInterval(this.WorkingTime.getStart(), tEnd);
		}
	}

	public TimeInterval getWorkingTime() {
		return this.WorkingTime;
	}

	private boolean isDateOrTime(ColumnProperties column) {
		if ((column.getMappingUnit().getType() == MappingType.Date)
				|| (column.getMappingUnit().getType() == MappingType.DateAndTime)
				|| (column.getMappingUnit().getType() == MappingType.Time)) {
			return true;
		}
		return false;
	}
}
