package TA_Report_Tool.Tests;

import static TA_Report_Tool.Tools.check.isFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

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
import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.MainApp.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_file_generated_by_real_system {

	private MappingUnit datetime, empID, number, text, event, device, fullname;

	@Disabled
	@Test
	public void Final_complete_testing_of_a_real_file() {
		try {
			Profile companyStarOne = new Profile("Company Star One");

			String filePath = "F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\Real Test System Card History for Tests.csv";
			companyStarOne.initializeCSVConn("Star One Card History Report", filePath, ",");

			preparePoolOfColumnTypesFoundInRealFile(companyStarOne);
			companyStarOne.getTableHeader().scanForColsProperties();
			mapScannedColsWithTypesFromPool(companyStarOne);

			companyStarOne.scanAndStoreTableContent();
			while (isFalse(companyStarOne.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			companyStarOne.captureExceptionsAfterScanning();
			companyStarOne.getTableData().displayTableContentForDebugging("Original Data");

			createFiltersForAllColumns(companyStarOne);
			companyStarOne.dataFiltersAndSettings().getFilter("Filter for column: Status")
					.addToExclusionList("Card Not Found");
			companyStarOne.filterTableData();
			companyStarOne.getFilteredData().displayTableContentForDebugging("Filtered Data");

			companyStarOne.dataFiltersAndSettings().addColToExtraTAcolDisplay("Name");
			companyStarOne.generateTAreport();
			while (isFalse(companyStarOne.isTAgenerationDone())) {
			}
			companyStarOne.captureExceptionsAfterGeneratingReport();

			companyStarOne.getDetailedTAReport().displayTableContentForDebugging("Detailed TA Report");
			companyStarOne.getSummaryTAReport().displayTableContentForDebugging("Summary TA Report");

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero
				| nullColumnPropertiesPassed | columnPropertiesDoesNotExist | TAreportGenerationException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void Final_complete_testing_of_a_real_file_90000_generated_entries_all_events_are_checks() {
		try {
			Profile companyStarOne = new Profile("Company Star One");

			String filePath = "F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\Generated Test 1.csv";
			companyStarOne.initializeCSVConn("Star One Card History Report", filePath, ",");

			preparePoolOfColumnTypesFoundInRealFile(companyStarOne);
			companyStarOne.getTableHeader().scanForColsProperties();
			mapScannedColsWithTypesFromPool(companyStarOne);

			companyStarOne.scanAndStoreTableContent();
			while (isFalse(companyStarOne.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			companyStarOne.captureExceptionsAfterScanning();
			// companyStarOne.getTableData().displayTableContentForDebugging("Original
			// Data");

			createFiltersForAllColumns(companyStarOne);
			companyStarOne.dataFiltersAndSettings().getFilter("Filter for column: Status")
					.addToExclusionList("Card Not Found");
			companyStarOne.filterTableData();
			// companyStarOne.getFilteredData().displayTableContentForDebugging("Filtered
			// Data");

			companyStarOne.dataFiltersAndSettings().addColToExtraTAcolDisplay("Name");
			companyStarOne.dataFiltersAndSettings().setDecimalsForFloatRound(2);
			companyStarOne.generateTAreport();
			while (isFalse(companyStarOne.isTAgenerationDone())) {
			}
			companyStarOne.captureExceptionsAfterGeneratingReport();

			companyStarOne.getDetailedTAReport().displayTableContentForDebugging("Detailed TA Report");
			companyStarOne.getSummaryTAReport().displayTableContentForDebugging("Summary TA Report");

			assertEquals(900, companyStarOne.getDetailedTAReport().getRowCount());
			assertEquals(30, companyStarOne.getSummaryTAReport().getRowCount());

			assertEquals((float) 0.0, companyStarOne.getDetailedTAReport()
					.vlookup("Antony, Mark", "Name", "Standard Working Hours").get(0));
			assertEquals((float) 8.0, companyStarOne.getDetailedTAReport()
					.vlookup("Antony, Mark", "Name", "Standard Working Hours").get(2));
			
			assertEquals((float) 168.0, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Standard Working Hours").get(0));
			assertEquals((float) 160.0, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Standard Working Hours").get(1));
			assertEquals((float) 184.0, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Standard Working Hours").get(2));
			
			assertEquals((float) 729.06, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Total Worked Hours").get(0));
			assertEquals((float) 657.85, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Total Worked Hours").get(1));
			assertEquals((float) 728.62, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Total Worked Hours").get(2));
			

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero
				| nullColumnPropertiesPassed | columnPropertiesDoesNotExist | TAreportGenerationException
				| tableDataMethodError e) {
			e.printStackTrace();
		}

	}

	@Test
	public void Final_complete_testing_of_a_real_file_90000_generated_entries_separate_check_in_and_check_out_events() {
		try {
			Profile companyStarOne = new Profile("Company Star One");

			String filePath = "F:\\Proiecte\\01.2022 Java Learning\\TA_Report_Consolidator\\src\\main\\resources\\Generated Test 1.csv";
			companyStarOne.initializeCSVConn("Star One Card History Report", filePath, ",");

			preparePoolOfColumnTypesFoundInRealFile(companyStarOne);
			companyStarOne.getTableHeader().scanForColsProperties();
			mapScannedColsWithTypesFromPool(companyStarOne);

			companyStarOne.scanAndStoreTableContent();
			while (isFalse(companyStarOne.isTableContentScanningAndParsingDone())) {
				// wait the scanner in separate thread to finish
			}
			companyStarOne.captureExceptionsAfterScanning();
			// companyStarOne.getTableData().displayTableContentForDebugging("Original
			// Data");

			createFiltersForAllColumns(companyStarOne);
			companyStarOne.dataFiltersAndSettings().getFilter("Filter for column: Status")
					.addToExclusionList("Card Not Found");
			companyStarOne.dataFiltersAndSettings().inOut().addCheckIn("pw6k - Reader 0");
			companyStarOne.dataFiltersAndSettings().inOut().addCheckIn("pw6k - Reader 1");
			companyStarOne.dataFiltersAndSettings().inOut().addCheckIn("pw6k - Reader 2");
			companyStarOne.dataFiltersAndSettings().inOut().addCheckOut("pw6k - Reader 4");
			companyStarOne.dataFiltersAndSettings().inOut().addCheckOut("pw6k - Reader 6");
			companyStarOne.dataFiltersAndSettings().inOut().addCheckOut("pw6k - Reader 8");
			companyStarOne.dataFiltersAndSettings().inOut().attachColumn("Reader");

			companyStarOne.filterTableData();
			// companyStarOne.getFilteredData().displayTableContentForDebugging("Filtered
			// Data");

			companyStarOne.dataFiltersAndSettings().addColToExtraTAcolDisplay("Name");
			companyStarOne.dataFiltersAndSettings().setDecimalsForFloatRound(2);
			companyStarOne.generateTAreport();
			while (isFalse(companyStarOne.isTAgenerationDone())) {
			}
			companyStarOne.captureExceptionsAfterGeneratingReport();

			companyStarOne.getDetailedTAReport()
					.displayTableContentForDebugging("Detailed TA Report with inidividual check in & check Out");
			companyStarOne.getSummaryTAReport()
					.displayTableContentForDebugging("Summary TA Report with inidividual check in & check Out");
			
			assertEquals(900, companyStarOne.getDetailedTAReport().getRowCount());
			assertEquals(30, companyStarOne.getSummaryTAReport().getRowCount());

			assertEquals((float) 0.0, companyStarOne.getDetailedTAReport()
					.vlookup("Antony, Mark", "Name", "Standard Working Hours").get(0));
			assertEquals((float) 8.0, companyStarOne.getDetailedTAReport()
					.vlookup("Antony, Mark", "Name", "Standard Working Hours").get(2));
			
			assertEquals((float) 168.0, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Standard Working Hours").get(0));
			assertEquals((float) 160.0, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Standard Working Hours").get(1));
			assertEquals((float) 184.0, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Standard Working Hours").get(2));
			
			assertEquals((float) 729.06, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Total Worked Hours").get(0));
			assertEquals((float) 657.85, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Total Worked Hours").get(1));
			assertEquals((float) 728.62, companyStarOne.getSummaryTAReport()
					.vlookup("Doe, Jane", "Name", "Total Worked Hours").get(2));

		} catch (nullArgument | nullNameConnection | InterruptedException | ExecutionException
				| connectionNotInitialized | dateOrTimeMissing | searchCantFindMappingUnitInCollection
				| headerNotScanned | tableDataNotInitialized | rowParameterNotHigherThanZero
				| nullColumnPropertiesPassed | columnPropertiesDoesNotExist | TAreportGenerationException | tableDataMethodError e) {
			e.printStackTrace();
		}

	}

	private void createFiltersForAllColumns(Profile realCompanyProfile)
			throws InterruptedException, ExecutionException, connectionNotInitialized, dateOrTimeMissing, nullArgument {

		for (ColumnProperties x : realCompanyProfile.getTableHeader().getColsPropertiesList()) {
			realCompanyProfile.dataFiltersAndSettings().addNewFilter("Filter for column: " + x.getName(), x);
		}

	}

	private void mapScannedColsWithTypesFromPool(Profile realCompanyProfile) throws connectionNotInitialized,
			InterruptedException, ExecutionException, dateOrTimeMissing, nullArgument, headerNotScanned {

		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("S.No", number);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Gen Time", datetime);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Seq ID", number);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Type", text);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Status", event);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("P", number);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Reader", device);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Site", text);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Card Number", empID);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Name", fullname);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Operator", text);
		realCompanyProfile.getTableHeader().changeMappingUnitOfColumnWithName("Message", text);

	}

	private void preparePoolOfColumnTypesFoundInRealFile(Profile realCompanyProfile)
			throws searchCantFindMappingUnitInCollection, nullArgument {

		this.empID = realCompanyProfile.getMappingCollection().getMappingUnitdByType(MappingType.EmployeeUniqueId);

//@formatter:off
		this.datetime = new MappingUnit("Date & Time",
										new MaskTemplate().addMonth().addSep("/").addDay().addSep("/").addYYYYear().addSep(" ").addhour().addSep(":").addmminute().addSep(":").addsecond().addSep(" ").markAMPMTime(),
										MappingType.DateAndTime);
		
		this.number = 	new MappingUnit("Number",
										new MaskTemplate().addNumber(),
										MappingType.Number);
		
		this.text = 	new MappingUnit("Text",
										new MaskTemplate().addAnyString(),
										MappingType.CustomFieldText);
		
		this.event = 	new MappingUnit("Event",
										new MaskTemplate().addAnyString(),
										MappingType.Event);
		
		this.device = 	new MappingUnit("Device",
										new MaskTemplate().addAnyString(),
										MappingType.SignalingDevice);
		
		this.fullname = new MappingUnit("Full Name",
										new MaskTemplate().addAnyString(),
										MappingType.EmployeeFullName);
//@formatter:on
	}

}
