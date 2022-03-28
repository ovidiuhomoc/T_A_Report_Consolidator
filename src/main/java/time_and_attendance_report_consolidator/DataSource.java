package time_and_attendance_report_consolidator;

import java.io.IOException;

public interface DataSource {
	
	public String getNextLine() throws IOException;

}
