package TA_Report_Tool.Tools;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class compute {
	public static float round(float number, int decimals) {
		if (decimals < 1) {
			return number;
		}

		String dfText = "#.";
		for (int i = 1; i <= decimals; i++) {
			dfText = dfText.concat("#");
		}

		DecimalFormat df = new DecimalFormat(dfText);
		df.setRoundingMode(RoundingMode.HALF_EVEN);

		String roundOff = df.format(number);
		return Float.parseFloat(roundOff);
	}
}
