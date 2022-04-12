package TA_Report_Tool.Test_Suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import TA_Report_Tool.Tests.Testing_the_Profile_methods;
import TA_Report_Tool.Tests.Testing_the_Profile_setup;

@Suite
@SelectClasses({ Testing_the_Profile_setup.class, Testing_the_Profile_methods.class })
class Test_Suite_1_Profile_setup_and_methods {
}
