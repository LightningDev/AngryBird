package ab.demo;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.lang.model.element.QualifiedNameable;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.ini4j.jdk14.edu.emory.mathcs.backport.java.util.Arrays;

import ab.planner.TrajectoryPlanner;
import ab.planner.abTrajectory;
import ab.utils.ABUtil;
import ab.vision.real.shape.*;
import ab.QualitativePhysics.QualitativeRep;
import ab.demo.other.ActionRobot;
import ab.demo.other.Shot;
import ab.intervalcalculus.Evaluation;
import ab.intervalcalculus.RectangleAlgebra;
import ab.vision.ABObject;
import ab.vision.ABShape;
import ab.vision.ABType;
import ab.vision.ShowSeg;
import ab.vision.Vision;
import ab.vision.VisionMBR;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.VisionRealShape;

public class DemoAgent implements Runnable 
{
	private ActionRobot aRobot;
	private int currentLevel = 5;
	private boolean gameOn = false;
	Random randomGenerator;
	TrajectoryPlanner tp;
	List<ABObject> destroyedList = new ArrayList<ABObject>();
	
	public DemoAgent ()
	{
		//aRobot = new ActionRobot();
		//ActionRobot.GoFromMainMenuToLevelSelection();
		randomGenerator = new Random();
		tp = new TrajectoryPlanner();
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
		System.out.println("5. Testing solve function");
		System.out.println("6. Testing Evaluation");
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
				String level = br.readLine();
				aRobot.loadLevel(Integer.parseInt(level));
				break;
			case "5":
				//PhysicsSolve();
				//Solve ();
				System.out.print("Enter id 1: ");
				String id1 = br.readLine();
				System.out.print("Enter id 2: ");
				String id2 = br.readLine();
				TestPhysicsSolve(Integer.parseInt(id1), Integer.parseInt(id2));
				break;
			case "6":
				System.out.print("Enter id: ");
				String id = br.readLine();
				_Solve(Integer.parseInt(id));
				break;
			}
			System.out.print("Enter action: ");
		}
		
	}
	
	public void TestPhysicsSolve (int id1, int id2)
	{
//		// capture Image
//		BufferedImage screenshot = ActionRobot.doScreenShot();
//
//		// process image
//		VisionRealShape vision = new VisionRealShape(screenshot);
//
//		// get all object
//		List<ABObject> objects = vision.findObjects();
//		List<ABObject> pigs = vision.findPigs();
//		List<ABObject> birds = vision.findBirds();
//		List<ABObject> hills = vision.findHills();
//
//		for (int i = 0; i < objects.size(); i++)
//		{
//			if (objects.get(i).id == id1)
//			{
//				ABObject o1 = objects.get(i);
//				for (int j = 0; j < objects.size(); j++)
//				{
//					if (objects.get(j).id == id2)
//					{
//						ABObject o2 = objects.get(j);
//						if (QualitativeRep.TouchRelation(o1, o2))
//						{
//							System.out.println("touch (" + o1.id + ", " + o2.id + ")");
//							System.out.println("touch (" + o1.type + ", " + o2.type + ")");
//							Point ps = QualitativeRep.LeftPoint(o1, o2);
//							int x = ps.x;
//							int y = ps.y;
//							System.out.println("left_p (" + o1.id + ", " + o2.id + ")" + " = " 
//							+ "(" + x + ", " + y + ")");
//							ps = QualitativeRep.RightPoint(o1, o2);
//							x = ps.x;
//							y = ps.y;
//							System.out.println("right_p (" + o1.id + ", " + o2.id + ")" + " = " 
//							+ "(" + x + ", " + y + ")");
//							ps = QualitativeRep.CenterPoint(o1, o2);
//							x = ps.x;
//							y = ps.y;
//							System.out.println("center_p (" + o1.id + ", " + o2.id + ")" + " = " 
//							+ "(" + x + ", " + y + ")");
//						}
//						else
//						{
//							System.out.println("!touch (" + o1.id + ", " + o2.id + ")");
//						}
//					}
//				}
//			}
//		}
	}
	
	public void PhysicsSolve ()
	{
		// capture Image
		BufferedImage screenshot = ActionRobot.doScreenShot();

		// process image
		VisionRealShape vision = new VisionRealShape(screenshot);

		// get all object
		List<ABObject> objects = vision.findObjects();
		List<ABObject> pigs = vision.findPigs();
		List<ABObject> birds = vision.findBirds();
		List<ABObject> hills = vision.findHills();

		for (int i = 0; i < objects.size(); i++)
		{
			ABObject o1 = objects.get(i);
			for (int j = 0; j < objects.size(); j++)
			{
				ABObject o2 = objects.get(j);
				if (QualitativeRep.TouchRelation(o1, o2))
				{
					System.out.println("touch (" + o1.id + ", " + o2.id + ")");
					System.out.println("touch (" + o1.type + ", " + o2.type + ")");
				}
			}
			
			for (int j = 0; j < pigs.size(); j++)
			{
				ABObject o2 = pigs.get(j);
				if (QualitativeRep.TouchRelation(o1, o2))
				{
					System.out.println("touch (" + o1.id + ", " + o2.id + ")");
					System.out.println("touch (" + o1.type + ", " + o2.type + ")");
				}
			}
			
			for (int j = 0; j < hills.size(); j++)
			{
				ABObject o2 = hills.get(j);
				if (QualitativeRep.TouchRelation(o1, o2))
				{
					System.out.println("touch (" + o1.id + ", " + o2.id + ")");
					System.out.println("touch (" + o1.type + ", " + o2.type + ")");
				}
			}
		}
	}
	
	public void Solve ()
	{
		// capture Image
		BufferedImage screenshot = ActionRobot.doScreenShot();
		
		// process image
		VisionRealShape vision = new VisionRealShape(screenshot);
		
		// get all object
		List<ABObject> objects = vision.findObjects();
		List<ABObject> pigs = vision.findPigs();
		List<ABObject> birds = vision.findBirds();
		List<ABObject> hills = vision.findHills();
		
		
		// find the slingshot
		Rectangle sling = vision.findSling();
		
		// confirm the slingshot
		while (sling == null && aRobot.getState() == GameState.PLAYING) {
			System.out
			.println("No slingshot detected. Please remove pop up or zoom out");
			ActionRobot.fullyZoomOut();
			screenshot = ActionRobot.doScreenShot();
			vision = new VisionRealShape(screenshot);
			sling = vision.findSling();
		}
		
		System.out.println("Number of pig: " + pigs.size());
		System.out.println("Number of bird: " + birds.size());
		System.out.println("Number of block: " + objects.size());
		System.out.println("Number of hill: " + hills.size());
		
		// Game State
		GameState state = aRobot.getState();
		
		if (sling != null)
		{
			if (!pigs.isEmpty())
			{
				Point releasePoint = null;
				Shot shot = new Shot();
				int dx, dy;
				double max = Double.MIN_VALUE;
				ABObject best = null;
				
				List<ABObject> allObjects = new ArrayList<ABObject>();
				Iterator<ABObject> index = objects.iterator();
				while (index.hasNext()) 
				{
				   ABObject s = index.next(); // must be called before you can call i.remove()
				   // Do something
				   for (int j = 0; j < destroyedList.size(); j++)
				   {
					   if (s.id == destroyedList.get(j).id)
						   index.remove();
				   }
				}
				allObjects.addAll(objects);
				allObjects.addAll(hills);
				allObjects.addAll(pigs);
				
				for (int i = 0; i < objects.size(); i++)
				{
					ABObject x = objects.get(i);
					System.out.println("Evaluate object " + x.id);
					Point tpt = x.getCenter();
					//TrajectoryPlanner tp = new TrajectoryPlanner();
					
					// estimate the trajectory
					ArrayList<Point> pts = tp.estimateLaunchPoint(sling, tpt);
					if (pts.size() > 0)
						releasePoint = pts.get(0);
					else
						break;
					
					// Get the reference point
					Point refPoint = tp.getReferencePoint(sling);
					
					int traY = tp.getYCoordinate(vision.findSling(), releasePoint, x.x);
					
					List<Point> points = tp.predictTrajectory(vision.findSling(), releasePoint);
					
					try 
					{
						double score = Evaluation.EvaluationShot(birds.get(0), x, points, allObjects, hills);
						if (score >= max)
						{
							max = score;
							best = x;
							destroyedList = Evaluation.destroyedList;
						}
					} 
					catch (FileNotFoundException e) 
					{
						e.printStackTrace();
					}
					//break;
				}
				
				System.out.println("Best shot is object " + best.id + " score " + max + " points");
				
				
				// estimate the trajectory
				ArrayList<Point> pts = tp.estimateLaunchPoint(sling, best.getCenter());
				releasePoint = pts.get(0);
				
				// Get the reference point
				Point refPoint = tp.getReferencePoint(sling);
				
				//Calculate the tapping time according the bird type 
				if (releasePoint != null) 
				{
					int tapInterval = 0;
					switch (aRobot.getBirdTypeOnSling()) 
					{

					case RedBird:
						tapInterval = 0; break;               // start of trajectory
					case YellowBird:
						tapInterval = 75 + randomGenerator.nextInt(15);break; // 75-90% of the way
					case WhiteBird:
						tapInterval =  70 + randomGenerator.nextInt(20);break; // 70-90% of the way
					case BlackBird:
						tapInterval =  70 + randomGenerator.nextInt(20);break; // 70-90% of the way
					case BlueBird:
						tapInterval =  65 + randomGenerator.nextInt(20);break; // 65-85% of the way
					default:
						tapInterval =  60;
					}

					int tapTime = tp.getTapTime(sling, releasePoint, best.getCenter(), tapInterval);
					dx = (int)releasePoint.getX() - refPoint.x;
					dy = (int)releasePoint.getY() - refPoint.y;
					shot = new Shot(refPoint.x, refPoint.y, dx, dy, 0, tapTime);
				}
				else
					{
						System.err.println("No Release Point Found");
					}
				
				aRobot.cshoot(shot);
			}
		}
	}
	
	public void _Solve (int id)
	{
		// capture Image
		BufferedImage screenshot = ActionRobot.doScreenShot();
		
		// process image
		Vision vision = new Vision(screenshot);
		
		// get all object
		List<ABObject> objects = vision.findBlocksRealShape();
		List<ABObject> pigs = vision.findPigsRealShape();
		List<ABObject> birds = vision.findBirdsRealShape();
		List<ABObject> hills = vision.findHills();
		
		
		// find the slingshot
		Rectangle sling = vision.findSlingshotMBR();
		
		// confirm the slingshot
		while (sling == null && aRobot.getState() == GameState.PLAYING) {
			System.out
			.println("No slingshot detected. Please remove pop up or zoom out");
			ActionRobot.fullyZoomOut();
			screenshot = ActionRobot.doScreenShot();
			vision = new Vision(screenshot);
			sling = vision.findSlingshotMBR();
		}
		
		System.out.println("Number of pig: " + pigs.size());
		System.out.println("Number of bird: " + birds.size());
		System.out.println("Number of block: " + objects.size());
		System.out.println("Number of hill: " + hills.size());
		
		// Game State
		GameState state = aRobot.getState();
		
		if (sling != null)
		{
			if (!pigs.isEmpty())
			{
				Point releasePoint = null;
				Shot shot = new Shot();
				int dx, dy;
				double max = Double.MIN_VALUE;
				ABObject best = null;
				
				for (int i = 0; i < pigs.size(); i++)
				{
					best = pigs.get(i);
					if (pigs.get(i).id == id)
					{
						best = pigs.get(i);
						break;
					}
				}
				
				// estimate the trajectory
				ArrayList<Point> pts = tp.estimateLaunchPoint(sling, best.getCenter());
				releasePoint = pts.get(0);
				
				// Get the reference point
				Point refPoint = tp.getReferencePoint(sling);
				
				
				List<Point> points = tp.predictTrajectory(sling, releasePoint);
				List<ABObject> result = new ArrayList<ABObject>();
				Point target = best.getCenter();
				for (Point point : points) {
					if (point.x < 840 && point.y < 480 && point.y > 100
							&& point.x > 400)
						for (ABObject ab : objects) {
							if (((ab.contains(point) && !ab.contains(target)) || Math
									.abs(vision.getMBRVision()._scene[point.y][point.x] - 72) < 10)
									&& point.x < target.x)
								{
								if(!result.contains(ab))
									result.add(ab);
								}
						}

				}
				for (int i = 0; i < result.size(); i++)
				{
					ABObject ab = result.get(i);
					System.out.println("Sheltering structure is: " + ab.id);
					System.out.println("Type: " + ab.type);
					System.out.println("Position: " + ab.getLocation());
				}
				
			}
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
