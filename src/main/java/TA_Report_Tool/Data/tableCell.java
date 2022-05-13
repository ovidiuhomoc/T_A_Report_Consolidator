package TA_Report_Tool.Data;

public class tableCell {
	private int row;
	private int col;

	/**
	 * Tuple object containing the coordinates of a cell
	 * 
	 * @param row the row coordinates. The app logic was designed to consider user's
	 *            perspective of first row beeing 1 and converting it automatically
	 *            to 0 (logic of Java)
	 *            <p>
	 * @param col the column coordinates. The app logic was designed to consider
	 *            user's perspective of first column beeing 1 and converting it
	 *            automatically to 0 (logic of Java)
	 *            <p>
	 */
	public tableCell(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRowCoordinates() {
		return this.row;
	}

	public int getColCoordinates() {
		return this.col;
	}

	/**
	 * Method used for returning a new object with row incremented of one.
	 * 
	 * @return a <b>NEW</b> tableCell object. The new object return is needed so
	 *         that method not modify the initial object used by other methods.
	 */
	public tableCell nextRowNewObject() {
		return new tableCell(this.row + 1, this.col);
	}

	/**
	 * Method used for incrementing row with one unit.
	 * 
	 * @return the <b>SAME</b> tableCell object with row parameter incremented.
	 */
	public tableCell nextRow() {
		this.row++;
		return this;
	}
}
