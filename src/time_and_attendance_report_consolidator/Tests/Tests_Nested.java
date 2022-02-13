package time_and_attendance_report_consolidator.Tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import time_and_attendance_report_consolidator.Profile;

class Tests_Nested {

	@Nested
	@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
	class Testing_the_profile_setup {

		@Test
		void Testing_profile_constructor() {
			int count = 0;
			ArrayList<Profile> profileArray = new ArrayList<Profile>();

			// Testing constructor with name
			count++;
			profileArray.add(new Profile("Profile " + count));
			assertEquals("Profile " + count, profileArray.get(count - 1).getName());

			// Testing constructor without name
			count++;
			profileArray.add(new Profile());
			assertEquals("Profile " + count, profileArray.get(count - 1).getName());

			Random_Generator random = new Random_Generator();
			String string = "";

			// Testing constructor with name + random type 1
			count++;
			string = random.randomTextAlphaNumeric(20);
			profileArray.add(new Profile(string));
			assertEquals(string, profileArray.get(count - 1).getName());

			// Testing constructor with name + random type 2
			count++;
			string = random.randomTextAlphaNumericAndSymbols(20);
			profileArray.add(new Profile(string));
			assertEquals(string, profileArray.get(count - 1).getName());

			// Testing constructor with name + random type 3
			count++;
			string = random.randomTextAlphaNumericAndBeginWithSymbols(20);
			profileArray.add(new Profile(string));
			assertEquals(string, profileArray.get(count - 1).getName());

			// Testing constructor with name + random type 4
			count++;
			string = random.randomTextAlphaNumericAndEndWithSymbols(20);
			profileArray.add(new Profile(string));
			assertEquals(string, profileArray.get(count - 1).getName());

			// Testing constructor with name + random type 5
			count++;
			string = random.randomTextAlphaNumericAndSymbolsBalancedDistribution(20);
			profileArray.add(new Profile(string));
			assertEquals(string, profileArray.get(count - 1).getName());

		}
	}
	//test
}
