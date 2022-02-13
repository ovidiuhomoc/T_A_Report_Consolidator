/*
 * required for recyclng purposes only - try catch part of connection testing

package time_and_attendance_report_consolidator;

import java.util.HashSet;
import java.util.Set;

//one DataSource object will contain all required methods (open, read, ...), details for each connection & set of all connections for this profile

public class DataSource {
	private String name;
	private Set<Connection> connections = new HashSet<Connection>();
	private Connection active;
	
	public DataSource(String ProfileName) {
		this.name = ProfileName;
	}
	
	
	// Needed for recycling !!!!!! try / catch part
	public void setCSV(String name, String file, String delimiter) {
		boolean okFlag = false;
		Connection x = new Connection();
		
		try {
			x.setConnection(name,"CSV",file);
			x.setCSVDelimiter(delimiter);
			okFlag = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (okFlag) {
			this.connections.add(x);
			if (this.connections.size()==0) {
				this.active=x;
			}
		}
		//return okFlag;
	}
	
	public Connection[] connectionsToArray() {
		Connection[] arr = this.connections.toArray(new Connection[0]);
		return arr;
	}
	
	public void removeConnection(Connection conn) {
		if(this.connections.contains(conn)) {
			if(this.active.equals(conn)) {
				if(this.connections.size()==1) {
					this.active = null;
				}
				else {
					this.active = this.connectionsToArray()[0];
				}
			}
			this.connections.remove(conn);
		}
	}
	
	//public void addExcel(String path, String fileName){}
	
	//public void addMSQL(SQL engine connection name, user, pass) {}
}
*/