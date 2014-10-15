package ab.QualitativePhysics;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import ab.vision.ABObject;
import ab.vision.ABShape;
import ab.vision.ABType;
import ab.vision.real.shape.Circle;
import ab.vision.real.shape.Poly;
import ab.vision.real.shape.Rect;

public class MathFunctions 
{
	 /**
     * Get the line equation
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return Equations
     * [0] = a
     * [1] = b
     * [2] = (x1 - x2 == 0) ? 1 : 0
     * [3] : x1
     * [4] : y1
     * [5] : x2
     * [6] : y2
     */
    public static LineEquation lineEqCompute (int x1, int y1, int x2, int y2)
    {
    	double[] arr = new double[7];
    	
    	// Cramer's Rule to solve system of equation ( 2 x 2 )
    	if (x1 - x2 != 0)
    	{
    		// y = ax + b
	    	arr[0] = (double)(y1 - y2) / (double)(x1 - x2);
	    	arr[1] = (double)(x1*y2 - y1*x2) / (double)(x1 - x2);
	    	arr[2] = 0;
    	}
    	else
    	{
    		// line equation based on x = ay + b
    		arr[0] = 0;
    		arr[1] = x1;
    		arr[2] = 1;
    	}
    	if (x1 < x2)
    	{
    		arr[3] = x1;
    		arr[5] = x2;
    		arr[4] = y1;
    		arr[6] = y2;
    	}
    	else if (x1 > x2)
    	{
    		arr[3] = x2;
    		arr[5] = x1;
    		arr[4] = y2;
    		arr[6] = y1;
    	}
    	else if (x1 == x2)
    	{
    		if (y1 < y2)
    		{
    			arr[3] = x1;
        		arr[5] = x2;
        		arr[4] = y1;
        		arr[6] = y2;
    		}
    		else
    		{
    			arr[3] = x2;
        		arr[5] = x1;
        		arr[4] = y2;
        		arr[6] = y1;
    		}
    	}
    	
    	LineEquation leq = new LineEquation(arr[0], arr[1], new Point((int)arr[3], (int)arr[4]), 
    			new Point((int)arr[5], (int)arr[6]), (arr[2] == 1) ? true : false);
    	
     	return leq;
    }
    
    public static List<LineEquation> objectLineEquations (List<Point> points)
    {
    	List<LineEquation> list = new ArrayList<LineEquation>(); 	
    	for (int i = 0; i < points.size(); i++)
    	{
    		int x1 = points.get(i).x;
    		int y1 = points.get(i).y;
    		if (i != points.size() - 1)
    		{
    			int x2 = points.get(i+1).x;
    			int y2 = points.get(i+1).y;
    			list.add(lineEqCompute(x1, y1, x2, y2));
    		}
    		else
    		{
    			int x2 = points.get(0).x;
    			int y2 = points.get(0).y;
    			list.add(lineEqCompute(x1, y1, x2, y2));
    		}
    	}
    	return list;
    }
    
    public static List<Point> getPointsOnLine (LineEquation line)
    {
    	List<Point> ps = new ArrayList<Point>();
    	// x = ay + b
    	if (line.isX())
    	{
    		double y1 = line.getStart().y;
			double y2 = line.getEnd().y;
			if (line.getStart().y > line.getEnd().y) {
				y1 = line.getEnd().y;
				y2 = line.getStart().y ;
			}
			double x = line.getStart().x;
			for (double j = y1; j <= y2; j++) {
				Point point = new Point((int) x, (int) j);
				ps.add(point);
			}
    	}
    	
    	// y = ax + b
    	else
    	{
    		double x1 = line.getStart().x;
			double x2 = line.getEnd().x;
			double a = line.getA();
			double b = line.getB();
			for (double j = x1; j <= x2; j++) {
				double y = a * j + b;
				Point point = new Point((int) j, (int) y);
				ps.add(point);
			}
    	}
    	
    	return ps;
    }
    
    public static List<Point2D> CircleContour (ABObject ob)
    {
    	Circle circle = (Circle)ob;
    	double radius = circle.r;
    	Point center = ob.getCenter();
    	List<Point2D> circles = new ArrayList<Point2D>();
    	
    	// Go from 0..360 degrees to find all points
    	for (int i = 0; i <=360; i++)
    	{
    		double x = center.x + radius * Math.cos(Math.toRadians(i));
    		double y = center.y + radius * Math.sin(Math.toRadians(i));
    		circles.add(new Point2D.Double(x, y));
    	}
    	
    	return circles;
    }
    
    public static List<Point> PolyCorners (ABObject ob)
    {
    	int[] x = {};
    	int[] y = {};
    	List<Point> poly = new ArrayList<Point>();
    	
    	// Fix the error in render regular rectangle
    	if (ob.getType() != ABType.Hill && !ob.IsAngular() && ob.shape == ABShape.Poly)
    	{
    		x = new int[]{(int)ob.getMinX(), (int)ob.getMaxX(), (int)ob.getMaxX(), (int)ob.getMinX()};
    		y = new int[]{(int)ob.getMinY(), (int)ob.getMinY(), (int)ob.getMaxY(), (int)ob.getMaxY()};
    	}
    	
    	// Rectangle
    	else if (ob.shape == ABShape.Rect)
    	{
    		x = new int[]{(int)ob.getMinX(), (int)ob.getMaxX(), (int)ob.getMaxX(), (int)ob.getMinX()};
			y = new int[]{(int)ob.getMinY(), (int)ob.getMinY(), (int)ob.getMaxY(), (int)ob.getMaxY()};
    	}
    	
    	// Triangle or Polygon
    	else if (ob.shape == ABShape.Triangle || ob.shape == ABShape.Poly)
    	{
    		Poly triangle = (Poly)ob;
    		x = triangle.polygon.xpoints;
    		y = triangle.polygon.ypoints;
    	}
    	
    	for (int i = 0; i < x.length; i++)
    	{
    		Point p = new Point(x[i], y[i]);
    		poly.add(p);
    	}
    	
    	return poly;
    }
    
    public static List<Point> PolyContour (List<Point> polyCorner)
    {
    	List<Point> poly = new ArrayList<Point>();
    	
    	// Line equations
    	List<LineEquation> leq = objectLineEquations(polyCorner);
    	
    	for (int i = 0; i < leq.size(); i++)
    	{
    		LineEquation le = leq.get(i);
    		poly.addAll(getPointsOnLine(le));
    	}
    	
    	return poly;
    }
    
    public static boolean checkPointLieOnLine (Point2D point, ABObject ob)
    {
    	List<LineEquation> leq = objectLineEquations(PolyCorners(ob));
    	for (int i = 0; i < leq.size(); i++)
    	{
    		LineEquation line = leq.get(i);
    		if (line.contains(point))
    			return true;
    	}
    	return false;
    }
    
    public static List<Point2D> convertToPoint2D (List<Point> points)
    {
    	List<Point2D> p2D = new ArrayList<Point2D>();
    	for (int i = 0; i < points.size(); i++)
    	{
    		p2D.add(new Point2D.Double(points.get(i).x, points.get(i).y));
    	}
    	return p2D;
    }
    
    public static List<Point> convertToPoint (List<Point2D> points)
    {
    	List<Point> p2D = new ArrayList<Point>();
    	for (int i = 0; i < points.size(); i++)
    	{
    		p2D.add(new Point ((int)points.get(i).getX(), (int)points.get(i).getY()));
    	}
    	return p2D;
    }
    
    public static List<Point2D> getObjectContourPoints (ABObject ob)
    {
    	List<Point2D> bounds = new ArrayList<Point2D>();
    	
    	// Circle object
    	// Especially is pig
    	if (ob.getType() == ABType.Pig || ob.shape == ABShape.Circle)
    	{
    		bounds = CircleContour(ob);
    	}
    	
    	else 
    	{
    		List<Point> ps = PolyContour(PolyCorners(ob));
    		bounds = convertToPoint2D(ps);
    	}
    	
    	return bounds;
    }
    
    public double objectArea (ABObject ob)
    {
    	double area = 0;
    	if (ob.shape == ABShape.Circle)
    	{
    		Circle c = (Circle)ob;
    		area = c.area;
    	}
    	else if (ob.shape == ABShape.Rect)
    	{
    		Rect r = (Rect)ob;
    		area =  r.area;
    	}
    	else
    	{
    		Poly p = (Poly)ob;
    		area = p.area;
    	}
    	return area;
    }
    
    public double objectRatio (ABObject ob)
    {
    	if (ob.shape == ABShape.Rect)
    	{
    		Rect r = (Rect) ob;
    		int width = r.width;
    		int height = r.height;
    		if (width >= height)
    			return 1;
    		else
    			return ((double)width / (double)height);
    	}
    	if (ob.width >= ob.height)
    		return 1;
    	else
    		return ((double)ob.width / (double)ob.height);
    }
    
}
