
package TA_Report_Tool.Data;

import static TA_Report_Tool.Tools.check.*;
import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.Tools.*;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;

/**
 * The class is used to encapsulate the pair (String) Column Name & (Boolean) To
 * Be Considered attribute; Objects of HeaderEntry type will be used to store
 * each entry of the header and the corresponding flag (Column data to be loaded
 * or not).
 * 
 * @author O.H.
 *
 */
public class ColumnProperties {
	private String colName;
	private boolean toBeLoaded;
	private MappingUnit mappingUnit;
	private int tableHeaderIndex = 0;

	/**
	 * Constructor for the ColumnProperties class, used to create an object for all
	 * properties of one column header cell
	 * 
	 * @param columnName       provides the name or label of that column identified
	 *                         in the header of the table
	 * @param toBeLoaded       marks if content of this column should be loaded or
	 *                         not
	 * @param mappingUnit      stores all details required for successfully mapping
	 *                         and parsing the content of the column
	 * @param tableHeaderIndex marks the index of this column in the table. First
	 *                         column has index 0
	 * @throws nullArgument
	 */
	public ColumnProperties(String columnName, boolean toBeLoaded, MappingUnit mappingUnit, int tableHeaderIndex)
			throws nullArgument {
		
		if (isNull(columnName)) {
			throw new ExceptionsPack.nullArgument("The passed column name argument is null");
		}
		
		if (isNull(mappingUnit)) {
			throw new ExceptionsPack.nullArgument("The passed mapping unit argument is null");
		}
		
		this.colName = columnName;
		this.toBeLoaded = toBeLoaded;
		this.mappingUnit = mappingUnit;
		this.tableHeaderIndex = tableHeaderIndex;
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
	public boolean loadData() {
		return this.toBeLoaded;
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
		this.toBeLoaded = status;
	}

	public MappingUnit getMappingUnit() {
		return this.mappingUnit;
	}

	public void setMappingUnit(MappingUnit type) {
		this.mappingUnit = type;
	}

	public int getTableHeaderIndex() {
		return this.tableHeaderIndex;
	}
}
