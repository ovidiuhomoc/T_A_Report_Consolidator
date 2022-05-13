package TA_Report_Tool.Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVdataSource implements DataSource {
	private BufferedReader csvReader = null;
	private String rowContent = null;
	private FileReader fileReader = null;

	@Override
	public String getNextLine() throws IOException {
		this.rowContent = this.csvReader.readLine();
		return this.rowContent;
	}

	public CSVdataSource(String filePath) throws FileNotFoundException {
		this.fileReader = new FileReader(filePath);
		this.csvReader = new BufferedReader(this.fileReader);
	}

	@Override
	public void closeFile() throws IOException {
		this.fileReader.close();		
	}
}
