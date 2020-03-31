package root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.joda.time.MutablePeriod;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodParser;
/**
 * Date : August-05-19
 * @author Aman Agrawal
 * @author Anushka Awasthi
 * @author Karun Singh
 *@version 1.0
 *
 *This Class is designed to calculate total working hours in a week, for a particular employee. *
 */
public class Test2 {
	public static void calculateWeekTotal(){
		try {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");
			if(dtf.format(now).equalsIgnoreCase("saturday")||dtf.format(now).equalsIgnoreCase("sunday")) {

				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","hr","hr");
				Statement stmt=conn.createStatement();
				ResultSet rs=stmt.executeQuery("select * from NEC ");
				while(rs.next()) {
					String Userid=rs.getString("id");

					String Mon=rs.getString("monday");
					if(rs.wasNull()) {
						LocalTime monday=LocalTime.parse("00:00");
						Mon=monday.toString();
					}

					String Tue=rs.getString("tuesday");
					if(rs.wasNull()) {
						LocalTime tuesday=LocalTime.parse("00:00");
						Tue=tuesday.toString();
					}

					String Wed=rs.getString("wednesday");
					if(rs.wasNull()) {
						LocalTime wednesday=LocalTime.parse("00:00");
						Wed=wednesday.toString();
					}

					String Thu=rs.getString("thursday");
					if(rs.wasNull()) {
						LocalTime thursday=LocalTime.parse("00:00");
						Thu=thursday.toString();
					}

					String Fri=rs.getString("friday");
					if(rs.wasNull()) {
						LocalTime friday=LocalTime.parse("00:00");
						Fri=friday.toString();
					}

					PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
					builder.printZeroAlways()
					.minimumPrintedDigits(2)
					.appendHours()
					.appendSeparator(":").appendMinutes();

					PeriodFormatter formatter = builder.toFormatter();
					PeriodParser parser = builder.toParser();

					MutablePeriod p1 = new MutablePeriod();
					MutablePeriod p2 = new MutablePeriod();
					MutablePeriod p3 = new MutablePeriod();
					MutablePeriod p4 = new MutablePeriod();
					MutablePeriod p5 = new MutablePeriod();
					parser.parseInto(p1, Mon, 0, Locale.getDefault());      
					parser.parseInto(p2, Tue, 0, Locale.getDefault());      
					parser.parseInto(p3, Wed, 0, Locale.getDefault());      
					parser.parseInto(p4, Thu, 0, Locale.getDefault());      
					parser.parseInto(p5, Fri, 0, Locale.getDefault());      

					p1.add(p2);
					p1.add(p3);
					p1.add(p4);
					p1.add(p5);
					String total= formatter.print(p1);
					int target=40;
					String[] result=total.split(":");
					int workedFor=Integer.parseInt(result[0]);
					String reached;
					if(workedFor>=target) {
						reached="yes";
					}
					else {
						reached="no";
					}

					PreparedStatement stmt2=conn.prepareStatement("update NEC set totalworkhours=?, targetreached=? where id=?");
					stmt2.setString(1,total);
					stmt2.setString(2,reached);
					stmt2.setString(3,Userid);

					stmt2.executeUpdate();


					//	                System.out.println(total);
				}
				stmt.executeUpdate("update NEC set Monday=null,Tuesday=null,Wednesday=null,Thursday=null,Friday=null");
				Test.calculateSalary();
			}
			else {
				System.err.println("Weekly total can only be calculated on weekends\n");
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
//CREATE TABLE NEC (
//id int,
//name varchar2(255),
//Entry varchar2(255),
//Exit varchar2(255),
//Monday varchar2(255),
//Tuesday varchar2(255),
//Wednesday varchar2(255),
//Thursday varchar2(255),
//Friday varchar2(255),
//TotalWorkHours varchar2(255)
//);
//update NEC set entry='08:00',Monday='08:00',Tuesday='08:00',Wednesday='08:00',Thursday='08:00',Friday='08:00' where id=1;
//update NEC set entry='08:00',Monday='08:00',Tuesday='08:00',Wednesday='08:00',Thursday='08:00',Friday='09:00' where id=2;
//update NEC set entry='00:00',Monday='00:00',Tuesday='04:00',Wednesday='04:00',Thursday='04:00',Friday='09:00' where id=3;
//
//
//
