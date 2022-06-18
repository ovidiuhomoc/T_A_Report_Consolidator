package TA_Report_Tool.Tools;

public class check {

	public static <T> boolean isNull(T toCheck) {
		if (toCheck == null) {
			return true;
		}
		return false;
	}

	public static <T> boolean ifNull(T toCheck) {
		return isNull(toCheck);
	}

	public static <T> boolean isNotNull(T toCheck) {
		if (toCheck != null) {
			return true;
		}
		return false;
	}

	public static <T> boolean ifNotNull(T toCheck) {
		return isNotNull(toCheck);
	}

	public static boolean isFalse(boolean toCheck) {
		if (!toCheck) {
			return true;
		}
		return false;
	}

	public static boolean ifFalse(boolean toCheck) {
		return isFalse(toCheck);
	}

	public static boolean isZero(int number) {
		if (number == 0) {
			return true;
		}
		return false;
	}

	public static boolean ifZero(int number) {
		return isZero(number);
	}

	public static boolean isEmpty(String string) {
		if (string.equals("")) {
			return true;
		}
		return false;
	}
}
