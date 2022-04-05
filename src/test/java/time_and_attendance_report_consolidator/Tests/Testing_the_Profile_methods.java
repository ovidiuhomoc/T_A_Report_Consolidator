package time_and_attendance_report_consolidator.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.Profile;
import TA_Report_Tool.MainApp.ExceptionsPack.profileDoesNotExist;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Testing_the_Profile_methods {
	@Test
	void Testing_set_name() {
		int count = 0;
		ArrayList<Profile> profileArray = new ArrayList<Profile>();
		Tools_Random_Generator random = new Tools_Random_Generator();
		String string = "";

		Profile.reset();

		// Testing blank constructor & set name
		count++;
		profileArray.add(new Profile());
		string = "Profil 1";
		profileArray.get(count - 1).setName(string);
		assertEquals(string, profileArray.get(count - 1).getName());

		count++;
		profileArray.add(new Profile());
		string = random.randomTextAlphaNumeric(20);
		profileArray.get(count - 1).setName(string);
		assertEquals(string, profileArray.get(count - 1).getName());

		count++;
		string = random.randomTextAlphaNumericAndSymbols(20);
		profileArray.add(new Profile());
		profileArray.get(count - 1).setName(string);
		assertEquals(string, profileArray.get(count - 1).getName());

		count++;
		string = random.randomTextAlphaNumericAndBeginWithSymbols(20);
		profileArray.add(new Profile());
		profileArray.get(count - 1).setName(string);
		assertEquals(string, profileArray.get(count - 1).getName());

		count++;
		string = random.randomTextAlphaNumericAndEndWithSymbols(20);
		profileArray.add(new Profile());
		profileArray.get(count - 1).setName(string);
		assertEquals(string, profileArray.get(count - 1).getName());

		count++;
		string = random.randomTextAlphaNumericAndSymbolsBalancedDistribution(20);
		profileArray.add(new Profile());
		profileArray.get(count - 1).setName(string);
		assertEquals(string, profileArray.get(count - 1).getName());

		count++;
		string = null;
		profileArray.add(new Profile());
		profileArray.get(count - 1).setName(string);
		assertEquals(string, profileArray.get(count - 1).getName());
	}

	@Test
	void Testing_toArray() {
		int count = 0;
		ArrayList<Profile> profileArray = new ArrayList<Profile>();
		Tools_Random_Generator random = new Tools_Random_Generator();
		String string = "";

		Profile.reset();

		// Testing blank constructor & set name
		count++;
		string = "Profil 1";
		profileArray.add(new Profile(string));

		count++;
		string = random.randomTextAlphaNumeric(20);
		profileArray.add(new Profile(string));

		count++;
		string = random.randomTextAlphaNumericAndSymbols(20);
		profileArray.add(new Profile(string));

		count++;
		string = random.randomTextAlphaNumericAndBeginWithSymbols(20);
		profileArray.add(new Profile(string));

		count++;
		string = random.randomTextAlphaNumericAndEndWithSymbols(20);
		profileArray.add(new Profile(string));

		count++;
		string = random.randomTextAlphaNumericAndSymbolsBalancedDistribution(20);
		profileArray.add(new Profile(string));

		count++;
		string = null;
		profileArray.add(new Profile(string));

		assertTrue(new Tools_Array_Equality_Test().TestProfileArrayUnorderedEquality(profileArray.toArray(new Profile[0]),
				Profile.toArray()));
	}

	@Test
	void Testing_Active_Profile_and_Removing() {
		int count = 0;
		ArrayList<Profile> profileArray = new ArrayList<Profile>();
		Tools_Random_Generator random = new Tools_Random_Generator();

		Profile.reset();

		count = 1; // 1 profile
		profileArray.add(new Profile("Profil 1"));
		assertEquals(profileArray.get(0), Profile.getActive());

		count = 2;// 2 profiles
		profileArray.add(new Profile(random.randomTextAlphaNumeric(20)));
		assertEquals(profileArray.get(0), Profile.getActive());

		count = 3;// 3 profiles
		profileArray.add(new Profile(random.randomTextAlphaNumericAndSymbols(20)));
		profileArray.get(count - 1).setActive();
		assertEquals(profileArray.get(count - 1), Profile.getActive());

		try {
			Profile.removeProfile(null); // still 3 profiles
		} catch (profileDoesNotExist e) {
			e.printStackTrace();
		}
		assertEquals(profileArray.get(count - 1), Profile.getActive());

		try {
			Profile.removeProfile(profileArray.get(count - 1)); // 2 profiles after this line
			count = 2;
		} catch (profileDoesNotExist e) {
			e.printStackTrace();
		}
		assertTrue(Profile.getActive() != null);

		try {
			Profile.removeProfile(profileArray.get(count - 1));// 1 profile after this line
			count = 1;
		} catch (profileDoesNotExist e) {
			e.printStackTrace();
		}

		try {
			Profile.removeProfile(profileArray.get(count - 1));// 0 profiles after this line
			count = 0;
		} catch (profileDoesNotExist e) {
			e.printStackTrace();
		}
		assertTrue(Profile.getActive() == null);

		assertThrows(ExceptionsPack.profileDoesNotExist.class, () -> {
			Profile.removeProfile(profileArray.get(0));
		});

	}
}
