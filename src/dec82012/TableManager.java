package dec82012;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class TableManager {
	
	public static String tweetsUserTableName;
	public static Connection con = null; 
	private static Logger fileLogger = Logger.getLogger("fileLogger");
	private static Logger emailLogger = Logger.getLogger("emailLogger");
	
	TableManager() {
		//Does Nothing
	}
	
	TableManager(Connection someConnection, String someDate) throws SQLException {
		con = someConnection;
		tweetsUserTableName = "tweets_MI_ReTweetInfo_"+someDate;
		try {
			
			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet resultSetTables = databaseMetaData.getTables(null, null, tweetsUserTableName, null);
			if(resultSetTables.next()){
				// If the table already exists, don't try to create it
			} else {
				this.makeTweetsUserTable();
			}
			
			
		} catch (Exception e) {
			fileLogger.error("There was a problem.",e);
			emailLogger.error("Error", e);
			e.printStackTrace();	
		}
	}
		
	public void makeTweetsUserTable() throws SQLException {
		Statement statement = null;
		String sql = "CREATE TABLE " + tweetsUserTableName + " ( " + 
				"tweet_id varchar(30) NOT NULL," +
				"ot_tweet_id varchar(30) NOT NULL," +
				"tweet_text varchar(170) DEFAULT NULL," +
				"created_time varchar(60) DEFAULT NULL," +
				"ot_created_time varchar(60) DEFAULT NULL," +
				"user_id varchar(30) DEFAULT NULL," +
				"user_handle varchar(30) DEFAULT NULL," +
				"user_ot_id varchar(30) DEFAULT NULL," +
				"user_ot_handle varchar(30) DEFAULT NULL," +
				"is_u_follwing_u_ot tinyint(1) DEFAULT NULL," +
				"is_u_ot_follwing_u tinyint(1) DEFAULT NULL," +
				"user_like_count int(11) DEFAULT NULL," +
				"user_ot_like_count int(11) DEFAULT NULL," +
				"user_followers_count int(11) DEFAULT NULL," +
				"user_ot_followers_count int(11) DEFAULT NULL," +
				"user_following_count int(11) DEFAULT NULL," +
				"user_ot_following_count int(11) DEFAULT NULL," +
				"user_tweet_count int(11) DEFAULT NULL," +
				"user_ot_tweet_count int(11) DEFAULT NULL," +
				"user_joined_on varchar(60) DEFAULT NULL," +
				"user_ot_joined_on varchar(60) DEFAULT NULL," +
				"user_location varchar(30) DEFAULT NULL," +
				"user_ot_location varchar(30) DEFAULT NULL," +
				"favorite_count int(11) DEFAULT NULL," +
				"ot_favorite_count varchar(30) DEFAULT NULL," +
				"retweet_count int(11) DEFAULT NULL," +
				"ot_retweet_count varchar(30) DEFAULT NULL," +
				"reply_to_status_id varchar(30) DEFAULT NULL," +
				"reply_to_user_id varchar(30) DEFAULT NULL," +
				"reply_to_user_handle varchar(30) DEFAULT NULL," +
				"is_sensitive tinyint(1) DEFAULT NULL," +
				"is_retweeted tinyint(1) DEFAULT NULL," +
				"user_mention_count int(11) DEFAULT NULL," +
				"url_count int(11) DEFAULT NULL," +
				"hashtag_count int(11) DEFAULT NULL," +
				"media_count int(11) DEFAULT NULL," +
				"symbol_count int(11) DEFAULT NULL" +
				" ) ";
		
		    
		try {
			statement = con.createStatement();
			statement.execute(sql);
			con.commit();		
		} catch (SQLException e) {
			fileLogger.error("There was a problem.",e);
			emailLogger.error("Error", e);
			e.printStackTrace();
		}
	}
}
