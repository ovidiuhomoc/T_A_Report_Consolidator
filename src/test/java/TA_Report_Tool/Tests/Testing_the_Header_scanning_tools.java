package TA_Report_Tool.Tests;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.Processors.ContentCSVparser;
import TA_Report_Tool.Tools_for_Tests.Tools_Random_Generator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_Header_scanning_tools {

	@Test
	void Testing_the_ColumnProperties_methods() {
		try {
			ArrayList<ColumnProperties> columnPropertiesList = new ArrayList<ColumnProperties>();
			int count = 0;

			Tools_Random_Generator rand = new Tools_Random_Generator();
			String string;

			string = rand.randomTextAlphaNumeric(25);

			columnPropertiesList.add(new ColumnProperties(string, true,
					new MappingUnit("Not Set", new MaskTemplate(), MappingType.NotSet), 1));

			count = count + 1;
			assertEquals(string, columnPropertiesList.get(count - 1).getName());
			assertTrue(columnPropertiesList.get(count - 1).loadData());
			assertEquals(MappingType.NotSet, columnPropertiesList.get(count - 1).getMappingUnit().getType());

			string = rand.randomTextAlphaNumericAndBeginWithSymbols(25);
			columnPropertiesList.add(new ColumnProperties(string, true,
					new MappingUnit("Not Set", new MaskTemplate(), MappingType.NotSet), 1));
			count = count + 1;
			assertEquals(string, columnPropertiesList.get(count - 1).getName());
			assertTrue(columnPropertiesList.get(count - 1).loadData());
			assertEquals(MappingType.NotSet, columnPropertiesList.get(count - 1).getMappingUnit().getType());

			string = rand.randomTextAlphaNumericAndEndWithSymbols(25);
			columnPropertiesList.add(new ColumnProperties(string, true,
					new MappingUnit("Not Set", new MaskTemplate(), MappingType.NotSet), 1));
			count = count + 1;
			assertEquals(string, columnPropertiesList.get(count - 1).getName());
			assertTrue(columnPropertiesList.get(count - 1).loadData());
			assertEquals(MappingType.NotSet, columnPropertiesList.get(count - 1).getMappingUnit().getType());

			string = rand.randomTextAlphaNumericAndSymbols(25);
			columnPropertiesList.add(new ColumnProperties(string, true,
					new MappingUnit("Not Set", new MaskTemplate(), MappingType.NotSet), 1));
			count = count + 1;
			assertEquals(string, columnPropertiesList.get(count - 1).getName());
			assertTrue(columnPropertiesList.get(count - 1).loadData());
			assertEquals(MappingType.NotSet, columnPropertiesList.get(count - 1).getMappingUnit().getType());

			string = rand.randomTextAlphaNumericAndSymbolsBalancedDistribution(25);
			columnPropertiesList.add(new ColumnProperties(string, true,
					new MappingUnit("Not Set", new MaskTemplate(), MappingType.NotSet), 1));
			count = count + 1;
			assertEquals(string, columnPropertiesList.get(count - 1).getName());
			assertTrue(columnPropertiesList.get(count - 1).loadData());
			assertEquals(MappingType.NotSet, columnPropertiesList.get(count - 1).getMappingUnit().getType());

			string = null;
			String stringForException = string;
			assertThrows(ExceptionsPack.nullArgument.class, () -> {
				columnPropertiesList.add(new ColumnProperties(stringForException, true,
						new MappingUnit("Not Set", new MaskTemplate(), MappingType.NotSet), 1));
			});

			columnPropertiesList.get(0).setLoadStatus(false);
			columnPropertiesList.get(0).setName("First Column");
			assertEquals("First Column", columnPropertiesList.get(0).getName());
			assertFalse(columnPropertiesList.get(0).loadData());

		} catch (nullArgument e) {
			e.printStackTrace();
		}
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

		assertEquals(generatedTestBackStoredArrayResults.length, generatedTestCSVParsedResults.length);
		assertEquals(30, generatedTestCSVParsedResults.length);
	}
}
