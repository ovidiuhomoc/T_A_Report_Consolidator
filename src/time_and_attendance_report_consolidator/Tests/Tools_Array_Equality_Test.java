package time_and_attendance_report_consolidator.Tests;

import java.util.Arrays;

import time_and_attendance_report_consolidator.Profile;

public class Tools_Array_Equality_Test {

	public boolean TestStringArrayEquality(String[] a, String[] b) {
		if (a.length != b.length) {
			return false;
		}

		for (int i = 0; i < a.length; i++) {
			if (a[i].equals(b[i])) {
				return false;
			}
		}

		return true;
	}
	
	public boolean TestStringArrayUnorderedEquality(String[] a, String[] b) {
		if (a.length != b.length) {
			return false;
		}

		for (int i = 0; i < b.length; i++) {
			if (!Arrays.asList(a).contains(b[i])) {
				return false;
			}
		}
		return true;
	}

	public boolean TestProfileArrayUnorderedEquality(Profile[] a, Profile[] b) {
		if (a.length != b.length) {
			return false;
		}

		for (int i = 0; i < b.length; i++) {
			if (!Arrays.asList(a).contains(b[i])) {
				return false;
			}
		}
		return true;
	}

}
