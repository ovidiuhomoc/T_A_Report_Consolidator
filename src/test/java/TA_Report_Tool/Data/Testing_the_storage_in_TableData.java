package TA_Report_Tool.Data;

import static TA_Report_Tool.Tools.check.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.cantBeParsedWithCurrentMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.cantParseEmptyStringForCurrentType;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.headerNotScanned;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.nullDataReceivedForParsing;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.MainApp.ExceptionsPack.parsingFailedDueToNullMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.rowParameterNotHigherThanZero;
import TA_Report_Tool.MainApp.ExceptionsPack.searchCantFindMappingUnitInCollection;
import TA_Report_Tool.MainApp.ExceptionsPack.tableDataNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.tableLengthAndDataLengthNotMatching;
import TA_Report_Tool.MainApp.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_storage_in_TableData {

	@Test
	void TableData_created_on_profile_creation_and_initialized_upon_scanning() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("Mock CSV Connection", "No path towards file as internal mock will be used");

			// When
			DataSource mockedDataSource = mock(DataSource.class);
			when(mockedDataSource.getNextLine()).thenReturn("Date in Table, Time in Table,Employee Unique Id in Table");

			// Then
			assertTrue(isNotNull(testProfile.getTableData()));

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void TableData_initialization_exception_throw() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("Mock CSV Connection", "No path towards file as internal mock will be used");

			// When
			DataSource mockedDataSource = mock(DataSource.class);
			when(mockedDataSource.getNextLine()).thenReturn("Date in Table, Time in Table,Employee Unique Id in Table");
			String[] stringForException = { "Date", "Time", "Employee" };

			// Then
			assertThrows(ExceptionsPack.tableDataNotInitialized.class, () -> {
				testProfile.getTableData().parseAndStoreOneDataRow(stringForException);
			});

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void Successful_data_structures_initialization_during_new_header_scan() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("Mock CSV Connection", "No path towards file as internal mock will be used");

			MappingUnit date = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Date);
			MappingUnit time = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Time);
			MappingUnit empID = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.EmployeeUniqueId);
			CSVdataSource mockedDataSource = mock(CSVdataSource.class);
			when(mockedDataSource.getNextLine()).thenReturn("Date in Table,Time in Table,Employee Unique Id in Table");

			// When

			// 1st
			testProfile.getTableHeader().scanForColsPropertiesMock(mockedDataSource);
			while (isFalse(testProfile.getTableHeader().isScanDone())) {
				// Do nothing until the scan is finished
				// Next instruction will need the scan to be finished. If it is not finished
				// will hold everything until scan is finished.
			}

			// 2nd
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Date in Table", date);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Time in Table", time);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Employee Unique Id in Table", empID);

			// Then
			assertTrue(testProfile.getTableData().containsColumnWithMappingUnit(date));
			assertTrue(testProfile.getTableData().containsColumnWithMappingUnit(time));
			assertTrue(testProfile.getTableData().containsColumnWithMappingUnit(empID));
			assertTrue(testProfile.getTableData().containsColumnWithMappingType(MappingType.Date));
			assertTrue(testProfile.getTableData().containsColumnWithMappingType(MappingType.Time));
			assertTrue(testProfile.getTableData().containsColumnWithMappingType(MappingType.EmployeeUniqueId));

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | IOException | searchCantFindMappingUnitInCollection
				| headerNotScanned e) {
			e.printStackTrace();
		}
	}

	@Test
	void Successful_data_storage_without_thread() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("Mock CSV Connection", "No path towards file as internal mock will be used");

			MappingUnit date = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Date);
			MappingUnit time = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Time);
			MappingUnit empID = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.EmployeeUniqueId);

			MappingUnit fullName = new MappingUnit("Full Name MU", new MaskTemplate().addAnyString(),
					MappingType.EmployeeFullName);
			testProfile.getMappingCollection().addMappingUnit(fullName);

			CSVdataSource mockedDataSource = mock(CSVdataSource.class);
			when(mockedDataSource.getNextLine())
					.thenReturn("Date in Table,Time in Table,Employee Unique Id in Table,Full Name");

			// 1st
			testProfile.getTableHeader().scanForColsPropertiesMock(mockedDataSource);
			// 2nd
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Date in Table", date);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Time in Table", time);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Employee Unique Id in Table", empID);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Full Name", fullName);

			// When
			String[] stringToBeParsed = { "14.04.2022", "11:30:59 AM", "E123456", "John Doe" };
			testProfile.getTableData().parseAndStoreOneDataRow(stringToBeParsed);

			// Then
			assertEquals(1, testProfile.getTableData().getRowCount());

			LocalDateTime expectedDateTimeResult = LocalDateTime.parse(stringToBeParsed[0] + " " + stringToBeParsed[1],
					DateTimeFormatter.ofPattern("d.M.yyyy h:m:ss a"));
			LocalDate expectedDateResult = LocalDate.parse(stringToBeParsed[0],
					DateTimeFormatter.ofPattern("d.MM.yyyy"));
			LocalTime expectedTimeResult = LocalTime.parse(stringToBeParsed[1],
					DateTimeFormatter.ofPattern("h:m:ss a"));

			assertEquals(expectedDateTimeResult, testProfile.getTableData().getDateTimeOfRow(1));

			assertEquals(expectedDateResult, testProfile.getTableData().getDataOfColAtRow("Date in Table", 1));
			assertEquals(expectedTimeResult, testProfile.getTableData().getDataOfColAtRow("Time in Table", 1));
			assertEquals(stringToBeParsed[2],
					testProfile.getTableData().getDataOfColAtRow("Employee Unique Id in Table", 1));
			assertEquals(stringToBeParsed[3], testProfile.getTableData().getDataOfColAtRow("Full Name", 1));

			String[] expectedStringArray = Arrays.asList(stringToBeParsed).subList(2, 4).toArray(new String[0]);
			assertArrayEquals(expectedStringArray, testProfile.getTableData().rowToArrayExceptDateAndTime(1));

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | IOException | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableLengthAndDataLengthNotMatching | nullDataReceivedForParsing
				| nullColumnPropertiesPassed | columnPropertiesDoesNotExist | cantBeParsedWithCurrentMappingMask
				| parsingFailedDueToNullMappingMask | rowParameterNotHigherThanZero | tableDataNotInitialized
				| cantParseEmptyStringForCurrentType e) {
			e.printStackTrace();
		}
	}

	@Test
	void Successful_data_storage_within_thread_from_real_file() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("CSV Example File Connection",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\TableSource Example.csv");

			// preparation of the pool of Mapping Units to be used later

			MappingUnit date = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Date);
			MappingUnit time = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Time);
			MappingUnit empID = testProfile.getMappingCollection().getMappingUnitdByType(MappingType.EmployeeUniqueId);

			MappingUnit signalingDev = new MappingUnit("Signaling Device MU", new MaskTemplate().addAnyString(),
					MappingType.SignalingDevice);
			testProfile.getMappingCollection().addMappingUnit(signalingDev);

			MappingUnit firstName = new MappingUnit("First Name MU", new MaskTemplate().addAnyString(),
					MappingType.EmployeeFirstName);
			testProfile.getMappingCollection().addMappingUnit(firstName);

			MappingUnit lastName = new MappingUnit("Last Name MU", new MaskTemplate().addAnyString(),
					MappingType.EmployeeLastName);
			testProfile.getMappingCollection().addMappingUnit(lastName);

			MappingUnit middleName = new MappingUnit("Emp. Middle Name MU", new MaskTemplate().addAnyString(),
					MappingType.EmployeeMiddleName);
			testProfile.getMappingCollection().addMappingUnit(middleName);

			MappingUnit event = new MappingUnit("Event MU", new MaskTemplate().addAnyString(), MappingType.Event);
			testProfile.getMappingCollection().addMappingUnit(event);

			MappingUnit eventId = new MappingUnit("Event ID MU", new MaskTemplate().addNumber(), MappingType.Number);
			testProfile.getMappingCollection().addMappingUnit(eventId);

			MappingUnit customF1 = new MappingUnit("Custom Field 1 MU", new MaskTemplate().addAnyString(),
					MappingType.CustomFieldText);
			testProfile.getMappingCollection().addMappingUnit(customF1);

			// scan the file for header & column properties
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(3, 2));
			// 1st step
			testProfile.getTableHeader().scanForColsProperties();
			// 2nd step: map all columns with their type from the pool of MappingUnits
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Event ID", eventId);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Event Date", date);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Event Time", time);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Signaling Device of the Event",
					signalingDev);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("User ID", empID);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("User First Name", firstName);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("User Last Name", lastName);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("User Middle Name", middleName);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Event", event);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Custom defined field 1", customF1);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName("Custom defined field 2", customF1);

			// When
			testProfile.scanAndStoreTableContent();
			while (isFalse(testProfile.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			testProfile.captureExceptionsAfterScanning();

			// Then
			// testProfile.getTableData().displayColumnPropertiesForDebugging();
			// testProfile.getTableData().displayTableContentForDebugging();
			assertEquals(7, testProfile.getTableData().getRowCount());

			for (int i = 1; i <= 7; i++) {
				assertEquals(i, testProfile.getTableData().getDataOfColAtRow("Event ID", i));
			}

			for (int i = 1; i <= 7; i++) {
				if (i < 3) {
					assertEquals(LocalDate.of(2022, 3, 2),
							testProfile.getTableData().getDataOfColAtRow("Event Date", i));
				} else {
					assertEquals(LocalDate.of(2022, 3, 3),
							testProfile.getTableData().getDataOfColAtRow("Event Date", i));
				}
			}

			ArrayList<LocalTime> expectedList = new ArrayList<>();
			expectedList.add(LocalTime.of(9, 0));
			expectedList.add(LocalTime.of(17, 0));
			expectedList.add(LocalTime.of(9, 0));
			expectedList.add(LocalTime.of(9, 0));
			expectedList.add(LocalTime.of(13, 0));
			expectedList.add(LocalTime.of(17, 0));
			expectedList.add(LocalTime.of(17, 0));
			assertArrayEquals(expectedList.toArray(), testProfile.getTableData().getDataOfCol("Event Time"));

			ArrayList<String> expectedList2 = new ArrayList<>();
			expectedList2.add("Turnstile 1 Reader");
			expectedList2.add("Turnstile 1 Reader");
			expectedList2.add("Turnstile 1 Reader");
			expectedList2.add("Turnstile 1 Reader");
			expectedList2.add("IT Room Door");
			expectedList2.add("Turnstile 2 Reader");
			expectedList2.add("Turnstile 3 Reader");

			assertArrayEquals(expectedList2.toArray(),
					testProfile.getTableData().getDataOfCol("Signaling Device of the Event"));

			ArrayList<String> expectedList3 = new ArrayList<>();
			expectedList3.add("543");
			expectedList3.add("543");
			expectedList3.add("543");
			expectedList3.add("1");
			expectedList3.add("543");
			expectedList3.add("543");
			expectedList3.add("1");

			assertArrayEquals(expectedList3.toArray(), testProfile.getTableData().getDataOfCol("User ID"));

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero e) {
			e.printStackTrace();
		}
	}

	@Disabled("Not implemented yet the data harvesting & testing from a mock instead of real file")
	@Test
	void Successful_data_storage_within_thread_from_mock_source() {
	}
}
