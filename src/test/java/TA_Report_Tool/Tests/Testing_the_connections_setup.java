package TA_Report_Tool.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import TA_Report_Tool.Data.ConnType;
import TA_Report_Tool.Data.Connection;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.Profile;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.Tools_for_Tests.Tools_Random_Generator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Testing_the_connections_setup {

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
		try {
			Profile.reset();
			Profile testProfile = new Profile("Test_Profile");

			assertEquals(null, testProfile.getActiveConn());

			ArrayList<String> connName = new ArrayList<String>();
			ArrayList<String> connPath = new ArrayList<String>();
			int count = 0;

			connName.add("CSV Connection 1");
			connPath.add("C:\\Test CSV 1.csv");
			count++;
			testProfile.initializeCSVConn(connName.get(count - 1), connPath.get(count - 1), ";");

			Connection firstConnection;
			firstConnection = testProfile.getActiveConn();

			assertEquals(connName.get(0), firstConnection.getName());
			assertEquals(connPath.get(0), firstConnection.getFilePath());
			assertEquals(";", firstConnection.getCSVDelimiter());
			assertEquals(ConnType.CSV, firstConnection.getType());

			Tools_Random_Generator random = new Tools_Random_Generator();
			String string = "";

			string = random.randomTextAlphaNumeric(20);
			connName.add(string);
			connPath.add("C:\\" + string + ".csv");
			count++;
			testProfile.initializeCSVConn(connName.get(count - 1), connPath.get(count - 1));

			string = random.randomTextAlphaNumericAndBeginWithSymbols(20);
			connName.add(string);
			connPath.add("C:\\" + string + ".csv");
			count++;
			testProfile.initializeCSVConn(connName.get(count - 1), connPath.get(count - 1));

			string = random.randomTextAlphaNumericAndEndWithSymbols(20);
			connName.add(string);
			connPath.add("C:\\" + string + ".csv");
			count++;
			testProfile.initializeCSVConn(connName.get(count - 1), connPath.get(count - 1));

			string = random.randomTextAlphaNumericAndSymbols(20);
			connName.add(string);
			connPath.add("C:\\" + string + ".csv");
			count++;
			testProfile.initializeCSVConn(connName.get(count - 1), connPath.get(count - 1));
			string = random.randomTextAlphaNumericAndSymbolsBalancedDistribution(20);
			connName.add(string);
			connPath.add("C:\\" + string + ".csv");
			count++;
			testProfile.initializeCSVConn(connName.get(count - 1), connPath.get(count - 1));

			firstConnection = testProfile.getActiveConn();
			assertEquals(connName.get(0), firstConnection.getName());
			assertEquals(connPath.get(0), firstConnection.getFilePath());
			assertEquals(";", firstConnection.getCSVDelimiter());
			assertEquals(ConnType.CSV, firstConnection.getType());

			assertEquals(testProfile.connectionsToArray().length, connName.size());
			assertTrue(this.DoesConnectionListMatchesConnectionsAdded(testProfile, connName));

			for (Connection currentConn : testProfile.connectionsToArray()) {
				testProfile.removeConnection(currentConn);
			}

			assertEquals(null, testProfile.getActiveConn());

			assertThrows(ExceptionsPack.nullNameConnection.class, () -> {
				testProfile.initializeCSVConn(null, "C:\\" + null + ".csv");
			});
			assertThrows(ExceptionsPack.nullNameConnection.class, () -> {
				testProfile.initializeCSVConn(null, "C:\\" + null + ".csv", ",");
			});

			testProfile.initializeCSVConn("Conn 1", "C:\\Docs\\1.csv");
			testProfile.initializeCSVConn("Conn 2", "C:\\Docs\\2.csv");

			assertEquals(testProfile.getConnectionByName("Conn 1"), testProfile.getActiveConn());
			testProfile.setActiveConn(testProfile.getConnectionByName("Conn 2"));
			assertEquals(testProfile.getConnectionByName("Conn 2"), testProfile.getActiveConn());

		} catch (nullNameConnection | nullArgument | InterruptedException | ExecutionException | connectionNotInitialized | dateOrTimeMissing e) {
			e.printStackTrace();
		}
	}
}
