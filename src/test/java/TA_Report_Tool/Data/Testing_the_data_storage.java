package TA_Report_Tool.Data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_data_storage {

	@Test
	void Mapping_object_created_on_profile_creation() {
		Profile testProfile;
		try {
			testProfile = new Profile("Test Profile");
		assertTrue(testProfile.getMappingCollection() != null);
		} catch (nullArgument e) {
			e.printStackTrace();
		}
	}
}
