package time_and_attendance_report_consolidator.Tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Test suite for Profile setup & methods")
@SelectClasses({ Testing_the_Profile_setup.class, Testing_the_Profile_methods.class })
public class Test_Suite {
}
