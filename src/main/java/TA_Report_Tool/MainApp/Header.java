package TA_Report_Tool.MainApp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import TA_Report_Tool.Data.DataSource;
import TA_Report_Tool.Data.HeaderEntry;
import TA_Report_Tool.Data.HeaderMapping;
import TA_Report_Tool.Data.HeaderMappingField;
import TA_Report_Tool.Data.tableCell;
import TA_Report_Tool.MainApp.ExceptionsPack.HeaderColumnDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.SearchedHeaderColumnIsNull;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.contentNotFound;
import TA_Report_Tool.MainApp.ExceptionsPack.scanError;

public class Header {
	private Connection connection = null;
	/*
	 * ================== Settings of table like data sources ==================
	 * (CSV, Excel, HTML table)
	 * 
	 * Table header starts at cell (startRow,StartCol).
	 * 
	 * Full Header length stored in length.
	 * 
	 * The first column and row of the table will start at 1 for user. As Java's
	 * array structures use 0 as first position, the get and set table start methods
	 * will ensure transition.
	 */
	private tableCell startCell = null;
	/*
	 * ====================== Settings of DB data sources ======================
	 * (MSQL)
	 * 
	 * To be filled in
	 */
	private HeaderMapping headerMapping = null;

	/**
	 * Constructor of Header class
	 * 
	 * @param activeConn    is a Connection type object that is passed so the
	 *                      methods to connect to data source and extract header.
	 * @param headerMapping represents an ArrayList of HeaderMappingField used to
	 *                      store the formatting of each column for correct parsing
	 */
	public Header(Connection activeConn, HeaderMapping headerMapping) {
		this.connection = activeConn;
		this.startCell = new tableCell(0, 0);
		this.headerMapping = headerMapping;
	}

	/**
	 * Setter method used to indicate the beginning of the header for table like
	 * data sources
	 * 
	 * <br>
	 * The conversion from user logic (first cell at 1,1) to Java logic (first cell
	 * at 0,0) is done inside
	 * 
	 * @param startRow a variable of int type indicating the starting row
	 * @param startCol a variable of int type indicating the starting column
	 */
	public void setHeaderStartCell(tableCell startCell) {
		this.startCell = new tableCell(startCell.getRowCoordinates() - 1, startCell.getColCoordinates() - 1);
	}

	public tableCell getHeaderStartCell() {
		return new tableCell(this.startCell.getRowCoordinates() + 1, this.startCell.getColCoordinates() + 1);
	}

	public ArrayList<HeaderEntry> getColumns()
			throws InterruptedException, ExecutionException, connectionNotInitialized {
		if (this.headerScannedAtLeastOnce == false) {
			this.scan();
		}

		if (this.freshScan) {
			storeScanResults();
			return this.headerColumns;
		}
		return this.headerColumns;
	}

	public boolean isScanDone() {
		return this.futureScanResults.isDone();
	}

	private void storeScanResults() throws InterruptedException, ExecutionException {
		this.headerColumns = this.futureScanResults.get();
		this.freshScan = false;
	}

	private boolean freshScan = false;
	private boolean headerScannedAtLeastOnce = false;
	private ArrayList<HeaderEntry> headerColumns = new ArrayList<HeaderEntry>();
	Future<ArrayList<HeaderEntry>> futureScanResults;
	private DataSource mockSource = null;
	private boolean useAlternateMockDataSource = false;

	/**
	 * Method scans the active data source and stores into the internal fullHeader
	 * set, the Header details marking all columns as to be loaded
	 * 
	 * @throws connectionNotInitialized
	 * 
	 * @throws Exception
	 * 
	 * @throws contentNotFound
	 * @throws scanError
	 */
	public void scan() throws connectionNotInitialized {
		if (this.connection.equals(null)) {
			throw new ExceptionsPack.connectionNotInitialized("The connection it was not initialized and it is null");
		}

		if (this.connection.isCSVtype()) {
			CSVHeaderScanner headerScanner;
			if (!this.useAlternateMockDataSource) {
				headerScanner = new CSVHeaderScanner(this.connection, this.startCell);
			} else {
				this.resetAlternateMockDataSourceUse();
				headerScanner = new CSVHeaderScanner(this.connection, this.startCell, this.mockSource);
			}
			this.futureScanResults = new InnerCSVHeaderScan().scan(headerScanner, this.headerMapping);
			this.freshScan = true;
			this.headerScannedAtLeastOnce = true;

			return;
		}

		if (connection.getType().equals("Excel")) {
			return;
		}

		if (connection.getType().equals("HTML")) {
			return;
		}

		if (connection.getType().equals("SQL")) {
			return;
		}
	}

	public void scanMock(DataSource mockSource) throws connectionNotInitialized {
		this.useAlternateMockDataSource = true;
		this.mockSource = mockSource;
		this.scan();
	}

	private void resetAlternateMockDataSourceUse() {
		this.useAlternateMockDataSource = false;
	}

	class InnerCSVHeaderScan {
		private ExecutorService executor = Executors.newSingleThreadExecutor();

		public Future<ArrayList<HeaderEntry>> scan(CSVHeaderScanner headerScanner, HeaderMapping headerMapping) {
			return executor.submit(() -> {
				ArrayList<HeaderEntry> scanResults = headerScanner.scanForHeader(headerMapping);
				return scanResults;
			});
		}
	}

	public void setMappingTypeOfColumnWithName(String name, HeaderMappingField type)
			throws connectionNotInitialized, InterruptedException, ExecutionException {
		if (this.headerScannedAtLeastOnce == false) {
			this.scan();
		}

		if (this.freshScan) {
			storeScanResults();
		}

		for (HeaderEntry x : this.headerColumns) {
			if (x.getName().equals(name)) {
				x.setHeaderMappingField(type);
				return;
			}
		}
	}

	public HeaderEntry getColumnByName(String columnName) throws SearchedHeaderColumnIsNull, InterruptedException,
			ExecutionException, connectionNotInitialized, HeaderColumnDoesNotExist {
		if (columnName == null) {
			throw new ExceptionsPack.SearchedHeaderColumnIsNull("The parameter for column name is null");
		}

		ArrayList<HeaderEntry> headerColumns = this.getColumns();
		for (HeaderEntry x : headerColumns) {
			if (x.getName().equals(columnName)) {
				return x;
			}
		}
		throw new ExceptionsPack.HeaderColumnDoesNotExist(
				"The header column with name " + columnName + " does not exist");
	}
}
