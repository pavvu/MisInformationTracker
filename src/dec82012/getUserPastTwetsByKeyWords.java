package dec82012;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class getUserPastTwetsByKeyWords 
{
	private static PreparedStatement preparedStatementForStoringUserTweets = null;
	private static Connection con = null;
	public static void main(String[] args) throws Exception 
	{
		buildQuery();
		getPastNTweets();
		System.out.println("done");
	}
	
	public static void buildQuery() throws SQLException
	{
		con = Settings.getConnection();
		String queryForStoringUserTweets = "insert into twitterdata.user_past_tweets_mi " +  
				"(" + 
				"tweet_id, " + 
				"tweet_text, " +
				"created_time, " +
				"user_id, " + 
				"user_handle, " +
				"rt_by_user_id," +
				"rt_by_user_handle," +
				"rt_favorite_count," +
				"rt_retweet_count," +
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
				"values"+
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		preparedStatementForStoringUserTweets = con.prepareStatement(queryForStoringUserTweets);
		con.commit();
	}
	public static void getPastNTweets() throws SQLException, InterruptedException
	   {
			  Twitter twitter = BuildConfiguration.getTwitter();
			  //for (long userid : UserIDToTrack)
			  //{
				  try 
				  {
					  int n =0;
					  // converting userid to user screen name
					  String keyWord= "vaccine autism";
					 
			          Query query = new Query(keyWord);
			  		  int numberOfTweets = Integer.MAX_VALUE;
			  		  long lastID = Long.MAX_VALUE;
			  		  List<Status> tweets = new ArrayList<Status>();
			  		  while (tweets.size () < numberOfTweets) 
			  		  {
			  			// in one hit, search can extract a maximum of 100 tweets
			  		    if (numberOfTweets - tweets.size() > 100)
			  		      query.setCount(100);
			  		    else 
			  		      query.setCount(numberOfTweets - tweets.size());
			  		    try 
			  		    {
			  		      
			  		      QueryResult result = twitter.search(query);
			  		      tweets.addAll(result.getTweets());
			  		      if(result.getTweets().size()==0) 
			  		      {
			  		    	  System.out.println("Gathered " + tweets.size() + " tweets " + keyWord);
			  		    	  break;
			  		      }
			  		    
			  		      //System.out.println("Gathered " + tweets.size() + " tweets " + keyWord);
			  		      // finding the least latest statusID to fetch tweets posted before this in next look
			  		      for (Status t: tweets) 
			  		        if(t.getId() < lastID) lastID = t.getId();
			  		    }

			  		    catch (TwitterException te) 
			  		    {
			  		    	Thread.sleep(Settings.getScrapeTimer() * 1000L);
			  		    	//System.out.println("Couldn't connect: " + te);
			  		    } 
			  		    query.setMaxId(lastID-1);
			  		  }
			  		  
			  		  //storing in db
			  		  int count=0;
			  		  for (Status tweet : tweets)
			  		  {
			  			 

	                	/*preparedStatementForStoringUserTweets.setInt(1, id);
	                	id++;
	                	preparedStatementForStoringUserTweets = PrepareStatement.prepareStatement(tweet, preparedStatementForStoringUserTweets);
	                	preparedStatementForStoringUserTweets.executeUpdate();
	                	//System.out.println(n++ +" "+ tweet.getCreatedAt()+ " @" + tweet.getUser().getScreenName() + " - " + tweet.getText());
	                    con.commit();*/
			  			if(count++%500==0) System.out.println("stored " + count + " tweets");
			  			try
			  			{
			  				 	preparedStatementForStoringUserTweets = PrepareStatement.prepareStatement(tweet, preparedStatementForStoringUserTweets,1);
			                	preparedStatementForStoringUserTweets.executeUpdate();
			                    con.commit();
					  			//System.out.println("got tweet");
			  			}
			  			catch (Exception e)
			  			{
			  				e.printStackTrace();
			  			}
			  			
	                   
					  }
			          
			      } 
				  
				  catch (Exception te) 
				  {
			            te.printStackTrace();
			            System.out.println("Failed to search tweets: " + te.getMessage());
			            System.exit(-1);
			       }
			    //System.out.println(twitter.showUser(userid).getScreenName());
			  //}
	   
	   }

}
