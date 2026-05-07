package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 //This class handles the JDBC connection to MySQL

public class DatabaseConnection {

	private static final String db_URL = "jdbc:mysql://localhost:3306/smart_hostel";
	private static final String db_User = "root";
	private static final String db_Password = "Erum@mysql123";
 
	private static Connection connection;

    	private DatabaseConnection() {}

   	public static Connection getConnection() {

    	try {
        	System.out.println("Connecting to DB...");

        	Class.forName("com.mysql.cj.jdbc.Driver");

		if (connection == null) {
        	connection = DriverManager.getConnection(db_URL, db_User, db_Password);
        	System.out.println("SUCCESS CONNECTION!!");    }
	
	
	}catch (Exception e) {
        	System.out.println("CONNECTION FAILED!!");
        	e.printStackTrace();		
	}

	return connection;	}

}

