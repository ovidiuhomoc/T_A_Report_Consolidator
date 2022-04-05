package TA_Report_Tool.MainApp;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import TA_Report_Tool.Data.CSVdataSource;
import TA_Report_Tool.Data.DataSource;
import TA_Report_Tool.Data.HeaderEntry;
import TA_Report_Tool.Data.HeaderMapping;
import TA_Report_Tool.Data.mappingType;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.Processors.ContentCSVparser;

public class CSVHeaderScanner {

	private ArrayList<HeaderEntry> selectedHeader = new ArrayList<HeaderEntry>();
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

	public ArrayList<HeaderEntry> scanForHeader(HeaderMapping headerMapping) throws Exception {
		if (mockFlag == false) {
			try {
				this.csvSource = new CSVdataSource(this.activeConn.getFilePath());
			} catch (FileNotFoundException e) {
				System.out.println("The exception encountered during file open is: " + e.toString());
				throw new Exception(e);
			}
		} else {
			this.csvSource = this.mockCSVSource;
		}

		try {
			while (readAndCheckIfLineExists(this.csvSource) && headerRowNotReached()) {
				if (isCurrentRowHeaderRow()) {
					setHeaderRowFound();
				}
			}
		} catch (Exception e) {
			System.out.println("The exception encountered during line read is: " + e.toString());
			throw new Exception(e);
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
			this.selectedHeader.add(new HeaderEntry(rowSplit[i], true,headerMapping.getHeaderMappingFieldByType(mappingType.NotSet)));
		}

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

	private boolean readAndCheckIfLineExists(DataSource csvSource) throws Exception {
		try {
			this.rowTempContainer = csvSource.getNextLine();
			if (this.rowTempContainer != null) {
				this.currentRow++;
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new Exception(e);
		}
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