package time_and_attendance_report_consolidator;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import time_and_attendance_report_consolidator.ExceptionsPack.scanError;

public class Header {
	private Connection connection;
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

	/**
	 * Constructor of Header class
	 * 
	 * @param activeConn is a Connection type object that is passed so the methods
	 *                   to connect to data source and extract header.
	 */
	public Header(Connection activeConn) {
		this.connection = activeConn;
		this.startCell = new tableCell(0, 0);
	}

	/**
	 * Setter method used to indicate the beginning of the header for table like
	 * data sources
	 * 
	 * @param startRow a variable of int type indicating the starting row
	 * @param startCol a variable of int type indicating the starting column
	 */
	public void setHeaderStartCell(tableCell startCell) {
		// Conversion from user logic (first cell at 1,1) to Java logic (first cell at
		// 0,0)
		this.startCell = new tableCell(startCell.getRowCoordinates() - 1, startCell.getColCoordinates() - 1);
	}

	public tableCell getHeaderStartCell() {
		return new tableCell(this.startCell.getRowCoordinates() + 1, this.startCell.getColCoordinates() + 1);
	}

	public ArrayList<HeaderEntry> getScanResults() throws InterruptedException, ExecutionException{
		return this.futureScanResults.get();
	}

	public boolean isScanDone() {
		return this.futureScanResults.isDone();
	}

	Future<ArrayList<HeaderEntry>> futureScanResults;

	/**
	 * Method scans the active data source and stores into the internal fullHeader
	 * set, the Header details marking all columns as to be loaded
	 * 
	 * @throws Exception
	 * 
	 * @throws contentNotFound
	 * @throws scanError
	 */
	public void scan(){
		if (this.connection.isCSVtype()) {
			CSVHeaderScanner headerScanner = new CSVHeaderScanner(this.connection, this.startCell);
			this.futureScanResults = new innerHeaderScan().scan(headerScanner);
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

	class innerHeaderScan {
		private ExecutorService executor = Executors.newSingleThreadExecutor();

		public Future<ArrayList<HeaderEntry>> scan(CSVHeaderScanner headerScanner) {
			return executor.submit(() -> {
				ArrayList<HeaderEntry> scanResults = headerScanner.scanForHeader();
				return scanResults;
			});
		}
	}
}
