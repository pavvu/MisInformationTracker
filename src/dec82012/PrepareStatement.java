package dec82012;
/***********************************************************************
 * Version History
 * 
 * Version_No		Date			Author  	Reason for Modification
 * 1.0				24-Jan-2016     Pavan		Initial Version
 */
// this method adds all the parameters to the prepareStatement to build the query
import java.sql.PreparedStatement;
import java.sql.SQLException;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PrepareStatement 
{
	static Twitter twitter = BuildConfiguration.getTwitter();
	/*public static PreparedStatement prepareStatement(Status status, PreparedStatement preparedStatement)throws TwitterException, SQLException
	{
		String textOfTweet = status.getText();
    	String userScreenName = status.getUser().getScreenName();
    	String isRetweet = status.isRetweet() + "";
    	String tweetID = status.getId() + "";
    	String userId = status.getUser().getId() + "";
    	String followers = status.getUser().getFollowersCount() + "";
    	java.sql.Timestamp timestamp = new java.sql.Timestamp(status.getCreatedAt().getTime());
    	String textOfTweetStrippedOfAllNonAlphaNumericCharacters = null;
    	textOfTweetStrippedOfAllNonAlphaNumericCharacters = textOfTweet.replaceAll("[^\\x00-\\x7F]", " ");
    	preparedStatement.setString(2, textOfTweetStrippedOfAllNonAlphaNumericCharacters);
    	preparedStatement.setString(3, userScreenName);
    	preparedStatement.setString(4, isRetweet);
    	preparedStatement.setString(5, followers);
    	preparedStatement.setString(6, tweetID);
    	preparedStatement.setString(7, userId);
    	preparedStatement.setTimestamp(8, timestamp);
    	return preparedStatement;
	}*/
	
	// extra int just to differentiate the function
	public static PreparedStatement prepareStatement(Status status, PreparedStatement preparedStatement, int k)throws TwitterException, SQLException
	{
		// reading the equired fields
		String textOfTweet = status.getText();
    	String user_handle = status.getUser().getScreenName();
    	String isRetweet = status.isRetweet() + "";
    	String tweetID = status.getId() + "";
    	String user_id = status.getUser().getId() + "";
    	java.sql.Timestamp created_time = new java.sql.Timestamp(status.getCreatedAt().getTime());
    	String textOfTweetStrippedOfAllNonAlphaNumericCharacters = null;
    	textOfTweetStrippedOfAllNonAlphaNumericCharacters = textOfTweet.replaceAll("[^\\x00-\\x7F]", " ");
    	String reply_to_status_id = Long.toString(status.getInReplyToStatusId());
    	String reply_to_user_id = Long.toString(status.getInReplyToUserId());
    	String reply_to_user_handle = status.getInReplyToScreenName();
    	boolean is_sensitive = status.isPossiblySensitive();
    	boolean is_retweeted = status.isRetweet();
    	String tweet_geolocation = status.getGeoLocation()==null?  "" : status.getGeoLocation().getLatitude()+","+status.getGeoLocation().getLongitude();
    	String tweet_place = status.getPlace()==null? "" : status.getPlace().getName();
    	String user_location = status.getUser().getLocation();
    	int favorite_count = status.getFavoriteCount();
    	int retweet_count  = status.getRetweetCount();
    	int user_mention_count = status.getUserMentionEntities().length;
    	int url_count = status.getURLEntities().length;
    	int hashtag_count = status.getHashtagEntities().length;
    	int media_count = status.getMediaEntities().length;
    	int symbol_count = status.getSymbolEntities().length;
    	String rt_by_user_id = "";
    	String rt_by_user_handle ="";
    	String  rt_favorite_count ="";	
    	String  rt_retweet_count ="";
    	
    	if(textOfTweet.substring(0, 2).equals("RT"))
    	{
    		rt_by_user_id = status.getRetweetedStatus().getUser().getId()+"";
    		rt_by_user_handle = status.getRetweetedStatus().getUser().getScreenName();
    		rt_favorite_count = status.getRetweetedStatus().getFavoriteCount()+"";
    		rt_retweet_count = status.getRetweetedStatus().getRetweetCount()+"";
    		
    		
    	}
    	
    	
    	//prepating the statement
    	preparedStatement.setString(1, tweetID);
    	preparedStatement.setString(2, textOfTweetStrippedOfAllNonAlphaNumericCharacters);
    	preparedStatement.setString(3, created_time.toString());
    	preparedStatement.setString(4, user_id);
    	preparedStatement.setString(5, user_handle);
    	preparedStatement.setString(6, rt_by_user_id);
    	preparedStatement.setString(7, rt_by_user_handle);
    	preparedStatement.setString(8, rt_favorite_count);
    	preparedStatement.setString(9, rt_retweet_count);
    	
    	
    	preparedStatement.setString(6+4, reply_to_status_id);
    	preparedStatement.setString(7+4, reply_to_user_id);
    	preparedStatement.setString(8+4, reply_to_user_handle);
    	preparedStatement.setBoolean(9+4, is_sensitive);
    	preparedStatement.setBoolean(10+4, is_retweeted);
    	preparedStatement.setString(11+4, tweet_geolocation);
    	preparedStatement.setString(12+4, tweet_place);
    	preparedStatement.setString(13+4,user_location );
    	preparedStatement.setInt(14+4, favorite_count);
    	preparedStatement.setInt(15+4, retweet_count);
    	preparedStatement.setInt(16+4, user_mention_count);
    	preparedStatement.setInt(17+4, url_count);
    	preparedStatement.setInt(18+4, hashtag_count);
    	preparedStatement.setInt(19+4, media_count);
    	preparedStatement.setInt(20+4, symbol_count);
    	return preparedStatement;
	}
	
	public static PreparedStatement prepareStatement(Status status, PreparedStatement preparedStatement)throws TwitterException, SQLException
	{
		// reading the equired fields
		String textOfTweet = status.getText();
    	String user_handle = status.getUser().getScreenName();
    	String isRetweet = status.isRetweet() + "";
    	String tweetID = status.getId() + "";
    	String user_id = status.getUser().getId() + "";
    	java.sql.Timestamp created_time = new java.sql.Timestamp(status.getCreatedAt().getTime());
    	String textOfTweetStrippedOfAllNonAlphaNumericCharacters = null;
    	textOfTweetStrippedOfAllNonAlphaNumericCharacters = textOfTweet.replaceAll("[^\\x00-\\x7F]", " ");
    	String reply_to_status_id = Long.toString(status.getInReplyToStatusId());
    	String reply_to_user_id = Long.toString(status.getInReplyToUserId());
    	String reply_to_user_handle = status.getInReplyToScreenName();
    	boolean is_sensitive = status.isPossiblySensitive();
    	boolean is_retweeted = status.isRetweet();
    	String tweet_geolocation = status.getGeoLocation()==null?  "" : status.getGeoLocation().getLatitude()+","+status.getGeoLocation().getLongitude();
    	String tweet_place = status.getPlace()==null? "" : status.getPlace().getName();
    	String user_location = status.getUser().getLocation();
    	/*int favorite_count = status.getFavoriteCount();
    	int retweet_count  = status.getRetweetCount();
    	int user_mention_count = status.getUserMentionEntities().length;
    	int url_count = status.getURLEntities().length;
    	int hashtag_count = status.getHashtagEntities().length;
    	int media_count = status.getMediaEntities().length;
    	int symbol_count = status.getSymbolEntities().length;*/
    	
    	//prepating the statement
    	preparedStatement.setString(1, tweetID);
    	preparedStatement.setString(2, textOfTweetStrippedOfAllNonAlphaNumericCharacters);
    	preparedStatement.setString(3, created_time.toString());
    	preparedStatement.setString(4, user_id);
    	preparedStatement.setString(5, user_handle);
    	preparedStatement.setString(6, reply_to_status_id);
    	preparedStatement.setString(7, reply_to_user_id);
    	preparedStatement.setString(8, reply_to_user_handle);
    	preparedStatement.setBoolean(9, is_sensitive);
    	preparedStatement.setBoolean(10, is_retweeted);
    	preparedStatement.setString(11, tweet_geolocation);
    	preparedStatement.setString(12, tweet_place);
    	preparedStatement.setString(13,user_location );
    	/*preparedStatement.setInt(14, favorite_count);
    	preparedStatement.setInt(15, retweet_count);
    	preparedStatement.setInt(16, user_mention_count);
    	preparedStatement.setInt(17, url_count);
    	preparedStatement.setInt(18, hashtag_count);
    	preparedStatement.setInt(19, media_count);
    	preparedStatement.setInt(20, symbol_count);*/
    	return preparedStatement;
	}
	public static PreparedStatement prepareStatementForReTweetNetwork(Status status, PreparedStatement preparedStatement)throws  SQLException, InterruptedException
	{
		// reading the equired fields
		
		Status ot_status;
		String textOfTweet = status.getText();
		
    	String user_handle = status.getUser().getScreenName();
    	String user_ot_handle = "";
    	String isRetweet = status.isRetweet() + "";
    	String tweetID = status.getId() + "";
    	String ot_tweetID = "";
    	
    	String user_id = status.getUser().getId() + "";
    	String ot_user_id = "";
    	
    	java.sql.Timestamp created_time = new java.sql.Timestamp(status.getCreatedAt().getTime());
    	java.sql.Timestamp ot_created_time = null;
    	
    	String textOfTweetStrippedOfAllNonAlphaNumericCharacters = null;
    	textOfTweetStrippedOfAllNonAlphaNumericCharacters = textOfTweet.replaceAll("[^\\x00-\\x7F]", " ");
    	
    	String tweet_geolocation = status.getGeoLocation()==null?  "" : status.getGeoLocation().getLatitude()+","+status.getGeoLocation().getLongitude();
    	String tweet_place = status.getPlace()==null? "" : status.getPlace().getName();
    	
    	
    	
    	int user_favorite_count = status.getUser().getFavouritesCount();
    	int user_ot_favorite_count=-1;
    	
    	int user_follower_count = status.getUser().getFollowersCount();
    	int user_ot_follower_count=-1; 
    	
    	int user_following_count = status.getUser().getFriendsCount();
    	int user_ot_following_count=-1; 
    	
    	int user_tweet_count = status.getUser().getStatusesCount();
    	int user_ot_tweet_count= -1; 
    	
    	java.sql.Timestamp  user_joined_on =  new java.sql.Timestamp(status.getUser().getCreatedAt().getTime());
    	java.sql.Timestamp  user_ot_joined_on = null;
    	
    	String user_location = status.getUser().getLocation();
    	String user_ot_location=""; 
    	
    	
    	int t_favorite_count = status.getFavoriteCount();
    	int ot_favorite_count=-1;
    	
    	int retweet_count  = status.getRetweetCount();
    	int ot_retweet_count = -1 ; 
    	
    	String reply_to_status_id = Long.toString(status.getInReplyToStatusId());
    	String reply_to_user_id = Long.toString(status.getInReplyToUserId());
    	String reply_to_user_handle = status.getInReplyToScreenName();
    	boolean is_sensitive = status.isPossiblySensitive();
    	boolean is_retweeted = status.isRetweet();
    	int user_mention_count = status.getUserMentionEntities().length;
    	int url_count = status.getURLEntities().length;
    	int hashtag_count = status.getHashtagEntities().length;
    	int media_count = status.getMediaEntities().length;
    	int symbol_count = status.getSymbolEntities().length;
    	boolean is_u_follwing_u_ot =  false;
    	boolean is_u_ot_follwing_u = false;
    	
    	
    	
    	if(is_retweeted)
    	{
    		ot_status = status.getRetweetedStatus();
    		user_ot_handle =ot_status.getUser().getScreenName();
    		ot_tweetID = ot_status.getId() + "";
    		ot_user_id = ot_status.getUser().getId()+"";
    		ot_created_time = new java.sql.Timestamp(ot_status.getCreatedAt().getTime());
    		user_ot_favorite_count = ot_status.getUser().getFavouritesCount();
    		user_ot_follower_count = ot_status.getUser().getFollowersCount();
    		user_ot_following_count = ot_status.getUser().getFriendsCount();
    		user_ot_tweet_count = ot_status.getUser().getStatusesCount();
    		user_ot_joined_on =  new java.sql.Timestamp(ot_status.getUser().getCreatedAt().getTime());
    		user_ot_location = ot_status.getUser().getLocation();
    		ot_favorite_count = ot_status.getFavoriteCount();
    		ot_retweet_count  = ot_status.getRetweetCount();
    		boolean isCompleted = false;
    		/*while(!isCompleted)
    		{*/
    			try 
        		{
    				is_u_follwing_u_ot = twitter.showFriendship(user_handle, user_ot_handle).isSourceFollowingTarget();
    				is_u_ot_follwing_u = twitter.showFriendship(user_handle, user_ot_handle).isTargetFollowingSource();
    				isCompleted = true;
    			} catch (TwitterException e) 
        		{
    				// TODO Auto-generated catch block
    				System.out.println("ErrorCode : " + e.getErrorCode() + " Message : " + e.getErrorMessage());
    				//System.out.println("");
    			}
    		/*}*/
    		
    		
    	}
    	
    	//prepating the statement
    	int i=1;
		preparedStatement.setString(i++, tweetID);//1
    	preparedStatement.setString(i++, ot_tweetID);//1
    	preparedStatement.setString(i++, textOfTweetStrippedOfAllNonAlphaNumericCharacters);
    	preparedStatement.setString(i++, created_time.toString());
    	preparedStatement.setString(i++, ot_created_time!=null ?ot_created_time.toString() : null);
    	preparedStatement.setString(i++, user_id);
    	preparedStatement.setString(i++, user_handle);
    	preparedStatement.setString(i++, ot_user_id);
    	preparedStatement.setString(i++, user_ot_handle);
    	preparedStatement.setBoolean(i++, is_u_follwing_u_ot);
    	preparedStatement.setBoolean(i++, is_u_ot_follwing_u);
    	preparedStatement.setInt(i++, user_favorite_count);
    	preparedStatement.setInt(i++, user_ot_favorite_count);
    	
    	preparedStatement.setInt(i++, user_follower_count);
    	preparedStatement.setInt(i++, user_ot_follower_count);
    	                         
    	preparedStatement.setInt(i++, user_following_count);
    	preparedStatement.setInt(i++, user_ot_following_count);
    	                         
    	preparedStatement.setInt(i++, user_tweet_count);
    	preparedStatement.setInt(i++, user_ot_tweet_count);
    	
    	preparedStatement.setString(i++, user_joined_on.toString());
    	preparedStatement.setString(i++, user_ot_joined_on!=null ? user_ot_joined_on.toString() : null);
    	                           
    	preparedStatement.setString(i++, user_location);
    	preparedStatement.setString(i++, user_ot_location);
    	
    	preparedStatement.setInt(i++, t_favorite_count);
    	preparedStatement.setInt(i++, ot_favorite_count);
    	                         
    	preparedStatement.setInt(i++, retweet_count);
    	preparedStatement.setInt(i++, ot_retweet_count);
    	
    	preparedStatement.setString(i++, reply_to_status_id);
    	preparedStatement.setString(i++, reply_to_user_id);
    	preparedStatement.setString(i++, reply_to_user_handle);
    	preparedStatement.setBoolean(i++, is_sensitive);
    	preparedStatement.setBoolean(i++, is_retweeted);
    	
    	
    	preparedStatement.setInt(i++, user_mention_count);
    	preparedStatement.setInt(i++, url_count);
    	preparedStatement.setInt(i++, hashtag_count);
    	preparedStatement.setInt(i++, media_count);
    	preparedStatement.setInt(i++, symbol_count);
    	return preparedStatement;
	}
}
