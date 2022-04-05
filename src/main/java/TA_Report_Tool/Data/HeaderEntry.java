
package TA_Report_Tool.Data;

/**
 * The class is used to encapsulate the pair (String) Column Name & (Boolean) To
 * Be Considered attribute; Objects of HeaderEntry type will be used to store
 * each entry of the header and the corresponding flag (Column data to be loaded
 * or not).
 * 
 * @author O.H.
 *
 */
public class HeaderEntry {
	private String colName;
	private boolean isLoaded;
	private HeaderMappingField columnType;

	public HeaderEntry(String columnName, boolean isLoaded, HeaderMappingField type) {
		this.colName = columnName;
		this.isLoaded = isLoaded;
		this.columnType = type;
	}

	/**
	 * Getter method used to retrieve name of a column from the complete Header of
	 * the Card History report
	 * 
	 * @return a variable of type String
	 */
	public String getName() {
		return this.colName;
	}

	/**
	 * Setter method used to set the name of a column from the complete Header of
	 * the Card History report
	 * 
	 * @param colName a variable of type String
	 */
	public void setName(String colName) {
		this.colName = colName;
	}

	/**
	 * Getter method that returns true if the selected column from the header is set
	 * to be loaded
	 * 
	 * @return a variable of type boolean
	 */
	public boolean getLoadStatus() {
		return this.isLoaded;
	}

	/**
	 * Setter method used to set the status of column concerning the loading of data
	 * from Card History report
	 * <ul>
	 * <li><i>true</i> the column data will be loaded</li>
	 * <li><i>false</i> the column data will not be loaded</li>
	 * </ul>
	 * 
	 * @param status a variable of boolean type
	 */
	public void setLoadStatus(boolean status) {
		this.isLoaded = status;
	}

	public HeaderMappingField getHeaderMappingField() {
		return this.columnType;
	}

	public void setHeaderMappingField(HeaderMappingField type) {
		this.columnType = type;
	}
}
