package time_and_attendance_report_consolidator.Tests;

public class mainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random_Generator ran = new Random_Generator();
		System.out.println(ran.randomIntBetween(5, 7));
		System.out.println(ran.randomTextAlphaNumeric(20));
		System.out.println(ran.randomTextAlphaNumericAndSymbols(20));
		System.out.println(ran.randomTextAlphaNumericAndBeginWithSymbols(20));
		System.out.println(ran.randomTextAlphaNumericAndEndWithSymbols(20));
		System.out.println(ran.randomTextAlphaNumericAndSymbolsBalancedDistribution(20));
	}

}
