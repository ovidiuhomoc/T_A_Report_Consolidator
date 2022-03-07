package time_and_attendance_report_consolidator;

import java.util.ArrayList;

import time_and_attendance_report_consolidator.ExceptionsPack.scanError;

public class Header {

	private ArrayList<HeaderEntry> selectedHeader = new ArrayList<HeaderEntry>();
	private Connection connection;
	private Exception encounteredException = null;
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

	public ArrayList<HeaderEntry> getScanResults() throws scanError {
		if (encounteredException != null) {
			throw new ExceptionsPack.scanError("Failed to scan the data source", this.encounteredException);
		}
		return this.selectedHeader;
	}

	public Exception getEncounteredException() {
		return this.encounteredException;
	}

	public boolean isScanActive() {
		return this.threadStartedScan.isAlive();
	}

	private Thread threadStartedScan = null;

	/**
	 * Method scans the active data source and stores into the internal fullHeader
	 * set, the Header details marking all columns as to be loaded
	 * 
	 * @throws contentNotFound
	 * @throws scanError
	 */
	public void scan() {
		if (this.connection.isCSVtype()) {
			CSVheaderScanThreadWrap headerScan = new CSVheaderScanThreadWrap(this, this.connection, this.startCell);
			this.threadStartedScan = new Thread(headerScan);
			this.threadStartedScan.start();
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

	protected void storeHeaderScan(ArrayList<HeaderEntry> header) {
		this.selectedHeader = header;
	}

	protected void storeException(Exception e) {
		this.encounteredException = e;
	}
}
