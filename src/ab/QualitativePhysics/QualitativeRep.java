package ab.QualitativePhysics;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import ab.vision.ABObject;
import ab.vision.ABShape;
import ab.vision.real.shape.Rect;

public class QualitativeRep 
{
	/**
	 * Detect touch relation between o1 and o2
	 * @param o1: ABObject 1 
	 * @param o2: ABObject 2
	 * Touch (o1, o2) = connection_p (o1, o2) > 0
	 */
	public static boolean TouchRelation (ABObject o1, ABObject o2)
	{
		if (ConnectionPoint(o1, o2).size() > 0)
		{
			return true;
		}
		return false;
	}
	
	public static List<Point2D> ConnectionPoint (ABObject o1, ABObject o2)
	{
		List<Point2D> connection_p = new ArrayList<Point2D>();
		if (o1.id != o2.id)
		{
			List<Point2D> pointsO1 = MathFunctions.getObjectContourPoints(o1);
			List<Point2D> pointsO2 = MathFunctions.getObjectContourPoints(o2);
			//System.out.println("Object " + o1.id + " size : " + pointsO1.size());
			//System.out.println("Object " + o2.id + " size : " + pointsO2.size());
			double error_distance = 2.0;
			
			for (int i = 0; i < pointsO1.size(); i++)
			{
				Point2D p1 = pointsO1.get(i);
				for (int j = 0; j < pointsO2.size(); j++)
				{
					Point2D p2 = pointsO2.get(j);
					double distance = Distance2Points(p1, p2);
					//System.out.println("Distance between " + p1 + ", " + p2 + " = " + distance);
					if (distance < error_distance)
					{
						/*System.out.println("Distance between " + "(" +  p1.x + ", " + p1.y + ")" + 
									"(" + p2.x + ", " + p2.y + ")" + 
								" = " + distance);*/
						connection_p.add(p1);
						connection_p.add(p2);
					}
				}
			}
		}
		return connection_p;
	}
	
	
	/**
	 * Left_p function
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static Point2D LeftPoint (ABObject o1, ABObject o2)
	{
		List<Point2D> connection_p = ConnectionPoint(o1, o2);
		if (connection_p.size() > 0)
		{		
			// Find x
			double x = FindMin (true, 0, connection_p);
			
			// Find y
			double min_y = FindMin(false, x, connection_p);
			double max_y = FindMax(false, x, connection_p);
			double y = (min_y + max_y) / 2;
			
			return new Point2D.Double (x, y);
		}
		return new Point2D.Double (0,0);
	}
	
	/**
	 * Right_p function
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static Point2D RightPoint (ABObject o1, ABObject o2)
	{
		List<Point2D> connection_p = ConnectionPoint(o1, o2);
		if (connection_p.size() > 0)
		{		
			// Find x
			double x = FindMax (true, 0, connection_p);
			
			// Find y
			double min_y = FindMin(false, x, connection_p);
			double max_y = FindMax(false, x, connection_p);
			double y = (min_y + max_y) / 2;
			
			return new Point2D.Double (x, y);
		}
		return new Point2D.Double (0,0);
	}
	
	/**
	 * Center_p function
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static Point2D CenterPoint (ABObject o1, ABObject o2)
	{
		List<Point2D> connection_p = ConnectionPoint(o1, o2);
		if (connection_p.size() > 0)
		{		
			// Find x
			double x_left = LeftPoint(o1, o2).getX();
			double x_right = RightPoint(o1, o2).getX();
			double x = (x_left + x_right) / 2;
			
			// Find y
			double min_y = FindMin(false, x, connection_p);
			double max_y = FindMax(false, x, connection_p);
			double y = (min_y + max_y) / 2;
			
			return new Point2D.Double (x, y);
		}
		return new Point2D.Double (0,0);
	}
	
	
	/**
	 * Upper edge of an object
	 * @param ob
	 * @return set of points of edge
	 */
	public static List<Point2D> UpperEdge (ABObject ob)
	{
		List<Point2D> objectContour = MathFunctions.getObjectContourPoints(ob);
		List<Point2D> edge = new ArrayList<Point2D>();
		
		for (int i = 0; i < objectContour.size(); i++)
		{
			Point2D p = objectContour.get(i);
			Point2D p1 = new Point2D.Double (p.getX(), p.getY()+1);
			if (!ob.contains(p1) && !MathFunctions.checkPointLieOnLine(p1, ob))
			{
				edge.add(p);
				edge.add(p1);
				//System.out.println(p);
				//System.out.println(p1);
			}
		}
		
		return edge;
	}
	
	/**
	 * o1 is above o2 or not
	 * @param o1
	 * @param o2
	 * @return true or false
	 */
	public static boolean AboveRelation (ABObject o1, ABObject o2)
	{
		List<Point2D> edge = UpperEdge(o2);
		boolean touch = TouchRelation(o1, o2);
		Point2D center = CenterPoint(o1, o2);
		int x = (int)center.getX();
		int y = (int)center.getY();
		Point2D _p = new Point2D.Double (x,y);
		System.out.println(_p);
		if (edge.contains(_p) && touch)
			return true;
		return false;
	}
	
	/**
	 * Objects diretly above ob
	 * @param ob
	 * @param obs
	 * @return list of objects
	 */
	public static List<ABObject> AboveRelation (ABObject ob, List<ABObject> obs)
	{
		List<ABObject> above = new ArrayList<ABObject>();
		for (int i = 0; i < obs.size(); i++)
		{
			ABObject o = obs.get(i);
			if (AboveRelation(o, ob))
				above.add(o);
		}
		return above;
	}
	
	/**
	 * Above transitive closure set
	 * @param ob
	 * @param obs
	 * @param aboveO
	 */
	public static void AboveRelation (ABObject ob, List<ABObject> obs, List<ABObject> aboveO)
	{
		List<ABObject> above = AboveRelation(ob, obs);
		aboveO.addAll(above);
		for (int i = 0; i < above.size(); i++)
		{
			ABObject o = above.get(i);
			AboveRelation(o, obs, aboveO);
		}		
	}
	
	
	public static double FindMin (boolean x, double n, List<Point2D> connection_p)
	{
		double _n = 999;
		for (int i = 0; i < connection_p.size(); i++)
		{
			if(x)
			{
				double _x = connection_p.get(i).getX();
				if (_x < _n)
				{
					_n = _x;
				}
			}
			else
			{
				double _y = connection_p.get(i).getY();
				double _x = connection_p.get(i).getX();
				if (_y < _n && _x == n)
				{
					_n = _y;
				}
			}
		}
		return _n;
	}
	
	public static double FindMax (boolean x, double n, List<Point2D> connection_p)
	{
		double _n = 0;
		for (int i = 0; i < connection_p.size(); i++)
		{
			if(x)
			{
				double _x = connection_p.get(i).getX();
				if (_x > _n)
				{
					_n = _x;
				}
			}
			else
			{
				double _y = connection_p.get(i).getY();
				double _x = connection_p.get(i).getX();
				if (_y > _n && _x == n)
				{
					_n = _y;
				}
			}
		}
		return _n;
	}
	
	/**
	 * Distance between 2 point
	 * @param p1
	 * @param p2
	 * @return distance value
	 */
	public static double Distance2Points (Point2D p1, Point2D p2)
	{
		double x = p1.getX();
		double y = p1.getY();
		double _x = p2.getX();
		double _y = p2.getY();
		double dx = _x - x;
		double dy = _y - y;
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		return distance;
	}
	
	/**
	 * Distance between o1 and o2
	 * @param o1
	 * @param o2
	 * @return distance value
	 */
	public static double Distance2Objects (ABObject o1, ABObject o2)
	{
		List<Point2D> pointsO1 = MathFunctions.getObjectContourPoints(o1);
		List<Point2D> pointsO2 = MathFunctions.getObjectContourPoints(o2);
		
		double error_distance = 2.0;
		double min_distance = 999.0;
		for (int i = 0; i < pointsO1.size(); i++)
		{
			Point2D p1 = pointsO1.get(i);
			for (int j = 0; j < pointsO2.size(); j++)
			{
				Point2D p2 = pointsO2.get(j);
				double distance = Distance2Points(p1, p2);
				//System.out.println("Distance between " + p1 + ", " + p2 + " = " + distance);
				if (distance < error_distance)
				{
					/*System.out.println("Distance between " + "(" +  p1.x + ", " + p1.y + ")" + 
								"(" + p2.x + ", " + p2.y + ")" + 
							" = " + distance);*/
					if (distance < min_distance)
						min_distance = distance;
				}
			}
		}
		
		return min_distance;
	}
}
