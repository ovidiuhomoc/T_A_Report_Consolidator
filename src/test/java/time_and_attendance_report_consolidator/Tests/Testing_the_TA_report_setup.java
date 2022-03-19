package time_and_attendance_report_consolidator.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import time_and_attendance_report_consolidator.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Testing_the_TA_report_setup {

	@Test
	void Testing_the_report_period_methods() {
		Profile.reset();
		Profile testProfile = new Profile("Test_Profile");
		assertEquals(1, testProfile.getRepMonthsPeriod());

		testProfile.setRepMonthsPeriod(3);
		assertEquals(3, testProfile.getRepMonthsPeriod());
	}
}
