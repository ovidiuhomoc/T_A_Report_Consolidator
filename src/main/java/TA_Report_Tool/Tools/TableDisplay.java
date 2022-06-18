package TA_Report_Tool.Tools;

import static TA_Report_Tool.Tools.check.isZero;

import java.util.ArrayList;

public class TableDisplay {
	private ArrayList<innerRowClass> table = new ArrayList<innerRowClass>();
	innerRowClass currentRow;
	int maxTabCell = 0;

	public TableDisplay() {
		this.table = new ArrayList<innerRowClass>();
		newDispRow();
	}

	public <T> void add(T element) {
		this.currentRow.add(String.valueOf(element));
	}

	public void newDispRow() {
		this.currentRow = new innerRowClass();
		this.table.add(this.currentRow);
	}

	public void display(String tableName) {
		int maxChBlockSize = 0;

		for (innerRowClass x : this.table) {
			for (String y : x.rowToArray()) {
				if (y.length() > maxChBlockSize) {
					maxChBlockSize = y.length();
				}
			}
		}

		if (isZero(maxChBlockSize % 8)) {
			this.maxTabCell = maxChBlockSize / 8 + 1;
		} else {
			this.maxTabCell = maxChBlockSize / 8;
		}

		System.out.println();
		if (tableName.equals("")) {
			System.out.println("--- Display Started ---");
		} else {
			System.out.println("--- Display Started for table " + tableName + " ---");
		}

		for (innerRowClass x : table) {
			for (String y : x.rowToArray()) {
				print(y);
			}
			System.out.println();
		}
		System.out.println("--- Display Ended ---");
		System.out.println();
	}

	private void print(String stringToPrint) {
		int currNoOfTabs = 0;
		currNoOfTabs = stringToPrint.length() / 8;
		System.out.print(stringToPrint);
		for (int i = 1; i <= (this.maxTabCell - currNoOfTabs + 1); i++) {
			System.out.print("\t");
		}
	}

	private class innerRowClass {
		private ArrayList<String> row;

		public innerRowClass() {
			this.row = new ArrayList<String>();
		}

		public void add(String element) {
			row.add(element);
		}

		public String[] rowToArray() {
			return this.row.toArray(new String[0]);
		}
	}
}
