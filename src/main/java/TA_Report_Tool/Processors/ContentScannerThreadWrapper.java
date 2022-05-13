package TA_Report_Tool.Processors;

import static TA_Report_Tool.Tools.check.isFalse;
import static TA_Report_Tool.Tools.check.isNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import TA_Report_Tool.Data.CSVdataSource;
import TA_Report_Tool.Data.DataSource;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.Profile;
import TA_Report_Tool.MainApp.ExceptionsPack.cantBeParsedWithCurrentMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.cantParseEmptyStringForCurrentType;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.nullDataReceivedForParsing;
import TA_Report_Tool.MainApp.ExceptionsPack.parsingFailedDueToNullMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.rowParameterNotHigherThanZero;
import TA_Report_Tool.MainApp.ExceptionsPack.tableDataNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.tableLengthAndDataLengthNotMatching;

public class ContentScannerThreadWrapper implements Callable<Void> {
	private tableCell firstCellOfRowToRead = null;
	private boolean mockFlag = false;
	private DataSource dataSource = null;
	Profile profileInUse = null;
	private int currentRow = -1;

	public void setStartRowForScanner(tableCell cell) {
		this.firstCellOfRowToRead = cell;
	}

	public void transferActiveProfile(Profile profile) {
		this.profileInUse = profile;
	}

	public void setMockDataSourceForMockScan(DataSource mock) {
		this.mockFlag = true;
		this.dataSource = mock;
	}

	@Override
	public Void call()
			throws IOException, tableLengthAndDataLengthNotMatching, nullDataReceivedForParsing, InterruptedException,
			ExecutionException, connectionNotInitialized, nullColumnPropertiesPassed, columnPropertiesDoesNotExist,
			cantBeParsedWithCurrentMappingMask, parsingFailedDueToNullMappingMask, rowParameterNotHigherThanZero,
			tableDataNotInitialized, dateOrTimeMissing, nullArgument, cantParseEmptyStringForCurrentType {
		if (isFalse(this.mockFlag)) {
			this.dataSource = new CSVdataSource(this.profileInUse.getActiveConn().getFilePath());
		}

		String tempContainerRow = this.dataSource.getNextLine();
		while (isNotNull(tempContainerRow)) {
			this.currentRow++;

			if (isThisTargetRow()) {

				parseAndStoreContentOfRow(tempContainerRow);
				moveTargetToNextRow();
			}
			tempContainerRow = this.dataSource.getNextLine();
		}

		this.dataSource.closeFile();
		return null;
	}

	private String[] getTargetedSubArray(String[] rowSplit) {
		String[] subArr = Arrays.copyOfRange(rowSplit, this.firstCellOfRowToRead.getColCoordinates(), rowSplit.length);
		return subArr;
	}

	private void parseAndStoreContentOfRow(String content)
			throws tableLengthAndDataLengthNotMatching, nullDataReceivedForParsing, InterruptedException,
			ExecutionException, connectionNotInitialized, nullColumnPropertiesPassed, columnPropertiesDoesNotExist,
			cantBeParsedWithCurrentMappingMask, parsingFailedDueToNullMappingMask, rowParameterNotHigherThanZero,
			tableDataNotInitialized, dateOrTimeMissing, nullArgument, cantParseEmptyStringForCurrentType {

		ContentCSVparser csvProcessor = new ContentCSVparser();
		String[] rowSplit = csvProcessor.parseCSVrow(content, delimiter());
		this.profileInUse.getTableData().parseAndStoreOneDataRow(getTargetedSubArray(rowSplit));
	}

	private String delimiter() {
		return this.profileInUse.getActiveConn().getCSVDelimiter();
	}

	private boolean isThisTargetRow() {
		if (this.firstCellOfRowToRead.getRowCoordinates() == this.currentRow) {
			return true;
		}
		return false;
	}

	private void moveTargetToNextRow() {
		this.firstCellOfRowToRead.nextRow();
	}
}