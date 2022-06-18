package TA_Report_Tool.Tools;

import static TA_Report_Tool.Tools.check.*;

import java.util.ArrayList;

public class debug {
	private static boolean displayFlag = false;
	private static ArrayList<String> toDisplay = new ArrayList<>();

	public static void debugDisplayOn(String className) {
		displayFlag = true;
		toDisplay.add(className);
	}

	public static void debugDisplayOff() {
		displayFlag = false;
		toDisplay.clear();
	}

	public static void debugDisplay(String className, String string) {
		if (isFalse(displayFlag)) {
			return;
		}

		if (toDisplay.contains(className)) {
			System.out.println(string);
		}
	}

}
