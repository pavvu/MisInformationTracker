package dec82012;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.joda.time.DateTime;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

class userLikesRetweets
{
	public int likes;
	public int retweets;
	public userLikesRetweets(int likes, int retweets)
	{
		this.likes = likes;
		this.retweets = retweets;
	}
}

public class AgregateUserStastics 
{
	private static String[] tableNames;
	private static String[] handles;
	private static Connection con ;
	public static void main(String[] args) throws Exception
	{
		handles = getUsersPastTweets.getHandles();
		getTableNames();
		
		for (String handle : handles) 
		{
			getAgregateStats(handle);
		}
		
	}
	
	public static userLikesRetweets getAgregateStats(String handle) throws SQLException, InterruptedException
	{
		userLikesRetweets likesRetweetsInAllTables = new userLikesRetweets(0,0);
		for (String tableName : tableNames) 
		{
				userLikesRetweets likesRetweetsInCurrentTable = getUserStatsInTable(tableName, handle);
				likesRetweetsInAllTables.likes = likesRetweetsInAllTables.likes + likesRetweetsInCurrentTable.likes;
				likesRetweetsInAllTables.retweets = likesRetweetsInAllTables.retweets + likesRetweetsInCurrentTable.retweets;
				
		}
		System.out.println(handle + " "  + likesRetweetsInAllTables.likes + " likes " +
				likesRetweetsInAllTables.retweets + " retweets ");
		return likesRetweetsInAllTables;
	}
	
	public static void getTableNames()
	{
		DateTime currentTime = new DateTime();
		int day = currentTime.getDayOfMonth();
		tableNames = new String[day];
		String tableName;
		for(int i=1; i<=day; i++)
		{
			tableName = "tweets_3_" + i+ "_2016";
			tableNames[i-1] = tableName;
			//System.out.println(tableName);
		}
	}
	
	public static userLikesRetweets getUserStatsInTable(String tableName, String handle) throws SQLException, InterruptedException
	{	
		int totalLikeCount=0;
		int totalReTweetCount =0;
		Twitter twitter = BuildConfiguration.getTwitter();
		Statement statement = null;
		ResultSet resultSet = null;
		long tweetID;
		con =Settings.getConnection();
		handle = handle.replace("@", "");
		String queryToGetUserTweetIds = "select tweet_id from " +  tableName + " where user_handle = \"" + handle + "\"" ;
		statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(queryToGetUserTweetIds);
		int i=0;
		while(resultSet.next()!=false)
		{
			try
			{
				tweetID = resultSet.getLong("tweet_id");
				Status tweet = twitter.showStatus(tweetID);
				int reTweetedCount = tweet.getRetweetCount();
				totalReTweetCount +=reTweetedCount;
				int likeCount = tweet.getFavoriteCount();
				totalLikeCount +=likeCount;		
				//System.out.println(tweetID);
			}
			
			catch(TwitterException te)
			{
				System.out.println("rate limit exceeded while getting tweet stats ");
				Thread.sleep(Settings.ONE_MINUTE);
			}
			
		}	
		con.close();
		return new userLikesRetweets(totalLikeCount, totalReTweetCount);
		
	}
	
}

