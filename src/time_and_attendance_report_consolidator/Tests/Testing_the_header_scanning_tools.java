package time_and_attendance_report_consolidator.Tests;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import time_and_attendance_report_consolidator.ContentCSVparser;
import time_and_attendance_report_consolidator.ExceptionsPack.nullNameConnection;
import time_and_attendance_report_consolidator.Header;
import time_and_attendance_report_consolidator.HeaderEntry;
import time_and_attendance_report_consolidator.Profile;
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

		fixedTestString = "\"\"\"Col 7\"\"\"";
		fixedTestCSVParsedResults = csvProcessor.parseCSVrow(fixedTestString, String.valueOf(','));
		assertEquals(1, fixedTestCSVParsedResults.length);
		assertEquals(String.valueOf('"') + "Col 7" + String.valueOf('"'), fixedTestCSVParsedResults[0]);

		Tools_Random_Generator rand = new Tools_Random_Generator();
		String generatedTestString = rand.randomCSVrowGenerator(30, ",");
		String[] generatedTestCSVParsedResults = csvProcessor.parseCSVrow(generatedTestString, String.valueOf(','));
		String[] generatedTestBackStoredArrayResults = rand.csvGeneratedRowToArray();

		System.out.println("The generated Test String is:" + generatedTestString);
		System.out.println("The parsed array result is:" + Arrays.deepToString(generatedTestCSVParsedResults));
		System.out.println("The array stored durign csv generation is:"
				+ Arrays.deepToString(generatedTestBackStoredArrayResults));
		/*int z = 0;
		for (String x : Arrays.asList(generatedTestBackStoredArrayResults)) {
			z = z + 1;
			System.out.println("Column " + z + " contains ->" + x + "<-");
		}*/

		assertEquals(generatedTestBackStoredArrayResults.length, generatedTestCSVParsedResults.length);
		assertEquals(30, generatedTestCSVParsedResults.length);
	}

	@Test
	void Testing_the_header_scan_wrapped_by_a_thread_for_real_file() {
		Profile testProfile = new Profile();
		Header header1 = null;
		Header header2 = null;

		try {
			testProfile.setCSVConn("CSV Connection 1",
					"F:\\Proiecte\\01.2022 T&A Report Consolidator\\Resources\\GoogleSheets_Test 1.csv", ",");
			testProfile.setCSVConn("CSV Connection 2",
					"F:\\Proiecte\\01.2022 T&A Report Consolidator\\Resources\\GoogleSheets_Test 2.csv", ",");
		} catch (nullNameConnection e) {
			e.printStackTrace();
		}
		assertNotNull(testProfile.getActiveConn());
		assertNotNull(header1 = testProfile.getHeader());

		testProfile.setActiveConn(testProfile.getConnectionByName("CSV Connection 2"));
		assertNotEquals(header1, header2 = testProfile.getHeader());

		class headerScan {
			private ExecutorService executor = Executors.newSingleThreadExecutor();

			public Future<ArrayList<HeaderEntry>> scan(Header header) {
				return executor.submit(() -> {
					header.scan();

					while (header.isScanActive()) {
					}
					if (header.getEncounteredException() == null) {
						return header.getScanResults();
					}
					throw header.getEncounteredException();
				});
			}
		}

		Future<ArrayList<HeaderEntry>> futureScanResults = new headerScan().scan(header2);
		ArrayList<HeaderEntry> scanResults = null;
		try {
			scanResults = futureScanResults.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		assertEquals(false, header2.isScanActive());
		assertTrue(new Tools_Array_Equality_Test()
				.headerEntryTypeUnorderedEquality(scanResults.toArray(new HeaderEntry[0]), stringsToHeaderEntryArray(
						new String[] { "", "\"Test1", "", "\"Test 2,2\"", "Test 3,3", "", "Test 4" })));

		header2.setHeaderStartCell(new tableCell(2, 3));
		futureScanResults = new headerScan().scan(header2);
		try {
			scanResults = futureScanResults.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertTrue(new Tools_Array_Equality_Test().headerEntryTypeUnorderedEquality(
				scanResults.toArray(new HeaderEntry[0]),
				stringsToHeaderEntryArray(new String[] { "\"Col 3", "", "\"\"", "", "\"Col 7\"" })));

	}

	private HeaderEntry[] stringsToHeaderEntryArray(String[] stringArray) {
		HeaderEntry[] tempHeaderEntryArray = new HeaderEntry[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			tempHeaderEntryArray[i] = new HeaderEntry(stringArray[i], true);
		}
		return tempHeaderEntryArray;
	}

}