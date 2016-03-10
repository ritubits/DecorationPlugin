package decorationproject;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.decorators.DecoratorManager;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.jface.viewers.LabelProvider;

import java.util.Vector;
import java.util.Enumeration;

import decorationproject.Activator;
import decorationproject.decoratorHttpClient;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;



public class decorationDetails extends LabelProvider implements ILabelDecorator {

	ImageDescriptor imageDesc1 = ImageDescriptor.createFromURL(FileLocator.find(Activator.getDefault().getBundle(),new Path("images/1.gif"),null));
	ImageDescriptor imageDesc2 = ImageDescriptor.createFromURL(FileLocator.find(Activator.getDefault().getBundle(),new Path("images/2.gif"),null));
	ImageDescriptor imageDesc3 = ImageDescriptor.createFromURL(FileLocator.find(Activator.getDefault().getBundle(),new Path("images/3.gif"),null));
	ImageDescriptor imageDesc4 = ImageDescriptor.createFromURL(FileLocator.find(Activator.getDefault().getBundle(),new Path("images/4.gif"),null));
	ImageDescriptor  imageDesc_Caution = ImageDescriptor.createFromURL(FileLocator.find(Activator.getDefault().getBundle(),new Path("images/caution.gif"),null));
	
	    Image img1 =  imageDesc1.createImage();
	    Image img2 =  imageDesc2.createImage();
	    Image img3 =  imageDesc3.createImage();
	    Image img4 =  imageDesc4.createImage();
	    Image CautionImg =  imageDesc_Caution.createImage();
	    
	    Image img=null;
	    
		public decorationDetails() {
			  super();
	}
		
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

		System.out.println("In addListener");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		System.out.println("In removeListener");
	}

	public static decorationDetails getDemoDecorator() {

	    IDecoratorManager decoratorManager = decorationproject.Activator
	            .getDefault().getWorkbench().getDecoratorManager();
	    
	    try {
	        decoratorManager.setEnabled("DecorationProject.myDecorator",true);
	        System.out.println("dedcorator enabled" + decoratorManager);
	        } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    if (decoratorManager.getEnabled("DecorationProject.myDecorator")) {
	        System.out.println("dedcorator enabled");
	        return (decorationDetails) decoratorManager.getLabelDecorator("DecorationProject.myDecorator");
	    }
	    return null;
	}
	

	@Override
	public Image decorateImage(Image baseImage, Object object) {
		// TODO Auto-generated method stub

		IResource objectResource;
		System.out.println("Object:: "+object);
		System.out.println("In decorateImage: "+ ActivityDetailsThread.getChangeStatus());
		Vector artifactVec= UpdateArtifactVector();

			 String countCollab=null;

					
					if (!(object.toString().contains("class")) || (object.toString().contains(".java"))){

				//check if exists in Vector, then updat accordingly
	    		countCollab= checkExits(artifactVec,object.toString());
	    	
	    		if ((countCollab!=null) && !(countCollab.equals('0')))
	    		{
	    			return (getImageObject(countCollab, baseImage));
	    		}
				 
			 }
		return null;

	}

	public Vector UpdateArtifactVector()
	{
		Vector artifactVec= null;	
		{					
			try {
				//if changed then get updated vector and set changed to false
				System.out.println("In second decorateImage: "+ ActivityDetailsThread.getChangeStatus());
				Enumeration details= (ActivityDetailsThread.getActivityVector()).elements();
				
		    	while (details.hasMoreElements())
		    	{
		    		artifactVec= parseString((String) details.nextElement());
		    	}		    	
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
		}
		return artifactVec;
	
	}

	@Override
	public String decorateText(String text, Object element) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void refresh() {
	    System.out.println("in refresh");
	    /**
	     * Get the Demo decorator
	     */
	    decorationDetails demoDecorator = getDemoDecorator();
	    if (demoDecorator == null) {
	    	System.out.println("Demo Decorator is Null");
	        return;
	    } else {

	     System.out.println("firing:::");
	        demoDecorator.fireLabelEvent(new LabelProviderChangedEvent(demoDecorator));
	    }
	}

	private void fireLabelEvent(final LabelProviderChangedEvent event) {
	       Display.getDefault().asyncExec(new Runnable() {
	        public void run() {
	        	System.out.println("Fire Event::");
	            fireLabelProviderChanged(event);
	        }
	    });
	}
	
	public Vector parseString(String data)
	{
		Vector artifactData= new Vector();
    	String name=null;
    	System.out.println("data:: "+data);
		  String[] temp1;
		  String[] temp2;
		 
		  String delimiter1 = "[|]";
		  String delimiter2 = "[:]";
		  
		  temp1 = data.split(delimiter1);
		  int i=0;
		  while (i< temp1.length)
		  {
		  
		  //System.out.println("From strat: "+temp1[i]);
		  
		  temp2 = temp1[i].split(delimiter2);
	 	  System.out.println("From parseString: "+temp2[0]+" "+temp2[1]);
	 	  ArtifactDataObject obj= new ArtifactDataObject(temp2[0],temp2[1]);
	 	  i++;
	 	  artifactData.add(obj);
		  }
		return artifactData;
	}
	
	public String checkExits(Vector searchVec, String name)
	{
		String number= null;
		ArtifactDataObject obj;
		Enumeration eVec= searchVec.elements();//error here
		while (eVec.hasMoreElements())
		{
			obj= (ArtifactDataObject)eVec.nextElement();
			System.out.println("Artifact Name:: "+obj.artifactName);
				if (name.contains(obj.artifactName))
				{
				System.out.println("Artifact Exists:: "+obj.artifactName);
					number= obj.noOfCollab;
					return number;
				}
		}
		return number;
	}
	
	public Image getImageObject(String countCollab, Image baseImage)
	{
		    
		    Integer i = Integer.parseInt(countCollab);
		    System.out.println("Image:: "+i);
		    switch (i)
		    {
		    case 0: System.out.println("returning image 0");
	    		break;
		    case 1: img=img1; 
		    img = getIconImage(baseImage, imageDesc1);
		    System.out.println("returning image 1");
		    	break;
		    case 2: img=img2; System.out.println("returning image 2");
	    		break;
		    case 3: img=img3; System.out.println("returning image 3");
	    		break;
		    case 4: img=img4; System.out.println("returning image 4");
	    		break;
	    	default: img= CautionImg;
	    		System.out.println("returning caution image");
	    		break;
		    }
		    
		    return img;
	}
	
	private Image getIconImage(Image baseImage, ImageDescriptor overlayImage) {
	   
	    DecorationOverlayIcon overlayIcon = new DecorationOverlayIcon(baseImage,overlayImage,IDecoration.TOP_RIGHT);
	    return overlayIcon.createImage();   
	    }
}

