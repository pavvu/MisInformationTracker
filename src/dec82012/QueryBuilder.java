package dec82012;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryBuilder 
{
	private static Connection con = null;
	private static PreparedStatement preparedStatementForStoringUserTweets = null;
	public static  PreparedStatement  getPSToStoreTweetData() throws SQLException
	  {
		  con = Settings.getConnection();
		  String queryForStoringUserTweets = "insert into user_past_tweets  " +
					"(" + 
					"tweet_id, " + 
					"tweet_text, " +
					"created_time, " +
					"user_id, " + 
					"user_handle, " +
					"reply_to_status_id, " + 
					"reply_to_user_id, " + 
					"reply_to_user_handle," +  
					"is_sensitive," +  
					"is_retweeted," +  
					"tweet_geolocation," +  
					"tweet_place," +  
					"user_location" +  
					") " + 
					"values" +
					"(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			preparedStatementForStoringUserTweets = con.prepareStatement(queryForStoringUserTweets);
			con.commit();
			return preparedStatementForStoringUserTweets;
	  }

}
