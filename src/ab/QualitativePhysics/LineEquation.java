package ab.QualitativePhysics;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineEquation 
{
	private double a;
	private double b;
	private Point start;
	private Point end;
	private boolean isX = false;
	
	public LineEquation(double a, double b, Point start, Point end, boolean isX)
	{
		this.a = a;
		this.b = b;
		this.start = start;
		this.end = end;
		this.isX = isX;
	}
	
	public boolean contains (Point2D point)
	{
		double x = point.getX();
		double y = point.getY();
		if (x >= start.getX() && x <= end.getX() && y >= start.getY() && y <= end.getY())
		{
			if (isX)
				return true;
			else
			{
				// y = ax + b
				double _y = a * x + b;
				if (_y == y)
					return true;
			}
		}
		return false;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public boolean isX() {
		return isX;
	}

	public void setX(boolean isX) {
		this.isX = isX;
	}
	
	
}
