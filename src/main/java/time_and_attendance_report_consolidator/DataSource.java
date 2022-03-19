package time_and_attendance_report_consolidator;

import java.io.IOException;

public interface DataSource {
	
	String getNextLine() throws IOException;

}
