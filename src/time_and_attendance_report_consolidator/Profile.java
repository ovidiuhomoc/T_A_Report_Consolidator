package time_and_attendance_report_consolidator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
	 */
	public Profile(String name) {
		Profile.count++;
		this.name = name;
		Profile.all.add(this);

		if (Profile.count == 1) {
			Profile.active = this;
		}
	}

	/**
	 * Constructor of class Profile. Creates a new object of type Profile using as
	 * name parameter a default "Profile " + count.
	 */
	public Profile() {
		Profile.count++;
		this.name = "Profile " + count;
		Profile.all.add(this);

		if (Profile.count == 1) {
			Profile.active = this;
		}
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

	/**
	 * A static method (accessed by Profile.removeProfile(Profile)) that completely
	 * remove the profile clearing all dependencies such active profile or the Set
	 * of profiles, if needed
	 * 
	 * @param profile is the object of type Profile to be removed
	 */
	public static void removeProfile(Profile profile) {
		if (Profile.all.contains(profile)) {
			if (Profile.active.equals(profile)) {
				if (Profile.all.size() == 1) {
					Profile.active = null;
				} else {
					Profile.active = Profile.toArray()[0];
				}
			}
			Profile.all.remove(profile);
			Profile.count--;
		}
	}

	// Profile duplicate - copie tot ce se poate copia
	// Save profile to XML to be implemented
	// Load profile from XML to be implemented

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
	public void setRepPeriod(int period) {
		this.repPeriod = period;
	}

	/**
	 * Method returns the current value of months for which the T&A report should be
	 * run
	 * 
	 * @return an int representing the number of months for which T&A report to run
	 */
	public int getRepPeriod() {
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
	private Connection activeConn;

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
	 */
	public void setCSVConn(String connName, String file, String delimiter) {
		boolean duplicateFlag = false;
		Connection temp = null;
		Connection newConn;

		Iterator<Connection> it = this.connections.iterator();
		while (it.hasNext()) {
			Connection temp2 = it.next();
			if (temp2.getName().equals(connName)) {
				duplicateFlag = true;
				temp = temp2;
			}
		}

		if (duplicateFlag) {
			newConn = temp;
		} else {
			newConn = new Connection();
		}

		newConn.setConnection(connName, "CSV", file);
		newConn.setCSVDelimiter(delimiter);
		this.connections.add(newConn);

		if (this.connections.size() == 0) {
			this.activeConn = newConn;
		}
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
	 */
	public void setCSVConn(String connName, String file) {
		boolean duplicateFlag = false;
		Connection temp = null;
		Connection newConn;

		Iterator<Connection> it = this.connections.iterator();
		while (it.hasNext()) {
			Connection temp2 = it.next();
			if (temp2.getName().equals(connName)) {
				duplicateFlag = true;
				temp = temp2;
			}
		}

		if (duplicateFlag) {
			newConn = temp;
		} else {
			newConn = new Connection();
		}

		newConn.setConnection(connName, "CSV", file);
		this.connections.add(newConn);

		if (this.connections.size() == 0) {
			this.activeConn = newConn;
		}
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

	/**
	 * Method that removes a specific connection from the list of connections of the
	 * object Profile, together with all dependent relations
	 * 
	 * @param conn a Connection type object to be removed from profile Set
	 */
	public void removeConnection(Connection conn) {
		if (this.connections.contains(conn)) {
			if (this.activeConn.equals(conn)) {
				if (this.connections.size() == 1) {
					this.activeConn = null;
				} else {
					this.activeConn = this.connectionsToArray()[0];
				}
			}
			this.connections.remove(conn);
		}
	}
	
	/**
	 * Getter used to return the active connection.
	 * @return a Connection type object
	 */
	public Connection getActiveConn() {
		return this.activeConn;
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

}
