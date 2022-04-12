package TA_Report_Tool.Tools;

public class check {

	public static <T> boolean isNull(T columnName) {
		if (columnName == null) {
			return true;
		}
		return false;
	}

}
