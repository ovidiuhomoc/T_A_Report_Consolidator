package time_and_attendance_report_consolidator.Tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Global Test Suite, containing all other test suites")
@SelectClasses({ Test_Suite_1_Profile_setup_and_methods.class, Test_Suite_2_TA_report_setup.class,
		Test_Suite_3_Connections.class, Test_Suite_4_Header_Scanning.class })
public class Global_Test_Suite {
}
