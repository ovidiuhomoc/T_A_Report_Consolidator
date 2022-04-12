package TA_Report_Tool.Tests;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.Data.CSVdataSource;
import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.Data.TableHeader;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.mappingUnitDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.MainApp.Profile;
import TA_Report_Tool.Tools_for_Tests.Tools_Random_Generator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_TableHeader_scanning_in_profile {

	@Test
	void Testing_the_Table_Header_scan_is_not_null() {
		try {
			Profile testProfile = new Profile();
			testProfile.initializeCSVConn("CSV Connection 1",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 1.csv",
					",");
			assertNotNull(testProfile.getActiveConn());
			assertNotNull(testProfile.getTableHeader());

		} catch (nullNameConnection | connectionNotInitialized | nullArgument | InterruptedException
				| ExecutionException | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_Table_Header_change() {
		try {
			Profile testProfile = new Profile();
			TableHeader header1 = null;
			testProfile.initializeCSVConn("CSV Connection 1",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 1.csv",
					",");
			testProfile.initializeCSVConn("CSV Connection 2",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 2.csv",
					",");

			assertNotNull(testProfile.getActiveConn());
			assertNotNull(header1 = testProfile.getTableHeader());

			testProfile.setActiveConn(testProfile.getConnectionByName("CSV Connection 2"));
			assertNotEquals(header1, testProfile.getTableHeader());

		} catch (nullNameConnection | connectionNotInitialized | nullArgument | InterruptedException
				| ExecutionException | dateOrTimeMissing e) {
			e.printStackTrace();
		}

	}

	@Test
	void Testing_the_Table_Header_scan_in_separate_thread() {
		try {
			// Given
			Profile testProfile = new Profile();
			testProfile.initializeCSVConn("CSV Connection 2",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 2.csv",
					",");

			ArrayList<ColumnProperties> scanResults = null;

			// When
			testProfile.getTableHeader().scanForColsProperties();
			while (!testProfile.getTableHeader().isScanDone()) {
				// do nothing - waiting scan to be finished
			}
			scanResults = testProfile.getTableHeader().extractColsProperties();

			// Then
			assertEquals(true, testProfile.getTableHeader().isScanDone());
			assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
					scanResults.toArray(new ColumnProperties[0]), stringsToHeaderEntryArray(
							new String[] { "", "\"Test1", "", "\"Test 2,2\"", "Test 3,3", "", "Test 4" })));

		} catch (nullNameConnection | connectionNotInitialized | InterruptedException | ExecutionException
				| nullArgument | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}

	private ColumnProperties[] stringsToHeaderEntryArray(String[] stringArray) throws nullArgument {
		ColumnProperties[] tempHeaderEntryArray = new ColumnProperties[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			tempHeaderEntryArray[i] = new ColumnProperties(stringArray[i], true,
					new MappingUnit("Not Set", new MaskTemplate(), MappingType.NotSet), i);
		}
		return tempHeaderEntryArray;
	}

	@Test
	void Testing_the_Table_Header_scan_with_start_row_and_column_change() {
		try {
			// Given
			Profile testProfile = new Profile();

			testProfile.initializeCSVConn("CSV Connection 2",
					"F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\test\\resources\\GoogleSheets_Test 2.csv",
					",");
			// When
			ArrayList<ColumnProperties> scanResults = null;
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(2, 3));
			testProfile.getTableHeader().scanForColsProperties();

			while (!testProfile.getTableHeader().isScanDone()) {
			}
			scanResults = testProfile.getTableHeader().extractColsProperties();

			// Then
			assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
					scanResults.toArray(new ColumnProperties[0]),
					stringsToHeaderEntryArray(new String[] { "\"Col 3", "", "\"\"", "", "\"Col 7\"" })));

		} catch (nullNameConnection | connectionNotInitialized | InterruptedException | ExecutionException
				| nullArgument | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}

	@Test
	void Testing_the_Table_Header_scan_no_file_found_exception_throw() {
		try {
			// Given
			Profile testProfile = new Profile();
			testProfile.initializeCSVConn("No CSV File Connection", "F:\\Proiecte\\No CSV File.csv", ",");

			// When
			testProfile.getTableHeader().scanForColsProperties(); // exception not captured - passed correctly under
																	// Future Scan area
			while (!testProfile.getTableHeader().isScanDone()) {
			}

			// Then
			assertThrows(ExecutionException.class, () -> {
				testProfile.getTableHeader().extractColsProperties();
			});

		} catch (nullNameConnection | connectionNotInitialized | nullArgument | InterruptedException
				| ExecutionException | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}

	@Disabled
	@Test
	void Testing_the_Table_Header_scan_mock_random_row_content() {
		try {
			// Given
			Profile testProfile = new Profile();
			CSVdataSource mockSource = mock(CSVdataSource.class);
			Tools_Random_Generator rand = new Tools_Random_Generator();
			String randGenRow1 = rand.randomCSVrowGenerator(30, ",");
			testProfile.initializeCSVConn("No CSV File Connection", "F:\\Proiecte\\No CSV File.csv", ",");

			when(mockSource.getNextLine()).thenReturn(randGenRow1);
			ArrayList<ColumnProperties> scanResults = null;

			// When
			testProfile.getTableHeader().scanForColsPropertiesMock(mockSource);
			scanResults = testProfile.getTableHeader().extractColsProperties();

			// Then
			assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
					scanResults.toArray(new ColumnProperties[0]),
					stringsToHeaderEntryArray(rand.csvGeneratedRowToArray())));

		} catch (nullNameConnection | IOException | connectionNotInitialized | InterruptedException | ExecutionException
				| nullArgument | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}

	@Disabled
	@Test
	void Testing_the_Table_Header_scan_mock_random_content_second_row() {
		try {
			// Given
			Profile testProfile = new Profile();
			CSVdataSource mockSource = mock(CSVdataSource.class);
			Tools_Random_Generator rand = new Tools_Random_Generator();
			String randGenRow1 = rand.randomCSVrowGenerator(30, ",");
			String randGenRow2 = rand.randomCSVrowGenerator(30, ",");
			testProfile.initializeCSVConn("No CSV File Connection", "F:\\Proiecte\\No CSV File.csv", ",");

			when(mockSource.getNextLine()).thenReturn(randGenRow1).thenReturn(randGenRow2);
			ArrayList<ColumnProperties> scanResults = null;

			// When
			testProfile.getTableHeader().setTableHeaderStartCell(new tableCell(2, 1));
			testProfile.getTableHeader().scanForColsPropertiesMock(mockSource);
			scanResults = testProfile.getTableHeader().extractColsProperties();

			// Then
			assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
					scanResults.toArray(new ColumnProperties[0]),
					stringsToHeaderEntryArray(rand.csvGeneratedRowToArray())));

		} catch (nullNameConnection | IOException | connectionNotInitialized | InterruptedException | ExecutionException
				| nullArgument | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}

	@Disabled
	@Test
	void Testing_the_Table_Header_scan_mock_no_connection_setup_Exception_throwing() {
		try {
			// Given
			Profile testProfile = new Profile();
			CSVdataSource mockSource = mock(CSVdataSource.class);
			Tools_Random_Generator rand = new Tools_Random_Generator();
			String randGenRow1 = rand.randomCSVrowGenerator(30, ",");
			when(mockSource.getNextLine()).thenReturn(randGenRow1);

			// When Then
			assertThrows(ExceptionsPack.connectionNotInitialized.class, () -> {
				testProfile.getTableHeader().scanForColsPropertiesMock(mockSource);
			});

		} catch (IOException | nullArgument e) {
			e.printStackTrace();
		}
	}

	@Disabled
	@Test
	void Testing_the_Table_Header_scan_mock_set_columns_type() {
		try {
			// Given
			Profile testProfile = new Profile();
			CSVdataSource mockSource = mock(CSVdataSource.class);
			Tools_Random_Generator rand = new Tools_Random_Generator();
			String randGenRow1 = rand.randomCSVrowGenerator(30, ",");
			testProfile.initializeCSVConn("No CSV File Connection", "F:\\Proiecte\\No CSV File.csv", ",");

			when(mockSource.getNextLine()).thenReturn(randGenRow1);
			// When
			testProfile.getTableHeader().scanForColsPropertiesMock(mockSource);
			String columnName = rand.csvGeneratedRowToArray()[0];
			MappingUnit newMappingField = testProfile.getMappingCollection()
					.getMappingUnitdByType(MappingType.EmployeeUniqueId);
			testProfile.getTableHeader().changeMappingUnitOfColumnWithName(columnName, newMappingField);

			// Then
			assertEquals(MappingType.EmployeeUniqueId,
					testProfile.getTableHeader().extractColsProperties().get(0).getMappingUnit().getType());

		} catch (nullNameConnection | IOException | connectionNotInitialized | InterruptedException | ExecutionException
				| mappingUnitDoesNotExist | nullArgument | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}
}
