package TA_Report_Tool.Tests;

import static TA_Report_Tool.Tools.check.isFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.TAreportGenerationException;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.headerNotScanned;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.MainApp.ExceptionsPack.rowParameterNotHigherThanZero;
import TA_Report_Tool.MainApp.ExceptionsPack.searchCantFindMappingUnitInCollection;
import TA_Report_Tool.MainApp.ExceptionsPack.tableDataMethodError;
import TA_Report_Tool.MainApp.ExceptionsPack.tableDataNotInitialized;
import TA_Report_Tool.MainApp.Profile;
import static TA_Report_Tool.Tools.debug.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_TA_generation {
	private MappingUnit date, time, empID, signalingDev, firstName, lastName, middleName, event, eventId, customF1;

	@Test
	public void generate_TA_without_filtered_table_generation_called() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			preparePoolOfMappingUnitsForRealFile(testProfile);

			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			testProfile.getTableHeader().scanForColsProperties();

			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}
			// testProfile.filterTableData();

			// Then
			assertThrows(ExceptionsPack.tableDataNotInitialized.class, () -> {
				testProfile.generateTAreport();
			});
		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection |

				headerNotScanned e) {
			e.printStackTrace();
		}
	}

	@Test
	public void generate_TA_without_any_filters_applied() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			preparePoolOfMappingUnitsForRealFile(testProfile);

			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			testProfile.getTableHeader().scanForColsProperties();

			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				ColumnProperties temp = x;
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + temp.getName(), temp);
			}
			testProfile.filterTableData();

			// When
			// debugDisplayOn("TAreportGenerator");
			// debugDisplayOn("Profile");

			testProfile.generateTAreport();
			while (isFalse(testProfile.isTAgenerationDone())) {
			}
			testProfile.captureExceptionsAfterGeneratingReport();

			// Then
			ArrayList<Object> expectedArray = new ArrayList<>();

			// testProfile.getDetailedTAReport().displayTableContentForDebugging();
			// testProfile.getSummaryTAReport().displayTableContentForDebugging();

			expectedArray.add((float)8);
			assertArrayEquals(expectedArray.toArray(),
					testProfile.getDetailedTAReport().vlookup("1", "User ID", "Total Worked Hours").toArray());
			expectedArray.clear();

			expectedArray.add((float)8);
			expectedArray.add((float)8);
			assertArrayEquals(expectedArray.toArray(),
					testProfile.getDetailedTAReport().vlookup("543", "User ID", "Total Worked Hours").toArray());
			expectedArray.clear();

			expectedArray.add((float)8);
			assertArrayEquals(expectedArray.toArray(),
					testProfile.getSummaryTAReport().vlookup("1", "User ID", "Total Worked Hours").toArray());
			expectedArray.clear();

			expectedArray.add((float)16);
			assertArrayEquals(expectedArray.toArray(),
					testProfile.getSummaryTAReport().vlookup("543", "User ID", "Total Worked Hours").toArray());
			expectedArray.clear();

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| tableDataNotInitialized | rowParameterNotHigherThanZero | headerNotScanned
				| TAreportGenerationException | columnPropertiesDoesNotExist | nullColumnPropertiesPassed
				| tableDataMethodError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void generate_TA_with_checkin_checkout() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			preparePoolOfMappingUnitsForRealFile(testProfile);

			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			testProfile.getTableHeader().scanForColsProperties();

			setMappingUnitsOfAllScannedColumns(testProfile);

			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			for (ColumnProperties x : testProfile.getTableHeader().getColsPropertiesList()) {
				testProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + x.getName(), x);
			}
			testProfile.filterTableData();

			// When
			// debugDisplayOn("TAreportGenerator");
			// debugDisplayOn("Profile");

			testProfile.dataFiltersAndSettings().inOut().attachColumn("Signaling Device of the Event");
			testProfile.dataFiltersAndSettings().inOut().addCheckIn("Turnstile 1 Reader");
			testProfile.dataFiltersAndSettings().inOut().addCheckOut("Turnstile 2 Reader");
			testProfile.dataFiltersAndSettings().inOut().addCheckOut("Turnstile 3 Reader");
			testProfile.dataFiltersAndSettings().addColToExtraTAcolDisplay("User First Name");
			testProfile.dataFiltersAndSettings().addColToExtraTAcolDisplay("User Middle Name");
			testProfile.dataFiltersAndSettings().addColToExtraTAcolDisplay("User Last Name");

			testProfile.generateTAreport();
			while (isFalse(testProfile.isTAgenerationDone())) {
			}
			testProfile.captureExceptionsAfterGeneratingReport();

			// Then
			ArrayList<Object> expectedArray = new ArrayList<>();

			// testProfile.getDetailedTAReport().displayTableContentForDebugging();
			// testProfile.getSummaryTAReport().displayTableContentForDebugging();

			expectedArray.add((float)8);
			assertArrayEquals(expectedArray.toArray(),
					testProfile.getDetailedTAReport().vlookup("1", "User ID", "Total Worked Hours").toArray());
			expectedArray.clear();

			expectedArray.add((float)8);
			expectedArray.add((float)8);
			assertArrayEquals(expectedArray.toArray(),
					testProfile.getDetailedTAReport().vlookup("543", "User ID", "Total Worked Hours").toArray());
			expectedArray.clear();

			expectedArray.add((float)8);
			assertArrayEquals(expectedArray.toArray(),
					testProfile.getSummaryTAReport().vlookup("1", "User ID", "Total Worked Hours").toArray());
			expectedArray.clear();

			expectedArray.add((float)16);
			assertArrayEquals(expectedArray.toArray(),
					testProfile.getSummaryTAReport().vlookup("543", "User ID", "Total Worked Hours").toArray());
			expectedArray.clear();

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| tableDataNotInitialized | rowParameterNotHigherThanZero | headerNotScanned
				| TAreportGenerationException | columnPropertiesDoesNotExist | nullColumnPropertiesPassed
				| tableDataMethodError e) {
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
