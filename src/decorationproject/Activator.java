package decorationproject;


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
		
		if (collabClient == null) 
		{
		collabClient= new decoratorHttpClient();
		collabClient.setConfigProjectValues("GreatProject", "heera", "localhost:8080", "localhost:3306");
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
	
}
