package TA_Report_Tool.Tests;

import java.util.Arrays;

import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.MainApp.Profile;

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

	public boolean headerEntryTypeUnorderedEquality(ColumnProperties[] a, ColumnProperties[] b) {
		if (a.length != b.length) {
			return false;
		}

		int size = a.length;
		String[] aColName = new String[size];
		String[] bColName = new String[size];

		boolean[] aStatus = new boolean[size];
		boolean[] bStatus = new boolean[size];

		for (int i = 0; i < size; i++) {
			aColName[i] = a[i].getName();
			bColName[i] = b[i].getName();

			aStatus[i] = a[i].loadData();
			bStatus[i] = b[i].loadData();
		}

		for (int i = 0; i < size; i++) {
			if (!Arrays.asList(aColName).contains(bColName[i])) {
				return false;
			}
			if (aStatus[Arrays.asList(aColName).indexOf(bColName[i])] != bStatus[i]) {
				return false;
			}
		}
		return true;
	}
}
