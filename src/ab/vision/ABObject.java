/*****************************************************************************
 ** ANGRYBIRDS AI AGENT FRAMEWORK
 ** Copyright (c) 2014, XiaoYu (Gary) Ge, Stephen Gould, Jochen Renz
 **  Sahan Abeyasinghe,Jim Keys,  Andrew Wang, Peng Zhang
 ** All rights reserved.
 **This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
 **To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 *or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *****************************************************************************/
package ab.vision;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import ab.intervalcalculus.RectangleAlgebra;
import ab.vision.real.shape.Poly;
import ab.vision.real.shape.Rect;

public class ABObject extends Rectangle {
	private static final long serialVersionUID = 1L;
	private static int counter = 0;
	public int id;
	public int depth = 0;
	public boolean isReachable;
	// object type
	public ABType type;

	public int area = 0;
	// For all MBRs, the shape is Rect by default.
	public ABShape shape = ABShape.Rect;

	// For all MBRs, the angle is 0 by default.
	public double angle = 0;

	// is Hollow or not
	public boolean hollow = false;

	public ABObject(Rectangle mbr, ABType type) {
		super(mbr);
		this.type = type;
		this.id = counter++;
	}

	public ABObject(Rectangle mbr, ABType type, int id) {
		super(mbr);
		this.type = type;
		this.id = id;
	}

	public ABObject(ABObject ab) {
		super(ab.getBounds());
		this.type = ab.type;
		this.id = ab.id;
	}

	public ABObject() {
		this.id = counter++;
		this.type = ABType.Unknown;
	}

	public ABType getType() {
		return type;
	}

	public boolean IsAngular() {
		double degree = Math.toDegrees(this.angle);
		if ((degree <= 85 && degree >= 5) || (degree <= 175 && degree >= 95)
				|| (degree <= 265 && degree >= 185)
				|| (degree <= 355 && degree >= 275))
			return true;
		return false;
	}

	public List<Point> GetPoints() {
		List<Point> points = new ArrayList<Point>();
		if (this.IsAngular()) {
			if (this.shape == ABShape.Rect && this.type != ABType.Pig) {
				Rect ob = (Rect) this;
				int[] x = ob.p.xpoints;
				int[] y = ob.p.ypoints;
				for (int i = 0; i < x.length; i++) {
					if (x[i] != 0 && y[i] != 0) {
						Point point = new Point(x[i], y[i]);
						points.add(point);
					}
				}
			} else if (this.shape == ABShape.Rect && this.type == ABType.Pig) {
				points.add(new Point((int) this.getMinX(), (int) this.getMinY()));
				points.add(new Point((int) this.getMaxX(), (int) this.getMinY()));
				points.add(new Point((int) this.getMaxX(), (int) this.getMaxY()));
				points.add(new Point((int) this.getMinX(), (int) this.getMaxY()));
			} else if (this.shape == ABShape.Circle) {
				points.add(new Point((int) this.getMinX(), (int) this.getMinY()));
				points.add(new Point((int) this.getMaxX(), (int) this.getMinY()));
				points.add(new Point((int) this.getMaxX(), (int) this.getMaxY()));
				points.add(new Point((int) this.getMinX(), (int) this.getMaxY()));
			} else if (this.shape == ABShape.Triangle
					|| this.shape == ABShape.Poly) {
				Poly ob = (Poly) this;
				int[] x = ob.polygon.xpoints;
				int[] y = ob.polygon.ypoints;
				for (int i = 0; i < x.length; i++) {
					Point point = new Point(x[i], y[i]);
					points.add(point);
				}
			}
		} else {
			if (this.shape != ABShape.Poly && this.shape != ABShape.Triangle) {
				points.add(new Point((int) this.getMinX(), (int) this.getMinY()));
				points.add(new Point((int) this.getMaxX(), (int) this.getMinY()));
				points.add(new Point((int) this.getMaxX(), (int) this.getMaxY()));
				points.add(new Point((int) this.getMinX(), (int) this.getMaxY()));
			} else {
				Poly ob = (Poly) this;
				int[] x = ob.polygon.xpoints;
				int[] y = ob.polygon.ypoints;
				for (int i = 0; i < x.length; i++) {
					if (x[i] != 0 && y[i] != 0) {
						Point point = new Point(x[i], y[i]);
						points.add(point);
					}
				}
			}
		}
		return points;
	}

	public List<Point> GetAllPoints() {
		List<Point> points = GetPoints();
		int[] xs = new int[points.size()];
		int[] ys = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			xs[i] = points.get(i).x;
			ys[i] = points.get(i).y;
		}

		// Find line equations based on objects's points
		List<double[]> lines = new ArrayList<double[]>();
		RectangleAlgebra.AddLineEquations(xs, ys, lines);

		// Find all points bound the object
		List<Point> ps = new ArrayList<Point>();
		for (int i = 0; i < lines.size(); i++) {
			double[] equation = lines.get(i);
			// x = ay + b
			if (equation[2] == 1) {
				double y1 = equation[4];
				double y2 = equation[6];
				if (equation[4] > equation[6]) {
					y1 = equation[6];
					y2 = equation[4];
				}
				double x = equation[3];
				for (double j = y1; j <= y2; j++) {
					Point point = new Point((int) x, (int) j);
					ps.add(point);
				}
			}
			// y = ax + b
			else {
				double x1 = equation[3];
				double x2 = equation[5];
				double a = equation[0];
				double b = equation[1];
				for (double j = x1; j <= x2; j++) {
					double y = a * j + b;
					Point point = new Point((int) j, (int) y);
					ps.add(point);
				}
			}
		}

		return ps;
	}

	public int[] GetBoundX() {
		return new int[] { (int) this.getMinX(), (int) this.getMinX(),
				(int) this.getMaxX(), (int) this.getMaxX() };
	}

	public int[] GetBoundY() {
		return new int[] { (int) this.getMinY(), (int) this.getMaxY(),
				(int) this.getMaxY(), (int) this.getMinY() };
	}

	public Point getCenter() {
		return new Point((int) getCenterX(), (int) getCenterY());
	}

	public static void resetCounter() {
		counter = 0;
	}

}
