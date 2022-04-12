package TA_Report_Tool.Test_Suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import TA_Report_Tool.Data.Testing_the_mapping_collection_creation_and_methods;
import TA_Report_Tool.Tests.Testing_the_TableHeader_scanning_in_profile;
import TA_Report_Tool.Tests.Testing_the_Header_scanning_tools;


@Suite
@SelectClasses({ Testing_the_Header_scanning_tools.class,Testing_the_TableHeader_scanning_in_profile.class,Testing_the_mapping_collection_creation_and_methods.class})
class Test_Suite_4_Header_Scanning {
}
