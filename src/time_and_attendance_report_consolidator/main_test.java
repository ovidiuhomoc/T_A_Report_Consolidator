package time_and_attendance_report_consolidator;

public class main_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(Profile.getActive());

		Profile profil1 = new Profile("Profile 1");
		System.out.println(Profile.getActive().getName());
		Profile profil2 = new Profile();
		System.out.println(Profile.getActive().getName());
		Profile profil3 = new Profile("Profile 3");
		profil3.setActive();
		System.out.println(Profile.getActive().getName());

		for (Profile x : Profile.toArray()) {
			System.out.println(x.getName());
		}

		profil1.setCSVConn("Conexiune CSV 1", "c://test.CSV");
		profil1.setCSVConn("Conexiune CSV 1", "c://test2.CSV");
		profil1.setCSVConn("Conexiune CSV 2", "c://test24.CSV");
		for (Connection con : profil1.connectionsToArray()) {
			System.out.println(con.getName() + " -- " + con.getFilePath());
		}

		profil2.setCSVConn("Conexiune CSV 2.1", "c://test.CSV");
		profil2.setCSVConn("Conexiune CSV 2.2", "c://test2.CSV");
		profil2.setCSVConn("Conexiune CSV 2.3", "c://test24.CSV");
		for (Connection con : profil2.connectionsToArray()) {
			System.out.println(con.getName() + " -- " + con.getFilePath());
		}

		// Header h = new Header(profil1.getActiveConn());

		ContentCSVparser csvProcesor = new ContentCSVparser();
		String csvRow = new String(",\"\"\"Test1\",,\"\"\"Test 2,2\"\"\",\"Test 3,3\",,Test 4");
		System.out.println(csvRow);

		int count = 0;
		for (String x : csvProcesor.parseCSVrow(csvRow, ",")) {
			count++;
			System.out.println("Column " + count + " is:-->" + x + "<--");
		}
		
		
		/*
		try {
			System.out.println("Checkpoint 1");
			throw new ExceptionsPack.contentNotFound(
					"Too few rows in the file and Header start row did not reached. End of file encountered.");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Checkpoint 2");
		}*/

	}

}
