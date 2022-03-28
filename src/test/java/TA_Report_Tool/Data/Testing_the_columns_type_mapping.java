package TA_Report_Tool.Data;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import time_and_attendance_report_consolidator.ExceptionsPack;
import time_and_attendance_report_consolidator.ExceptionsPack.MappingFieldDoesNotExist;
import time_and_attendance_report_consolidator.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_columns_type_mapping {

	@Test
	void Mapping_object_created_on_profile_creation() {
		Profile testProfile = new Profile("Test Profile");
		assertTrue(testProfile.getHeaderMapping() != null);
	}

	@Test
	void Mapping_object_contains_minimum_number_of_necessary_mapping_fields() {
		Profile testProfile = new Profile("Test Profile");
		assertEquals(4, testProfile.getHeaderMapping().getCount());
	}

	@Test
	void Mapping_object_contains_minimum_necessary_mapping_fields() {
		Profile testProfile = new Profile("Test Profile");
		assertTrue(testProfile.getHeaderMapping().contains(mappingType.Date)
				|| testProfile.getHeaderMapping().contains(mappingType.DateAndTime));
		assertTrue(testProfile.getHeaderMapping().contains(mappingType.Time)
				|| testProfile.getHeaderMapping().contains(mappingType.DateAndTime));
		assertTrue(testProfile.getHeaderMapping().contains(mappingType.EmployeeUniqueId));
		assertTrue(testProfile.getHeaderMapping().contains(mappingType.NotSet));
	}

	@Test
	void Mapping_object_returns_mapping_field_by_name() {
		Profile testProfile = new Profile("Test Profile");
		try {
			assertNotNull(testProfile.getHeaderMapping().getHeaderMappingFieldByName("Date1"));
			assertNotNull(testProfile.getHeaderMapping().getHeaderMappingFieldByName("Time1"));
			assertNotNull(testProfile.getHeaderMapping().getHeaderMappingFieldByName("Employee Unique ID"));
			assertThrows(ExceptionsPack.MappingFieldDoesNotExist.class, () -> {
				testProfile.getHeaderMapping().getHeaderMappingFieldByName("Mapping Field doesn't exists");
			});

			assertFalse(testProfile.getHeaderMapping().contains(mappingType.EmployeeFullName));
			testProfile.getHeaderMapping().addMappingField("Employee Full Name", new MaskTemplate().getObj(),
					mappingType.EmployeeFullName);
			assertTrue(testProfile.getHeaderMapping().contains(mappingType.EmployeeFullName));
			
			HeaderMappingField fullNameEmplMappField = testProfile.getHeaderMapping().getHeaderMappingFieldByName("Employee Full Name");
			assertFalse(fullNameEmplMappField.isMaskSet());
			
			MaskTemplate fullNameEmplMask = new MaskTemplate().addAnyString().addSep(" ").addAnyString();
			fullNameEmplMappField.setMask(fullNameEmplMask);
			assertEquals(fullNameEmplMask,testProfile.getHeaderMapping().getHeaderMappingFieldByName("Employee Full Name").getMask());
		} catch (MappingFieldDoesNotExist e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void Mapping_object_returns_mapping_field_by_type() {
		Profile testProfile = new Profile("Test Profile");
		try {
			assertEquals("Date1",testProfile.getHeaderMapping().getHeaderMappingFieldByType(mappingType.Date).getName());
			assertEquals("Time1",testProfile.getHeaderMapping().getHeaderMappingFieldByType(mappingType.Time).getName());
			assertEquals("Employee Unique ID",testProfile.getHeaderMapping().getHeaderMappingFieldByType(mappingType.EmployeeUniqueId).getName());
			assertThrows(ExceptionsPack.MappingFieldDoesNotExist.class, () -> {
				testProfile.getHeaderMapping().getHeaderMappingFieldByType(mappingType.SignalingDevice);
			});

			
			HeaderMappingField fullNameEmplMappField = new HeaderMappingField("Employee Full Name", new MaskTemplate().getObj(),
					mappingType.EmployeeFullName);
			testProfile.getHeaderMapping().addMappingField(fullNameEmplMappField);			
			assertFalse(fullNameEmplMappField.isMaskSet());
			
			MaskTemplate fullNameEmplMask = new MaskTemplate().addAnyString().addSep(" ").addAnyString();
			fullNameEmplMappField.setMask(fullNameEmplMask);
			assertEquals(fullNameEmplMask,testProfile.getHeaderMapping().getHeaderMappingFieldByType(mappingType.EmployeeFullName).getMask());
		} catch (MappingFieldDoesNotExist e) {
			e.printStackTrace();
		}
	}
}
