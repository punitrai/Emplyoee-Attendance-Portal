package root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
/**
 * Date : August-05-19
 * @author Aman Agrawal
 * @author Anushka Awasthi
 * @author Karun Singh
 *@version 1.0
 *
 *This Class is designed to take a count of number of daily visitors.
 *It also ensures that at the end of the day the number of visitors entering should exit the premises*
 *It also gives daily account of visitor's entry to the admin. 
 */
public class Guest {

	public static void visitor() {
		System.out.println("1) Entry ");
		System.out.println("2) Exit ");
		Scanner sc=new Scanner(System.in);
		int i=sc.nextInt();
		switch (i) {
		case 1: countEntry();
		break;
		case 2: countExit();
		break;
		default: System.err.println("invalid selection");
		visitor();
		break;
		}		
	}



	public static void countEntry() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select entries from visitor");
			while(rs.next()) {
				int k=rs.getInt(1);
				k=k+1;
				stmt.executeUpdate("update visitor set entries="+k);
				System.out.println("Welcome to NEC! \n");
			}	
		} catch (Exception e) {
			System.out.println(e);	}	 


	}
	public static void countExit() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select entries from visitor");
			while(rs.next()) {
				int j=rs.getInt(1);
				if(j>0)
				{
					j=j-1;
				}
				else {
					System.err.println("Entries out of sync. \n");
					break;
				}
				stmt.executeUpdate("update visitor set entries="+j);
				System.out.println("Thank you for visiting NEC! \n");
			}	
		} catch (Exception e) {
			System.out.println(e);	
		}	 
	}
	
	public static void countVisitor()
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select entries from visitor");
			while(rs.next()) {
				int k=rs.getInt(1);
				System.out.println("Total number of visitors currently inside office "+k+"\n" );
			}

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}