package TA_Report_Tool.MainApp;

import java.util.ArrayList;
import static TA_Report_Tool.Tools.debug.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.ConnType;
import TA_Report_Tool.Data.Connection;
import TA_Report_Tool.Data.DataSource;
import TA_Report_Tool.Data.MappingCollection;
import TA_Report_Tool.Data.MappingType;
import TA_Report_Tool.Data.MappingUnit;
import TA_Report_Tool.Data.MaskTemplate;
import TA_Report_Tool.Data.TableData;
import TA_Report_Tool.Data.TableHeader;
import TA_Report_Tool.Filters.TAfiltersAndSettings;
import TA_Report_Tool.MainApp.ExceptionsPack.TAreportGenerationException;
import TA_Report_Tool.MainApp.ExceptionsPack.columnPropertiesDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.connectionNotInitialized;
import TA_Report_Tool.MainApp.ExceptionsPack.dateOrTimeMissing;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.MainApp.ExceptionsPack.nullColumnPropertiesPassed;
import TA_Report_Tool.MainApp.ExceptionsPack.nullNameConnection;
import TA_Report_Tool.MainApp.ExceptionsPack.profileDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.rowParameterNotHigherThanZero;
import TA_Report_Tool.MainApp.ExceptionsPack.searchCantFindMappingUnitInCollection;
import TA_Report_Tool.MainApp.ExceptionsPack.tableDataNotInitialized;
import TA_Report_Tool.Processors.ContentScannerThreadWrapper;
import TA_Report_Tool.Processors.TAreportGenerator;

import static TA_Report_Tool.Tools.check.*;

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
		this.localFiltersAndSettings = new TAfiltersAndSettings(this);
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
		this.localFiltersAndSettings = new TAfiltersAndSettings(this);
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

	/*
	 * ======================== Section 3 ========================
	 * ========================= Events ==========================
	 * 
	 * The T&A report will consider as check-in all events [allEvents - true] or
	 * only the events from set selEvents
	 */

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
	private TableData tableData = null;

	private void initializeDataStructures()
			throws InterruptedException, ExecutionException, connectionNotInitialized, dateOrTimeMissing, nullArgument {
		this.tableData = new TableData();
		this.tableHeader = new TableHeader(this.getActiveConn(), this.tableData);
		this.localFiltersAndSettings = new TAfiltersAndSettings(this);
	}

	public TableHeader getTableHeader() throws connectionNotInitialized {
		if (isNull(this.getActiveConn())) {
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

	public TableData getTableData() throws InterruptedException, ExecutionException {
		return this.tableData;
	}

	private Future<Void> currentRowScanResults;
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	private boolean contentScanMockFlag = false;
	private DataSource contentScanMockDS = null;

	public void scanAndStoreTableContent() throws connectionNotInitialized {
		ContentScannerThreadWrapper contentScanner = new ContentScannerThreadWrapper();

		if (this.contentScanMockFlag) {
			contentScanner.setMockDataSourceForMockScan(this.contentScanMockDS);
			this.contentScanMockFlag = false;
			this.contentScanMockDS = null;
		}

		contentScanner.transferActiveProfile(this);
		contentScanner.setStartRowForScanner(
				this.getTableHeader().getTableHeaderStartCellForEndInternalUse().nextRowNewObject());

		currentRowScanResults = this.executor.submit(contentScanner);
	}

	public void scanAndStoreTableContentFromMock(DataSource contentScanMockDS) throws connectionNotInitialized {
		this.contentScanMockFlag = true;
		this.contentScanMockDS = contentScanMockDS;
		this.scanAndStoreTableContent();
	}

	public boolean isTableContentScanningAndParsingDone() {
		return this.currentRowScanResults.isDone();
	}

	public void captureExceptionsAfterScanning() throws InterruptedException, ExecutionException {
		// if (this.currentRowScanResults.isDone()) {
		this.currentRowScanResults.get();
		// }
	}

	/*
	 * ========================== Section 7 ===========================
	 * ================ TA Report Filters & Settings ==================
	 *
	 */

	private TAfiltersAndSettings localFiltersAndSettings = null;
	private TableData filteredTableData = null;

	public void filterTableData() throws tableDataNotInitialized, InterruptedException, ExecutionException,
			connectionNotInitialized, dateOrTimeMissing, nullArgument, rowParameterNotHigherThanZero {
		if (isFalse(this.tableData.isInitialized())) {
			throw new ExceptionsPack.tableDataNotInitialized(
					"Can't filter the original data as table data was not initialized (no data was copied into table data). Typpically this step happens upon header scanning (or ScanForColsProperties)");
		}

		this.filteredTableData = new TableData();
		this.filteredTableData.copyStructureFrom(this.tableData);

		for (int i = 1; i <= this.tableData.getRowCount(); i++) {
			if (this.tableData.passFiltersAtRow(i, this.localFiltersAndSettings)) {
				this.filteredTableData.copyDataRowFrom(i, this.tableData);
			}
		}
	}

	public TAfiltersAndSettings dataFiltersAndSettings() {
		return this.localFiltersAndSettings;
	}

	public TableData getFilteredData() throws tableDataNotInitialized, InterruptedException, ExecutionException,
			connectionNotInitialized, dateOrTimeMissing, nullArgument, rowParameterNotHigherThanZero {
		if (isNull(this.filteredTableData)) {
			this.filterTableData();
		}
		if (isFalse(this.filteredTableData.isInitialized())) {
			this.filterTableData();
		}
		return this.filteredTableData;
	}

	/*
	 * ========================== Section 8 ===========================
	 * ======================= TA Calculation =========================
	 *
	 */

	private TableData detailedTAreport = null;
	private TableData summaryTAreport = null;
	private Future<Void> TAreportGenerator;
	private ExecutorService executorForTAGenerator = Executors.newFixedThreadPool(2);

	public void generateTAreport()
			throws tableDataNotInitialized, InterruptedException, ExecutionException, connectionNotInitialized,
			dateOrTimeMissing, nullArgument, TAreportGenerationException, columnPropertiesDoesNotExist,
			nullColumnPropertiesPassed, searchCantFindMappingUnitInCollection, rowParameterNotHigherThanZero {

		debugDisplay(this.getClass().getSimpleName(), "TA report generator started");

		if (isNull(this.filteredTableData)) {
			throw new ExceptionsPack.tableDataNotInitialized(
					"Can't generate the TA report, as the filtered table is null and not initialized. Typpically this step happens upon filterTableData method request.");
		}

		if (isFalse(this.filteredTableData.isInitialized())) {
			throw new ExceptionsPack.tableDataNotInitialized(
					"Can't generate the TA report, as there is no filtered table initialized. Typpically this step happens upon filterTableData method request.");
		}

		this.detailedTAreport = new TableData();
		this.summaryTAreport = new TableData();

		setupTAReportsStructures();

		if (isNull(this.getFilteredData())) {
			throw new ExceptionsPack.TAreportGenerationException(
					"TA report can't be generated as the data have not been filtered");
		}

		TAreportGenerator taReportGen = new TAreportGenerator();
		taReportGen.transferPrerequisites(this, this.localFiltersAndSettings, this.detailedTAreport,
				this.summaryTAreport);
		TAreportGenerator = this.executorForTAGenerator.submit(taReportGen);
	}

	public void captureExceptionsAfterGeneratingReport()
			throws InterruptedException, ExecutionException, TAreportGenerationException {
		if (isNull(this.TAreportGenerator)) {
			throw new ExceptionsPack.TAreportGenerationException("The TA Report Generator object is null");
		}

		this.TAreportGenerator.get();
	}

	public boolean isTAgenerationDone() {
		return this.TAreportGenerator.isDone();
	}

	public TableData getDetailedTAReport()
			throws InterruptedException, ExecutionException, TAreportGenerationException {
		if (isFalse(isTAgenerationDone())) {
			captureExceptionsAfterGeneratingReport();
		}
		while (isFalse(isTAgenerationDone())) {
		}
		return this.detailedTAreport;
	}

	public TableData getSummaryTAReport() throws InterruptedException, ExecutionException, TAreportGenerationException {
		if (isFalse(isTAgenerationDone())) {
			captureExceptionsAfterGeneratingReport();
		}
		while (isFalse(isTAgenerationDone())) {
		}
		return this.summaryTAreport;
	}

	private void setupTAReportsStructures()
			throws columnPropertiesDoesNotExist, InterruptedException, ExecutionException, connectionNotInitialized,
			dateOrTimeMissing, nullArgument, nullColumnPropertiesPassed, searchCantFindMappingUnitInCollection {

		ArrayList<ColumnProperties> structureOfDetailedReport = new ArrayList<>();
		ArrayList<ColumnProperties> structureOfSummaryReport = new ArrayList<>();

		this.getMappingCollection().addMappingUnit(new MappingUnit("TA Day",
				new MaskTemplate().addDay().addSep(".").addMonth().addSep(".").addYYYYear(), MappingType.Date));
		this.getMappingCollection().addMappingUnit(
				new MappingUnit("TA Month", new MaskTemplate().addAnyString(), MappingType.CustomFieldText));
		this.getMappingCollection()
				.addMappingUnit(new MappingUnit("Time Sum", new MaskTemplate().addNumber(), MappingType.Number));

		MappingUnit TAday = this.getMappingCollection().getMappingUnitdByName("TA Day");
		MappingUnit TAmonth = this.getMappingCollection().getMappingUnitdByName("TA Month");
		MappingUnit timeSum = this.getMappingCollection().getMappingUnitdByName("Time Sum");

		structureOfDetailedReport
				.add(this.getTableHeader().getColPropertiesByMappingType(MappingType.EmployeeUniqueId));
		structureOfSummaryReport.add(this.getTableHeader().getColPropertiesByMappingType(MappingType.EmployeeUniqueId));

		if (isNotNull(this.localFiltersAndSettings.getExtraColsToDisplay())) {
			for (ColumnProperties x : this.localFiltersAndSettings.getExtraColsToDisplay()) {
				structureOfDetailedReport.add(x);
				structureOfSummaryReport.add(x);
			}
		}

		structureOfSummaryReport.add(new ColumnProperties("TA Month", true, TAmonth, 0));
		structureOfDetailedReport.add(new ColumnProperties("TA Month", true, TAmonth, 0));
		structureOfDetailedReport.add(new ColumnProperties("TA Day", true, TAday, 0));

		structureOfDetailedReport.add(new ColumnProperties("Standard Working Hours", true, timeSum, 0));
		structureOfSummaryReport.add(new ColumnProperties("Standard Working Hours", true, timeSum, 0));

		structureOfDetailedReport.add(new ColumnProperties("Overtime Hours", true, timeSum, 0));
		structureOfSummaryReport.add(new ColumnProperties("Overtime Hours", true, timeSum, 0));

		structureOfDetailedReport.add(new ColumnProperties("Total Worked Hours", true, timeSum, 0));
		structureOfSummaryReport.add(new ColumnProperties("Total Worked Hours", true, timeSum, 0));

		this.detailedTAreport.initializeTableData(structureOfDetailedReport);
		this.summaryTAreport.initializeTableData(structureOfSummaryReport);
	}
}
