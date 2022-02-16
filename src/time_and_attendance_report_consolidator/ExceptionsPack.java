package time_and_attendance_report_consolidator;

public class ExceptionsPack {

	public static class contentNotFound extends Exception {
		public contentNotFound(String msg) {
			super(msg);
		}
	}

	public static class scanError extends Exception {
		public scanError(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
	
	public static class profileDoesNotExist extends Exception {
		public profileDoesNotExist(String msg) {
			super(msg);
		}
	}
}
