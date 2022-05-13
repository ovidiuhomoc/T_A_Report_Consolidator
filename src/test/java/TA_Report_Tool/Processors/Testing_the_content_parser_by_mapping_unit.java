package TA_Report_Tool.Processors;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.Data.CSVdataSource;
import TA_Report_Tool.Data.MappingCollection;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.Data.TableHeader;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.MainApp.ExceptionsPack.cantBeParsedWithCurrentMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.cantParseEmptyStringForCurrentType;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.searchCantFindMappingUnitInCollection;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.parsingFailedDueToNullMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.headerNotScanned;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_content_parser_by_mapping_unit {

	@Test
	void Testing_the_creation_of_new_content_parser_object() {
		try {// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("Null Connection", "Null Connection");
			TableHeader testProfileTableHeader = testProfile.getTableHeader();

			// When
			ContentParserByMappingUnit<?> contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());

			// Then
			assertNotNull(contentParser);
		} catch (connectionNotInitialized | nullNameConnection | nullArgument | InterruptedException | ExecutionException | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_a_date_content() {
		try {// Given
			Profile testProfile = new Profile("Test Profile");
			MappingCollection testProfileMappingCollection = testProfile.getMappingCollection();
			testProfile.initializeCSVConn("Null Connection", "Null Connection");

			CSVdataSource mockSource = mock(CSVdataSource.class);
			testProfile.getTableHeader().scanForColsPropertiesMock(mockSource);
			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Signaling Device,Employee Unique ID,Employee Full Name,Custom Text 2ch & 2 digits");
			testProfile.getTableHeader().scanForColsPropertiesMock(mockSource);

			TableHeader testProfileTableHeader = testProfile.getTableHeader();
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Date2",
					new MaskTemplate().addDDay().addSep("/").addMMonth().addSep("/").addYYYYear(), MappingType.Date));
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Date3",
					new MaskTemplate().addDDay().addSep("-").addMMonth().addSep("-").addYYYYear(), MappingType.Date));
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Date4",
					new MaskTemplate().addDDay().addSep(" ").addMMonth().addSep(" ").addYYYYear(), MappingType.Date));
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Date5",
					new MaskTemplate().addDDay().addSep("--").addMMonth().addSep("--").addYYYYear(), MappingType.Date));

			// When
			String columnName = "Date";
			String mappingField = "Date1";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));
			ContentParserByMappingUnit<?> contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
			String stringToBeParsed = "25.11.2022";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When 2
			columnName = "Date";
			mappingField = "Date2";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));
			stringToBeParsed = "25/11/2022";
			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then 2
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When 3
			columnName = "Date";
			mappingField = "Date3";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));
			contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
			stringToBeParsed = "25-11-2022";
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then 3
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When 4
			columnName = "Date";
			mappingField = "Date4";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));
			stringToBeParsed = "25 11 2022";
			formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
			expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then 4
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When 5
			columnName = "Date";
			mappingField = "Date5";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));
			stringToBeParsed = "25--11--2022";
			formatter = DateTimeFormatter.ofPattern("dd--MM--yyyy");
			expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then 5
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When6

			String columnNameForException = "Date";
			String stringToBeParsedForException = "25-11-2022";

			// Then6
			assertThrows(ExceptionsPack.cantBeParsedWithCurrentMappingMask.class, () -> {
				ContentParserByMappingUnit<?> contentParser2 = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
				contentParser2.parse(stringToBeParsedForException, columnNameForException);
			});

		} catch (nullNameConnection | IOException | InterruptedException | ExecutionException | searchCantFindMappingUnitInCollection
				| nullColumnPropertiesPassed | columnPropertiesDoesNotExist | cantBeParsedWithCurrentMappingMask
				| parsingFailedDueToNullMappingMask | connectionNotInitialized | nullArgument | dateOrTimeMissing | headerNotScanned | cantParseEmptyStringForCurrentType e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_a_time_content() {
		try {
			Profile testProfile = new Profile("Test Profile");
			MappingCollection testProfileMappingCollection = testProfile.getMappingCollection();
			testProfile.initializeCSVConn("Null Connection", "Null Connection");

			TableHeader testProfileTableHeader = testProfile.getTableHeader();
			CSVdataSource mockSource = mock(CSVdataSource.class);

			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Signaling Device,Employee Unique ID,Employee Full Name,Custom Text 2ch & 2 digits");
			testProfileTableHeader.scanForColsPropertiesMock(mockSource);

			// When
			String columnName = "Time";
			String mappingField = "Time1";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			ContentParserByMappingUnit<?> contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
			String stringToBeParsed = "11:59:59 AM";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
			LocalTime expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When2
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Time2", new MaskTemplate().addhour()
					.addSep(":").addminute().addSep(":").addsecond().addSep(" ").markAMPMTime(), MappingType.Time));

			columnName = "Time";
			mappingField = "Time2";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "11:59:59 AM";
			formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
			expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then2
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When3
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Time3",
					new MaskTemplate().addhhour().addSep(":").addmminute().addSep(":").addsecond().mark24hTime(),
					MappingType.Time));

			columnName = "Time";
			mappingField = "Time3";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "23:59:59";
			formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then3
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When4
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Time4",
					new MaskTemplate().addhhour().addSep(":").addmminute().mark24hTime(), MappingType.Time));

			columnName = "Time";
			mappingField = "Time4";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "23:59";
			formatter = DateTimeFormatter.ofPattern("HH:mm");
			expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then4
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When5
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Time5",
					new MaskTemplate().addhour().addSep(" ").addminute().mark24hTime(), MappingType.Time));

			columnName = "Time";
			mappingField = "Time5";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "3 59";
			formatter = DateTimeFormatter.ofPattern("H mm");
			expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then5
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When6

			String columnNameForException = "Time";
			String stringToBeParsedForException = "3 59 AM";

			// Then6
			assertThrows(ExceptionsPack.cantBeParsedWithCurrentMappingMask.class, () -> {
				ContentParserByMappingUnit<?> contentParser2 = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
				contentParser2.parse(stringToBeParsedForException, columnNameForException);
			});

		} catch (nullNameConnection | connectionNotInitialized | IOException | InterruptedException | ExecutionException
				| searchCantFindMappingUnitInCollection | nullColumnPropertiesPassed | columnPropertiesDoesNotExist
				| cantBeParsedWithCurrentMappingMask | parsingFailedDueToNullMappingMask | nullArgument | dateOrTimeMissing | headerNotScanned | cantParseEmptyStringForCurrentType e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_a_date_time_content() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			testProfile.initializeCSVConn("Null Connection", "Null Connection");

			TableHeader testProfileTableHeader = testProfile.getTableHeader();
			CSVdataSource mockSource = mock(CSVdataSource.class);

			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Signaling Device,Employee Unique ID,Employee Full Name,Custom Text 2ch & 2 digits");
			testProfileTableHeader.scanForColsPropertiesMock(mockSource);

			// When
			String columnName = "Date and Time";
			String mappingField = "DateAndTime1";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			ContentParserByMappingUnit<?> contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
			String stringToBeParsed = "31.12.2022 11:59:59 AM";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy h:m:ss a");
			LocalDateTime expectedTime = LocalDateTime.parse(stringToBeParsed, formatter);

			// Then
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When2

			String columnNameForException = "Date and Time";
			String stringToBeParsedForException = "31.12.2022 11.59.59";

			// Then2
			assertThrows(ExceptionsPack.cantBeParsedWithCurrentMappingMask.class, () -> {
				ContentParserByMappingUnit<?> contentParser2 = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
				contentParser2.parse(stringToBeParsedForException, columnNameForException);
			});

		} catch (nullNameConnection | connectionNotInitialized | IOException | InterruptedException | ExecutionException
				| searchCantFindMappingUnitInCollection | nullColumnPropertiesPassed | columnPropertiesDoesNotExist
				| cantBeParsedWithCurrentMappingMask | parsingFailedDueToNullMappingMask | nullArgument | dateOrTimeMissing | headerNotScanned | cantParseEmptyStringForCurrentType e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_a_fixed_digit_numbers() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			MappingCollection testProfileMappingCollection = testProfile.getMappingCollection();
			testProfile.initializeCSVConn("Null Connection", "Null Connection");

			TableHeader testProfileTableHeader = testProfile.getTableHeader();
			CSVdataSource mockSource = mock(CSVdataSource.class);

			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Signaling Device,Employee Unique ID,Employee Full Name,Custom Text 2ch & 2 digits");
			testProfileTableHeader.scanForColsPropertiesMock(mockSource);
			testProfileMappingCollection.addMappingUnit(new MappingUnit("2digitNumber",
					new MaskTemplate().addSingleInt().addSingleInt(), MappingType.FixedDigitNumber));

			// When
			String columnName = "Event Severity Category";
			String mappingField = "2digitNumber";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			ContentParserByMappingUnit<?> contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
			String stringToBeParsed = "03";

			// Then
			assertEquals(3, (Integer) contentParser.parse(stringToBeParsed, columnName));

			// When2
			stringToBeParsed = "99";

			// Then2
			assertEquals(99, (Integer) contentParser.parse(stringToBeParsed, columnName));

			// When3
			String stringToBeParsedForException = "999";

			// Then3
			assertThrows(ExceptionsPack.cantBeParsedWithCurrentMappingMask.class, () -> {
				new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList())
						.parse(stringToBeParsedForException, columnName);
			});

		} catch (nullNameConnection | connectionNotInitialized | IOException | InterruptedException | ExecutionException
				| searchCantFindMappingUnitInCollection | nullColumnPropertiesPassed | columnPropertiesDoesNotExist
				| cantBeParsedWithCurrentMappingMask | parsingFailedDueToNullMappingMask | nullArgument | dateOrTimeMissing | headerNotScanned | cantParseEmptyStringForCurrentType e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_a_large_number() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			MappingCollection testProfileMappingCollection = testProfile.getMappingCollection();
			testProfile.initializeCSVConn("Null Connection", "Null Connection");

			TableHeader testProfileTableHeader = testProfile.getTableHeader();
			CSVdataSource mockSource = mock(CSVdataSource.class);

			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Event Count,Signaling Device,Employee Unique ID,Employee Full Name,Custom Text 2ch & 2 digits");
			testProfileTableHeader.scanForColsPropertiesMock(mockSource);
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Large Number",
					new MaskTemplate().addNumber(), MappingType.FixedDigitNumber));

			// When
			String columnName = "Event Count";
			String mappingField = "Large Number";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			ContentParserByMappingUnit<?> contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());
			String stringToBeParsed = "1234567890";

			// Then
			assertEquals(1234567890, (Integer) contentParser.parse(stringToBeParsed, columnName));

			// When2
			String stringToBeParsedForException = "1234567890123";

			// Then2
			assertThrows(ExceptionsPack.cantBeParsedWithCurrentMappingMask.class, () -> {
				new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList())
						.parse(stringToBeParsedForException, columnName);
			});

		} catch (nullNameConnection | connectionNotInitialized | IOException | InterruptedException | ExecutionException
				| searchCantFindMappingUnitInCollection | nullColumnPropertiesPassed | columnPropertiesDoesNotExist
				| cantBeParsedWithCurrentMappingMask | parsingFailedDueToNullMappingMask | nullArgument | dateOrTimeMissing | headerNotScanned | cantParseEmptyStringForCurrentType e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_string_columns() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			MappingCollection testProfileMappingCollection = testProfile.getMappingCollection();
			testProfile.initializeCSVConn("Null Connection", "Null Connection");

			TableHeader testProfileTableHeader = testProfile.getTableHeader();
			CSVdataSource mockSource = mock(CSVdataSource.class);

			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Event Count,Signaling Device,Employee Unique ID,FirstName,MiddleName,LastName,Employee Full Name,Custom Text 2ch & 2 digits");
			testProfileTableHeader.scanForColsPropertiesMock(mockSource);

			ContentParserByMappingUnit<?> contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());

			// When1
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Signaling Device",
					new MaskTemplate().addAnyString(), MappingType.SignalingDevice));

			String columnName = "Signaling Device";
			String mappingField = "Signaling Device";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			String stringToBeParsed = "Front Entry door floor #5";

			// Then1
			assertEquals("Front Entry door floor #5", contentParser.parse(stringToBeParsed, columnName));

			// When2
			columnName = "Employee Unique ID";
			mappingField = "Employee Unique ID";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "UID12345678";

			// Then2
			assertEquals("UID12345678", contentParser.parse(stringToBeParsed, columnName));

			// When3
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Employee First Name",
					new MaskTemplate().addAnyString(), MappingType.EmployeeFirstName));

			columnName = "FirstName";
			mappingField = "Employee First Name";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "John";

			// Then3
			assertEquals("John", contentParser.parse(stringToBeParsed, columnName));

			// When4
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Employee Middle Name",
					new MaskTemplate().addAnyString(), MappingType.EmployeeMiddleName));

			columnName = "MiddleName";
			mappingField = "Employee Middle Name";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "";

			// Then4
			assertEquals("", contentParser.parse(stringToBeParsed, columnName));

			// When5
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Employee Last Name",
					new MaskTemplate().addAnyString(), MappingType.EmployeeLastName));

			columnName = "LastName";
			mappingField = "Employee Last Name";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "Doe";

			// Then5
			assertEquals("Doe", contentParser.parse(stringToBeParsed, columnName));

			// When6
			testProfileMappingCollection.addMappingUnit(new MappingUnit("Employee Full Name",
					new MaskTemplate().addAnyString(), MappingType.EmployeeLastName));

			columnName = "Employee Full Name";
			mappingField = "Employee Full Name";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "John Doe";

			// Then6
			assertEquals("John Doe", contentParser.parse(stringToBeParsed, columnName));

			// When7
			testProfileMappingCollection.addMappingUnit(
					new MappingUnit("Event", new MaskTemplate().addAnyString(), MappingType.Event));

			columnName = "Event";
			mappingField = "Event";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			stringToBeParsed = "Access Granted, valid card & valid PIN";

			// Then7
			assertEquals("Access Granted, valid card & valid PIN", contentParser.parse(stringToBeParsed, columnName));

		} catch (nullNameConnection | connectionNotInitialized | IOException | InterruptedException | ExecutionException
				| searchCantFindMappingUnitInCollection | nullColumnPropertiesPassed | columnPropertiesDoesNotExist
				| cantBeParsedWithCurrentMappingMask | parsingFailedDueToNullMappingMask | nullArgument | dateOrTimeMissing | headerNotScanned | cantParseEmptyStringForCurrentType e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_custom_columns() {
		try {
			// Given
			Profile testProfile = new Profile("Test Profile");
			MappingCollection testProfileMappingCollection = testProfile.getMappingCollection();
			testProfile.initializeCSVConn("Null Connection", "Null Connection");

			TableHeader testProfileTableHeader = testProfile.getTableHeader();
			CSVdataSource mockSource = mock(CSVdataSource.class);

			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Signaling Device,Employee Unique ID,Employee Full Name,Custom Text 2ch - 2 digits - 1 special ch - space - 2 digits");
			testProfileTableHeader.scanForColsPropertiesMock(mockSource);

			ContentParserByMappingUnit<?> contentParser = new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList());

			// When1
			testProfileMappingCollection.addMappingUnit(new MappingUnit(
					"Custom Field 1", new MaskTemplate().addSingleCh().addSingleCh().addSingleInt().addSingleInt()
							.addSingleSpecialCh().addSep(" ").addSingleInt().addSingleInt(),
					MappingType.CustomFieldText));

			String columnName = "Custom Text 2ch - 2 digits - 1 special ch - space - 2 digits";
			String mappingField = "Custom Field 1";

			testProfileTableHeader.changeMappingUnitOfColumnWithName(columnName,
					testProfile.getMappingCollection().getMappingUnitdByName(mappingField));

			String stringToBeParsed = "Ch12! 12";

			// Then1
			assertEquals("Ch12! 12", contentParser.parse(stringToBeParsed, columnName));

			// When2
			String stringToBeParsedForException = "Ch12! 1g";

			// Then2
			assertThrows(ExceptionsPack.cantBeParsedWithCurrentMappingMask.class, () -> {
				new ContentParserByMappingUnit<>(testProfileTableHeader.getColsPropertiesList())
						.parse(stringToBeParsedForException, columnName);
			});

		} catch (nullNameConnection | connectionNotInitialized | IOException | InterruptedException | ExecutionException
				| searchCantFindMappingUnitInCollection | nullColumnPropertiesPassed | columnPropertiesDoesNotExist
				| cantBeParsedWithCurrentMappingMask | parsingFailedDueToNullMappingMask | nullArgument | dateOrTimeMissing | headerNotScanned | cantParseEmptyStringForCurrentType e) {
			e.printStackTrace();
		}
	}

}
