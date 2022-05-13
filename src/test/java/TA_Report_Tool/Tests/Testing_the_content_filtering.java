package TA_Report_Tool.Tests;

import static TA_Report_Tool.Tools.check.isFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.headerNotScanned;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.MainApp.ExceptionsPack.rowParameterNotHigherThanZero;
import TA_Report_Tool.MainApp.ExceptionsPack.searchCantFindMappingUnitInCollection;
import TA_Report_Tool.MainApp.ExceptionsPack.tableDataNotInitialized;
import TA_Report_Tool.MainApp.Profile;
import static TA_Report_Tool.Tools.debug.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_content_filtering {

	private MappingUnit date, time, empID, signalingDev, firstName, lastName, middleName, event, eventId, customF1;

	@Test
	void No_filter_applied_data_stored_from_real_file() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			// preparation of the pool of Mapping Units to be used later

			preparePoolOfMappingUnitsForRealFile(testProfile);

			// scan the file for header & column properties
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			// 1st step
			testProfile.getTableHeader().scanForColsProperties();
			// 2nd step: map all columns with their type from the pool of MappingUnits
			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			// When
			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}

			testProfile.filterTableData();
			// Then
			assertEquals(7, testProfile.getFilteredData().getRowCount());

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero e) {
			e.printStackTrace();
		}
	}

	@Test
	void Date_specific_filters_applied_data_stored_from_real_file() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			// preparation of the pool of Mapping Units to be used later

			preparePoolOfMappingUnitsForRealFile(testProfile);

			// scan the file for header & column properties
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			// 1st step
			testProfile.getTableHeader().scanForColsProperties();
			// 2nd step: map all columns with their type from the pool of MappingUnits
			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			// When
			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}

			testProfile.filterTableData();

			// Then
			assertTrue(testProfile.dataFiltersAndSettings().dateTime().isNoMonthFiltered());

			// When2
			testProfile.dataFiltersAndSettings().dateTime().filterThisMonth();
			testProfile.filterTableData();

			// Then2
			assertTrue(testProfile.dataFiltersAndSettings().dateTime().isThisMonthFiltered());

			// When3
			testProfile.dataFiltersAndSettings().dateTime().filterPrevMonth();
			testProfile.filterTableData();

			// Then3
			assertTrue(testProfile.dataFiltersAndSettings().dateTime().isPrevMonthFiltered());

			// When4
			testProfile.dataFiltersAndSettings().dateTime().notOlderThan(LocalDate.of(2022, 3, 2));
			testProfile.filterTableData();

			// Then4
			assertTrue(testProfile.dataFiltersAndSettings().dateTime().isSpecificOldestDateFiltered());

			// When5
			testProfile.dataFiltersAndSettings().dateTime().notNewerThan(LocalDate.of(2022, 3, 2));
			testProfile.filterTableData();

			// Then5
			assertTrue(testProfile.dataFiltersAndSettings().dateTime().isSpecificOldestDateFiltered());
			assertTrue(testProfile.dataFiltersAndSettings().dateTime().isSpecificNewesttDateFiltered());

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero e) {
			e.printStackTrace();
		}
	}

	@Test
	void Date_filter_applied_data_stored_from_real_file() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			// preparation of the pool of Mapping Units to be used later

			preparePoolOfMappingUnitsForRealFile(testProfile);

			// scan the file for header & column properties
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			// 1st step
			testProfile.getTableHeader().scanForColsProperties();
			// 2nd step: map all columns with their type from the pool of MappingUnits
			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			// When
			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}

			testProfile.dataFiltersAndSettings().dateTime().notOlderThan(LocalDate.of(2022, 3, 3));
			testProfile.dataFiltersAndSettings().dateTime().notNewerThan(LocalDate.of(2022, 3, 3));

			testProfile.filterTableData();

			// Then
			assertEquals(5, testProfile.getFilteredData().getRowCount());

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero e) {
			e.printStackTrace();
		}
	}

	@Test
	void Time_filter_applied_data_stored_from_real_file() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			// preparation of the pool of Mapping Units to be used later

			preparePoolOfMappingUnitsForRealFile(testProfile);

			// scan the file for header & column properties
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			// 1st step
			testProfile.getTableHeader().scanForColsProperties();
			// 2nd step: map all columns with their type from the pool of MappingUnits
			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			// When
			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}

			testProfile.dataFiltersAndSettings().dateTime().setTimeFilter(1, LocalTime.of(8, 0), LocalTime.of(10, 0));
			testProfile.dataFiltersAndSettings().dateTime().setTimeFilter(2, LocalTime.of(16, 0), LocalTime.of(18, 0));

			testProfile.filterTableData();

			// Then
			assertEquals(6, testProfile.getFilteredData().getRowCount());

			// When2
			testProfile.dataFiltersAndSettings().dateTime().noTimeFiltering();
			testProfile.filterTableData();
			// Then2
			assertEquals(7, testProfile.getFilteredData().getRowCount());

			// When3
			testProfile.dataFiltersAndSettings().dateTime().setTimeFilter(LocalTime.of(8, 0), null);
			testProfile.filterTableData();
			// Then3
			assertEquals(7, testProfile.getFilteredData().getRowCount());

			// When4
			testProfile.dataFiltersAndSettings().dateTime().setTimeFilter(null, LocalTime.of(9, 0));
			testProfile.filterTableData();
			// Then4
			assertEquals(3, testProfile.getFilteredData().getRowCount());

			// When5
			testProfile.dataFiltersAndSettings().dateTime().noTimeFiltering();
			testProfile.dataFiltersAndSettings().dateTime().setTimeFilter(LocalTime.of(8, 0), LocalTime.of(9, 0));
			testProfile.filterTableData();

			// Then5
			assertEquals(3, testProfile.getFilteredData().getRowCount());

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero e) {
			e.printStackTrace();
		}
	}

	@Test
	void Text_filter_applied_data_stored_from_real_file() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			// preparation of the pool of Mapping Units to be used later

			preparePoolOfMappingUnitsForRealFile(testProfile);

			// scan the file for header & column properties
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			// 1st step
			testProfile.getTableHeader().scanForColsProperties();
			// 2nd step: map all columns with their type from the pool of MappingUnits
			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			// When
			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event")
					.addToExclusionList("IT Room Door");
			testProfile.filterTableData();

			// Then
			assertEquals(6, testProfile.getFilteredData().getRowCount());

			// When2
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").clearAllListsAndLimits();
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event")
					.addToInclusionList("Turnstile 1 Reader");
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event")
					.addToInclusionList("Turnstile 2 Reader");
			testProfile.filterTableData();

			// Then2
			assertEquals(5, testProfile.getFilteredData().getRowCount());

			// When3
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").clearAllListsAndLimits();
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").addSubtextExclusion("2");
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").addSubtextExclusion("3");
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").addSubtextExclusion("Door");

			testProfile.filterTableData();

			// Then3
			assertEquals(4, testProfile.getFilteredData().getRowCount());

			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event")
					.removeSubtextExclusion("Door");
			testProfile.filterTableData();
			assertEquals(5, testProfile.getFilteredData().getRowCount());

			// When4
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").clearAllListsAndLimits();
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").addSubtextInclusion("Reader");

			testProfile.filterTableData();

			// Then4
			assertEquals(6, testProfile.getFilteredData().getRowCount());

			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").addSubtextInclusion("1");
			testProfile.filterTableData();
			assertEquals(4, testProfile.getFilteredData().getRowCount());

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero e) {
			e.printStackTrace();
		}
	}

	@Test
	void Number_filter_applied_data_stored_from_real_file() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			// preparation of the pool of Mapping Units to be used later

			preparePoolOfMappingUnitsForRealFile(testProfile);

			// scan the file for header & column properties
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			// 1st step
			testProfile.getTableHeader().scanForColsProperties();
			// 2nd step: map all columns with their type from the pool of MappingUnits
			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			// When
			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").setMaxLimit(5);
			testProfile.filterTableData();

			// Then
			assertEquals(5, testProfile.getFilteredData().getRowCount());

			// When2
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").clearAllListsAndLimits();
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").addToInclusionList(1);
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").addToInclusionList(2);
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").addToInclusionList(3);
			testProfile.filterTableData();

			// Then2
			assertEquals(3, testProfile.getFilteredData().getRowCount());

			// When3
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").clearAllListsAndLimits();
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").addToExclusionList(1);
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").addToExclusionList(2);
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").addToExclusionList(3);
			testProfile.filterTableData();

			// Then3
			assertEquals(4, testProfile.getFilteredData().getRowCount());

			// When4
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").clearAllListsAndLimits();
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event ID").setMinLimit(3);
			testProfile.filterTableData();

			// Then4
			assertEquals(5, testProfile.getFilteredData().getRowCount());

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero e) {
			e.printStackTrace();
		}
	}

	@Test
	void Real_filterring_of_all_employees_with_specific_criterias_applied_data_stored_from_real_file() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			// preparation of the pool of Mapping Units to be used later

			preparePoolOfMappingUnitsForRealFile(testProfile);

			// scan the file for header & column properties
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			// 1st step
			testProfile.getTableHeader().scanForColsProperties();
			// 2nd step: map all columns with their type from the pool of MappingUnits
			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			// When
			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}

			// displayOn("TableData");
			// displayOn("StdFilter");
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event")
					.addSubtextInclusion("Turnstile");
			testProfile.dataFiltersAndSettings().getFilterForColumn("Signaling Device of the Event").addSubtextInclusion("Reader");
			testProfile.dataFiltersAndSettings().getFilterForColumn("Event").addSubtextInclusion("Access Granted");
			testProfile.dataFiltersAndSettings().dateTime().notOlderThan(LocalDate.of(2022, 3, 3));

			testProfile.filterTableData();

			// Then
			assertEquals(3, testProfile.getFilteredData().getRowCount());

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero e) {
			e.printStackTrace();
		}
	}

	private void setMappingUnitsOfAllScannedColumns(Profile testProfile) throws connectionNotInitialized,
			InterruptedException, ExecutionException, dateOrTimeMissing, nullArgument, headerNotScanned {
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Event ID", this.eventId);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Event Date", this.date);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Event Time", this.time);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Signaling Device of the Event",
				this.signalingDev);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("User ID", this.empID);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("User First Name", this.firstName);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("User Last Name", this.lastName);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("User Middle Name", this.middleName);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Event", this.event);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Custom defined field 1", this.customF1);
		testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Custom defined field 2", this.customF1);
	}

	private void preparePoolOfMappingUnitsForRealFile(Profile testProfile)
			throws searchCantFindMappingUnitInCollection, nullArgument {
		this.date = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Date);
		this.time = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Time);
		this.empID = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.EmployeeUniqueId);

		this.signalingDev = new MappingUnit("Signaling Device MU", new MaskTemplate().addAnyString(),
				MappingType.SignalingDevice);
		testProfile.getMappingCollection().addMappingUnit(this.signalingDev);

		this.firstName = new MappingUnit("First Name MU", new MaskTemplate().addAnyString(),
				MappingType.EmployeeFirstName);
		testProfile.getMappingCollection().addMappingUnit(this.firstName);

		this.lastName = new MappingUnit("Last Name MU", new MaskTemplate().addAnyString(),
				MappingType.EmployeeLastName);
		testProfile.getMappingCollection().addMappingUnit(this.lastName);

		this.middleName = new MappingUnit("Emp. Middle Name MU", new MaskTemplate().addAnyString(),
				MappingType.EmployeeMiddleName);
		testProfile.getMappingCollection().addMappingUnit(this.middleName);

		this.event = new MappingUnit("Event MU", new MaskTemplate().addAnyString(), MappingType.Event);
		testProfile.getMappingCollection().addMappingUnit(this.event);

		this.eventId = new MappingUnit("Event ID MU", new MaskTemplate().addNumber(), MappingType.Number);
		testProfile.getMappingCollection().addMappingUnit(this.eventId);

		this.customF1 = new MappingUnit("Custom Field 1 MU", new MaskTemplate().addAnyString(),
				MappingType.CustomFieldText);
		testProfile.getMappingCollection().addMappingUnit(this.customF1);
	}
}
