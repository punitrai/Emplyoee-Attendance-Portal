package root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
/**
 * Date : August-05-19
 * @author Aman Agrawal
 * @author Anushka Awasthi
 * @author Karun Singh
 *@version 1.0
 *
 *This Class is designed to check for entry and exit details of an employee
 *It also calculates per day working hours for an employee.
 *It further extends to calculate weekly salary of an employee.
 *
 */
public class Test{



	public static void checkEntry(String id) {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select Entry from NEC where id="+id);
			if (!rs.isBeforeFirst() ) {    
				System.err.println("User " +id+ " doesnt exist in the Database.\n");
			}
			while(rs.next()) {

				String a=rs.getString(1);
				if(rs.wasNull()) {
					entry(id);
				}
				else {
					exit(id);
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}


	public static void entry(String id) {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");  
			int result=stmt.executeUpdate("update NEC set Entry='"+dtf.format(now)+"' where id="+id);
			ResultSet rs=stmt.executeQuery("select name from NEC where id="+id);

			while(rs.next()) {
				String a=rs.getString(1);
				System.out.println("Hello "+a+", Welcome to NEC!");
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}


	public static void exit(String id) {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");  
			int result=stmt.executeUpdate("update NEC set Exit='"+dtf.format(now)+"' where id="+id);
			ResultSet rs=stmt.executeQuery("select name from NEC where id="+id);

			while(rs.next()) {
				String[] a=rs.getString(1).split(" ");
				System.out.println("Bye "+a[0]+".");
			}
			calculateDayHours(id);
			setNull(id);

		}
		catch(Exception e) {
			System.out.println(e);
		}
	}


	public static void calculateDayHours(String id) {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");
			ResultSet rs=stmt.executeQuery("select entry,exit,"+dtf.format(now)+" from NEC where id="+id);

			while(rs.next()) {
				DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm");
				LocalTime entryTime = LocalTime.parse(rs.getString(1));
				LocalTime exitTime = LocalTime.parse(rs.getString(2));
				System.out.println("Your entry time was: "+entryTime+" and exit time is: " +exitTime+ " ");
				LocalTime todaysTotal;
				ResultSet rs2=stmt.executeQuery("select "+dtf.format(now)+" from NEC where id="+id);
				while(rs2.next()) {
					String b=rs2.getString(1);
					if(rs2.wasNull()) {
						todaysTotal=LocalTime.parse("00:00");
					}
					else{
						todaysTotal=LocalTime.parse(rs2.getString(1));
					}
					LocalTime difference=LocalTime.parse("00:00");
					difference=exitTime.minusHours(entryTime.getHour()).minusMinutes(entryTime.getMinute());
					System.out.println("Length of current session was "+difference+" hours.");
					todaysTotal=todaysTotal.plusHours(difference.getHour()).plusMinutes(difference.getMinute());
					System.out.println("You have worked for a total of "+todaysTotal+" hours today.");
					int result=stmt.executeUpdate("update NEC set "+dtf.format(now)+"='"+todaysTotal+"' where id="+id);
				}
			}

		}  
		catch(Exception e) {
			System.out.println(e);
		}
	}


	public static void setNull(String id) {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			int result=stmt.executeUpdate("update NEC set Entry=null,Exit=null where id="+id);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	public static void adminenq() {

		System.out.println("1)Visitor Enquiry. ");
		System.out.println("2)Calculate this week's salary. ");
		System.out.println("3)Logout ");

		Scanner sc = new Scanner(System.in);  
		int x = sc.nextInt();

		switch (x) {
		case 1: 
			Guest.countVisitor();
			break;
		case 2: 
			Test2.calculateWeekTotal();
			break;
		case 3: 
			System.out.println();
			Main.main(null); 
		default: 
			System.err.println("invalid selection");
			break;
		}
		adminenq();

	}
	public static void calculateSalary() {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select id,totalworkhours from NEC ");
			while(rs.next()) {
				String time=rs.getString("totalworkhours");
				String[] result=time.split(":");
				int workedFor=Integer.parseInt(result[0]);
				int basicSal=20000;
				int variableSal=200;
				int totalSal=0;
				if(workedFor>=40) {
					totalSal=basicSal+(variableSal*40);
				}
				else {
					totalSal=basicSal+(variableSal*workedFor);
				}
				String id=rs.getString("id");
				PreparedStatement stmt3=conn.prepareStatement("update NEC set Salary=? where id=?");
				stmt3.setInt(1,totalSal);
				stmt3.setString(2,id);
				stmt3.executeUpdate();
			}
			System.out.println("Salary Calculation completed.\n");
		}
		catch (Exception e) {
			System.out.println(e);	
		}
	}
}