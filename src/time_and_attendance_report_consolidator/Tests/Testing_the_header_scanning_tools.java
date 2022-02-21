package time_and_attendance_report_consolidator.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import time_and_attendance_report_consolidator.ContentCSVparser;
import time_and_attendance_report_consolidator.HeaderEntry;
import time_and_attendance_report_consolidator.tableCell;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Testing_the_header_scanning_tools {

	@Test
	void Testing_the_HeaderEntry_methods() {
		ArrayList<HeaderEntry> headerCols = new ArrayList<HeaderEntry>();
		int count = 0;

		Tools_Random_Generator rand = new Tools_Random_Generator();
		String string;

		string = rand.randomTextAlphaNumeric(25);
		headerCols.add(new HeaderEntry(string, true));
		count = count + 1;
		assertEquals(string, headerCols.get(count - 1).getName());
		assertTrue(headerCols.get(count - 1).getLoadStatus());

		string = rand.randomTextAlphaNumericAndBeginWithSymbols(25);
		headerCols.add(new HeaderEntry(string, true));
		count = count + 1;
		assertEquals(string, headerCols.get(count - 1).getName());
		assertTrue(headerCols.get(count - 1).getLoadStatus());

		string = rand.randomTextAlphaNumericAndEndWithSymbols(25);
		headerCols.add(new HeaderEntry(string, true));
		count = count + 1;
		assertEquals(string, headerCols.get(count - 1).getName());
		assertTrue(headerCols.get(count - 1).getLoadStatus());

		string = rand.randomTextAlphaNumericAndSymbols(25);
		headerCols.add(new HeaderEntry(string, true));
		count = count + 1;
		assertEquals(string, headerCols.get(count - 1).getName());
		assertTrue(headerCols.get(count - 1).getLoadStatus());

		string = rand.randomTextAlphaNumericAndSymbolsBalancedDistribution(25);
		headerCols.add(new HeaderEntry(string, true));
		count = count + 1;
		assertEquals(string, headerCols.get(count - 1).getName());
		assertTrue(headerCols.get(count - 1).getLoadStatus());

		string = null;
		headerCols.add(new HeaderEntry(string, true));
		count = count + 1;
		assertEquals(string, headerCols.get(count - 1).getName());
		assertTrue(headerCols.get(count - 1).getLoadStatus());

		headerCols.get(0).setLoadStatus(false);
		headerCols.get(0).setName("First Column");
		assertEquals("First Column", headerCols.get(0).getName());
		assertFalse(headerCols.get(0).getLoadStatus());
	}

	@Test
	void Testing_the_tableCell() {
		Tools_Random_Generator rand = new Tools_Random_Generator();
		int row, col;

		row = rand.randomIntBetween(0, 1000);
		col = rand.randomIntBetween(0, 1000);
		tableCell cell = new tableCell(row, col);

		assertEquals(row, cell.getRowCoordinates());
		assertEquals(col, cell.getColCoordinates());
	}

	@Test
	void Testing_the_csv_parser_fixed_and_random_text() {
		ContentCSVparser csvProcessor = new ContentCSVparser();
		String fixedTestString = ",\"\"\"Test1\",,\"\"\"Test 2,2\"\"\",\"Test 3,3\",,Test 4";
		String[] fixedTestCSVParsedResults = csvProcessor.parseCSVrow(fixedTestString, String.valueOf(','));

		String emptyString = "";
		assertEquals(7, fixedTestCSVParsedResults.length);
		assertEquals(emptyString, fixedTestCSVParsedResults[0]);
		assertEquals(String.valueOf('"') + "Test1", fixedTestCSVParsedResults[1]);
		assertEquals(emptyString, fixedTestCSVParsedResults[2]);
		assertEquals(String.valueOf('"') + "Test 2,2" + String.valueOf('"'), fixedTestCSVParsedResults[3]);
		assertEquals("Test 3,3", fixedTestCSVParsedResults[4]);
		assertEquals(emptyString, fixedTestCSVParsedResults[5]);
		assertEquals("Test 4", fixedTestCSVParsedResults[6]);

		Tools_Random_Generator rand = new Tools_Random_Generator();
		String generatedTestString = rand.randomCSVrowGenerator(30, ",");
		String[] generatedTestCSVParsedResults = csvProcessor.parseCSVrow(generatedTestString, String.valueOf(','));
		String[] generatedTestBackStoredArrayResults = rand.csvGeneratedRowToArray();

		assertEquals(generatedTestBackStoredArrayResults.length, generatedTestCSVParsedResults.length);
		assertEquals(30, generatedTestCSVParsedResults.length);
	}

}
