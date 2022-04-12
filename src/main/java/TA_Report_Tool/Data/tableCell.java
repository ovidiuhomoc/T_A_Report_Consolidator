package TA_Report_Tool.Data;

public class tableCell {
	private int row;
	private int col;

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
}
