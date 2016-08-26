package dec82012;

import java.awt.Toolkit;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;
import org.joda.time.DateTime;

import twitter4j.TwitterException;

public class ScraperImplementationMI {
	static Connection con = null;
	static int quantityOfCompaniesToTrack = -1;
	static String[] stockNamesForTwitter;
	static String[] stockSymbolsForYahoo;
	static String[] companyForTable;
	static String[] projectCodeForTable;
	static long[] usersToTrack;
	static String currentDate = null;
	static String checkDate = null;
	
	// totalTweets keeps track of all the tweets throughout the day for console reporting
	public static int totalUserTweets = 0;
	static int deleteRowsInIndex = 0;
	static int addRowsInIndex = 1;
	
	public static void incrementTotalUserTweets() {
		/*
		 * The purpose of this method is to keep track of the number of tweets throughout the day so that they can be
		 * output to the console for reporting purposes. I think it's reassuring to see the number of tweets updated  
		 * after each scrape session; it tells me the program is working. 
		 */
		totalUserTweets++;
	}
	
	/*
	 *  I can refer to this logger in the entire program by making a new Logger instance and initializing it like,
	 *  Logger someLogger = Logger.getLogger("MasterLogger");
	 *  This way I don't need to keep passing around the reference. 
	 */
	private static Logger fileLogger = Logger.getLogger("fileLogger");
	private static Logger emailLogger = Logger.getLogger("emailLogger");
	
	//this will create a new logger for the new day
	
	public static void updateLogger() throws IOException
	{
		DateTime currentTime = new DateTime();
		currentDate = currentTime.getMonthOfYear() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getYear();
		//checkDate = currentTime.getMonthOfYear() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getYear();
		String fileNameAndPath = Settings.getFilePathForLogFile() + "logFile" + currentDate + ".txt";
		SimpleLayout layout = new SimpleLayout();  
	    FileAppender appender = new FileAppender(layout, fileNameAndPath,false);  
	    appender.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} [%t] %m%n"));
	    fileLogger.addAppender(appender);
	    fileLogger.setLevel((Level) Level.INFO);
		fileLogger.info("Starting up...");
	}
	
	public static void main(String [] args) throws Exception {
		
		Settings.configure();
		TimeZoneAnalyzer.startup();
		
		// This is where I setup the advanced logger. I'm creating a file name dynamically using the date.
		System.setProperty ("twitter4j.loggerFactory","twitter4j.internal.logging.NullLoggerFactory");
		PropertyConfigurator.configure("log4j.properties");
		updateLogger();
		/*DateTime currentTime = new DateTime();
		DateTime checkTime;
		currentDate = currentTime.getMonthOfYear() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getYear();
		//checkDate = currentTime.getMonthOfYear() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getYear();
		String fileNameAndPath = Settings.getFilePathForLogFile() + "logFile" + currentDate + ".txt";
		SimpleLayout layout = new SimpleLayout();  
	    FileAppender appender = new FileAppender(layout, fileNameAndPath,false);  
	    appender.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} [%t] %m%n"));
	    fileLogger.addAppender(appender);
	    fileLogger.setLevel((Level) Level.INFO);
		fileLogger.info("Starting up...");*/
		DateTime checkTime;
		Toolkit.getDefaultToolkit().beep();
		try {
			con = DriverManager.getConnection(Settings.getMySQLURL(),Settings.getMySQLUserName(),Settings.getMySQLPassword());
			con.setAutoCommit(false);
		} catch (SQLException e) {
			fileLogger.error("Unable to connect to MySQL. ",e);
			emailLogger.error("Unable to connect to MySQL. ", e);
			e.printStackTrace();
		}
		
		TableManager twitterTable = new TableManager(con, currentDate);
	
		//TableManager.createNewIndex(deleteRowsInIndex, addRowsInIndex);
		
		// Retrieve from our database the names and stock symbols of the companies that we would like to scrape.
		try {
			Statement statement = null;
			ResultSet resultSet = null;
			
			String query2 = "select user_id from userid";
			String queryGetSizeOfTable2 = "select count(*) from userid";
			statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(queryGetSizeOfTable2);
			resultSet.next();
			int quantityOfUsersToTrack = resultSet.getInt(1);
			resultSet.close();
			//System.out.println(quantityOfUsersToTrack);
			statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(query2);
			usersToTrack = new long[quantityOfUsersToTrack];
			
			for(int currentUser = 0; currentUser < quantityOfUsersToTrack; currentUser++) {
				resultSet.next();
				usersToTrack[currentUser] = Long.parseLong(resultSet.getString("user_id"));
				TrackerManager.addTracker(resultSet.getString("user_id"));
			}	
			
		} catch (SQLException e) {
			fileLogger.error("There was a problem. ",e);
			emailLogger.error("Error", e);
			e.printStackTrace();
		} catch (Exception e) {
			fileLogger.error("There was a problem. ",e);
			emailLogger.error("Error", e);
			e.printStackTrace();
		}
		
		Scraper scraper2 = new Scraper(con, usersToTrack );
		int numberOfScrapingSessions = 0; 
		int recordScraping = 0;
		
		// If the output table already exists, update each Tracker's timeIndex variable
		
		scraper2.collectUserTweets();
		//scraper2.getPastNTweets();
		/*String startDate = "2016-01-16";
		String endDate =   "2016-01-23";
		//scraper2.getTweetsDuring(startDate, endDate);
		scraper2.getPastNTweets();*/
		//scraper2.getPastNTweets();
		while (numberOfScrapingSessions < Settings.getNumberOfScrapingSessions())	
		{
			checkTime = new DateTime();
			checkDate = checkTime.getMonthOfYear() + "_" + checkTime.getDayOfMonth() + "_" + checkTime.getYear();
			System.out.println(checkDate);
			if(checkDate.equals(currentDate)) {
				//System.out.println("Same Day");
			} else
			{
				// new day, new table, new count
				updateLogger();
				currentDate = checkDate;
				twitterTable = new TableManager(con, currentDate);
				scraper2.updatePrepareStament();
				numberOfScrapingSessions=0;
				recordScraping=0;
				totalUserTweets=0;
			}
			try {
				//scraper.scrapeYahooMainPage();
				//numberOfScrapingSessions++;
				recordScraping++;
				fileLogger.info(recordScraping + " successful scraping sessions.");
				fileLogger.info(totalUserTweets + " total User tweets scraped.");
				// logging for every 5 minutes
				Thread.sleep(5L * Settings.getScrapeTimer() * 1000L);
			} catch (Exception e) {
				fileLogger.error("A general exception occured while trying to scrape data. We might have been temporarily blocked.", e);
				emailLogger.error("A general exception occured while trying to scrape data. We might have been temporarily blocked.", e);

				Thread.sleep(Settings.getScrapeTimer() * 1000L);
				//scraper.scrapeYahooMainPage();
				e.printStackTrace();
			}		
		}
		// This closes the connection to Twitter
		//scraper2.twitterStream.cleanUp();
		scraper2.twitterStream2.cleanUp();
		
		// Alerting me that the program is done
		Toolkit.getDefaultToolkit().beep(); Thread.sleep(1L * 1000L);
		Toolkit.getDefaultToolkit().beep(); Thread.sleep(1L * 1000L);
		Toolkit.getDefaultToolkit().beep();
		fileLogger.info("Finished.");
	}
}
