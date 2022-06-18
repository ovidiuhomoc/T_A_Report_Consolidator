
package TA_Report_Tool.Data;

import java.util.ArrayList;

import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.rowParameterNotHigherThanZero;

public class ColumnData {
	private ColumnProperties currentColumnProperties = null;
	private ArrayList<Object> currentColumnData = new ArrayList<Object>();

	/**
	 * Constructor that creates new ColumnData object used to store the data of a
	 * table column
	 * 
	 * @param columnProperties a <i><code>ColumnProperties</code></i> object that
	 *                         encapsulates all the necessary parameters and
	 *                         properties needed to store data into the ColumnData
	 *                         object
	 */
	public ColumnData(ColumnProperties columnProperties) {
		this.currentColumnProperties = columnProperties;
	}

	public int getColumnHeaderIndex() {
		return this.currentColumnProperties.getTableHeaderIndex();
	}

	public String getColumnName() {
		return this.currentColumnProperties.getName();
	}

	public MappingUnit getColumnMappingUnit() {
		return this.currentColumnProperties.getMappingUnit();
	}

	public void addData(Object data) {
		this.currentColumnData.add(data);
	}

	public Object getDataAt(int row) throws rowParameterNotHigherThanZero {
		if (row < 1) {
			throw new ExceptionsPack.rowParameterNotHigherThanZero("The requested row:" + row + " is not at least 1");
		}
		return currentColumnData.get(row - 1);
	}

}
