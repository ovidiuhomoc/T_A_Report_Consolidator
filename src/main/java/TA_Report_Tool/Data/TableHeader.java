package TA_Report_Tool.Data;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static TA_Report_Tool.Tools.check.*;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.contentNotFound;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.headerNotScanned;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.scanError;
import TA_Report_Tool.Processors.CSVHeaderScanner;
import TA_Report_Tool.Tools.check;

public class TableHeader {
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
	private TableData tableData;

	/**
	 * Constructor of Header class
	 * 
	 * @param activeConn    is a Connection type object that is passed so the
	 *                      methods to connect to data source and extract header.
	 * @param headerMapping represents an ArrayList of HeaderMappingField used to
	 *                      store the formatting of each column for correct parsing
	 */
	public TableHeader(Connection activeConn, TableData tableData) {
		this.connection = activeConn;
		this.startCell = new tableCell(0, 0);
		this.tableData = tableData;
	}

	/**
	 * Setter method used to indicate the beginning of the header for table like
	 * data sources
	 * 
	 * <p>
	 * The conversion from user logic (first cell at 1,1) to Java logic (first cell
	 * at 0,0) is done inside
	 * 
	 * @param startRow a variable of int type indicating the starting row
	 * @param startCol a variable of int type indicating the starting column
	 */
	public void setTableHeaderStartCell(tableCell startCell) {
		this.startCell = new tableCell(startCell.getRowCoordinates() - 1, startCell.getColCoordinates() - 1);
	}

	/**
	 * Method returns the tuple tableCell containing the coordinates of Row &
	 * Column. The coordinates are represented as for the end user where the first
	 * row & first column have value 1 while in Java these will have value 0.
	 * 
	 * @return
	 */
	public tableCell getTableHeaderStartCellForEndUserUse() {
		return new tableCell(this.startCell.getRowCoordinates() + 1, this.startCell.getColCoordinates() + 1);
	}

	/**
	 * Method will return the tuple Row & Column and should be used for internal use
	 * only as the first column & row have index 0 as Java requires.
	 * 
	 * @return
	 */
	public tableCell getTableHeaderStartCellForEndInternalUse() {
		return new tableCell(this.startCell.getRowCoordinates(), this.startCell.getColCoordinates());
	}

	public ArrayList<ColumnProperties> getColsPropertiesList()
			throws InterruptedException, ExecutionException, connectionNotInitialized, dateOrTimeMissing, nullArgument {
		if (this.headerScannedAtLeastOnce == false) {
			this.scanForColsProperties();
		}

		if (this.freshScan) {
			storeScanResults();
			return this.localColumnsPropertiesList;
		}
		return this.localColumnsPropertiesList;
	}

	public boolean isScanDone() {
		return this.futureScanResults.isDone();
	}

	private void storeScanResults()
			throws InterruptedException, ExecutionException, connectionNotInitialized, dateOrTimeMissing, nullArgument {
		this.localColumnsPropertiesList = this.futureScanResults.get();
		this.freshScan = false;
		this.tableData.initializeTableData(this.localColumnsPropertiesList);
	}

	private boolean freshScan = false;
	private boolean headerScannedAtLeastOnce = false;
	private ArrayList<ColumnProperties> localColumnsPropertiesList = new ArrayList<ColumnProperties>();
	Future<ArrayList<ColumnProperties>> futureScanResults;
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
	public void scanForColsProperties() throws connectionNotInitialized {
		if (isNull(this.connection)) {
			throw new ExceptionsPack.connectionNotInitialized("The connection it was not initialized and it is null");
		}

		switch (this.connection.getType()) {
		case CSV:
			CSVHeaderScanner headerScanner;
			if (this.useAlternateMockDataSource) {
				this.resetAlternateMockDataSourceUse();
				headerScanner = new CSVHeaderScanner(this.connection, this.startCell, this.mockSource);
			} else {
				headerScanner = new CSVHeaderScanner(this.connection, this.startCell);
			}
			this.futureScanResults = new InnerCSVHeaderScan().scan(headerScanner);
			this.freshScan = true;
			this.headerScannedAtLeastOnce = true;

			return;
		case Excel:
			return;
		case HTML:
			return;
		case SQL:
			return;
		}
	}

	public void scanForColsPropertiesMock(DataSource mockSource) throws connectionNotInitialized {
		this.useAlternateMockDataSource = true;
		this.mockSource = mockSource;
		this.scanForColsProperties();
	}

	private void resetAlternateMockDataSourceUse() {
		this.useAlternateMockDataSource = false;
	}

	class InnerCSVHeaderScan {
		private ExecutorService executor = Executors.newFixedThreadPool(1);

		public Future<ArrayList<ColumnProperties>> scan(CSVHeaderScanner headerScanner) {
			return executor.submit(() -> {
				ArrayList<ColumnProperties> scanResults = headerScanner.scanForHeader();
				return scanResults;
			});
		}
	}

	/**
	 * The method changes the properties of a column (the assigned MappingUnit) from
	 * the default NotSet
	 * 
	 * @param colName     : The name of the column (in the header of the table)
	 *                    found in the datasource when scanning
	 * @param mappingUnit : The new type of the column
	 * @throws connectionNotInitialized
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws dateOrTimeMissing
	 * @throws nullArgument
	 * @throws headerNotScanned
	 */
	public void changeMappingUnitOfColumnWithName(String colName, MappingUnit mappingUnit)
			throws connectionNotInitialized, InterruptedException, ExecutionException, dateOrTimeMissing, nullArgument,
			headerNotScanned {
		if (isFalse(this.headerScannedAtLeastOnce)) {
			throw new ExceptionsPack.headerNotScanned(
					"The change of mapping can't be completed as header was not scanned neither once from the DataSource");
		}

		if (this.freshScan) {
			storeScanResults();
		}

		for (ColumnProperties x : this.localColumnsPropertiesList) {
			if (x.getName().equals(colName)) {
				x.setMappingUnit(mappingUnit);
				return;
			}
		}
	}

	public ColumnProperties getColPropertiesByColName(String columnName)
			throws nullColumnPropertiesPassed, InterruptedException, ExecutionException, connectionNotInitialized,
			columnPropertiesDoesNotExist, dateOrTimeMissing, nullArgument {
		if (isNull(columnName)) {
			throw new ExceptionsPack.nullColumnPropertiesPassed("The parameter for column name is null");
		}

		ArrayList<ColumnProperties> columnsPropertiesCollection = this.getColsPropertiesList();
		for (ColumnProperties x : columnsPropertiesCollection) {
			if (x.getName().equals(columnName)) {
				return x;
			}
		}
		throw new ExceptionsPack.columnPropertiesDoesNotExist(
				"The header column with name " + columnName + " does not exist");
	}

	public boolean headerScanned() {
		if (isFalse(this.headerScannedAtLeastOnce)) {
			return false;
		}
		return true;
	}
}
