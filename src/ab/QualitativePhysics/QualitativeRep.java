package ab.QualitativePhysics;

import java.awt.Point;
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
	
	public static List<Point> ConnectionPoint (ABObject o1, ABObject o2)
	{
		List<Point> connection_p = new ArrayList<Point>();
		if (o1.id != o2.id)
		{
			List<Point> pointsO1 = o1.GetAllPoints();
			List<Point> pointsO2 = o2.GetAllPoints();
			//System.out.println("Object " + o1.id + " size : " + pointsO1.size());
			//System.out.println("Object " + o2.id + " size : " + pointsO2.size());
			double error_distance = 2.0;
			
			for (int i = 0; i < pointsO1.size(); i++)
			{
				Point p1 = pointsO1.get(i);
				for (int j = 0; j < pointsO2.size(); j++)
				{
					Point p2 = pointsO2.get(j);
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
	
	public static Point LeftPoint (ABObject o1, ABObject o2)
	{
		List<Point> connection_p = ConnectionPoint(o1, o2);
		if (connection_p.size() > 0)
		{		
			// Find x
			int x = FindMin (true, 0, connection_p);
			
			// Find y
			int min_y = FindMin(false, x, connection_p);
			int max_y = FindMax(false, x, connection_p);
			int y = (min_y + max_y) / 2;
			
			return new Point (x, y);
		}
		return new Point (0,0);
	}
	
	public static Point RightPoint (ABObject o1, ABObject o2)
	{
		List<Point> connection_p = ConnectionPoint(o1, o2);
		if (connection_p.size() > 0)
		{		
			// Find x
			int x = FindMax (true, 0, connection_p);
			
			// Find y
			int min_y = FindMin(false, x, connection_p);
			int max_y = FindMax(false, x, connection_p);
			int y = (min_y + max_y) / 2;
			
			return new Point (x, y);
		}
		return new Point (0,0);
	}
	
	public static Point CenterPoint (ABObject o1, ABObject o2)
	{
		List<Point> connection_p = ConnectionPoint(o1, o2);
		if (connection_p.size() > 0)
		{		
			// Find x
			int x_left = LeftPoint(o1, o2).x;
			int x_right = RightPoint(o1, o2).x;
			int x = (x_left + x_right) / 2;
			
			// Find y
			int min_y = FindMin(false, x, connection_p);
			int max_y = FindMax(false, x, connection_p);
			int y = (min_y + max_y) / 2;
			
			return new Point (x, y);
		}
		return new Point (0,0);
	}
	
	public static int FindMin (boolean x, int n, List<Point> connection_p)
	{
		int _n = 999;
		for (int i = 0; i < connection_p.size(); i++)
		{
			if(x)
			{
				int _x = connection_p.get(i).x;
				if (_x < _n)
				{
					_n = _x;
				}
			}
			else
			{
				int _y = connection_p.get(i).y;
				int _x = connection_p.get(i).x;
				if (_y < _n && _x == n)
				{
					_n = _y;
				}
			}
		}
		return _n;
	}
	
	public static int FindMax (boolean x, int n, List<Point> connection_p)
	{
		int _n = 0;
		for (int i = 0; i < connection_p.size(); i++)
		{
			if(x)
			{
				int _x = connection_p.get(i).x;
				if (_x > _n)
				{
					_n = _x;
				}
			}
			else
			{
				int _y = connection_p.get(i).y;
				int _x = connection_p.get(i).x;
				if (_y > _n && _x == n)
				{
					_n = _y;
				}
			}
		}
		return _n;
	}
	
	
	public static double Distance2Points (Point p1, Point p2)
	{
		int x = p1.x;
		int y = p1.y;
		int _x = p2.x;
		int _y = p2.y;
		int dx = _x - x;
		int dy = _y - y;
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		return distance;
	}
}
