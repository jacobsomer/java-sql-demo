package net.codejava.sql;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class JavaConnect2SQL {
	private final String databaseName = "SQL_Project";
	private final String  tableName= "Orders";
	private final String  userName= "jacob";
	private final String  password= "password";
	public Connection conn= null;
	public void getConnection() throws SQLException {
		String url="jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName="+this.databaseName+";";
		String username=this.userName;
		String password =this.password;
		conn = DriverManager.getConnection(url,username,password);
	}
	public boolean executeUpdate(String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	public void getShareValue(int userid) throws SQLException {
		if (userid<1001 || userid>1007) {
			System.out.println("Invalid User Input");
			return;
		}
		Statement stmt = null;
		String userstring=String.valueOf(userid);
		stmt = conn.createStatement();
		String createString =
			        "SELECT *, (PRICE*SHARES) AS TOTAL " +
			        "From "+ this.tableName+ " " +
			        "WHERE USERID = "+ userstring +";";
		ResultSet rs = stmt.executeQuery(createString);
		int sum=0;
		while(rs.next()){
	         //Retrieve by column name
	         sum += rs.getInt("TOTAL");
	      }
		System.out.println("The portfolio value of User: "+userstring+" is "+ String.valueOf(sum));
	}
	public void insertData() {
		try {
		    String createString =
			        "INSERT INTO " + this.tableName +" VALUES "+
			        "(1,1001,'MFST',20,200), " +
			        "(2,1002,'AAPL',1,300), " +
			        "(3,1003,'ALB',30,400), " +
			        "(4,1001,'AAPN',14,500), " +
			        "(5,1004,'GOOGL',64,600), " +
			        "(6,1005,'SPY',3,700), " +
			        "(7,1003,'APPN',75,500), " +
			        "(8,1006,'TSLA',43,1000), " +
			        "(9,1004,'MFST',4,200), " +
			        "(10,1007,'FSLR',56,900) " ;
			this.executeUpdate( createString);
			System.out.println("Created a table");
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table");
			e.printStackTrace();
			return;
		}
	}
	public void createTables() {
		try {
		    String createString =
			        "CREATE TABLE " + this.tableName + " ( " +
			        "ORDERID int NOT NULL, " +
			        "USERID  int NOT NULL, " +
			        "SYMBOL varchar(50) NOT NULL, " +
			        "SHARES int NOT NULL, " +
			        "PRICE int NOT NULL, " +
			        "PRIMARY KEY (OrderID))";
			this.executeUpdate( createString);
			System.out.println("Created a table");
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table");
			e.printStackTrace();
			return;
		}
	}
	public void dropTables() {
		
		// Drop the table
				try {
				    String dropString = "DROP TABLE " + this.tableName;
					this.executeUpdate( dropString);
					System.out.println("Dropped the table");
			    } catch (SQLException e) {
					System.out.println("ERROR: Could not drop the table");
					e.printStackTrace();
					return;
				}
	}
	public void run() {
		
		try {
			this.getConnection();
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		System.out.println("Creating Tables");
		this.createTables();
		System.out.println("Inserting Data");
		this.insertData();
		System.out.println("Getting data");
		try {
			this.getShareValue(1001);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			TimeUnit.SECONDS.sleep(120);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dropTables();	
	}
	/**
	 * Connect to the DB and do some stuff
	 */
	public static void main(String[] args) {
		JavaConnect2SQL app = new JavaConnect2SQL();
		app.run();
	}
}