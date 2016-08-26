package dec82012;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
/* 
 * This class makes it easier to transition code from one computer to another.  
 */

public class Settings {
	
	public static long ONE_MINUTE = 1000*60;
	// To update all local settings, all you will need to do is change the currentUser string
	private static String ryan = "ryan";
	private static String hari = "hari";
	private static String serv = "server";
	private static String root = "root";
	private static String currentUser = "root";
			
	// Database settings
	private static String url = "jdbc:mysql://localhost:3306/twitterdata";
	private static String dbName = "twitterdata";
	private static String userName = "root"; 
	private static String password = "root";
	
	// File path for log file
	private static String filePathForLogFile = "C:\\Users\\CBA\\Desktop\\TwitterDataRetrieval\\MILogs\\log";
	
	// File path for .csv file
	private static String filePathForCsvFile = "C:\\Users\\CBA\\Desktop\\TwitterDataRetrieval\\logs\\log";
	
	// The number of seconds in between each scrape session
	private static long scrapeTimer = 60L;
	
	public static int numberOfScrapingSessions = 24*60;
	
	public static void configure(){
		if(currentUser.equals(ryan)){
			url = "jdbc:mysql://localhost:3306/";
			dbName = "TaftiZotti";
			userName = "root"; 
			password = "";
			filePathForLogFile = "/Users/ryanzotti/Desktop/";
			filePathForCsvFile = "/Users/ryanzotti/Desktop/";
		} else if(currentUser.equals(hari)){
			url = "jdbc:mysql://localhost:3306/";
			dbName = "test";
			userName = "root"; 
			password = "hari";
			//filePathForLogFile = "C:\\JavaDev\\Tafti_Zotti_Research\\LogFiles\\";
			//filePathForCsvFile = "C:\\JavaDev\\Tafti_Zotti_Research\\TwitterScrapingOutput";
			filePathForLogFile= "D:\\RA";	
			filePathForCsvFile = "D:\\RA\\TwitterYahooScrapingOutput";		
		} else if(currentUser.equals(serv)){
			url = "";
			dbName = "";
			userName = ""; 
			password = "";
			filePathForLogFile= "D:\\RA";	
			filePathForCsvFile = "D:\\RA\\TwitterYahooScrapingOutput";		
		} 
		else if(currentUser.equals(root)){
			/*url = "";
			dbName = "";
			userName = ""; 
			password = "";
			filePathForLogFile= "D:\\RA";	
			filePathForCsvFile = "D:\\RA\\TwitterYahooScrapingOutput";		*/
		} 
	}
	
	public static int getNumberOfScrapingSessions() {
		return numberOfScrapingSessions;
	}
	
	public static long getScrapeTimer(){
		return scrapeTimer;
	}
	
	public static String getMySQLURL(){
		String input = null;
		input = url;
		return input;
	}
	
	public static String getMySQLUserName(){
		return userName;
	}
	
	public static String getMySQLPassword(){
		return password;
	}
	
	public static String getFilePathForLogFile(){
		return filePathForLogFile;
	}
	
	public static String getFilePathForCsvFile(){
		return filePathForCsvFile;
	}
	
	public static Connection getConnection()
	{
		Connection con=null;
		try 
		{
			con = DriverManager.getConnection(Settings.getMySQLURL(),Settings.getMySQLUserName(),Settings.getMySQLPassword());
			con.setAutoCommit(false);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
		   
	}
}


