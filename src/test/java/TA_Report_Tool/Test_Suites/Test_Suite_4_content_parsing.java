package TA_Report_Tool.Test_Suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import TA_Report_Tool.Data.Testing_the_storage_in_TableData;
import TA_Report_Tool.Processors.Testing_the_content_parser_by_mapping_unit;

@Suite
@SelectClasses({ Testing_the_content_parser_by_mapping_unit.class , Testing_the_storage_in_TableData.class})
class Test_Suite_4_content_parsing {
}
