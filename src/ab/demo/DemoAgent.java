package ab.demo;

import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import ab.vision.real.shape.*;
import ab.demo.other.ActionRobot;
import ab.intervalcalculus.RectangleAlgebra;
import ab.vision.ABObject;
import ab.vision.ABShape;
import ab.vision.ABType;
import ab.vision.ShowSeg;
import ab.vision.Vision;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.VisionRealShape;

public class DemoAgent implements Runnable 
{
	private ActionRobot aRobot;
	private int currentLevel = 5;
	private boolean gameOn = false;
	
	public DemoAgent ()
	{
		//aRobot = new ActionRobot();
		//ActionRobot.GoFromMainMenuToLevelSelection();
	}
	
	public void Run () throws InvalidFileFormatException, IOException
	{	
		// Menu actions
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("List action: ");
		System.out.println("1. Go to Angry Bird");
		System.out.println("2. Action Robot");
		System.out.println("3. Visualization");
		System.out.println("4. Load Level");
		System.out.println("5. Translate relational algebra and stability checker");
		System.out.print("Enter action: ");		
		String s = "";		
		while ( (s = br.readLine()) != null)
		{
			switch (s)
			{
			case "1":
				if (!gameOn)
				{
					openWebpage (new URL("http://chrome.angrybirds.com/"));
					aRobot = new ActionRobot();
					ActionRobot.GoFromMainMenuToLevelSelection();
					gameOn = true;
				}
				break;
			case "2":
				if (!gameOn)
				{
					aRobot = new ActionRobot();
					ActionRobot.GoFromMainMenuToLevelSelection();
					gameOn = true;
				}
				break;
			case "3":
				ShowSeg.useRealshape = true;
				Thread thre = new Thread(new ShowSeg());
				thre.start();
				break;
			case "4":
				System.out.print("Enter Level: ");
				String[] level = br.readLine().split("-");
				aRobot.loadLevel(Integer.parseInt(level[1]));
				break;
			case "5":
				// capture Image
				BufferedImage screenshot = ActionRobot.doScreenShot();
				
				// process image
				VisionRealShape vision = new VisionRealShape(screenshot);
				
				// get all object
				List<ABObject> objects = vision.findObjects();
				List<ABObject> pigs = vision.findPigs();
				List<ABObject> birds = vision.findBirds();
				
				// find the slingshot
				Rectangle sling = vision.findSling();
				
				System.out.println("Number of pig: " + pigs.size());
				System.out.println("Number of bird: " + birds.size());
				System.out.println("Number of block: " + objects.size());
				
				/*for (int i = 0; i < objects.size(); i++)
				{
					ABObject temp = objects.get(i);
					switch (objects.get(i).shape)
					{
					case Rect:
						temp = (Rect) temp;
						break;
					case Poly:
						temp = (Poly) temp;
						break;
					case Circle:
						temp = (Circle) temp;
						break;
					case Triangle:
						temp = (ABObject) temp;
						break;
					}
					System.out.println("Object " + objects.get(i).id + " Position: " + temp.getBounds().x + "," + temp.getBounds().getMinY());
				}*/
				
				long startTime = System.nanoTime();
				RectangleAlgebra.TranslateToRA(objects);
				RectangleAlgebra.ExtractContact();
				//RectangleAlgebra.PrintRA();
				System.out.println();
				//RectangleAlgebra.PrintCR();
				long stopTime = System.nanoTime();
				System.out.println("RA finish in: " + (stopTime - startTime) * Math.pow(10, -9) + " seconds");
				break;
			}
			System.out.print("Enter action: ");
		}
		
	}

	public void openWebpage(URI uri) 
	{
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) 
	    {
	        try 
	        {
	            desktop.browse(uri);
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void openWebpage(URL url) {
	    try 
	    {
	        openWebpage(url.toURI());
	    } 
	    catch (URISyntaxException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void run() 
	{
		DemoAgent na = new DemoAgent();
		try 
		{
			na.Run();
		} 
		catch (InvalidFileFormatException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
