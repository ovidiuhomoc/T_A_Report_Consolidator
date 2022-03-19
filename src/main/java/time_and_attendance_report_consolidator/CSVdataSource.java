package time_and_attendance_report_consolidator;

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

		try {
			this.csvReader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(e.getLocalizedMessage());
		}
	}

}
