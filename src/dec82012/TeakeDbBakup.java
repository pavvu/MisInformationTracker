package dec82012;

public class TeakeDbBakup 
{
	public static void main(String[] args) {

        String path = "C:\\Users\\CBA\\Desktop\\TwitterDataRetrieval\\DB_BackUP\\twitter_data_bkp_02_05_2016.sql";
        String username = "root";
        String password = "root";
        String dbname = "twitterdata";
        String executeCmd = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump -u" + username + " -p" + password + " " + dbname + " -r " + path;
        Process runtimeProcess;
        try {
//            System.out.println(executeCmd);//this out put works in mysql shell
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            System.out.println(executeCmd);
//            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            System.out.println("processComplete"+processComplete);
            if (processComplete == 0) {
                System.out.println("Backup created successfully");

            } else {
                System.out.println("Could not create the backup");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
