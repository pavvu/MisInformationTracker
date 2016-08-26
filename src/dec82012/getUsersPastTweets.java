package dec82012;
/***********************************************************************
 * Version History
 * 
 * Version_No		Date			Author  	Reason for Modification
 * 1.0				23-Jan-2016     Pavan		Initial Version
 */
// This code collects tweets posted by given users in DataBase and saves them in a table handle_tweets
// This can extract onlly past 3200 tweets.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import twitter4j.*;

class getUsersPastTweets
{
	private static PreparedStatement preparedStatementForStoringUserTweets;
	private static Twitter twitter;
	private static Connection con;
	private static int i =0;
	private static int FIVE_MINUTES = 1000*60*5;
	private static int countDuplicates = 0;
	public static void main(String[] args) throws Exception
	{
		preparedStatementForStoringUserTweets = null;
		twitter = BuildConfiguration.getTwitter();
		int pageno = 1;
		String[] handles = getHandles();
		List<Status> statuses = new ArrayList();
		
		// db connections and building query
		con = Settings.getConnection();
		String query = buildQueryString();
		preparedStatementForStoringUserTweets = con.prepareStatement(query);
		con.commit();
		
		// code to get tweets of given users 
		int size =0;
		
		for (String  handle  : handles) 
		{
			while(true)
			{
				// from a given page reading 100 tweets and repeating the same for next page
				try
				{
					Paging page = new Paging(pageno++, 100);
					//size = statuses.size();
				    statuses.addAll(twitter.getUserTimeline(handle, page));
				    if(statuses.size()==0)
				    {
				    	System.out.println("done with user " + handle);
				    	break;
				    }
				    	
				    storeInDB(statuses);
				    statuses.clear();
				    //System.out.println("collected "+ size + " tweets");
				}
				
				
				catch(TwitterException e)
				{
					System.out.println("!!Rate Limit Exceeded!!");
					pageno--;
					Thread.sleep(FIVE_MINUTES);
				}
			}
			
			System.out.println(getUserStatistics.getTimeStamp() + " collected "+ size +" tweets for  " + handle );
			pageno=1;
		}
		
		System.out.println("all tweets are stored in database");
		System.out.println(" duplicates " + countDuplicates);
	}
	
	// store in db
	public static void storeInDB(List<Status> statuses)
	{
		for (Status status : statuses) 
		{
			try
			{
				if(++i%1000==0) System.out.println(getUserStatistics.getTimeStamp() + " Stored " +i+" tweets in database");
				preparedStatementForStoringUserTweets = PrepareStatement.prepareStatement(status, preparedStatementForStoringUserTweets);
				preparedStatementForStoringUserTweets.executeUpdate();
	            con.commit();
				/*if(status.getSymbolEntities().length!=0 &
				   status.getUserMentionEntities().length!=0 &
				   status.getURLEntities().length!=0 &
				   status.getMediaEntities().length!=0)
				{
					System.out.println(status.getText());
				}
				if(status.getGeoLocation()!=null)
				{
					System.out.println("geo location not null!!");
				}*/
			}
			catch(MySQLIntegrityConstraintViolationException mse)
			{
				System.out.println("duplicate status " + ++countDuplicates);
			}
			catch(Exception e)
			{
				
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static String buildQueryString()
	{
		String query = "insert into user_past_tweets  " +
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
				"user_location," +  
				"favorite_count,"+
				"retweet_count,"+
				"user_mention_count,"+
				"url_count,"+
				"hashtag_count,"+
				"media_count,"+
				"symbol_count"+
				") " + 
				"values" +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,)";
		return query;
	}
	// getting handles from the database
	public static String[] getHandles() throws Exception
	{
		Twitter twitter = BuildConfiguration.getTwitter();
		Statement statement = null;
		ResultSet resultSet = null;
		Connection con = DriverManager.getConnection(Settings.getMySQLURL(),Settings.getMySQLUserName(),Settings.getMySQLPassword());
		String query2 = "select user_handle from userid";
		String queryGetSizeOfTable2 = "select count(*) from userid";
		statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(queryGetSizeOfTable2);
		resultSet.next();
		int quantityOfUsersToTrack = resultSet.getInt(1);
		resultSet.close();
		resultSet = statement.executeQuery(query2);
		String[] handles = new String[quantityOfUsersToTrack];
		int i=0;
		while(resultSet.next()!=false)
		{
			//long userid = Long.parseLong(resultSet.getString("user_handle"));
			handles[i++] = resultSet.getString("user_handle");
		}	
		return handles;
	}
	
}
