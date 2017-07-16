package decorationproject;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import decorationproject.decoratorHttpClient;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	boolean DEBUG= false;
	public static final String PLUGIN_ID = "DecorationProject.myDecorator"; //$NON-NLS-1$
	
	static String projectName;
	static String collabName;
	static String ipAddTomcat;
	static String ipAddMySQL;

	static decoratorHttpClient collabClient= null;
	
	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
	
		super.start(context);
		plugin = this;
		if (DEBUG) System.out.println("Here in Activator");
		
		Boolean success= false;
		String configString= null;

		if (collabClient == null) 
		{
		collabClient= new decoratorHttpClient();
		configString= readConfigParameters("D://configDecorator.java");
		//System.out.println(configString);
		getAddConfig(configString);
		
		//collabClient.setConfigProjectValues("MathProject", "CollabClient1", "192.168.1.6:8080", "192.168.1.6:3306");
		//collabClient.setConfigProjectValues("MathProject", "CollabClientF", "localhost:8080", "localhost:3306");
		collabClient.setConfigProjectValues(ipAddTomcat,ipAddMySQL);
		success= collabClient.createCollabClient();	
		}

		
		if (success)
			{
			Boolean changed= false;
			//start new thread
			//update activityDetails Vector
			// and changed boolean 
			//this thread invokes the servlet every 5 minutes and update the activity vector
			ActivityDetailsThread myRunnable = new ActivityDetailsThread(collabClient);
			Thread t = new Thread(myRunnable, "getActivityDataThread");
			t.start();
			}
	}

	void getAddConfig(String configString)
	{
		  String[] temp1;
		  String delimiter1 = "[\\n]";
		  temp1 = configString.split("\\r\\n|\\n|\\r");
		  
		  int index= temp1[0].indexOf("|");
		  System.out.println("length:"+temp1[0].length());
		  projectName= temp1[0].substring(index+1, temp1[0].length());
		  System.out.println("projectName:"+projectName);
		  
		  index= temp1[1].indexOf("|");
		  collabName= temp1[1].substring(index+1, temp1[1].length());
		  System.out.println("collabName:"+collabName);
		  
		  index= temp1[2].indexOf("|");
		  ipAddTomcat= temp1[2].substring(index+1, temp1[2].length());
		  System.out.println("ipAddTomcat:"+ipAddTomcat);
		  
		  index= temp1[3].indexOf("|");
		  ipAddMySQL= temp1[3].substring(index+1, temp1[3].length());
		  System.out.println("ipAddMySQL:"+ipAddMySQL);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		//close client object here
		if (collabClient != null ) collabClient.closeClient();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}


	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public String readConfigParameters(String filePath) throws IOException 
	{
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {

			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return  fileData.toString();	
	}
	
}
