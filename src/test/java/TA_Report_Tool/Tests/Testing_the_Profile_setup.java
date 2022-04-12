package TA_Report_Tool.Tests;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.MainApp.Profile;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.Tools_for_Tests.Tools_Random_Generator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_Profile_setup {

	@Test
	void Testing_Profile_constructor() {
		try {
			int count = 0;
			ArrayList<Profile> profileArray = new ArrayList<Profile>();

			Profile.reset();

			// Testing constructor with name
			count++;
			profileArray.add(new Profile("Profile " + count));
			assertEquals("Profile " + count, profileArray.get(count - 1).getName());

			// Testing constructor without name
			count++;
			profileArray.add(new Profile());
			assertEquals("Profile " + count, profileArray.get(count - 1).getName());

			Tools_Random_Generator random = new Tools_Random_Generator();
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

			// Testing constructor with null
			count++;
			string = null;
			profileArray.add(new Profile(string));
			assertEquals(string, profileArray.get(count - 1).getName());
		} catch (nullArgument e) {
			e.printStackTrace();
		}

	}

}