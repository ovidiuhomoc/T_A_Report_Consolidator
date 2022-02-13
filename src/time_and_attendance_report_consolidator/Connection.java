package time_and_attendance_report_consolidator;

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

	/*
	 * required for recycling; connection tests will be done at the moment of
	 * parsing file public void setConnection(String name, String type, String file)
	 * throws IOException { this.name = name; this.type = type; this.file = file;
	 * 
	 * if (file.length() > 4) { String extension = file.substring(file.length() -
	 * 3);
	 * 
	 * if (extension.equals("csv") || extension.equals("CSV")) {
	 * 
	 * File csvFile = new File(file); if (!csvFile.isFile()) { throw new
	 * IllegalArgumentException("File can't be accessed!"); }
	 * 
	 * BufferedReader csvReader = new BufferedReader(new FileReader(file));
	 * csvReader.close(); } else { throw new
	 * IllegalArgumentException("Not CSV file!"); } } else { throw new
	 * IllegalArgumentException("Unexpected short file name!"); } }
	 */

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
