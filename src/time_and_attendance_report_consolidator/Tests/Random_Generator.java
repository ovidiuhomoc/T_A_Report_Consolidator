package time_and_attendance_report_consolidator.Tests;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Random_Generator {

	public int randomIntBetween(int min, int max) {
		Random r = new Random();
		return (r.nextInt(max + 1 - min) + min);
	}

	private String randomSymbolOnly() {
		String content = new String();
		content = "~!@#$%^&*()_+`-={}|[]\\:\";'<>?,./";

		Random r = new Random();
		return String.valueOf(content.charAt(r.nextInt(content.length() - 1)));
	}

	private String randomNumericalOnly() {
		String content = new String();
		content = "0123456789";

		Random r = new Random();
		return String.valueOf(content.charAt(r.nextInt(content.length() - 1)));
	}

	private String randomAlphaOnly() {
		String content = new String();
		content = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

		Random r = new Random();
		return String.valueOf(content.charAt(r.nextInt(content.length() - 1)));
	}

	public String randomTextAlphaNumeric(int length) {
		String content = "";

		for (int i = 0; i < length; i++) {
			switch (this.randomIntBetween(1, 2)) {
			case 1:
				content = content.concat(this.randomAlphaOnly());
				break;
			case 2:
				content = content.concat(this.randomNumericalOnly());
				break;
			}
		}
		return content;
	}

	public String randomTextAlphaNumericAndSymbols(int length) {
		String content = "";

		for (int i = 0; i < length; i++) {
			switch (this.randomIntBetween(1, 3)) {
			case 1:
				content = content.concat(this.randomAlphaOnly());
				break;
			case 2:
				content = content.concat(this.randomNumericalOnly());
				break;
			case 3:
				content = content.concat(this.randomSymbolOnly());
				break;
			}
		}
		return content;
	}

	public String randomTextAlphaNumericAndBeginWithSymbols(int length) {
		String content = "";

		content = content.concat(this.randomSymbolOnly());
		
		if (length == 1) {
			return content;
		}

		for (int i = 1; i < length; i++) {
			switch (this.randomIntBetween(1, 3)) {
			case 1:
				content = content.concat(this.randomAlphaOnly());
				break;
			case 2:
				content = content.concat(this.randomNumericalOnly());
				break;
			case 3:
				content = content.concat(this.randomSymbolOnly());
				break;
			}
		}
		return content;
	}

	public String randomTextAlphaNumericAndEndWithSymbols(int length) {
		String content = "";

		for (int i = 0; i < length - 1; i++) {
			switch (this.randomIntBetween(1, 3)) {
			case 1:
				content = content.concat(this.randomAlphaOnly());
				break;
			case 2:
				content = content.concat(this.randomNumericalOnly());
				break;
			case 3:
				content = content.concat(this.randomSymbolOnly());
				break;
			}
		}

		content = content.concat(this.randomSymbolOnly());
		return content;
	}

	public String randomTextAlphaNumericAndSymbolsBalancedDistribution(int length) {
		String content = "";
		Set<Integer> used = new HashSet<Integer>();
		int tempChoice;

		for (int i = 0; i < length; i++) {
			if (used.size() == 3) {
				used.clear();
				tempChoice = this.randomIntBetween(1, 3);
			} else {
				while (used.contains(tempChoice = this.randomIntBetween(1, 3))) {
				}

			}
			used.add(tempChoice);

			switch (tempChoice) {
			case 1:
				content = content.concat(this.randomAlphaOnly());
				break;
			case 2:
				content = content.concat(this.randomNumericalOnly());
				break;
			case 3:
				content = content.concat(this.randomSymbolOnly());
				break;
			}
		}
		return content;
	}
}
