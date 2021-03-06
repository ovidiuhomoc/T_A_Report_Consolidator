package TA_Report_Tool.Processors;

import static TA_Report_Tool.Tools.check.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskingItem;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.cantBeParsedWithCurrentMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.cantParseEmptyStringForCurrentType;
import TA_Report_Tool.MainApp.ExceptionsPack.parsingFailedDueToNullMappingMask;

public class ContentParserByMappingUnit<T> {

	private ArrayList<ColumnProperties> listOfColumnProperties;
	private boolean unlimitedChNumberFlag = false;

	public ContentParserByMappingUnit(ArrayList<ColumnProperties> listOfColumnProperties) {
		this.listOfColumnProperties = listOfColumnProperties;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T parse(String stringToBeParsed, String columnName)
			throws nullColumnPropertiesPassed, InterruptedException, ExecutionException, connectionNotInitialized,
			columnPropertiesDoesNotExist, cantBeParsedWithCurrentMappingMask, parsingFailedDueToNullMappingMask,
			dateOrTimeMissing, nullArgument, cantParseEmptyStringForCurrentType {
		MappingUnit currentColumnMapping = getColPropertiesByColName(columnName).getMappingUnit();
		ArrayList<MaskingItem> mask = currentColumnMapping.getMask().toArrayList();

		this.unlimitedChNumberFlag = false;
		int minCountOfCh = this.getMinCountOfCh(mask);
		int maxCountOfCh = this.getMaxCountOfCh(mask);

		if ((isLimitedValue()) && !stringSizeWithinLimits(stringToBeParsed, minCountOfCh, maxCountOfCh)) {
			throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
					"The row can't be parsed with current mask as the string size does not fit in the expected size.\n The mask indicates a specific limited number of ch. and string does not fit in min("
							+ minCountOfCh + ") and max(" + maxCountOfCh + ").\nThe String is:" + stringToBeParsed
							+ " and its size is:" + stringToBeParsed.length() + " and column name is:" + columnName);
		}

		if ((!isLimitedValue()) && (stringToBeParsed.length() > maxCountOfCh)) {
			throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
					"The row can't be parsed with current mask as the string size does not fit in the expected size.\n The mask indicates a not a limited number of ch. but string is over system's capable max("
							+ maxCountOfCh + ").\nThe tring is:" + stringToBeParsed + " and its size is:"
							+ stringToBeParsed.length() + " and column name is:" + columnName);
		}

		switch (currentColumnMapping.getType()) {
		case Date:
			return (T) parseDate(stringToBeParsed, mask);
		case Time:
			return (T) parseTime(stringToBeParsed, mask);
		case DateAndTime:
			return (T) parseDateAndTime(stringToBeParsed, mask);
		case Number:
			return (T) parseNumber(stringToBeParsed, mask);
		case FixedDigitNumber:
			return (T) parseNumber(stringToBeParsed, mask);
		case SignalingDevice:
			return (T) stringToBeParsed;
		case EmployeeUniqueId:
			return (T) stringToBeParsed;
		case EmployeeFirstName:
			return (T) stringToBeParsed;
		case EmployeeMiddleName:
			return (T) stringToBeParsed;
		case EmployeeLastName:
			return (T) stringToBeParsed;
		case EmployeeFullName:
			return (T) stringToBeParsed;
		case Event:
			return (T) stringToBeParsed;
		case CustomFieldText:
			return (T) parseCustomText(stringToBeParsed, mask);
		case NotSet:
			return null;

		}
		return (T) stringToBeParsed;
	}

	private ColumnProperties getColPropertiesByColName(String columnName) throws nullArgument {
		if (isNull(columnName)) {
			throw new ExceptionsPack.nullArgument("Cannot look for a column which name argument is null");
		}
		for (ColumnProperties x : listOfColumnProperties) {
			if (x.getName().equals(columnName)) {
				return x;
			}
		}
		return null;
	}

	private String parseCustomText(String stringToBeParsed, ArrayList<MaskingItem> mask)
			throws cantBeParsedWithCurrentMappingMask {
		if (mask.get(0) == MaskingItem.AnyString) {
			return stringToBeParsed;
		}

		if (mask.get(0) == MaskingItem.Number) {
			for (int i = 0; i < stringToBeParsed.length(); i++) {
				char c = stringToBeParsed.charAt(i);
				if (!"0123456789".contains(String.valueOf(c))) {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected a number containing only digits from 0 to 9 and received "
									+ String.valueOf(c));
				}
			}
			return stringToBeParsed;
		}

		if (stringToBeParsed.length() != mask.size()) {
			throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
					"Parsing of string with current mapping failed.The mask covers a fixed size string (" + mask.size()
							+ ") and the string to be parsed has different length of " + stringToBeParsed.length());
		}

		for (int i = 0; i < mask.size(); i++) {
			MaskingItem currentMask = mask.get(i);
			char c = stringToBeParsed.charAt(i);

			switch (currentMask) {
			case SpaceSep:
				if (c != ' ') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected ' ' and received "
									+ String.valueOf(c));
				}
				break;
			case SepColons:
				if (c != ':') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected ':' and received "
									+ String.valueOf(c));
				}
				break;
			case SepSemiColons:
				if (c != ';') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected ';' and received "
									+ String.valueOf(c));
				}
				break;
			case SepDot:
				if (c != '.') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected '.' and received "
									+ String.valueOf(c));
				}
				break;
			case SepComma:
				if (c != ',') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected ',' and received "
									+ String.valueOf(c));
				}
				break;
			case SepExclamationMark:
				if (c != '!') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected '!' and received "
									+ String.valueOf(c));
				}
				break;
			case SepMinus:
				if (c != '-') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected '-' and received "
									+ String.valueOf(c));
				}
				break;
			case SepSlash:
				if (c != '/') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected '/' and received "
									+ String.valueOf(c));
				}
				break;
			case SepBackSlah:
				if (c != '\\') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected '\' and received "
									+ String.valueOf(c));
				}
				break;
			case SepVerticalLine:
				if (c != '|') {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected '|' and received "
									+ String.valueOf(c));
				}
				break;
			case SingleDigitInt:
				if (!"0123456789".contains(String.valueOf(c))) {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected a digit from 0 to 9 and received "
									+ String.valueOf(c));
				}
				break;
			case SingleCharacter:
				if (!"qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".contains(String.valueOf(c))) {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected an upperCase or lowerCase letter and received "
									+ String.valueOf(c));
				}
				break;
			case SingleSpecialCh:
				if (!"`~!@#$%^&*()_-+=[]{};':\",./<>?\\|/*-+".contains(String.valueOf(c))) {
					throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
							"Parsing of string with current mapping failed.Expected a special character and received "
									+ String.valueOf(c));
				}
				break;
			default:
				throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
						"Parsing of string with current mapping failed.Does not fit in any of cases and received "
								+ String.valueOf(c) + " The Masking Item is:" + currentMask);
			}
		}
		return stringToBeParsed;
	}

	private Object parseNumber(String stringToBeParsed, ArrayList<MaskingItem> mask)
			throws parsingFailedDueToNullMappingMask, cantParseEmptyStringForCurrentType {

		if (isEmpty(stringToBeParsed)) {
			throw new ExceptionsPack.cantParseEmptyStringForCurrentType(
					"Parsing failed as the string to be parsed is empty and is unsupported for current type");
		}
		if (stringToBeParsed.contains(".")) {
			return Float.parseFloat(stringToBeParsed);
		} else {
			return Integer.parseInt(stringToBeParsed);
		}

	}

	private Object parseTime(String stringToBeParsed, ArrayList<MaskingItem> mask)
			throws parsingFailedDueToNullMappingMask, cantBeParsedWithCurrentMappingMask,
			cantParseEmptyStringForCurrentType {
		LocalTime parsedTime = null;
		String maskStoredAsString = maskArrayListToStringFormaterFormat(mask);
		if (isNull(maskStoredAsString)) {
			throw new ExceptionsPack.parsingFailedDueToNullMappingMask(
					"Parsing of ArrayList Mapping returned null. ArrayList of MaskingItem is null");
		}

		if (isEmpty(stringToBeParsed)) {
			throw new ExceptionsPack.cantParseEmptyStringForCurrentType(
					"Parsing failed as the string to be parsed is empty and is unsupported for current type");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(maskStoredAsString);
		try {
			parsedTime = LocalTime.parse(stringToBeParsed, formatter);
		} catch (Exception e) {
			throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
					"Parsing of string with current mapping failed");
		}
		return parsedTime;

	}

	private LocalDate parseDate(String stringToBeParsed, ArrayList<MaskingItem> mask)
			throws parsingFailedDueToNullMappingMask, cantBeParsedWithCurrentMappingMask,
			cantParseEmptyStringForCurrentType {
		LocalDate parsedDate = null;
		String maskStoredAsString = maskArrayListToStringFormaterFormat(mask);
		if (isNull(maskStoredAsString)) {
			throw new ExceptionsPack.parsingFailedDueToNullMappingMask(
					"Parsing of ArrayList Mapping returned null. ArrayList of MaskingItem is null");
		}

		if (isEmpty(stringToBeParsed)) {
			throw new ExceptionsPack.cantParseEmptyStringForCurrentType(
					"Parsing failed as the string to be parsed is empty and is unsupported for current type");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(maskStoredAsString);
		try {
			parsedDate = LocalDate.parse(stringToBeParsed, formatter);
		} catch (Exception e) {
			throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
					"Parsing of string with current mapping failed");
		}
		return parsedDate;
	}

	private Object parseDateAndTime(String stringToBeParsed, ArrayList<MaskingItem> mask)
			throws parsingFailedDueToNullMappingMask, cantBeParsedWithCurrentMappingMask,
			cantParseEmptyStringForCurrentType {
		LocalDateTime parsedDateTime = null;
		String maskStoredAsString = maskArrayListToStringFormaterFormat(mask);
		if (isNull(maskStoredAsString)) {
			throw new ExceptionsPack.parsingFailedDueToNullMappingMask(
					"Parsing of ArrayList Mapping returned null. ArrayList of MaskingItem is null");
		}

		if (isEmpty(stringToBeParsed)) {
			throw new ExceptionsPack.cantParseEmptyStringForCurrentType(
					"Parsing failed as the string to be parsed is empty and is unsupported for current type");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(maskStoredAsString);

		try {
			parsedDateTime = LocalDateTime.parse(stringToBeParsed, formatter);
		} catch (Exception e) {
			throw new ExceptionsPack.cantBeParsedWithCurrentMappingMask(
					"Parsing of string with current mapping failed");
		}

		return parsedDateTime;
	}

	private String maskArrayListToStringFormaterFormat(ArrayList<MaskingItem> mask) {
		String maskStoredAsString = "";

		for (MaskingItem x : mask) {
			switch (x) {
			case SingleOrDoubleDigitDay:
				maskStoredAsString = maskStoredAsString.concat("d");
				break;
			case DoubleDigitDay:
				maskStoredAsString = maskStoredAsString.concat("dd");
				break;
			case SingleOrDoubleDigitMonth:
				maskStoredAsString = maskStoredAsString.concat("M");
				break;
			case DoubleDigitMonth:
				maskStoredAsString = maskStoredAsString.concat("MM");
				break;
			case DoubleDigitYear:
				maskStoredAsString = maskStoredAsString.concat("yy");
				break;
			case FourDigitYear:
				maskStoredAsString = maskStoredAsString.concat("yyyy");
				break;
			case SingleOrDoubleDigitHour:
				if (mask.contains(MaskingItem.AMorPMMark)) {
					maskStoredAsString = maskStoredAsString.concat("h");
				} else {
					maskStoredAsString = maskStoredAsString.concat("H");
				}
				break;
			case DoubleDigitHour:
				if (mask.contains(MaskingItem.AMorPMMark)) {
					maskStoredAsString = maskStoredAsString.concat("hh");
				} else {
					maskStoredAsString = maskStoredAsString.concat("HH");
				}
				break;
			case SingleOrDoubleDigitMinute:
				maskStoredAsString = maskStoredAsString.concat("m");
				break;
			case DoubleDigitMinute:
				maskStoredAsString = maskStoredAsString.concat("mm");
				break;
			case SingleOrDoubleDigitSecond:
				maskStoredAsString = maskStoredAsString.concat("s");
				break;
			case DoubleDigitSecond:
				maskStoredAsString = maskStoredAsString.concat("ss");
				break;
			case AMorPMMark:
				maskStoredAsString = maskStoredAsString.concat("a");
				break;
			case MilitaryHour:
				break;
			case SpaceSep:
				maskStoredAsString = maskStoredAsString.concat(" ");
				break;
			case SepColons:
				maskStoredAsString = maskStoredAsString.concat(":");
				break;
			case SepSemiColons:
				maskStoredAsString = maskStoredAsString.concat(";");
				break;
			case SepDot:
				maskStoredAsString = maskStoredAsString.concat(".");
				break;
			case SepComma:
				maskStoredAsString = maskStoredAsString.concat(",");
				break;
			case SepExclamationMark:
				maskStoredAsString = maskStoredAsString.concat("!");
				break;
			case SepMinus:
				maskStoredAsString = maskStoredAsString.concat("-");
				break;
			case SepSlash:
				maskStoredAsString = maskStoredAsString.concat("/");
				break;
			case SepBackSlah:
				maskStoredAsString = maskStoredAsString.concat("\\");
				break;
			case SepVerticalLine:
				maskStoredAsString = maskStoredAsString.concat("|");
				break;
			default:
				break;
			}
		}
		if (!maskStoredAsString.equals("")) {
			return maskStoredAsString;
		} else {
			return null;
		}
	}

	private boolean stringSizeWithinLimits(String stringToBeParsed, int minCountOfCh, int maxCountOfCh) {
		if ((stringToBeParsed.length() < minCountOfCh) || (stringToBeParsed.length() > maxCountOfCh)) {
			return false;
		}
		return true;
	}

	private boolean isLimitedValue() {
		if (this.unlimitedChNumberFlag) {
			return false;
		}
		return true;
	}

	private int getMinCountOfCh(ArrayList<MaskingItem> mask) {
		int minCountOfCh = 0;
		for (MaskingItem x : mask) {
			switch (x) {
			case NotSet:
				break;
			case SingleOrDoubleDigitDay:
				minCountOfCh = minCountOfCh + 1;
				break;
			case DoubleDigitDay:
				minCountOfCh = minCountOfCh + 2;
				break;
			case SingleOrDoubleDigitMonth:
				minCountOfCh = minCountOfCh + 1;
				break;
			case DoubleDigitMonth:
				minCountOfCh = minCountOfCh + 2;
				break;
			case DoubleDigitYear:
				minCountOfCh = minCountOfCh + 2;
				break;
			case FourDigitYear:
				minCountOfCh = minCountOfCh + 4;
				break;
			case SingleOrDoubleDigitHour:
				minCountOfCh = minCountOfCh + 1;
				break;
			case DoubleDigitHour:
				minCountOfCh = minCountOfCh + 2;
				break;
			case SingleOrDoubleDigitMinute:
				minCountOfCh = minCountOfCh + 1;
				break;
			case DoubleDigitMinute:
				minCountOfCh = minCountOfCh + 2;
				break;
			case SingleOrDoubleDigitSecond:
				minCountOfCh = minCountOfCh + 1;
				break;
			case DoubleDigitSecond:
				minCountOfCh = minCountOfCh + 2;
				break;
			case AMorPMMark:
				minCountOfCh = minCountOfCh + 2;
				break;
			case MilitaryHour:
				break;
			case SpaceSep:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepColons:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepSemiColons:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepDot:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepComma:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepExclamationMark:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepMinus:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepSlash:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepBackSlah:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SepVerticalLine:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SingleDigitInt:
				minCountOfCh = minCountOfCh + 1;
				break;
			case Number:
				minCountOfCh = 0;
				this.unlimitedChNumberFlag = true;
				break;
			case AnyString:
				this.unlimitedChNumberFlag = true;
				minCountOfCh = 0;
				break;
			case SingleCharacter:
				minCountOfCh = minCountOfCh + 1;
				break;
			case SingleSpecialCh:
				minCountOfCh = minCountOfCh + 1;
				break;
			}
		}
		return minCountOfCh;
	}

	private int getMaxCountOfCh(ArrayList<MaskingItem> mask) {
		int maxCountOfCh = 0;
		for (MaskingItem x : mask) {
			switch (x) {
			case NotSet:
				break;
			case SingleOrDoubleDigitDay:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case DoubleDigitDay:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case SingleOrDoubleDigitMonth:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case DoubleDigitMonth:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case DoubleDigitYear:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case FourDigitYear:
				maxCountOfCh = maxCountOfCh + 4;
				break;
			case SingleOrDoubleDigitHour:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case DoubleDigitHour:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case SingleOrDoubleDigitMinute:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case DoubleDigitMinute:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case SingleOrDoubleDigitSecond:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case DoubleDigitSecond:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case AMorPMMark:
				maxCountOfCh = maxCountOfCh + 2;
				break;
			case MilitaryHour:
				break;
			case SpaceSep:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepColons:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepSemiColons:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepDot:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepComma:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepExclamationMark:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepMinus:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepSlash:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepBackSlah:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SepVerticalLine:
				maxCountOfCh = maxCountOfCh + 1;
			case SingleDigitInt:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case Number:
				maxCountOfCh = 10;
				break;
			case AnyString:
				maxCountOfCh = 2147483647;
				break;
			case SingleCharacter:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			case SingleSpecialCh:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			}
		}
		return maxCountOfCh;
	}

}
