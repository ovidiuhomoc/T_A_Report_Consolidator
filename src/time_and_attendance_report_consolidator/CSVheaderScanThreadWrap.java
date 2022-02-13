package time_and_attendance_report_consolidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CSVheaderScanThreadWrap implements Runnable {
	private Connection activeConn;
	private Exception encounterdException;
	private boolean exceptionFlag = false;
	private BufferedReader csvReader;
	private String rowContent = "";
	private tableCell startHeaderCell;
	private boolean headerRowFound = false;
	private int currentRow = -1;
	private ArrayList<HeaderEntry> selectedHeader = new ArrayList<HeaderEntry>();
	private boolean scanActive = false;
	private Header parentHeaderObject;

	public void setActiveConn(Connection activeConn) {
		this.activeConn = activeConn;
	}

	public void setHeaderStartCell(tableCell startCell) {
		this.startHeaderCell = startCell;
	}

	private String filePath() {
		return this.activeConn.getFilePath();
	}

	private void setExceptionFlag() {
		this.exceptionFlag = true;
	}

	private boolean isException() {
		if (this.exceptionFlag) {
			return true;
		}
		return false;
	}

	private void fileOpen() {
		try {
			this.csvReader = new BufferedReader(new FileReader(filePath()));
		} catch (Exception e) {
			this.encounterdException = e;
			setExceptionFlag();
		}
	}

	private boolean readAndCheckIfLineExists() {
		try {
			this.rowContent = this.csvReader.readLine();
			if (this.rowContent != null) {
				this.currentRow++;
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			this.encounterdException = e;
			setExceptionFlag();
			return false;
		}
	}

	private boolean headerRowNotReached() {
		if (!this.headerRowFound) {
			return true;
		}
		return false;
	}

	private boolean isCurrentRowHeaderRow() {
		if (this.currentRow == this.startHeaderCell.getRowCoordinates()) {
			return true;
		}
		return false;
	}

	private void setHeaderRowFound() {
		this.headerRowFound = true;
	}

	private String delimiter() {
		return this.activeConn.getCSVDelimiter();
	}

	public CSVheaderScanThreadWrap(Header parentHeaderObject) {
		this.parentHeaderObject = parentHeaderObject;
	}
	
	public boolean isScanActive() {
		return this.scanActive;
	}
	
	@Override
	public void run() {
		this.scanActive = true;
		this.parentHeaderObject.scanStatus(this.scanActive);

		this.fileOpen();
		if (this.isException()) {
			System.out.println("The exception encountered during file open is: " + this.encounterdException.toString());
			this.scanActive = false;
			return;
		}

		while (this.readAndCheckIfLineExists() && this.headerRowNotReached()) {
			if (this.isException()) {
				System.out.println(
						"The exception encountered during line read is: " + this.encounterdException.toString());
				this.scanActive = false;
				return;
			}

			if (this.isCurrentRowHeaderRow()) {
				this.setHeaderRowFound();
			}
		}

		ContentCSVparser csvProcessor = new ContentCSVparser();
		String[] rowSplit = csvProcessor.parseCSVrow(this.rowContent, this.delimiter());

		for (int i = this.startHeaderCell.getColCoordinates(); i <= rowSplit.length - 1; i++) {
			this.selectedHeader.add(new HeaderEntry(rowSplit[i], true));
		}

		if (this.headerRowNotReached()) {
			this.encounterdException = new ExceptionsPack.contentNotFound(
					"Too few rows in the file and Header starting Row was not reached until End of file.");
			setExceptionFlag();
		}

		if (this.isException()) {
			System.out.println(this.encounterdException.toString());
			this.scanActive = false;
			return;
		}

		this.parentHeaderObject.storeHeaderScan(this.selectedHeader);
		this.scanActive = false;
		this.parentHeaderObject.setHeaderLength(this.selectedHeader.size());
		this.parentHeaderObject.storeException(this.encounterdException);
		this.parentHeaderObject.scanStatus(this.scanActive);		
	}

}
