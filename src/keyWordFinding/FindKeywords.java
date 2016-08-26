package keyWordFinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FindKeywords 
{
	private static Map<String, Integer> unigramFrequency = new TreeMap<>();
	private static String[] tweets;
	
	public static void main(String[] args) throws SQLException, IOException
	{
		tweets = ReadTweetsFromDB.readTweets();
		countUnigrams(tweets);
		printUnigramCount();
		//String tweet = "FDA lists Tripedia DTaP vaccine side effects-ITP, SIDS, anaphylactic rxn, cellulitis, AUTISM, convulsions...pg 11 https://t.co/5g1HdzN8W6";
		
	}
	
	public static void countUnigrams(String[] tweets)
	{
		int i =0;
		for (String tweet : tweets) 
		{
			tweet = tweet.toLowerCase();
			tweet.replace("\n", "");
			String[] words = tweet.split(" ");
			for (String word : words) 
			{
				word = word.replace(" ", "");
				word = word.replaceAll("[^\\w\\s]","");
				word = word.replace("\n", "");
				if(unigramFrequency.containsKey(word))
				{
					int currCount = unigramFrequency.get(word);
					unigramFrequency.put(word, currCount+1);
				}
				else
				{
					unigramFrequency.put(word, 1);
				}
				
			}
			//if(++i%500 ==0) System.out.println("done with " + i + " tweets");
		}
		
	}
	
	public static void printUnigramCount() throws IOException
	{
		
		String fileName = "C:\\Users\\CBA\\Desktop\\TwitterDataRetrieval\\MisInforamationTracking\\unigramCount.txt";
		FileWriter fw = new FileWriter(fileName);;
		String newline = System.getProperty("line.separator");
		for(String word : unigramFrequency.keySet())
		{
			System.out.println(word + " " + unigramFrequency.get(word));
			fw.write(word + "\t" + unigramFrequency.get(word)+"\n");
		}		
		fw.close();
		System.out.println("done");
	}
}


