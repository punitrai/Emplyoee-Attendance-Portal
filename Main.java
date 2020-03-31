package root;

import java.util.Scanner;

/**
 * Date : August-05-19
 * @author Aman Agrawal
 * @author Anushka Awasthi
 * @author Karun Singh
 *@version 1.0
 *
 *This application replicates the login system for maintaining employee working hours records.
 *
 */
public class Main{
	

	public static void main(String[] args) {
		Scanner sc1 = new Scanner(System.in);  
		System.out.print("Enter LoginID: ");
		String id = sc1.nextLine();
		String adminID="admin";
		if(adminID.equals(id))
		{
			Test.adminenq();
			main(args);
		}
		
		if(id.equalsIgnoreCase("visitor")) {
			Guest.visitor();
			main(args);
		}

		Test.checkEntry(id);
		System.out.println();
		main(args);
	}
}
//column name format a20;
//column entry format a5;
//column exit format a5;
//column monday format a6;
//column tuesday format a7;
//column wednesday format a9;
//column thursday format a8;
//column friday format a6;
//column sunday format a6;
//column totalworkhours format a14;