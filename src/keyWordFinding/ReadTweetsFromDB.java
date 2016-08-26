package keyWordFinding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dec82012.Settings;

public class ReadTweetsFromDB 
{
	public static void main(String[] args) throws SQLException 
	{
		readTweets();
	}

	public static String[] readTweets() throws SQLException
	{
		String tableName = "twitterdata.tweets_mi_3_30_2016";
		Statement statement = null;
		ResultSet resultSet = null;
		Connection con = Settings.getConnection();
		statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		// finding no of rows in the table 
		String queryToCountRows = "select count(*) from " + tableName;
		resultSet =  statement.executeQuery(queryToCountRows);
		resultSet.next();
		int tweetsCount = resultSet.getInt(1);	
		String[] tweets = new String[tweetsCount];
		
		//reading tweets
		String query = "select tweet_text from " + tableName;
		resultSet = statement.executeQuery(query);
		
		int i=0;
		while(resultSet.next()!=false)
		{
			tweets[i++] = resultSet.getString("tweet_text");
			//if(i%500 ==0) System.out.println("done with " + i + " tweets");
		}	
		//System.out.println("done " + i);
		return tweets;
	}
}
