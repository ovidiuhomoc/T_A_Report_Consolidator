package time_and_attendance_report_consolidator;

import java.util.ArrayList;

public class ContentCSVparser {
	private ArrayList<String> results = new ArrayList<String>();
	private boolean citation = false;
	private String temp = "";
	private String delimiter = "";
	private boolean charSkipFlag = false;
	private String citationMark = String.valueOf('"');

	public String[] parseCSVrow(String content, String delimiter) {
		setDelimiter(delimiter);
		this.results.clear();

		for (int i = 0; i <= content.length() - 1; i++) {
			if (i != (content.length() - 1)) {
				this.parseCurrentChar(String.valueOf(content.charAt(i)), String.valueOf(content.charAt(i + 1)));
			} else {
				this.parseCurrentChar(String.valueOf(content.charAt(i)), null);
			}
		}

		return results.toArray(new String[0]);
	}

	private void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	private String delimiter() {
		return this.delimiter;
	}

	private void startCitation() {
		this.citation = true;
	}

	private void stopCitation() {
		this.citation = false;
	}

	private boolean isCitationStarted() {
		return this.citation;
	}

	private void addCurrentChar(String currentChar) {
		this.temp = this.temp + currentChar.charAt(0);
	}

	private void addEmptyCol() {
		this.results.add("");
	}

	private void addCurrentCol() {
		this.results.add(this.temp);
		this.temp = "";
	}

	private boolean isCitationEndRowEnd(String currentChar, String nextChar) {
		if (currentChar.equals('"') && nextChar == null) {
			return true;
		}
		return false;
	}

	private boolean isEmptyColRowEnd(String currentChar, String nextChar) {
		if (currentChar.equals(this.delimiter()) && nextChar == null) {
			return true;
		}
		return false;
	}

	private boolean isRowEnd(String currentChar, String nextChar) {
		if (nextChar == null) {
			return true;
		}
		return false;
	}

	private boolean isCurrentCharToBeSkipped() {
		if (this.charSkipFlag == true) {
			this.charSkipFlag = false;
			return true;
		}
		return false;
	}

	private void skipNextChar() {
		this.charSkipFlag = true;
	}

	private boolean isQuoteSignInsertion(String currentChar, String nextChar) {
		if (this.isCitationStarted() && (currentChar.equals(citationMark)) && (nextChar.equals(citationMark))) {
			return true;
		}
		return false;
	}

	private boolean isStartOfCitation(String currentChar, String nextChar) {
		if (!this.isCitationStarted() && (currentChar.equals(citationMark))) {
			return true;
		}
		return false;
	}

	private boolean isEndOfCitation(String currentChar, String nextChar) {
		if (this.isCitationStarted() && (currentChar.equals(citationMark)) && !(nextChar.equals(citationMark))) {
			return true;
		}
		return false;
	}

	private boolean isDelimiterNotInCitation(String currentChar, String nextChar) {
		if (!this.isCitationStarted() && (currentChar.equals(this.delimiter()))) {
			return true;
		}
		return false;
	}

	private void parseCurrentChar(String currentChar, String nextChar) {
		if (isCurrentCharToBeSkipped()) {
			return;
		}

		if (isCitationEndRowEnd(currentChar, nextChar)) {
			this.stopCitation();
			this.addCurrentCol();
			return;
		}

		if (isEmptyColRowEnd(currentChar, nextChar)) {
			this.addEmptyCol();
			return;
		}

		if (isRowEnd(currentChar, nextChar)) {
			this.addCurrentChar(currentChar);
			this.addCurrentCol();
			return;
		}

		if (isQuoteSignInsertion(currentChar, nextChar)) {
			this.addCurrentChar(currentChar);
			this.skipNextChar();
			return;
		}

		if (isStartOfCitation(currentChar, nextChar)) {
			this.startCitation();
			return;
		}

		if (isEndOfCitation(currentChar, nextChar)) {
			this.stopCitation();
			return;
		}

		if (isDelimiterNotInCitation(currentChar, nextChar)) {
			this.addCurrentCol();
			return;
		}

		this.addCurrentChar(currentChar);
	}

}
