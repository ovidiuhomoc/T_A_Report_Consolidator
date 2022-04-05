package time_and_attendance_report_consolidator.Tests;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.Data.CSVdataSource;
import TA_Report_Tool.Data.HeaderEntry;
import TA_Report_Tool.Data.HeaderMappingField;
import TA_Report_Tool.Data.mappingType;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.Header;
import TA_Report_Tool.MainApp.Profile;
import TA_Report_Tool.MainApp.ExceptionsPack.MappingFieldDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Testing_the_Header_scanning_in_profile {

	@Test
	void Testing_the_header_scan_is_not_null() {
		Profile testProfile = new Profile();
		try {

			testProfile.setCSVConn("CSV Connection 1",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 1.csv",
					",");
			assertNotNull(testProfile.getActiveConn());
			assertNotNull(testProfile.activeHeader());

		} catch (nullNameConnection | connectionNotInitialized e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_header_change() {
		Profile testProfile = new Profile();
		Header header1 = null;
		try {
			testProfile.setCSVConn("CSV Connection 1",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 1.csv",
					",");
			testProfile.setCSVConn("CSV Connection 2",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 2.csv",
					",");

			assertNotNull(testProfile.getActiveConn());
			assertNotNull(header1 = testProfile.activeHeader());

			testProfile.setActiveConn(testProfile.getConnectionByName("CSV Connection 2"));
			assertNotEquals(header1, testProfile.activeHeader());

		} catch (nullNameConnection | connectionNotInitialized e) {
			e.printStackTrace();
		}

	}

	@Test
	void Testing_the_header_scan_in_separate_thread() {

		try {

			// Given
			Profile testProfile = new Profile();
			testProfile.setCSVConn("CSV Connection 2",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 2.csv",
					",");

			ArrayList<HeaderEntry> scanResults = null;

			// When
			testProfile.activeHeader().scan();
			while (!testProfile.activeHeader().isScanDone()) {
				// do nothing - waiting scan to be finished
			}
			scanResults = testProfile.activeHeader().getColumns();

			// Then
			assertEquals(true, testProfile.activeHeader().isScanDone());
			assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
					scanResults.toArray(new HeaderEntry[0]), stringsToHeaderEntryArray(
							new String[] { "", "\"Test1", "", "\"Test 2,2\"", "Test 3,3", "", "Test 4" })));

		} catch (nullNameConnection | connectionNotInitialized | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private HeaderEntry[] stringsToHeaderEntryArray(String[] stringArray) {
		HeaderEntry[] tempHeaderEntryArray = new HeaderEntry[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			tempHeaderEntryArray[i] = new HeaderEntry(stringArray[i], true,
					new HeaderMappingField("Not Set", null, mappingType.NotSet));
		}
		return tempHeaderEntryArray;
	}

	@Test
	void Testing_the_header_scan_with_start_row_and_column_change() {
		try {
			// Given
			Profile testProfile = new Profile();

			testProfile.setCSVConn("CSV Connection 2",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 2.csv",
					",");
			// When
			ArrayList<HeaderEntry> scanResults = null;
			testProfile.activeHeader().setHeaderStartCell(new tableCell(2, 3));
			testProfile.activeHeader().scan();

			while (!testProfile.activeHeader().isScanDone()) {
			}
			scanResults = testProfile.activeHeader().getColumns();

			// Then
			assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
					scanResults.toArray(new HeaderEntry[0]),
					stringsToHeaderEntryArray(new String[] { "\"Col 3", "", "\"\"", "", "\"Col 7\"" })));

		} catch (nullNameConnection | connectionNotInitialized | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_header_scan_no_file_found_exception_throw() {
		try {
			// Given
			Profile testProfile = new Profile();
			testProfile.setCSVConn("No CSV File Connection", "F:\\Proiecte\\No CSV File.csv", ",");

			// When
			testProfile.activeHeader().scan(); // exception not captured - passed correctly under Future Scan area
			while (!testProfile.activeHeader().isScanDone()) {
			}

			// Then
			assertThrows(ExecutionException.class, () -> {
				testProfile.activeHeader().getColumns();
			});

		} catch (nullNameConnection | connectionNotInitialized e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_header_scan_mock_random_row_content() {
		try {
			// Given
			Profile testProfile = new Profile();
			CSVdataSource mockSource = mock(CSVdataSource.class);
			Tools_Random_Generator rand = new Tools_Random_Generator();
			String randGenRow1 = rand.randomCSVrowGenerator(30, ",");
			testProfile.setCSVConn("No CSV File Connection", "F:\\Proiecte\\No CSV File.csv", ",");

			when(mockSource.getNextLine()).thenReturn(randGenRow1);
			ArrayList<HeaderEntry> scanResults = null;

			// When
			testProfile.activeHeader().scanMock(mockSource);
			scanResults = testProfile.activeHeader().getColumns();

			// Then
			assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
					scanResults.toArray(new HeaderEntry[0]), stringsToHeaderEntryArray(rand.csvGeneratedRowToArray())));

		} catch (nullNameConnection | IOException | connectionNotInitialized | InterruptedException
				| ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_header_scan_mock_random_content_second_row() {
		try {
			// Given
			Profile testProfile = new Profile();
			CSVdataSource mockSource = mock(CSVdataSource.class);
			Tools_Random_Generator rand = new Tools_Random_Generator();
			String randGenRow1 = rand.randomCSVrowGenerator(30, ",");
			String randGenRow2 = rand.randomCSVrowGenerator(30, ",");
			testProfile.setCSVConn("No CSV File Connection", "F:\\Proiecte\\No CSV File.csv", ",");

			when(mockSource.getNextLine()).thenReturn(randGenRow1).thenReturn(randGenRow2);
			ArrayList<HeaderEntry> scanResults = null;

			// When
			testProfile.activeHeader().setHeaderStartCell(new tableCell(2, 1));
			testProfile.activeHeader().scanMock(mockSource);
			scanResults = testProfile.activeHeader().getColumns();

			// Then
			assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
					scanResults.toArray(new HeaderEntry[0]), stringsToHeaderEntryArray(rand.csvGeneratedRowToArray())));

		} catch (nullNameConnection | IOException | connectionNotInitialized | InterruptedException
				| ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_header_scan_mock_no_connection_setup_Exception_throwing() {
		try {
			// Given
			Profile testProfile = new Profile();
			CSVdataSource mockSource = mock(CSVdataSource.class);
			Tools_Random_Generator rand = new Tools_Random_Generator();
			String randGenRow1 = rand.randomCSVrowGenerator(30, ",");
			when(mockSource.getNextLine()).thenReturn(randGenRow1);

			// When Then
			assertThrows(ExceptionsPack.connectionNotInitialized.class, () -> {
				testProfile.activeHeader().scanMock(mockSource);
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_header_scan_mock_set_columns_type() {
		try {
			// Given
			Profile testProfile = new Profile();
			CSVdataSource mockSource = mock(CSVdataSource.class);
			Tools_Random_Generator rand = new Tools_Random_Generator();
			String randGenRow1 = rand.randomCSVrowGenerator(30, ",");
			testProfile.setCSVConn("No CSV File Connection", "F:\\Proiecte\\No CSV File.csv", ",");

			when(mockSource.getNextLine()).thenReturn(randGenRow1);
			// When
			testProfile.activeHeader().scanMock(mockSource);
			String columnName = rand.csvGeneratedRowToArray()[0];
			HeaderMappingField newMappingField = testProfile.getHeaderMapping()
					.getHeaderMappingFieldByType(mappingType.EmployeeUniqueId);
			testProfile.activeHeader().setMappingTypeOfColumnWithName(columnName, newMappingField);

			// Then
			assertEquals(mappingType.EmployeeUniqueId,testProfile.activeHeader().getColumns().get(0).getHeaderMappingField().getType());

		} catch (nullNameConnection | IOException | connectionNotInitialized | InterruptedException
				| ExecutionException | MappingFieldDoesNotExist e) {
			e.printStackTrace();
		}
	}
}
