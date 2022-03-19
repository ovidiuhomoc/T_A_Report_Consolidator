package time_and_attendance_report_consolidator.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import time_and_attendance_report_consolidator.Connection;
import time_and_attendance_report_consolidator.ExceptionsPack;
import time_and_attendance_report_consolidator.ExceptionsPack.nullNameConnection;
import time_and_attendance_report_consolidator.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Testing_the_connections_setup {

	private boolean DoesConnectionListMatchesConnectionsAdded(Profile profile, ArrayList<String> addedConnNames) {
		for (Connection currentConnection : profile.connectionsToArray()) {
			if (!addedConnNames.contains(currentConnection.getName())) {
				return false;
			}
		}
		return true;
	}

	@Test
	void Testing_the_CSV_connection_setup() {
		Profile.reset();
		Profile testProfile = new Profile("Test_Profile");

		assertEquals(null, testProfile.getActiveConn());

		ArrayList<String> connName = new ArrayList<String>();
		ArrayList<String> connPath = new ArrayList<String>();
		int count = 0;

		connName.add("CSV Connection 1");
		connPath.add("C:\\Test CSV 1.csv");
		count++;
		try {
			testProfile.setCSVConn(connName.get(count - 1), connPath.get(count - 1), ";");
		} catch (nullNameConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection firstConnection;
		firstConnection = testProfile.getActiveConn();

		assertEquals(connName.get(0), firstConnection.getName());
		assertEquals(connPath.get(0), firstConnection.getFilePath());
		assertEquals(";", firstConnection.getCSVDelimiter());
		assertTrue(firstConnection.isCSVtype());

		Tools_Random_Generator random = new Tools_Random_Generator();
		String string = "";

		string = random.randomTextAlphaNumeric(20);
		connName.add(string);
		connPath.add("C:\\" + string + ".csv");
		count++;
		try {
			testProfile.setCSVConn(connName.get(count - 1), connPath.get(count - 1));
		} catch (nullNameConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		string = random.randomTextAlphaNumericAndBeginWithSymbols(20);
		connName.add(string);
		connPath.add("C:\\" + string + ".csv");
		count++;
		try {
			testProfile.setCSVConn(connName.get(count - 1), connPath.get(count - 1));
		} catch (nullNameConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		string = random.randomTextAlphaNumericAndEndWithSymbols(20);
		connName.add(string);
		connPath.add("C:\\" + string + ".csv");
		count++;
		try {
			testProfile.setCSVConn(connName.get(count - 1), connPath.get(count - 1));
		} catch (nullNameConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		string = random.randomTextAlphaNumericAndSymbols(20);
		connName.add(string);
		connPath.add("C:\\" + string + ".csv");
		count++;
		try {
			testProfile.setCSVConn(connName.get(count - 1), connPath.get(count - 1));
		} catch (nullNameConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		string = random.randomTextAlphaNumericAndSymbolsBalancedDistribution(20);
		connName.add(string);
		connPath.add("C:\\" + string + ".csv");
		count++;
		try {
			testProfile.setCSVConn(connName.get(count - 1), connPath.get(count - 1));
		} catch (nullNameConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		firstConnection = testProfile.getActiveConn();
		assertEquals(connName.get(0), firstConnection.getName());
		assertEquals(connPath.get(0), firstConnection.getFilePath());
		assertEquals(";", firstConnection.getCSVDelimiter());
		assertTrue(firstConnection.isCSVtype());

		assertEquals(testProfile.connectionsToArray().length, connName.size());
		assertTrue(this.DoesConnectionListMatchesConnectionsAdded(testProfile, connName));

		for (Connection currentConn : testProfile.connectionsToArray()) {
			testProfile.removeConnection(currentConn);
		}

		assertEquals(null, testProfile.getActiveConn());

		assertThrows(ExceptionsPack.nullNameConnection.class, () -> {
			testProfile.setCSVConn(null, "C:\\" + null + ".csv");
		});
		assertThrows(ExceptionsPack.nullNameConnection.class, () -> {
			testProfile.setCSVConn(null, "C:\\" + null + ".csv",",");
		});
		
		try {
			testProfile.setCSVConn("Conn 1", "C:\\Docs\\1.csv");
			testProfile.setCSVConn("Conn 2", "C:\\Docs\\2.csv");
		} catch (nullNameConnection e) {
			e.printStackTrace();
		}
		
		assertEquals(testProfile.getConnectionByName("Conn 1"),testProfile.getActiveConn());
		testProfile.setActiveConn(testProfile.getConnectionByName("Conn 2"));
		assertEquals(testProfile.getConnectionByName("Conn 2"),testProfile.getActiveConn());
	}
}
