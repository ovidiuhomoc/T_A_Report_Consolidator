package TA_Report_Tool.MainApp;

public class Connection {
	private String type;
	private String path;
	private String name;

	/**
	 * This method sets or modifies the details of a connection object
	 * <p>
	 * 
	 * @param name Name that should replace old entry
	 * @param type Type of connection (CSV, Excel, MSQL, HTML) that should replace
	 *             the old type
	 * @param file The full path to the connection
	 */
	public void setConnection(String name, String type, String file) {
		this.name = name;
		this.type = type;
		this.path = file;
	}

	private String delimiter = ",";

	public void setCSVDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getCSVDelimiter() {
		return this.delimiter;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public boolean isCSVtype() {
		if (this.type == "CSV") {
			return true;
		}
		return false;
	}

	public String getFilePath() {
		return this.path;
	}
}
