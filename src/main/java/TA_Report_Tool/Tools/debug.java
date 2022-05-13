package TA_Report_Tool.Tools;

import static TA_Report_Tool.Tools.check.*;

import java.util.ArrayList;

public class debug {
	private static boolean displayFlag = false;
	private static ArrayList<String> toDisplay = new ArrayList<>();

	public static void displayOn(String cls) {
		displayFlag = true;
		toDisplay.add(cls);
	}

	public static void displayOff() {
		displayFlag = false;
		toDisplay.clear();
	}

	public static void display(String cls, String string) {
		if (isFalse(displayFlag)) {
			return;
		}

		if (toDisplay.contains(cls)) {
			System.out.println(string);
		}
	}

}
