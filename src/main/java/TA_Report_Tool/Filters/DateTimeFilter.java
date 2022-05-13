package TA_Report_Tool.Filters;

import static TA_Report_Tool.Tools.check.isFalse;
import static TA_Report_Tool.Tools.check.isNotNull;
import static TA_Report_Tool.Tools.check.isNull;
import static TA_Report_Tool.Tools.check.isZero;
import static TA_Report_Tool.Tools.debug.display;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import TA_Report_Tool.Data.TimeInterval;

public class DateTimeFilter {

	private TimeInterval timeTuple1 = null;
	private TimeInterval timeTuple2 = null;

	public DateTimeFilter() {
		useOnlyOneTimeFilter();
		noDateFiltering();
	}

	/**
	 * Setting the start & end period of one out of 2 time intervals that can be
	 * used for filtering
	 * 
	 * @param filterNo 1 or 2
	 * @param tStart   the start time (LocalTime type)
	 * @param tEnd     the end time (LocalTime type)
	 */
	public void setTimeFilter(int filterNo, LocalTime tStart, LocalTime tEnd) {
		LocalTime localTStart, localTEnd;

		localTStart = tStart;
		localTEnd = tEnd;

		if (isNull(tStart)) {
			localTStart = LocalTime.of(0, 0, 0);
		}

		if (isNull(tEnd)) {
			localTEnd = LocalTime.of(23, 59, 59);
		}

		switch (filterNo) {
		case 1:
			this.timeTuple1 = new TimeInterval(localTStart, localTEnd);
			break;
		case 2:
			this.timeTuple2 = new TimeInterval(localTStart, localTEnd);
			break;
		default:
			break;
		}
	}

	public void setTimeFilter(LocalTime tStart, LocalTime tEnd) {
		setTimeFilter(1, tStart, tEnd);
	}

	public void useOnlyOneTimeFilter() {
		if (isNull(timeTuple1)) {
			setTimeFilter(1, LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59));
		}
		setTimeFilter(2, LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 0));
	}

	/**
	 * returns the filter 1 or 2, a LocalTime tuple object that supports 2 methods:
	 * getStart & getEnd
	 * 
	 * @param filterNo 1 or 2
	 * @return TimeTuple object
	 */
	public TimeInterval getTimeFilter(int filterNo) {
		switch (filterNo) {
		case 1:
			return this.timeTuple1;
		case 2:
			return this.timeTuple2;
		}
		return timeTuple1;
	}

	public TimeInterval getTimeFilter() {
		return this.getTimeFilter(1);
	}

	public void noTimeFiltering() {
		this.timeTuple1 = null;
		this.timeTuple2 = null;
		useOnlyOneTimeFilter();
	}

	private LocalDate oldestDate = null;
	private LocalDate newestDate = null;
	private boolean currMflag = false;
	private boolean prevMflag = false;
	private boolean noMflag = true;

	public void filterThisMonth() {
		this.currMflag = true;
		this.prevMflag = false;
		this.noMflag = false;
	}

	public void filterPrevMonth() {
		this.currMflag = false;
		this.prevMflag = true;
		this.noMflag = false;
	}

	public void notOlderThan(LocalDate date) {
		if (isNull(date)) {
			return;
		}
		this.oldestDate = date;
		this.currMflag = false;
		this.prevMflag = false;
		this.noMflag = false;
	}

	public void notNewerThan(LocalDate date) {
		if (isNull(date)) {
			return;
		}
		this.newestDate = date;
		this.currMflag = false;
		this.prevMflag = false;
		this.noMflag = false;
	}

	public void noDateFiltering() {
		this.currMflag = false;
		this.prevMflag = false;
		this.noMflag = true;
	}

	public boolean isThisMonthFiltered() {
		return this.currMflag;
	}

	public boolean isPrevMonthFiltered() {
		return this.prevMflag;
	}

	public boolean isNoMonthFiltered() {
		return this.noMflag;
	}

	public boolean isSpecificOldestDateFiltered() {
		if (isFalse(this.currMflag) && isFalse(this.prevMflag) & isFalse(this.noMflag) & isNotNull(this.oldestDate)) {
			return true;
		}
		return false;
	}
	
	public boolean isSpecificNewesttDateFiltered() {
		if (isFalse(this.currMflag) && isFalse(this.prevMflag) & isFalse(this.noMflag) & isNotNull(this.newestDate)) {
			return true;
		}
		return false;
	}

	public LocalDate getDateFilterOldest() {
		if (isNotNull(this.oldestDate)) {
			return this.oldestDate;
		}

		if (this.noMflag) {
			return null;
		}

		if (this.currMflag) {
			return LocalDate.now().withDayOfMonth(1);
		}

		if (this.prevMflag) {
			int mNo = LocalDate.now().getMonthValue() - 1;
			if (isZero(mNo)) {
				int year = LocalDate.now().getYear();
				return LocalDate.now().withDayOfMonth(1).withMonth(12).withYear(year - 1);
			}
			return LocalDate.now().withDayOfMonth(1).withMonth(mNo);
		}
		return null;
	}

	public LocalDate getDateFilterNewest() {
		if (isNotNull(this.newestDate)) {
			return this.newestDate;
		}

		if (this.noMflag) {
			return null;
		}

		if (this.currMflag) {
			return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		}

		if (this.prevMflag) {
			int mNo = LocalDate.now().getMonthValue() - 1;
			if (isZero(mNo)) {
				int year = LocalDate.now().getYear();
				return LocalDate.now().withMonth(12).withYear(year - 1).with(TemporalAdjusters.lastDayOfMonth());
			}
			return LocalDate.now().withMonth(mNo).with(TemporalAdjusters.lastDayOfMonth());
		}
		return null;
	}

	public boolean passDateTimeFilter(LocalDateTime dateTime) {
		display(this.getClass().getSimpleName(), "LocalDateTime to pass the filters is " + dateTime);

		if (isFalse(passTimeFilters(dateTime.toLocalTime()))) {
			display(this.getClass().getSimpleName(), "-------- Time filter failed");
			return false;
		}
		if (isFalse(passDateFilter(dateTime.toLocalDate()))) {
			display(this.getClass().getSimpleName(), "-------- Date filter failed");
			return false;
		}
		return true;
	}

	private boolean passDateFilter(LocalDate date) {
		if (isNull(getDateFilterOldest()) && isNull(getDateFilterNewest())) {
			return true;
		}

		if (isNotNull(getDateFilterOldest()) && isFalse(date.compareTo(getDateFilterOldest()) >= 0)) {
			return false;
		}

		if (isNotNull(getDateFilterNewest()) && isFalse(date.compareTo(getDateFilterNewest()) <= 0)) {
			return false;
		}

		return true;
	}

	private boolean passTimeFilters(LocalTime time) {
		if (passTimeFilter(1, time)) {
			display(this.getClass().getSimpleName(), "-------- Time filter 1 passes");
			return true;
		}
		display(this.getClass().getSimpleName(),
				"-------- Time filter 1 between " + this.getTimeFilter(1).getStart().toString() + " - "
						+ this.getTimeFilter(1).getEnd().toString() + " failed");

		if (passTimeFilter(2, time)) {
			display(this.getClass().getSimpleName(), "-------- Time filter 2 passes");
			return true;
		}
		display(this.getClass().getSimpleName(),
				"-------- Time filter 2 between " + this.getTimeFilter(2).getStart().toString() + " - "
						+ this.getTimeFilter(2).getEnd().toString() + " failed");

		return false;
	}

	private boolean passTimeFilter(int i, LocalTime time) {
		TimeInterval timeFilter = this.getTimeFilter(i);

		if (isNull(timeFilter)) {
			return true;
		}

		if (isFalse(time.compareTo(timeFilter.getStart()) >= 0)) {
			return false;
		}
		if (isFalse(time.compareTo(timeFilter.getEnd()) <= 0)) {
			return false;
		}
		return true;
	}
}
