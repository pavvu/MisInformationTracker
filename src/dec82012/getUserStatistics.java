package dec82012;

import java.io.File;
import java.io.FileWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import org.joda.time.DateTime;

import twitter4j.*;

class userDetails
{
	//liked,dliked,followers,dfollowers,following,dfollowing,tweets
	public  int liked=0;
	public  int followers=0;
	public  int following=0;
	public  int tweets=0;
	
}

public class getUserStatistics 
{
	private static final int FIFTEEN_MINUTES = 15*60*1000;
	private static String[] handles; 
	private static Twitter twitter; 
	private static String directoryName; 
	private static int session=0;
	private static int timeIndex=0;
	private static userDetails[] userPreviousStats;// = new userDetails[257];
	private static String timestamp;
	private static String newline;
	private static int firstTimeIndex=1;
	
	public static void main(String[] args) throws Exception
	{
		 handles = getUsersPastTweets.getHandles();
		 twitter = BuildConfiguration.getTwitter();
		 directoryName = "C:\\Users\\CBA\\Dropbox\\SharedDataFolder\\HealthOrganizationHandlesStatistics";
		 
		 while(true)
		 {
			 getUserStatistics();
			 Thread.sleep(FIFTEEN_MINUTES);
		 }
		 //getUserStatistics();
	}
	
	
	public static void getUserStatistics() throws Exception
	{
		
		String fileName = getFileName();
		FileWriter fw;
		newline = System.getProperty("line.separator");
		timestamp="";
		
		if( new File(directoryName, fileName).exists())
		{
			fw = new FileWriter(directoryName+"\\"+fileName,true);
			timeIndex++;
		}
		//new day started
		else
		{
			userPreviousStats = new userDetails[257];
			initializeUserDeatils();
			 timeIndex=1;
			 fw = new FileWriter(directoryName+"\\"+fileName,true);
			 fw.write(getColumnNames()+newline);
			 session =0;
		}
		session++;
		System.out.println(getTimeStamp() + " : started " + session + " session");
		
		for (int i=0; i<handles.length; i++)
		{
			try
			{
				User u = twitter.showUser(handles[i]);
				//handle,liked,followers,following,tweets,location
				
				String column = buildColumn(u, timeIndex, i);
				fw.write(column);
				if(i%50==0)
					 System.out.println("done with " +  i + " user handles");
				
			}
			catch(Exception e)
			{
				System.out.println("rate limit exceeded");
				//e.printStackTrace();
				i--;
				Thread.sleep(5000*60);
			}
			
		}
		fw.close();
		System.out.println(timestamp + " : completed " + session + " sessions");
	}
	
	public static void initializeUserDeatils()
	{
		for(int i=0; i<257; i++)
		{
			userPreviousStats[i] = new userDetails();
		}
	}
	
	public static String buildColumn(User u, int timeIndex, int userIndex)
	{
		// first scrapping session . so no change
		//liked,dliked,followers,dfollowers,following,dfollowing,tweets,dtweets
		// d stands for delta, change wrt previous timeindex stats
		int dliked;
		int dfollowers;
		int dfollowing;
		int dtweets;
		if(timeIndex==firstTimeIndex)
		{
			userPreviousStats[userIndex].liked = u.getFavouritesCount();
			userPreviousStats[userIndex].followers = u.getFollowersCount();
			userPreviousStats[userIndex].following = u.getFriendsCount();
			userPreviousStats[userIndex].tweets = u.getStatusesCount();
			dliked =0;
			dfollowers=0;
			dfollowing=0;
			dtweets=0;
			
		}
		else
		{
			// storing the change from previous scraping session
			dliked 		= u.getFavouritesCount() - userPreviousStats[userIndex ].liked;
			dfollowers 	= u.getFollowersCount()	 - userPreviousStats[userIndex ].followers;
			dfollowing 	= u.getFriendsCount() 	 - userPreviousStats[userIndex ].following;
			dtweets 	= u.getStatusesCount() 	 - userPreviousStats[userIndex ].tweets;
			
			// updating with the values of current scrapingsession
			userPreviousStats[userIndex].liked = u.getFavouritesCount();
			userPreviousStats[userIndex].followers = u.getFollowersCount();
			userPreviousStats[userIndex].following = u.getFriendsCount();
			userPreviousStats[userIndex].tweets = u.getStatusesCount();
		}
		
		timestamp = getTimeStamp();
		String column = timeIndex+","+
				timestamp+","+
				u.getScreenName()+","+ 
				u.getFavouritesCount()+","+ dliked+","+
				u.getFollowersCount()+","+	dfollowers+","+
				u.getFriendsCount()+","+   dfollowing+","+
				u.getStatusesCount()+","+  dtweets+","+
				u.getLocation().replace(",", ".")+newline;
		return column;
	}
	public static String  getFileName()
	{
		DateTime currentTime = new DateTime();
		String year = ""+currentTime.getYear();
		String month = "" + currentTime.getMonthOfYear();
		String date = "" + currentTime.getDayOfMonth();
		//currentTime.get
	    String fileName = month+"_" + date + "_" + year + ".csv";
		//String directoryName = "C:\\Users\\kpava\\Dropbox\\SharedDataFolder\\HealthOrganizationHandlesStatistics";
		//String filePath =  directoryName+ "\\" +fileName ;
		return fileName;
	}
	
	public static String getTimeStamp()
	{
		DateTime currentTime = new DateTime();
		String year = ""+currentTime.getYear();
		String month = formatToTwoDigits("" + currentTime.getMonthOfYear());
		
		String date = formatToTwoDigits("" + currentTime.getDayOfMonth());
		String hour = formatToTwoDigits("" + currentTime.getHourOfDay());
		String min = formatToTwoDigits("" + currentTime.getMinuteOfHour());
		String sec = formatToTwoDigits(""+ currentTime.getSecondOfMinute());
		String timeStamp = " "+year + "-" + month+ "-" + date +  " " + 
						   hour + ":" + min + ":" + sec + ".0";
		return timeStamp;
	}
	
	public static String formatToTwoDigits(String str)
	{
		if(Integer.parseInt(str)<=9)
		{
			return ("0"+str);
		}
		return str;
	}
	public static String getColumnNames()
	{
		String columnNames = "timeindex,timestamp,handle,liked,dliked,followers,dfollowers,following,dfollowing,tweets,dtweets,location";
		return columnNames;
	}
}
