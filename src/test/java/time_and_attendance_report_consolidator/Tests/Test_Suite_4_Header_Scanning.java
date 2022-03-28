package time_and_attendance_report_consolidator.Tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import TA_Report_Tool.Data.Testing_the_columns_type_mapping;


@Suite
@SelectClasses({ Testing_the_Header_scanning_tools.class,Testing_the_Header_scanning_in_profile.class,Testing_the_columns_type_mapping.class})
class Test_Suite_4_Header_Scanning {
}
