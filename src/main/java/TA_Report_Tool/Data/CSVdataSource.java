package TA_Report_Tool.Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVdataSource implements DataSource {
	private BufferedReader csvReader = null;
	private String rowContent = null;

	public String getNextLine() throws IOException {
		this.rowContent = this.csvReader.readLine();
		return this.rowContent;
	}

	public CSVdataSource(String filePath) throws FileNotFoundException {
		this.csvReader = new BufferedReader(new FileReader(filePath));
	}
}
