package TA_Report_Tool.MainApp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import TA_Report_Tool.Data.ConnType;
import TA_Report_Tool.Data.Connection;
import TA_Report_Tool.Data.MappingCollection;
import TA_Report_Tool.Data.TableData;
import TA_Report_Tool.Data.TableHeader;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.MainApp.ExceptionsPack.profileDoesNotExist;

public class Profile {
	private String name;
	protected static int count = 0;
	private static Set<Profile> all = new HashSet<Profile>();// later to be considered TreeSet or LinkedHashSet for
																// keeping the Profiles in their addition order
	private static Profile active;

	/**
	 * Constructor of class Profile. Creates a new object of type Profile using as
	 * parameter a <String>String</String> representing name of profile.
	 * 
	 * @param name a String variable containing the profile name
	 * @throws nullArgument
	 * @throws SeparatorMoreThanOneCharachter
	 */
	public Profile(String name) throws nullArgument {
		this.name = name;
		Profile.all.add(this);

		if (Profile.noOfProfiles() == 1) {
			Profile.active = this;
		}

		this.mappingCollection = new MappingCollection();
	}

	/**
	 * Constructor of class Profile. Creates a new object of type Profile using as
	 * name parameter a default "Profile " + count.
	 * 
	 * @throws nullArgument
	 * 
	 * @throws SeparatorMoreThanOneCharachter
	 */
	public Profile() throws nullArgument {
		this.name = "Profile " + (Profile.noOfProfiles() + 1);
		Profile.all.add(this);

		if (Profile.noOfProfiles() == 1) {
			Profile.active = this;
		}

		this.mappingCollection = new MappingCollection();
	}

	public static void reset() {
		Profile.active = null;
		Profile.all.clear();
	}

	/**
	 * Sets the name of the profile.
	 * 
	 * @param name The name of profile to be set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the selected profile.
	 * 
	 * @return a <String>String</String> containing the profile name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Method that converts the internal Set of Profiles to an Array of Profiles for
	 * easier iteration.
	 * 
	 * @return an Array of type Profile
	 */
	public static Profile[] toArray() {
		// returns an array of type "Profile"
		Profile[] arr = Profile.all.toArray(new Profile[0]);
		return arr;
	}

	/**
	 * Sets the active profile. The active connection of the active profile will be
	 * used connect to the data source and retrieve content that will be parsed.
	 */
	public void setActive() {
		Profile.active = this;
	}

	/**
	 * Gets the active profile
	 * 
	 * @return a variable of type Profile representing the active profile
	 */
	public static Profile getActive() {
		return Profile.active;
	}

	private boolean isActive() {
		if (this == Profile.active) {
			return true;
		}
		return false;
	}

	private boolean isLast() {
		if (Profile.noOfProfiles() == 1) {
			return true;
		}
		return false;
	}

	private static int noOfProfiles() {
		return all.size();
	}

	/**
	 * A static method (accessed by Profile.removeProfile(Profile)) that completely
	 * remove the profile clearing all dependencies such active profile or the Set
	 * of profiles, if needed
	 * 
	 * @param profile is the object of type Profile to be removed
	 * @throws profileDoesNotExist
	 */
	public static void removeProfile(Profile profile) throws profileDoesNotExist {
		if (profile == null) {
			return;
		}

		if (!Profile.all.contains(profile)) {
			throw new ExceptionsPack.profileDoesNotExist(
					"The provided profiles does not exist into the stored list of profiles");
		}

		if (profile.isActive()) {
			Profile.active = Profile.toArray()[0];
		}

		if (profile.isLast()) {
			Profile.active = null;

		}

		Profile.all.remove(profile);
	}

	public void createCopy() {
		// Profile duplicate - copie tot ce se poate copia
	}

	public boolean saveProfile() {
		// Save profile to XML to be implemented
		return false;
	}

	public boolean loadProfile() {
		// Load profile from XML to be implemented
		return false;
	}

	/*
	 * ======================== Section 2 ========================
	 * ====================== Report Period ======================
	 * 
	 * Period "repPeriod" [no of last repPeriod months] for which the T&A report
	 * runs Current month determined by most recent event date and period includes
	 * current month
	 */
	private int repPeriod = 1;

	/**
	 * Method changes the default period of 1 month for the T&A report with the
	 * specified value
	 * 
	 * @param period is the number of months (int) for which the T&A reports should
	 *               run
	 */
	public void setRepMonthsPeriod(int period) {
		this.repPeriod = period;
	}

	/**
	 * Method returns the current value of months for which the T&A report should be
	 * run
	 * 
	 * @return an int representing the number of months for which T&A report to run
	 */
	public int getRepMonthsPeriod() {
		return this.repPeriod;
	}

	/*
	 * ======================== Section 3 ========================
	 * ========================= Events ==========================
	 * 
	 * The T&A report will consider as check-in all events [allEvents - true] or
	 * only the events from set selEvents
	 */
	private boolean allEvents = true;
	private Set<String> selEvents = new HashSet<String>();

	/**
	 * Method used to change the filtering mode from all events to a list of events
	 * and adding 1 event to the list
	 * 
	 * @param event is a String type parameter used for adding one event to the
	 *              event list
	 */
	public void addEvent(String event) {
		this.selEvents.add(event);
		this.allEvents = false;
	}

	/**
	 * Method verifies if the event is contained by the events list used for
	 * filtering
	 * 
	 * @param event is a String parameter representing the event to be checked
	 * @return method returns a boolean indicating the containing of specified event
	 */
	public boolean containsEvent(String event) {
		return this.selEvents.contains(event);
	}

	/**
	 * Method removes an event from filtering list and if list does not contains any
	 * filter anymore, changes filtering mode to all events
	 * 
	 * @param event a String with the event to be removed
	 */
	public void removeEvent(String event) {
		if (this.selEvents.contains(event)) {
			this.selEvents.remove(event);
		}
		if (selEvents.size() == 0) {
			this.allEvents = true;
		}
	}

	/**
	 * Method checks if filtering is set to All Events or selected events only
	 * 
	 * @return a boolean true if the filtering is set to All Events
	 */
	public boolean isAllEventsActive() {
		return this.allEvents;
	}

	/**
	 * Method returns the entire Set of events into an Array format for easier
	 * iteration
	 * 
	 * @return and Array of String type
	 */
	public String[] eventsToArray() {
		String[] arr = selEvents.toArray(new String[0]);
		return arr;
	}

	/**
	 * Method clears the full Set of events used for filtering and switches
	 * filtering mode to All Events
	 */
	public void useAllEvents() {
		this.selEvents.clear();
		this.allEvents = true;
	}

	// T&A report filtration by value of other columns (other than events) -
	// implementation to be considered

	/*
	 * ========================== Section 4 ===========================
	 * ==================== Connections management ====================
	 *
	 * The definition of a series of connections to different data sources like
	 * files (SV, Excel, HTML) or DBs (MSQL) where the RAW data of card history /
	 * access history, sits
	 */

	private Set<Connection> connections = new HashSet<Connection>();
	private Connection activeConn = null;

	public Connection getConnectionByName(String connName) {
		Connection connContainer = null;

		Iterator<Connection> connectionsIterator = this.connections.iterator();
		while (connectionsIterator.hasNext()) {
			Connection currentConnection = connectionsIterator.next();
			if (currentConnection.getName().equals(connName)) {
				return currentConnection;
			}
		}
		return connContainer;
	}

	private boolean doesConnectionExist(String connName) {
		Connection connToBeFound = getConnectionByName(connName);
		if (connToBeFound != null) {
			return true;
		}
		return false;
	}

	/**
	 * This method takes several parameters that describe the minimum of a
	 * connection to a CSV file. <br>
	 * The implementation allows addition or editing of offline connections at this
	 * stage. <br>
	 * Connection should be tested further at the stage of parsing command.
	 * <p>
	 * This method requires a delimiter to be explicitly provided.
	 * <p>
	 * 
	 * @param connName  The name of the connection in current profile
	 * @param file      The full path towards the file (including file name and
	 *                  extension)
	 * @param delimiter The delimiter that should be used for CSV parsing.
	 * @throws nullNameConnection
	 * @throws nullArgument
	 * @throws dateOrTimeMissing
	 * @throws connectionNotInitialized
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void initializeCSVConn(String connName, String file, String delimiter) throws nullNameConnection,
			InterruptedException, ExecutionException, connectionNotInitialized, dateOrTimeMissing, nullArgument {
		Connection newConn;

		if (connName == null) {
			throw new ExceptionsPack.nullNameConnection("Connection can't be set with a null name");
		}

		if (this.doesConnectionExist(connName)) {
			newConn = this.getConnectionByName(connName);
		} else {
			newConn = new Connection();
		}

		newConn.setConnection(connName, ConnType.CSV, file);
		newConn.setCSVDelimiter(delimiter);
		if (this.connections.size() == 0) {
			this.activeConn = newConn;
			initializeDataStructures();
		}
		this.connections.add(newConn);
	}

	/**
	 * This method takes several parameters that describe the minimum of a
	 * connection to a CSV file. <br>
	 * The implementation allows addition or editing of offline connections at this
	 * stage. <br>
	 * Connection should be tested further at the stage of parsing command.
	 * <p>
	 * <br>
	 * This method takes as default delimiter the comma character ","
	 * <p>
	 * 
	 * @param connName The name of the connection in current profile
	 * @param file     The full path towards the file (including file name and
	 *                 extension)
	 * @throws nullNameConnection
	 * @throws nullArgument
	 * @throws dateOrTimeMissing
	 * @throws connectionNotInitialized
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void initializeCSVConn(String connName, String file) throws nullNameConnection, InterruptedException,
			ExecutionException, connectionNotInitialized, dateOrTimeMissing, nullArgument {
		this.initializeCSVConn(connName, file, ",");
	}

	/**
	 * Method converts the Set of Connections to an Array for easier later
	 * iterations
	 * 
	 * @return and Array of Connections type
	 */
	public Connection[] connectionsToArray() {
		Connection[] arr = this.connections.toArray(new Connection[0]);
		return arr;
	}

	private boolean isTheActiveConnection(Connection testConn) {
		if (this.activeConn.equals(testConn)) {
			return true;
		}
		return false;
	}

	private boolean isLastConnection(Connection testConn) {
		if (this.connections.size() == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Method that removes a specific connection from the list of connections of the
	 * object Profile, together with all dependent relations
	 * 
	 * @param conn a Connection type object to be removed from profile Set
	 */
	public void removeConnection(Connection conn) {
		if (conn == null) {
			return;
		}

		if (!this.doesConnectionExist(conn.getName())) {
			return;
		}

		if (this.isTheActiveConnection(conn)) {
			this.activeConn = this.connectionsToArray()[0];
		}

		if (isLastConnection(conn)) {
			this.activeConn = null;
		}

		this.connections.remove(conn);
	}

	/**
	 * Getter used to return the active connection.
	 * 
	 * @return a Connection type object
	 */
	public Connection getActiveConn() {
		return this.activeConn;
	}

	public void setActiveConn(Connection conn)
			throws InterruptedException, ExecutionException, connectionNotInitialized, dateOrTimeMissing, nullArgument {
		this.activeConn = conn;
		initializeDataStructures();
	}

	// SQL, Excel, HTML connections to be still setup

	/*
	 * ========================== Section 5 ===========================
	 * ==================== Header Columns Mapping ====================
	 *
	 * Reads a limited part of the data source for identification of header and
	 * column names. If no header exists, user is prompted for header columns names
	 * and minimal settings. Data start or no header situation are details provided
	 * by user.
	 */

	private TableHeader tableHeader = null;
	private MappingCollection mappingCollection = null;
	private TableData tableData;

	private void initializeDataStructures()
			throws InterruptedException, ExecutionException, connectionNotInitialized, dateOrTimeMissing, nullArgument {
		this.tableData = new TableData();
		this.tableHeader = new TableHeader(this.getActiveConn(),this.tableData);
	}

	public TableHeader getTableHeader() throws connectionNotInitialized {
		if (this.getActiveConn() == null) {
			throw new ExceptionsPack.connectionNotInitialized("The connection it was not initialized and it is null");
		}
		return this.tableHeader;
	}

	public MappingCollection getMappingCollection() {
		return this.mappingCollection;
	}

	/*
	 * ========================== Section 6 ===========================
	 * ======================== Data Storage ==========================
	 *
	 * The data after the header is read, parsed and stored within this structure
	 * for later usage. Only the columns marked as to be loaded will be parsed and
	 * read.
	 */

	// functie de scan continut
	// la momentul scanarii, se reface complet obiectul de stocare

	// obiectul de stocare contine un ArrayList cu obiecte coloana + numar de
	// randuri totale (fara header)

}
