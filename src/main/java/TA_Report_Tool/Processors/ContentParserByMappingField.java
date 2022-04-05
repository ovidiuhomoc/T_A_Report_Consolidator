package TA_Report_Tool.Processors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import TA_Report_Tool.Data.HeaderMapping;
import TA_Report_Tool.Data.HeaderMappingField;
import TA_Report_Tool.Data.MaskingItem;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.CantBeParsedWithCurrentMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.HeaderColumnDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.ParsingFailedDueToNullMappingMask;
import TA_Report_Tool.MainApp.ExceptionsPack.SearchedHeaderColumnIsNull;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.Header;

public class ContentParserByMappingField {

	private HeaderMapping headerMapping;
	private Header profileHeader;

	public ContentParserByMappingField(HeaderMapping headerMapping, Header profileHeader) {
		this.headerMapping = headerMapping;
		this.profileHeader = profileHeader;
	}

	public Object parse(String stringToBeParsed, String columnName)
			throws SearchedHeaderColumnIsNull, InterruptedException, ExecutionException, connectionNotInitialized,
			HeaderColumnDoesNotExist, CantBeParsedWithCurrentMappingMask, ParsingFailedDueToNullMappingMask {
		HeaderMappingField currentColumnMapping = this.profileHeader.getColumnByName(columnName)
				.getHeaderMappingField();
		ArrayList<MaskingItem> mask = currentColumnMapping.getMask().toArrayList();

		int minCountOfCh = this.getMinCountOfCh(mask);
		int maxCountOfCh = this.getMaxCountOfCh(mask);

		if ((isLimitedValue(minCountOfCh)) && !stringSizeWithinLimits(stringToBeParsed, minCountOfCh, maxCountOfCh)) {
			throw new ExceptionsPack.CantBeParsedWithCurrentMappingMask(
					"The row can't be parsed with current mask as the string size does not fit in the expected size");
		}

		switch (currentColumnMapping.getType()) {
		case Date:
			return parseDate(stringToBeParsed, mask);
		case Time:
			return parseTime(stringToBeParsed, mask);
		case DateAndTime:
			return parseDateAndTime(stringToBeParsed);
		case Number:
			return parseNumber(stringToBeParsed);
		case FixedDigitNumber:
			return parseNumber(stringToBeParsed);
		case SignalingDevice:
			return stringToBeParsed;
		case EmployeeUniqueId:
			return stringToBeParsed;
		case EmployeeFirstName:
			return stringToBeParsed;
		case EmployeeMiddleName:
			return stringToBeParsed;
		case EmployeeLastName:
			return stringToBeParsed;
		case EmployeeFullName:
			return stringToBeParsed;
		case Event:
			return stringToBeParsed;
		case CustomFieldText:
			return parseCustomText(stringToBeParsed);
		case NotSet:
			return null;

		}
		return stringToBeParsed;
	}

	private Object parseCustomText(String stringToBeParsed) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseNumber(String stringToBeParsed) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseTime(String stringToBeParsed, ArrayList<MaskingItem> mask)
			throws ParsingFailedDueToNullMappingMask {
		LocalTime parsedTime = null;
		String maskStoredAsString = maskArrayListToStringFormaterFormat(mask);
		if (maskStoredAsString == null) {
			throw new ExceptionsPack.ParsingFailedDueToNullMappingMask(
					"Parsing of ArrayList Mapping returned null. ArrayList of MaskingItem is null");
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(maskStoredAsString);
		parsedTime = LocalTime.parse(stringToBeParsed, formatter);
		return parsedTime;

	}

	private Object parseDateAndTime(String stringToBeParsed) {
		// TODO Auto-generated method stub
		return null;
	}

	private LocalDate parseDate(String stringToBeParsed, ArrayList<MaskingItem> mask)
			throws ParsingFailedDueToNullMappingMask {
		LocalDate parsedDate = null;
		String maskStoredAsString = maskArrayListToStringFormaterFormat(mask);
		if (maskStoredAsString == null) {
			throw new ExceptionsPack.ParsingFailedDueToNullMappingMask(
					"Parsing of ArrayList Mapping returned null. ArrayList of MaskingItem is null");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(maskStoredAsString);
		parsedDate = LocalDate.parse(stringToBeParsed, formatter);
		return parsedDate;
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

	private boolean isLimitedValue(int minCountOfCh) {
		if (minCountOfCh != (-(int) (Math.pow(2, 31)))) {
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
				minCountOfCh = -(int) (Math.pow(2, 31));
				break;
			case AnyString:
				minCountOfCh = -(int) (Math.pow(2, 31));
				break;
			case SingleCharacter:
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
				maxCountOfCh = -(int) (Math.pow(2, 31));
				break;
			case AnyString:
				maxCountOfCh = -(int) (Math.pow(2, 31));
				break;
			case SingleCharacter:
				maxCountOfCh = maxCountOfCh + 1;
				break;
			}
		}
		return maxCountOfCh;
	}

}
