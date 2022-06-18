package TA_Report_Tool.Processors;

import static TA_Report_Tool.Tools.check.*;
import static TA_Report_Tool.Tools.compute.*;
import static TA_Report_Tool.Tools.debug.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.TableData;
import TA_Report_Tool.Data.employeeWorkedPeriod;
import TA_Report_Tool.Data.timeCheckType;
import TA_Report_Tool.Filters.TAfiltersAndSettings;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.TAreportGenerationException;
import TA_Report_Tool.MainApp.ExceptionsPack.cantBeParsedWithCurrentMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.cantParseEmptyStringForCurrentType;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.nullDataReceivedForParsing;
import TA_Report_Tool.MainApp.ExceptionsPack.parsingFailedDueToNullMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.rowParameterNotHigherThanZero;
import TA_Report_Tool.MainApp.ExceptionsPack.tableDataNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.tableLengthAndDataLengthNotMatching;
import TA_Report_Tool.MainApp.Profile;

public class TAreportGenerator implements Callable<Void> {

	private TableData detailedReport, summaryReport, filteredTableInUse;
	private Profile profileInUse;
	TAfiltersAndSettings filtersInUse;
	ArrayList<String> employeesIDlist = new ArrayList<>();
	ArrayList<employeeWorkedPeriod> workedPeriodList = new ArrayList<>();

	String className;

	@Override
	public Void call() throws tableDataNotInitialized, TAreportGenerationException, rowParameterNotHigherThanZero,
			tableLengthAndDataLengthNotMatching, nullDataReceivedForParsing, InterruptedException, ExecutionException,
			connectionNotInitialized, nullColumnPropertiesPassed, columnPropertiesDoesNotExist,
			cantBeParsedWithCurrentMappingMask, parsingFailedDueToNullMappingMask, dateOrTimeMissing, nullArgument,
			cantParseEmptyStringForCurrentType {

		className = this.getClass().getSimpleName();

		this.filteredTableInUse = this.profileInUse.getFilteredData();
		String colUIDname = this.filteredTableInUse.getColumnWithMappingType(MappingType.EmployeeUniqueId).getName();

		if (isNull(colUIDname)) {
			debugDisplay(className, "Getting the column with mapping type EmployeeUniqueID failed and returned null ");
			throw new ExceptionsPack.TAreportGenerationException("No Employee Unique ID column was set");
		}

		debugDisplay(className, "Getting the column with mapping type EmployeeUniqueID did not failed and returned ->"
				+ colUIDname + "<-");

		debugDisplay(className, "");
		debugDisplay(className, "The filtered table number of rows is " + this.filteredTableInUse.getRowCount());

		for (int i = 1; i <= this.filteredTableInUse.getRowCount(); i++) {
			String currentEmpID = String.valueOf(this.filteredTableInUse.getDataOfColAtRow(colUIDname, i));

			if (isFalse(individualCheckEvents())) {
				debugDisplay(className, "No individual check events at row " + i + " and date time is "
						+ this.filteredTableInUse.getDateTimeOfRow(i));

				processEntry(currentEmpID, this.filteredTableInUse.getDateTimeOfRow(i),
						timeCheckType.noSeparateCheckEvents);
				continue;
			}

			if (noAttachedCol() && individualCheckEvents()) {
				throw new ExceptionsPack.TAreportGenerationException(
						"Individual Check events setup with no column set as container for Check In & Check Out");
			}

			if (individualCheckEvents()) {
				String inOutColumn = this.profileInUse.dataFiltersAndSettings().inOut().getAttachedColumn();

				if (isCheckIn(inOutColumn, i)) {
					debugDisplay(className, "Check In at row " + i);

					processEntry(currentEmpID, this.filteredTableInUse.getDateTimeOfRow(i), timeCheckType.checkIn);
					continue;
				}

				if (isCheckOut(inOutColumn, i)) {
					debugDisplay(className, "Check Out at row " + i);

					processEntry(currentEmpID, this.filteredTableInUse.getDateTimeOfRow(i), timeCheckType.checkOut);
					continue;
				}
			}
		}

		for (employeeWorkedPeriod x : this.workedPeriodList) {
			x.computeWorkedIntervals();
		}

		ArrayList<String> sortedEmployeesIDlist = new ArrayList<>();
		for (String x : this.employeesIDlist) {
			sortedEmployeesIDlist.add(x);
		}

		Collections.sort(sortedEmployeesIDlist);
		generateTAReports(sortedEmployeesIDlist);

		return null;
	}

	public void transferPrerequisites(Profile profile, TAfiltersAndSettings localFiltersAndSettings,
			TableData detailedTAreport, TableData summaryTAreport) {

		this.profileInUse = profile;
		this.detailedReport = detailedTAreport;
		this.summaryReport = summaryTAreport;
		this.filtersInUse = localFiltersAndSettings;
	}

	private boolean isCheckOut(String inOutColumn, int i) throws rowParameterNotHigherThanZero {
		return this.profileInUse.dataFiltersAndSettings().inOut()
				.isThisCheckOut(this.filteredTableInUse.getDataOfColAtRow(inOutColumn, i));
	}

	private boolean isCheckIn(String inOutColumn, int i) throws rowParameterNotHigherThanZero {
		return this.profileInUse.dataFiltersAndSettings().inOut()
				.isThisCheckIn(this.filteredTableInUse.getDataOfColAtRow(inOutColumn, i));
	}

	private boolean individualCheckEvents() {
		if (this.profileInUse.dataFiltersAndSettings().inOut().individualCheckInCheckOut()) {
			return true;
		}
		return false;
	}

	private boolean noAttachedCol() {
		return isNull(this.profileInUse.dataFiltersAndSettings().inOut().getAttachedColumn());
	}

	private void generateTAReports(ArrayList<String> sortedEmployeesIDlist)
			throws tableLengthAndDataLengthNotMatching, nullDataReceivedForParsing, InterruptedException,
			ExecutionException, connectionNotInitialized, nullColumnPropertiesPassed, columnPropertiesDoesNotExist,
			cantBeParsedWithCurrentMappingMask, parsingFailedDueToNullMappingMask, rowParameterNotHigherThanZero,
			tableDataNotInitialized, dateOrTimeMissing, nullArgument, cantParseEmptyStringForCurrentType {
		for (String empID : sortedEmployeesIDlist) {
			for (employeeWorkedPeriod wrkPeriod : this.workedPeriodList) {
				if (empID.equals(wrkPeriod.getEmpID())) {

					ArrayList<String> tempBasicRowStorage = new ArrayList<>();
					ArrayList<String> tempDetailedRowStorage = new ArrayList<>();
					ArrayList<String> tempSummaryRowStorage = new ArrayList<>();
					ArrayList<String> valForXtraCols = new ArrayList<>();

					if (isNotNull(this.profileInUse.dataFiltersAndSettings().getExtraColsToDisplay())) {
						for (ColumnProperties x : this.profileInUse.dataFiltersAndSettings().getExtraColsToDisplay()) {
							valForXtraCols.add(getXtraColValue(empID, x.getName()));
						}
					}

					tempBasicRowStorage.add(empID);
					tempBasicRowStorage.addAll(valForXtraCols);

					ArrayList<String> months = new ArrayList<>();
					ArrayList<Float> wrkH = new ArrayList<>();
					ArrayList<Float> overH = new ArrayList<>();
					int decimals = this.profileInUse.dataFiltersAndSettings().getDecimalsForFloatRound();

					for (int i = 0; i < wrkPeriod.getRecordCount(); i++) {
						tempDetailedRowStorage.clear();
						tempDetailedRowStorage.addAll(tempBasicRowStorage);

						LocalDate day = wrkPeriod.getDateOfRecord(i);
						String month = day.format(DateTimeFormatter.ofPattern("MM.yyyy"));

						tempDetailedRowStorage.add(month);
						tempDetailedRowStorage.add(day.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

						Float workedH, overtimeH;
						workedH = wrkPeriod.getWorkingHoursForDay(day);
						overtimeH = wrkPeriod.getOvertimeHoursForDay(day);

						tempDetailedRowStorage.add(String.valueOf(round(workedH, decimals)));
						tempDetailedRowStorage.add(String.valueOf(round(overtimeH, decimals)));
						tempDetailedRowStorage.add(String.valueOf(round(workedH + overtimeH, decimals)));

						if (isFalse(months.contains(month))) {
							months.add(month);
							wrkH.add(workedH);
							overH.add(overtimeH);
						} else {
							int index = months.indexOf(month);
							wrkH.set(index, wrkH.get(index) + workedH);
							overH.set(index, overH.get(index) + overtimeH);
						}

						this.detailedReport.suppressDateTimeFullCheck();
						this.detailedReport.parseAndStoreOneDataRow(tempDetailedRowStorage.toArray(new String[0]));
					}

					for (int k = 0; k < months.size(); k++) {
						tempSummaryRowStorage.clear();
						tempSummaryRowStorage.addAll(tempBasicRowStorage);
						tempSummaryRowStorage.add(months.get(k));
						tempSummaryRowStorage.add(String.valueOf(round(wrkH.get(k), decimals)));
						tempSummaryRowStorage.add(String.valueOf(round(overH.get(k), decimals)));
						tempSummaryRowStorage.add(String.valueOf(round(wrkH.get(k) + overH.get(k), decimals)));

						this.summaryReport.suppressDateTimeFullCheck();
						this.summaryReport.parseAndStoreOneDataRow(tempSummaryRowStorage.toArray(new String[0]));
					}
				}
			}
		}
	}

	private String getXtraColValue(String empID, String colName)
			throws tableDataNotInitialized, rowParameterNotHigherThanZero {
		return this.filteredTableInUse.getMostEncounteredEntryOfColForEmpID(empID, colName);
	}

	private void processEntry(String empID, LocalDateTime dateTimeOfRow, timeCheckType inOutSetup) {
		if (isFalse(this.employeesIDlist.contains(empID))) {
			this.employeesIDlist.add(empID);
			this.workedPeriodList.add(new employeeWorkedPeriod(empID,
					this.profileInUse.dataFiltersAndSettings().getWorkingScheduleFullWeek()));
		}

		int index = this.employeesIDlist.indexOf(empID);
		this.workedPeriodList.get(index).addTimeEntry(dateTimeOfRow.toLocalDate(), dateTimeOfRow.toLocalTime(),
				inOutSetup);
	}
}
