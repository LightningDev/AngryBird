package ab.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ab.demo.other.Shot;
import ab.intervalcalculus.IntervalRelations.ERA;
import ab.intervalcalculus.StabilityChecker.ContactRelation;
import ab.intervalcalculus.RectangleAlgebra;
import ab.intervalcalculus.StabilityRules;
import ab.planner.TrajectoryPlanner;
import ab.vision.ABObject;
import ab.vision.ABShape;
import ab.vision.ABType;
import ab.vision.Vision;
import ab.vision.real.shape.Rect;

public class ABUtil {

	public static int gap = 3; // vision tolerance.
	private static TrajectoryPlanner tp = new TrajectoryPlanner();

	// If o1 supports o2, return true
	public static boolean isSupport(ABObject o2, ABObject o1) {
		if (o2.x == o1.x && o2.y == o1.y && o2.width == o1.width
				&& o2.height == o1.height)
			return false;

		int ex_o1 = o1.x + o1.width;
		int ex_o2 = o2.x + o2.width;

		int ey_o2 = o2.y + o2.height;
		if ((Math.abs(ey_o2 - o1.y) < gap)
				&& !(o2.x - ex_o1 > gap || o1.x - ex_o2 > gap))
			return true;
		return false;
	}
	
	// Angular Rectangle support
	public static boolean isAngularSupport (ABObject o2, ABObject o1) {
		//System.out.println("checking angular support between " + o2.id + "(" + o2.shape + ")" + " " + o1.id + "(" + o1.shape + ")" );
		if (o2.IsAngular() && o2.shape == ABShape.Rect && !isSupport(o2, o1) && o1.shape == ABShape.Rect
				&& o2.getType() != ABType.Pig && o1.getType() != ABType.Pig)
		{
			Rect rect = (Rect)o2;
			Rect rect1 = (Rect)o1;
			int[] yPoints = rect.p.ypoints;
			int maxY = 0;
			int x = 0;
			int secondY = 0;
			int secondX = 0;
			
			// Find lower edge
			for (int i = 0; i < yPoints.length; i++)
			{
				if (yPoints[i] > maxY)
				{
					maxY = yPoints[i];
					x = rect.p.xpoints[i];
				}
			}
			for (int i = 0; i < yPoints.length; i++)
			{
				int pX = rect.p.xpoints[i];
				int pY = yPoints[i];
				int vecX = pX - x;
				int vecY = pY - maxY;
				double distance = Math.sqrt(Math.pow(vecX, 2) + Math.pow(vecY, 2));
				if (distance == rect.getpLength())
				{
					secondX = pX;
					secondY = pY;
					break;
				}
			}
			
			// Get Line equation of lower edge
			// and check if any points of the other rectangle
			// are contact with this line, if yes add to list
			double[] lines = RectangleAlgebra.LineEquation(x, maxY, secondX, secondY);
			List<double[]> lineList = new ArrayList<double[]>(); 
			lineList.add(lines);
			ContactRelation cr = RectangleAlgebra.CheckLineEquations(rect1.p.xpoints,rect1.p.ypoints, lineList);
			if (cr != ContactRelation.NULL)
				return true;
		}
		return false;
	}

	// Return a link list of ABObjects that support o1 (test by isSupport
	// function ).
	// objs refers to a list of potential supporters.
	// Empty list will be returned if no such supporters.
	public static List<ABObject> getSupporters(ABObject o2, List<ABObject> objs, int depth) {
		List<ABObject> result = new LinkedList<ABObject>();
		// Loop through the potential supporters
		for (ABObject o1 : objs) {
			if (isSupport(o2, o1))
			{
				o1.depth = depth;
				result.add(o1);
			}
			
			// Angular support
			if (isAngularSupport(o2, o1))
			{
				o1.depth = depth;
				result.add(o1);
			}
		}
		return result;
	}
	
	// Return a link list of ABObjects that support o1 (test by isSupport
	// function ).
	// objs refers to a list of potential supporters.
	// Empty list will be returned if no such supporters.
	public static List<ABObject> getSupporters(ABObject o2, List<ABObject> objs) {
		List<ABObject> result = new LinkedList<ABObject>();
		// Loop through the potential supporters
		for (ABObject o1 : objs) {
			if (isSupport(o2, o1))
			{
				result.add(o1);
			}
			
			// Angular support
			if (isAngularSupport(o2, o1))
			{
				result.add(o1);
			}
		}
		return result;
	}
	
	// Get all supporter of o1 included direct or indirect
	public static List<ABObject> getAllSupporter (List<ABObject> objs,List<ABObject> ob)
	{
		List<ABObject> result = new LinkedList<ABObject>();
		try 
		{			
			for (int i = 0; i < objs.size(); i++)
			{
				List<ABObject> temp = getSupporters (objs.get(i), ob, objs.get(i).depth + 1);
				if (!result.containsAll(temp))
					result.addAll(temp);
				System.out.println("Call supporter of " + objs.get(i).id);
				List<ABObject> recursive = getAllSupporter (temp, ob);
				if (!result.containsAll(recursive))
					result.addAll(recursive);
			}
			//System.out.println(result.size());
			return result;
		}
		catch (StackOverflowError e)
		{
			return result;
		}
	}
		
	// Return a link list of ABObjects that are supported by o1
	public static List<ABObject> getSupportees(ABObject o1, List<ABObject> objs, int depth)
	{
		List<ABObject> result = new LinkedList<ABObject>();
		// Loop through the potential supporters
		for (ABObject o2 : objs) {
			if (isSupport(o2, o1))
			{
				o2.depth = depth;
				result.add(o2);
			}
			
			// Angular support
			if (isAngularSupport(o2, o1))
			{
				o2.depth = depth;
				result.add(o2);
			}
		}
		return result;
	}
	
	// Return a link list of ABObjects that are supported by o1
	public static List<ABObject> getSupportees(ABObject o1, List<ABObject> objs)
	{
		List<ABObject> result = new LinkedList<ABObject>();
		// Loop through the potential supporters
		for (ABObject o2 : objs) {
			if (isSupport(o2, o1))
			{
				result.add(o2);
			}
			
			// Angular support
			if (isAngularSupport(o2, o1))
			{
				result.add(o2);
			}
		}
		return result;
	}
	
	// Get all supportee of o1 included direct or indirect
	public static List<ABObject> getAllSupportees (List<ABObject> objs,List<ABObject> ob)
	{
		List<ABObject> result = new LinkedList<ABObject>();
		for (int i = 0; i < objs.size(); i++)
		{
			List<ABObject> temp = getSupportees (objs.get(i), ob, objs.get(i).depth + 1);
			if (!result.containsAll(temp))
				result.addAll(temp);
			//System.out.println("Call supportee of " + objs.get(i).id);
			List<ABObject> recursive = getAllSupportees(temp, ob);
			if (!result.containsAll(recursive))
				result.addAll(recursive);
		}
		//System.out.println(result.size());
		return result;
	}
	
	public static List<ABObject> getNeighbours (ABObject ob, List<ABObject> objs)
	{
		return  null;
	}

	// Return true if the target can be hit by releasing the bird at the
	// specified release point
	public static boolean isReachable(Vision vision, Point target, Shot shot) {
		// test whether the trajectory can pass the target without considering
		// obstructions
		Point releasePoint = new Point(shot.getX() + shot.getDx(), shot.getY()
				+ shot.getDy());
		int traY = tp.getYCoordinate(vision.findSlingshotMBR(), releasePoint,
				target.x);
		if (Math.abs(traY - target.y) > 100) 
		{
			// System.out.println(Math.abs(traY - target.y));
			return false;
		}
		boolean result = true;
		List<Point> points = tp.predictTrajectory(vision.findSlingshotMBR(),
				releasePoint);
		for (Point point : points) {
			if (point.x < 840 && point.y < 480 && point.y > 100
					&& point.x > 400)
				for (ABObject ab : vision.findBlocksMBR()) {
					if (((ab.contains(point) && !ab.contains(target)) || Math
							.abs(vision.getMBRVision()._scene[point.y][point.x] - 72) < 10)
							&& point.x < target.x)
						return false;
				}

		}
		return result;
	}
	
	// Return List of ABObject on a trajectory way
	public static List<ABObject> Reachable(Vision vision, Point target, Point releasePoint) {
		List<ABObject> result = new ArrayList<ABObject>();
		List<Point> points = tp.predictTrajectory(vision.findSlingshotMBR(),
				releasePoint);
		for (Point point : points) {
			if (point.x < 840 && point.y < 480 && point.y > 100
					&& point.x > 400)
				for (ABObject ab : vision.findBlocksMBR()) {
					if (((ab.contains(point) && !ab.contains(target)) || Math
							.abs(vision.getMBRVision()._scene[point.y][point.x] - 72) < 10)
							&& point.x < target.x)
						result.add(ab);
				}

		}
		return result;
	}

}
