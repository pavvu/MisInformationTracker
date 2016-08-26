package dec82012;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class Scraper {

	Connection con = null;
	PreparedStatement preparedStatementForStoringTweets = null;
	PreparedStatement preparedStatementForStoringYahooFinanceData = null;
	Statement statementToGetAStockSymbolToScrapeFromYahooFinance = null;
	PreparedStatement preparedStatementStoringAggregateData = null;
	PreparedStatement preparedStatementForStoringUserTweets = null;
	int quantityOfCompaniesToTrack = -1;
	
	
	/*access_token_key = "2185546296-gRONfUuMh6I66oRqgfgf9LGPyAHkFMdktowvVYf"
			access_token_secret = "r6aXja6Bg0Tt45nKg6zngG6LvAAVWyk9jW77JpYTgq5tP"
			consumer_key = "cVPo3i10x5sViu0VKSIn5bkNN"
			consumer_secret = "m0IA7U3PBDMfh8xoUNKwgjrDVvcpTQWYMVaw0HfsQK19tgkr63*/
	private static final String CONSUMER_KEY = "cVPo3i10x5sViu0VKSIn5bkNN";
	private static final String CONSUMER_SECRET = "m0IA7U3PBDMfh8xoUNKwgjrDVvcpTQWYMVaw0HfsQK19tgkr63";
	private static final String ACCESS_TOKEN = "2185546296-gRONfUuMh6I66oRqgfgf9LGPyAHkFMdktowvVYf";
	private static final String ACCESS_TOKEN_SECRET = "r6aXja6Bg0Tt45nKg6zngG6LvAAVWyk9jW77JpYTgq5tP";
	
	private static Logger fileLogger = Logger.getLogger("fileLogger");
	private static Logger emailLogger = Logger.getLogger("emailLogger");
	
	public static int id = 1;

	private long[] UserIDToTrack;// = {18839785};
	
	TwitterStream twitterStream;
	TwitterStream twitterStream2 ;
	
	
	 public static int increment() {
		 id++;
		 return id;
	  }
	 // default constructor
	 public Scraper()
	 {
		 
	 }
	 public Scraper(Connection someConnection, long[] someUserIDToTrack) throws SQLException {
			
			con = someConnection;
			//UserIDToTrack = {18839785};
			UserIDToTrack = someUserIDToTrack;
			try {
				
				String queryForStoringUserTweets = "insert into " + TableManager.tweetsUserTableName + 
						" values "+
						"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				preparedStatementForStoringUserTweets = con.prepareStatement(queryForStoringUserTweets);
				con.commit();
				
				
			} catch (SQLException e) {
				fileLogger.error("There was a problem.",e);
				e.printStackTrace();
				emailLogger.error("Error", e);
			} catch (Exception e) {
				fileLogger.error("There was a problem.",e);
				e.printStackTrace();
				emailLogger.error("Error", e);
			}
			
		}
		
	public void collectUserTweets() throws Exception {
			
			/*
	    	 * This section takes the four variables and sends them to Twitter. Or it does something magical with them. 
	    	 * I don't know. It's not very well explained in the Twitter4J API.
	    	 * Just know that it's important. 
	    	 */
	    	ConfigurationBuilder cb = new ConfigurationBuilder(); 
			  cb.setDebugEnabled(true); 
			  cb.setOAuthConsumerKey(CONSUMER_KEY); 
			  cb.setOAuthConsumerSecret(CONSUMER_SECRET); 
			  cb.setOAuthAccessToken(ACCESS_TOKEN); 
			  cb.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET); 
			
			  /*
			   * When you want to get a Twitter Stream, you have to use the four credentials listed above.  
			   */
			  TwitterStreamFactory twitterStreamFactory2 = new TwitterStreamFactory( cb.build() ); 
			  twitterStream2 = twitterStreamFactory2.getInstance();
			  //String[] handles = getUsersPastTweets.getHandles();
	        StatusListener listener = new StatusListener() {
	            public void onStatus(Status status) {
	            	
	            	ScraperImplementationMI.incrementTotalUserTweets();
	            	
	            	
	            	// store each tweet that we get
	            	try {
	            		
	            		Twitter twitter = TwitterFactory.getSingleton();
	            		preparedStatementForStoringUserTweets = PrepareStatement.prepareStatementForReTweetNetwork(status, preparedStatementForStoringUserTweets);
	                	preparedStatementForStoringUserTweets.executeUpdate();
	                    con.commit();
	                    
	            	} catch (SQLException e) {
	        			fileLogger.error("There was a problem.",e);
	        			e.printStackTrace();
	        			emailLogger.error("Error", e);
	            	} catch (Exception e) {
	        			fileLogger.error("There was a problem.",e);
	        			e.printStackTrace();
	        			emailLogger.error("Error", e);
	            	}
	            	
	            }

	            // The four lines of code below are require for the Twitter4J interface to work
	            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { 
	            }

	            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	            }

	            public void onScrubGeo(long userId, long upToStatusId) { 
	            }

	            public void onException(Exception ex) {
	           }

				@Override
				public void onStallWarning(StallWarning arg0) {
					// TODO Auto-generated method stub
					
				}
	        };
	        
	        /*
	         * These lasts lines down here are important. 
	         */
	        twitterStream2.addListener(listener);
	        String[] keywords = new String[]{"vaccine autism"};
	        FilterQuery filterQuery =new FilterQuery(0, null,keywords ); 
	        //filterQuery.track(handles);
	        twitterStream2.filter(filterQuery);
	 
		}
	
	   
	  // when date is changed, the table name in query must also be changed.
	  public   void updatePrepareStament() throws SQLException
	  {
		  String queryForStoringUserTweets = "insert into " + TableManager.tweetsUserTableName + 
				  " values "+
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			preparedStatementForStoringUserTweets = con.prepareStatement(queryForStoringUserTweets);
			con.commit();
	  }
	  // This method is added By Pavan.
	  // it collects past 350 tweets that contain the keywords given in database
	   public void getPastNTweets() throws SQLException, InterruptedException
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
			  		      if(result.getTweets().size()==0) break;
			  		      System.out.println("Gathered " + tweets.size() + " tweets " + keyWord);
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
			  		  
			  		  for (Status tweet : tweets)
			  		  {
			  			ScraperImplementationMI.incrementTotalUserTweets();

	                	/*preparedStatementForStoringUserTweets.setInt(1, id);
	                	id++;
	                	preparedStatementForStoringUserTweets = PrepareStatement.prepareStatement(tweet, preparedStatementForStoringUserTweets);
	                	preparedStatementForStoringUserTweets.executeUpdate();
	                	//System.out.println(n++ +" "+ tweet.getCreatedAt()+ " @" + tweet.getUser().getScreenName() + " - " + tweet.getText());
	                    con.commit();*/
			  			try
			  			{
			  				 	preparedStatementForStoringUserTweets = PrepareStatement.prepareStatement(tweet, preparedStatementForStoringUserTweets);
			                	preparedStatementForStoringUserTweets.executeUpdate();
			                    con.commit();
					  			System.out.println("got tweet");
			  			}
			  			catch (Exception e)
			  			{
			  				e.printStackTrace();
			  				fileLogger.error("There was a problem.",e);
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

