package TA_Report_Tool.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.MainApp.Profile;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_TA_report_setup {

	@Test
	void Testing_the_report_period_methods() {
		try {
			Profile.reset();
			Profile testProfile = new Profile("Test_Profile");
			assertEquals(1, testProfile.getRepMonthsPeriod());

			testProfile.setRepMonthsPeriod(3);
			assertEquals(3, testProfile.getRepMonthsPeriod());
		} catch (nullArgument e) {
			e.printStackTrace();
		}
	}
}
