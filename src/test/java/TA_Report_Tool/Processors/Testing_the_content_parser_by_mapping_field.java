package TA_Report_Tool.Processors;

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
import TA_Report_Tool.Data.HeaderMapping;
import TA_Report_Tool.Data.HeaderMappingField;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.Data.mappingType;
import TA_Report_Tool.MainApp.ExceptionsPack.CantBeParsedWithCurrentMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.HeaderColumnDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.MappingFieldDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.ParsingFailedDueToNullMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.SearchedHeaderColumnIsNull;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.MainApp.Header;
import TA_Report_Tool.MainApp.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Testing_the_content_parser_by_mapping_field {

	@Test
	void Testing_the_creation_of_new_content_parser_object() {
		try {// Given
			Profile testProfile = new Profile("Test Profile");
			HeaderMapping testProfileHeaderMapping = testProfile.getHeaderMapping();
			testProfile.setCSVConn("Null Connection", "Null Connection");
			Header testProfileHeader = testProfile.activeHeader();

			// When
			ContentParserByMappingField contentParser = new ContentParserByMappingField(testProfileHeaderMapping,
					testProfileHeader);

			// Then
			assertNotNull(contentParser);
		} catch (connectionNotInitialized | nullNameConnection e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_a_date_content() {
		try {// Given
			Profile testProfile = new Profile("Test Profile");
			HeaderMapping testProfileHeaderMapping = testProfile.getHeaderMapping();
			testProfile.setCSVConn("Null Connection", "Null Connection");

			CSVdataSource mockSource = mock(CSVdataSource.class);
			testProfile.activeHeader().scanMock(mockSource);
			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Signaling Device,Employee Unique ID,Employee Full Name,Custom Text 2ch & 2 digits");
			testProfile.activeHeader().scanMock(mockSource);

			Header testProfileHeader = testProfile.activeHeader();
			testProfileHeaderMapping.addMappingField(new HeaderMappingField("Date2",
					new MaskTemplate().addDDay().addSep("/").addMMonth().addSep("/").addYYYYear(), mappingType.Date));
			testProfileHeaderMapping.addMappingField(new HeaderMappingField("Date3",
					new MaskTemplate().addDDay().addSep("-").addMMonth().addSep("-").addYYYYear(), mappingType.Date));
			testProfileHeaderMapping.addMappingField(new HeaderMappingField("Date4",
					new MaskTemplate().addDDay().addSep(" ").addMMonth().addSep(" ").addYYYYear(), mappingType.Date));
			testProfileHeaderMapping.addMappingField(new HeaderMappingField("Date5",
					new MaskTemplate().addDDay().addSep("--").addMMonth().addSep("--").addYYYYear(), mappingType.Date));

			// When
			String columnName = "Date";
			String mappingField = "Date1";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));
			ContentParserByMappingField contentParser = new ContentParserByMappingField(testProfileHeaderMapping,
					testProfileHeader);
			String stringToBeParsed = "25.11.2022";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When 2
			columnName = "Date";
			mappingField = "Date2";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));
			contentParser = new ContentParserByMappingField(testProfileHeaderMapping, testProfileHeader);
			stringToBeParsed = "25/11/2022";
			formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then 2
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When 3
			columnName = "Date";
			mappingField = "Date3";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));
			contentParser = new ContentParserByMappingField(testProfileHeaderMapping, testProfileHeader);
			stringToBeParsed = "25-11-2022";
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then 3
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When 4
			columnName = "Date";
			mappingField = "Date4";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));
			contentParser = new ContentParserByMappingField(testProfileHeaderMapping, testProfileHeader);
			stringToBeParsed = "25 11 2022";
			formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
			expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then 4
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

			// When 5
			columnName = "Date";
			mappingField = "Date5";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));
			contentParser = new ContentParserByMappingField(testProfileHeaderMapping, testProfileHeader);
			stringToBeParsed = "25--11--2022";
			formatter = DateTimeFormatter.ofPattern("dd--MM--yyyy");
			expectedDate = LocalDate.parse(stringToBeParsed, formatter);

			// Then 5
			assertEquals(expectedDate, contentParser.parse(stringToBeParsed, columnName));

		} catch (nullNameConnection | IOException | InterruptedException | ExecutionException | MappingFieldDoesNotExist
				| SearchedHeaderColumnIsNull | HeaderColumnDoesNotExist | CantBeParsedWithCurrentMappingMask
				| ParsingFailedDueToNullMappingMask | connectionNotInitialized e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_parsing_of_a_time_content() {
		try {
			Profile testProfile = new Profile("Test Profile");
			HeaderMapping testProfileHeaderMapping = testProfile.getHeaderMapping();
			testProfile.setCSVConn("Null Connection", "Null Connection");

			Header testProfileHeader = testProfile.activeHeader();
			CSVdataSource mockSource = mock(CSVdataSource.class);

			when(mockSource.getNextLine()).thenReturn(
					"Event Id,Date,Time,Date and Time,Event Severity Category,Event,Signaling Device,Employee Unique ID,Employee Full Name,Custom Text 2ch & 2 digits");
			testProfileHeader.scanMock(mockSource);

			// When
			String columnName = "Time";
			String mappingField = "Time1";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));

			ContentParserByMappingField contentParser = new ContentParserByMappingField(testProfileHeaderMapping,
					testProfileHeader);
			String stringToBeParsed = "11:59:59 AM";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
			LocalTime expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When2
			testProfileHeaderMapping.addMappingField(new HeaderMappingField("Time2", new MaskTemplate().addhour()
					.addSep(":").addminute().addSep(":").addsecond().addSep(" ").markAMPMTime(), mappingType.Time));

			columnName = "Time";
			mappingField = "Time2";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));

			contentParser = new ContentParserByMappingField(testProfileHeaderMapping, testProfileHeader);
			stringToBeParsed = "11:59:59 AM";
			formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
			expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then2
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When3
			testProfileHeaderMapping.addMappingField(new HeaderMappingField("Time3",
					new MaskTemplate().addhhour().addSep(":").addmminute().addSep(":").addsecond().mark24hTime(),
					mappingType.Time));

			columnName = "Time";
			mappingField = "Time3";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));

			contentParser = new ContentParserByMappingField(testProfileHeaderMapping, testProfileHeader);
			stringToBeParsed = "23:59:59";
			formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then3
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When4
			testProfileHeaderMapping.addMappingField(new HeaderMappingField("Time4",
					new MaskTemplate().addhhour().addSep(":").addmminute().mark24hTime(), mappingType.Time));

			columnName = "Time";
			mappingField = "Time4";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));

			contentParser = new ContentParserByMappingField(testProfileHeaderMapping, testProfileHeader);
			stringToBeParsed = "23:59";
			formatter = DateTimeFormatter.ofPattern("HH:mm");
			expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then4
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

			// When5
			testProfileHeaderMapping.addMappingField(new HeaderMappingField("Time5",
					new MaskTemplate().addhour().addSep(" ").addminute().mark24hTime(), mappingType.Time));

			columnName = "Time";
			mappingField = "Time5";

			testProfileHeader.setMappingTypeOfColumnWithName(columnName,
					testProfile.getHeaderMapping().getHeaderMappingFieldByName(mappingField));

			contentParser = new ContentParserByMappingField(testProfileHeaderMapping, testProfileHeader);
			stringToBeParsed = "3 59";
			formatter = DateTimeFormatter.ofPattern("H mm");
			expectedTime = LocalTime.parse(stringToBeParsed, formatter);

			// Then5
			assertEquals(expectedTime, contentParser.parse(stringToBeParsed, columnName));

		} catch (nullNameConnection | connectionNotInitialized | IOException | InterruptedException | ExecutionException
				| MappingFieldDoesNotExist | SearchedHeaderColumnIsNull | HeaderColumnDoesNotExist
				| CantBeParsedWithCurrentMappingMask | ParsingFailedDueToNullMappingMask e) {
			e.printStackTrace();
		}
	}
}
