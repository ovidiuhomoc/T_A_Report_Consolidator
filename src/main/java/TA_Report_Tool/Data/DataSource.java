package TA_Report_Tool.Data;

import java.io.IOException;

public interface DataSource {
	
	public String getNextLine() throws IOException;
	
	public void closeFile() throws IOException;

}
