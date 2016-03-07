package decorationproject;

import java.util.Vector;



public class ActivityDetailsThread implements Runnable {

	 static boolean changeStatus=false;
	 decoratorHttpClient httpClient;
	 static Vector activityDetails = null;
	    public ActivityDetailsThread(decoratorHttpClient client) {

	        httpClient= client;
	        try {
				activityDetails= httpClient.getCollabActivityDetails();
				changeStatus= true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 System.out.println("Run.....ActivityDetailsThread");
		
		 while (true)
		 {
			if (httpClient != null)
			{
				
			try{
				//1sec = 1000 millisecond
				//for 5 minutes 1000* 60*5
				Thread.sleep(1000*60*1);//2 min
				changeStatus=false;
				Thread.sleep(1000*60*1);//2min
				activityDetails= httpClient.getCollabActivityDetails();
				changeStatus= true;
				
				 
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			}//end of if
		 }//end of while

	}//end of run
	
	public static void setChangeStatus(boolean status)
	{
		changeStatus=status;
	}
	
	public static boolean getChangeStatus()
	{
		return(changeStatus);
	}
	
	public static Vector getActivityVector()
	{
		return(activityDetails);
	}
}
