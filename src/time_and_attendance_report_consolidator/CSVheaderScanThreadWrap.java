package time_and_attendance_report_consolidator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVheaderScanThreadWrap implements Runnable {
	private Connection activeConn;
	private Exception encounterdException = null;
	private boolean exceptionFlag = false;
	private String rowContent = "";
	private String tempContent = "";
	private tableCell startHeaderCell;
	private boolean headerRowFound = false;
	private int currentRow = -1;
	private ArrayList<HeaderEntry> selectedHeader = new ArrayList<HeaderEntry>();
	private Header parentHeaderObject;
	private String threadName = null;

	public CSVheaderScanThreadWrap(Header parentHeaderObject, Connection connection, tableCell startCell) {
		this.parentHeaderObject = parentHeaderObject;
		setActiveConn(connection);
		setHeaderStartCell(startCell);
	}

	public String getThreadName() {
		return this.threadName;
	}

	@Override
	public void run() {
		CSVdataSource csvSource;

		this.threadName = Thread.currentThread().getName();

		try {
			csvSource = new CSVdataSource(this.activeConn.getFilePath());
		} catch (FileNotFoundException e) {
			registerException(e);
			System.out.println("The exception encountered during file open is: " + e.toString());
			return;
		}
		
		while (readAndCheckIfLineExists(csvSource) && headerRowNotReached()) {
			if (exceptionMet()) {
				System.out.println(
						"The exception encountered during line read is: " + this.encounterdException.toString());
				return;
			}
			if (isCurrentRowHeaderRow()) {
				setHeaderRowFound();
			}
		}

		if (headerRowNotReached()) {
			this.encounterdException = new ExceptionsPack.contentNotFound(
					"Too few rows in the file and Header starting Row was not reached until End of file.");
			registerException(this.encounterdException);
			System.out.println(this.encounterdException.toString());
			return;
		}

		ContentCSVparser csvProcessor = new ContentCSVparser();
		String[] rowSplit = csvProcessor.parseCSVrow(this.rowContent, delimiter());

		if (rowSplit == null) {
			return;
		}

		for (int i = this.startHeaderCell.getColCoordinates(); i <= rowSplit.length - 1; i++) {
			this.selectedHeader.add(new HeaderEntry(rowSplit[i], true));
		}
		
		this.parentHeaderObject.storeHeaderScan(this.selectedHeader);
		this.parentHeaderObject.storeException(this.encounterdException);
	}

	private boolean readAndCheckIfLineExists(CSVdataSource csvSource) {
		try {
			this.tempContent = csvSource.getNextLine();
			if (this.tempContent != null) {
				this.currentRow++;
				return true;
			}
			return false;
		} catch (Exception e) {
			registerException(e);
			return false;
		}
	}

	private void registerException(Exception e) {
		this.exceptionFlag = true;
		this.encounterdException = e;
	}

	private boolean exceptionMet() {
		return this.exceptionFlag;
	}

	private boolean isCurrentRowHeaderRow() {
		if (this.currentRow == this.startHeaderCell.getRowCoordinates()) {
			return true;
		}
		return false;
	}

	private void setHeaderRowFound() {
		this.rowContent=this.tempContent;
		this.headerRowFound = true;
	}

	private String delimiter() {
		return this.activeConn.getCSVDelimiter();
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
