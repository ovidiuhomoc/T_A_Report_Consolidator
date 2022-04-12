package TA_Report_Tool.Data;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.Profile;
import TA_Report_Tool.MainApp.ExceptionsPack.mappingUnitDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_mapping_collection_creation_and_methods {

	@Test
	void Mapping_collection_created_on_profile_creation() {
		try {
			Profile testProfile = new Profile("Test Profile");
			assertTrue(testProfile.getMappingCollection() != null);
		} catch (nullArgument e) {
			e.printStackTrace();
		}
	}

	@Test
	void Mapping_collection_contains_minimum_number_of_necessary_mapping_units() {
		try {
			Profile testProfile = new Profile("Test Profile");
			assertEquals(5, testProfile.getMappingCollection().getCount());
		} catch (nullArgument e) {
			e.printStackTrace();
		}
	}

	@Test
	void Mapping_collection_contains_minimum_necessary_mapping_units() {
		try {
			Profile testProfile = new Profile("Test Profile");
			assertTrue(testProfile.getMappingCollection().contains(MappingType.Date)
					|| testProfile.getMappingCollection().contains(MappingType.DateAndTime));
			assertTrue(testProfile.getMappingCollection().contains(MappingType.Time)
					|| testProfile.getMappingCollection().contains(MappingType.DateAndTime));
			assertTrue(testProfile.getMappingCollection().contains(MappingType.EmployeeUniqueId));
			assertTrue(testProfile.getMappingCollection().contains(MappingType.NotSet));
		} catch (nullArgument e) {
			e.printStackTrace();
		}
	}

	@Test
	void Mapping_collection_returns_mapping_unit_by_name() {
		try {
			Profile testProfile = new Profile("Test Profile");
			assertNotNull(testProfile.getMappingCollection().getMappingUnitdByName("Date1"));
			assertNotNull(testProfile.getMappingCollection().getMappingUnitdByName("Time1"));
			assertNotNull(testProfile.getMappingCollection().getMappingUnitdByName("Employee Unique ID"));
			assertThrows(ExceptionsPack.mappingUnitDoesNotExist.class, () -> {
				testProfile.getMappingCollection().getMappingUnitdByName("Mapping Field doesn't exists");
			});

			assertFalse(testProfile.getMappingCollection().contains(MappingType.EmployeeFullName));
			testProfile.getMappingCollection().addMappingUnit("Employee Full Name", new MaskTemplate(),
					MappingType.EmployeeFullName);
			assertTrue(testProfile.getMappingCollection().contains(MappingType.EmployeeFullName));

			MappingUnit fullNameEmplMappField = testProfile.getMappingCollection()
					.getMappingUnitdByName("Employee Full Name");
			assertFalse(fullNameEmplMappField.isMaskSet());

			MaskTemplate fullNameEmplMask = new MaskTemplate().addAnyString().addSep(" ").addAnyString();
			fullNameEmplMappField.setMask(fullNameEmplMask);
			assertEquals(fullNameEmplMask,
					testProfile.getMappingCollection().getMappingUnitdByName("Employee Full Name").getMask());
		} catch (mappingUnitDoesNotExist | nullArgument e) {
			e.printStackTrace();
		}
	}

	@Test
	void Mapping_collection_returns_mapping_unit_by_type() {
		try {
			Profile testProfile = new Profile("Test Profile");
			assertEquals("Date1", testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Date).getName());
			assertEquals("Time1", testProfile.getMappingCollection().getMappingUnitdByType(MappingType.Time).getName());
			assertEquals("Employee Unique ID",
					testProfile.getMappingCollection().getMappingUnitdByType(MappingType.EmployeeUniqueId).getName());
			assertThrows(ExceptionsPack.mappingUnitDoesNotExist.class, () -> {
				testProfile.getMappingCollection().getMappingUnitdByType(MappingType.SignalingDevice);
			});

			MappingUnit fullNameEmplMappField = new MappingUnit("Employee Full Name", new MaskTemplate(),
					MappingType.EmployeeFullName);
			testProfile.getMappingCollection().addMappingUnit(fullNameEmplMappField);
			assertFalse(fullNameEmplMappField.isMaskSet());

			MaskTemplate fullNameEmplMask = new MaskTemplate().addAnyString().addSep(" ").addAnyString();
			fullNameEmplMappField.setMask(fullNameEmplMask);
			assertEquals(fullNameEmplMask,
					testProfile.getMappingCollection().getMappingUnitdByType(MappingType.EmployeeFullName).getMask());
		} catch (mappingUnitDoesNotExist | nullArgument e) {
			e.printStackTrace();
		}
	}
}
