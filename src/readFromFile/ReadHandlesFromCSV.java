
package readFromFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;

import dec82012.Settings;
import dec82012.TableManager;

public class ReadHandlesFromCSV 
{
	static String  Path = "C:\\Users\\kpava\\Desktop\\TwitterDataRetrieval\\healthOrganizationHandles\\";
	static String queryToStoreUserHandles = "insert into userid" +  
			"(" + 
			"user_state_code, " + 
			"user_handle, " +
			"user_id " +
			") " + 
			"values" +
			"(?,?,?)";
	static PreparedStatement PSToStoreuserHandles= null;
	static Connection con=null;
	public static void main(String[] args) throws Exception
	{
	    con = Settings.getConnection();
		PSToStoreuserHandles = con.prepareStatement(queryToStoreUserHandles);
		con.commit();
		readHandlesFromCSV();
	}
	
	public static void readHandlesFromCSV() throws IOException, SQLException
	{
		String filePath = Path;
		File file = new File(filePath);
		BufferedReader br = null;
		String line = "";
		String[] lineSplit;  
		String stateCode, userhandle;
		HashSet<String> uniqueHandles = new HashSet<>();
		long userID;
		for(String csvFile : file.list())
		{
			br = new BufferedReader(new FileReader(Path+csvFile));
			br.readLine();
			System.out.println("****" + csvFile + "***");
			while((line = br.readLine())!=null)
			{
				lineSplit = line.split(",");
				if(!lineSplit[2].equals("NTP"))
				{
					try
					{
						stateCode = lineSplit[0];
						userhandle = lineSplit[2];
						userID = Long.parseLong(lineSplit[3]);
						uniqueHandles.add(userhandle);
						//prepareStament(stateCode,userhandle, userID );
						//PSToStoreuserHandles.executeUpdate();
						//con.commit();
						//System.out.println(lineSplit[0] + " " + lineSplit[2] + " " + lineSplit[3]);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
				}
			}
			System.out.println(uniqueHandles.size());
		}
	}
	
	public static void prepareStament(String stateCode, String userHandle, long userID) throws SQLException
	{
		PSToStoreuserHandles.setString(1, stateCode);
		PSToStoreuserHandles.setString(2, userHandle);
		PSToStoreuserHandles.setLong(3, userID);
	}
}
