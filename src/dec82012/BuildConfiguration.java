package dec82012;
/***********************************************************************
 * Version History
 * 
 * Version_No		Date			Author  	Reason for Modification
 * 1.0				23-Jan-2016     Pavan		Initial Version
 */
// this code does the Authentication and returns instance of Twitter
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class BuildConfiguration {
	
	public static Twitter getTwitter()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		/*
		 * access_token_key = "2185546296-gRONfUuMh6I66oRqgfgf9LGPyAHkFMdktowvVYf"
			access_token_secret = "r6aXja6Bg0Tt45nKg6zngG6LvAAVWyk9jW77JpYTgq5tP"
			consumer_key = "cVPo3i10x5sViu0VKSIn5bkNN"
			consumer_secret = "m0IA7U3PBDMfh8xoUNKwgjrDVvcpTQWYMVaw0HfsQK19tgkr63"*/
		cb.setOAuthConsumerKey("cVPo3i10x5sViu0VKSIn5bkNN");
		cb.setOAuthConsumerSecret("m0IA7U3PBDMfh8xoUNKwgjrDVvcpTQWYMVaw0HfsQK19tgkr63");
		cb.setOAuthAccessToken("2185546296-gRONfUuMh6I66oRqgfgf9LGPyAHkFMdktowvVYf");
		cb.setOAuthAccessTokenSecret("r6aXja6Bg0Tt45nKg6zngG6LvAAVWyk9jW77JpYTgq5tP");
		return new TwitterFactory(cb.build()).getInstance();
	}

}
