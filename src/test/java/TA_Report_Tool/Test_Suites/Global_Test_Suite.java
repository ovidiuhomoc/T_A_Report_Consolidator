package TA_Report_Tool.Test_Suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Global Test Suite, containing all other test suites")
@SelectClasses({ Test_Suite_1_Profile_setup_and_methods.class, Test_Suite_2_TA_report_setup.class,
		Test_Suite_3_Connections.class, Test_Suite_4_Header_Scanning.class,Test_Suite_5_content_parsing.class})
public class Global_Test_Suite {
}
