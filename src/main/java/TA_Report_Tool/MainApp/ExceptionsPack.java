package TA_Report_Tool.MainApp;

public class ExceptionsPack {

	public static class tableDataMethodError extends Exception {
		public tableDataMethodError(String msg) {
			super(msg);
		}
	}

	public static class TAreportGenerationException extends Exception {
		public TAreportGenerationException(String msg) {
			super(msg);
		}
	}

	public static class cantParseEmptyStringForCurrentType extends Exception {
		public cantParseEmptyStringForCurrentType(String msg) {
			super(msg);
		}
	}

	public static class headerNotScanned extends Exception {
		public headerNotScanned(String msg) {
			super(msg);
		}
	}

	public static class tableDataNotInitialized extends Exception {
		public tableDataNotInitialized(String msg) {
			super(msg);
		}
	}

	public static class nullArgument extends Exception {
		public nullArgument(String msg) {
			super(msg);
		}
	}

	public static class dateOrTimeMissing extends Exception {
		public dateOrTimeMissing(String msg) {
			super(msg);
		}
	}

	public static class nullDataReceivedForParsing extends Exception {
		public nullDataReceivedForParsing(String msg) {
			super(msg);
		}
	}

	public static class tableLengthAndDataLengthNotMatching extends Exception {
		public tableLengthAndDataLengthNotMatching(String msg) {
			super(msg);
		}
	}

	public static class rowParameterNotHigherThanZero extends Exception {
		public rowParameterNotHigherThanZero(String msg) {
			super(msg);
		}
	}

	public static class parsingFailedDueToNullMappingMask extends Exception {
		public parsingFailedDueToNullMappingMask(String msg) {
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

	public static class searchCantFindMappingUnitInCollection extends Exception {
		public searchCantFindMappingUnitInCollection(String msg) {
			super(msg);
		}
	}

	public static class nullColumnPropertiesPassed extends Exception {
		public nullColumnPropertiesPassed(String msg) {
			super(msg);
		}
	}

	public static class columnPropertiesDoesNotExist extends Exception {
		public columnPropertiesDoesNotExist(String msg) {
			super(msg);
		}
	}

	public static class cantBeParsedWithCurrentMappingMask extends Exception {
		public cantBeParsedWithCurrentMappingMask(String msg) {
			super(msg);
		}
	}
}
