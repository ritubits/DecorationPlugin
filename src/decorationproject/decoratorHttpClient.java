package decorationproject;


import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



public class decoratorHttpClient {
	
	HttpEntity entity=null;
	Vector projectVector=null;
	CloseableHttpClient httpclient=null;
	
	String projectName;
	String collabName;
	String ipAddTomcat;
	String ipAddMySQL;

	static Boolean status=null;
	
	public void setConfigProjectValues(String pName, String cName, String ipT, String ipSQL )
	{
		projectName = pName;
		collabName= cName;
		ipAddTomcat = ipT;
		ipAddMySQL= ipSQL;
	}
		   
	public boolean createCollabClient()
	{
		 System.out.println("Before creating client: decoratorHttpClient");
		 httpclient = HttpClients.createDefault();
		 
		 System.out.println("After creating client: decoratorHttpClient");
		 
		 if (httpclient != null) return true; 
		 else return false;
		
	}
	
	public Vector getCollabActivityDetails() throws Exception {
		
	     	//returns all collaborator with artifact details
			System.out.println("Invoking servlet getCollaboratorDetails"+"http://"+ipAddTomcat+"/collabserver/getCollaboratorDetails?pName="+projectName+"&cName="+collabName);
	    	HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/collabserver/getCollaboratorDetails?pName="+projectName+"&cName="+collabName);
	    	CloseableHttpResponse response = httpclient.execute(httpget);
	    	String respStatus=null;	    
			
	    	try {
	   
	    	System.out.println(response.getProtocolVersion());
	    	System.out.println(response.getStatusLine().getStatusCode());
	    	System.out.println(response.getStatusLine().getReasonPhrase());
	    	System.out.println(response.getStatusLine().toString());
	    	respStatus= response.getStatusLine().toString();
	    	
		    	entity = response.getEntity();
		   	
		    	// transfer the response to a Vector
	    	  projectVector = new Vector();
	    		  
		    	if (entity != null) {
		    		long len = entity.getContentLength();
		    		if (len != -1 && len < 2048) {
		    		projectVector.add(EntityUtils.toString(entity));
		    		
		    		} else {
		    		// Stream content out
		    			System.out.println("Received empty string from server in decoratorHttpClient");
		    		}
		    		
		    
		    	}
	    	} finally {
	    	response.close();
	    
	    	}
	    	
	    	//check if returned status is not correct
	    	if (respStatus.contains("Error")) status= false; 
	    	else    	
	    		status= true; 
	    	
	    	return projectVector;
	 
	    }
	
	public void closeClient() throws Exception
	{
		try
		{
			System.out.println("Closing Client");
			httpclient.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
