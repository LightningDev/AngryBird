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

import ab.QualitativePhysics.MathFunctions;
import ab.intervalcalculus.RectangleAlgebra;
import ab.vision.real.shape.Poly;
import ab.vision.real.shape.Rect;

public class ABObject extends Rectangle {
	private static final long serialVersionUID = 1L;
	private static int counter = 0;
	public int id;
	public int depth = 0;
	public boolean isReachable;
	private double sheltersValue = 0;
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
	
	public double getSheltersValue() {
		return sheltersValue;
	}

	public void setSheltersValue(double sheltersValue) {
		this.sheltersValue = sheltersValue;
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
