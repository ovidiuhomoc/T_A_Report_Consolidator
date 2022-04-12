package TA_Report_Tool.Test_Suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import TA_Report_Tool.Tests.*;

@Suite
@SelectClasses({ Testing_the_TA_report_setup.class, Testing_the_TA_report_events_filters.class })
class Test_Suite_2_TA_report_setup {
}
