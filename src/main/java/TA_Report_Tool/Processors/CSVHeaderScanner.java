package TA_Report_Tool.Processors;

import static TA_Report_Tool.Tools.check.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import TA_Report_Tool.Data.CSVdataSource;
import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.Connection;
import TA_Report_Tool.Data.DataSource;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.contentNotFound;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.Tools.check;

public class CSVHeaderScanner {

	private ArrayList<ColumnProperties> selectedHeader = new ArrayList<ColumnProperties>();
	private DataSource csvSource;
	private DataSource mockCSVSource = null;
	private boolean mockFlag = false;
	private Connection activeConn;
	private tableCell startHeaderCell;
	private String rowTempContainer = "";
	private String rowContent = "";
	private int currentRow = -1;
	private boolean headerRowFound = false;

	public CSVHeaderScanner(Connection connection, tableCell startCell) {
		setActiveConn(connection);
		setHeaderStartCell(startCell);
	}

	public CSVHeaderScanner(Connection connection, tableCell startCell, DataSource mockSource) {
		setActiveConn(connection);
		setHeaderStartCell(startCell);
		this.mockFlag = true;
		this.mockCSVSource = mockSource;
	}

	public ArrayList<ColumnProperties> scanForHeader() throws IOException, contentNotFound, nullArgument{
		if (isFalse(mockFlag)) {
			this.csvSource = new CSVdataSource(this.activeConn.getFilePath());
		} else {
			this.csvSource = this.mockCSVSource;
		}
		while (readAndCheckIfLineExists(this.csvSource) && headerRowNotReached()) {
			if (isCurrentRowHeaderRow()) {
				setHeaderRowFound();
			}
		}
		
		if (headerRowNotReached()) {
			throw new ExceptionsPack.contentNotFound(
					"Too few rows in the file and Header starting Row was not reached until End of file.");
		}

		ContentCSVparser csvProcessor = new ContentCSVparser();
		String[] rowSplit = csvProcessor.parseCSVrow(this.rowContent, delimiter());

		if (rowSplit == null) {
			return null;
		}

		for (int i = this.startHeaderCell.getColCoordinates(); i < rowSplit.length; i++) {
			this.selectedHeader.add(new ColumnProperties(rowSplit[i], true,
					new MappingUnit("Not Set", new MaskTemplate(), MappingType.NotSet),
					i - this.startHeaderCell.getColCoordinates()));
		}

		this.csvSource.closeFile();
		return this.selectedHeader;
	}

	private String delimiter() {
		return this.activeConn.getCSVDelimiter();
	}

	private boolean isCurrentRowHeaderRow() {
		if (this.currentRow == this.startHeaderCell.getRowCoordinates()) {
			return true;
		}
		return false;
	}

	private void setHeaderRowFound() {
		this.rowContent = this.rowTempContainer;
		this.headerRowFound = true;
	}

	private boolean readAndCheckIfLineExists(DataSource csvSource) throws IOException{
			this.rowTempContainer = csvSource.getNextLine();
			if (this.rowTempContainer != null) {
				this.currentRow++;
				return true;
			}
			return false;
	}

	private boolean headerRowNotReached() {
		if (!this.headerRowFound) {
			return true;
		}
		return false;
	}

	private void setActiveConn(Connection activeConn) {
		this.activeConn = activeConn;
	}

	private void setHeaderStartCell(tableCell startCell) {
		this.startHeaderCell = startCell;
	}

}
