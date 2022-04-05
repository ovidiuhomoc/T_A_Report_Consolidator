package TA_Report_Tool.MainApp;

public class ExceptionsPack {

	public static class ParsingFailedDueToNullMappingMask extends Exception {
		public ParsingFailedDueToNullMappingMask(String msg) {
			super(msg);
		}
	}

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
	
	public static class nullNameConnection extends Exception {
		public nullNameConnection(String msg) {
			super(msg);
		}
	}
	
	public static class connectionNotInitialized extends Exception {
		public connectionNotInitialized(String msg) {
			super(msg);
		}
	}
	
	public static class MappingFieldDoesNotExist extends Exception {
		public MappingFieldDoesNotExist(String msg) {
			super(msg);
		}
	}
	
	public static class SearchedHeaderColumnIsNull extends Exception {
		public SearchedHeaderColumnIsNull(String msg) {
			super(msg);
		}
	}
	
	public static class HeaderColumnDoesNotExist extends Exception {
		public HeaderColumnDoesNotExist(String msg) {
			super(msg);
		}
	}
	
	public static class CantBeParsedWithCurrentMappingMask extends Exception {
		public CantBeParsedWithCurrentMappingMask(String msg) {
			super(msg);
		}
	}
}
