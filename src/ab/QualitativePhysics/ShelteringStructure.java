package ab.QualitativePhysics;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ab.vision.ABObject;

public class ShelteringStructure 
{
	private ABObject target;
	private List<ABObject> shelters;
	
	public ShelteringStructure(ABObject target)
	{
		this.target = target;
		this.shelters = new ArrayList<ABObject>();
	}
	
	public List<ABObject> ShelterObjects (List<Point> traj, List<ABObject> objects)
	{
		shelters.clear();
		for (Point point : traj) {
			if (point.x < 840 && point.y < 480 && point.y > 100
					&& point.x > 400)
				for (ABObject ab : objects) {
					if (ab.contains(point) && !ab.contains(this.target) && point.x < this.target.x)
						{
						if(!this.shelters.contains(ab))
							this.shelters.add(ab);
						}
				}

		}
		shelter_Val();
		
		return this.shelters;
	}
	
	private void shelter_Val ()
	{
		for (int i = 0; i < shelters.size(); i++)
		{
			ABObject o1 = shelters.get(i);
			double nobetween = 1.0 / (double) no_Between(o1, target);
			double dist = 1.0 / QualitativeRep.Distance2Objects(o1, target);
			double value = nobetween * dist;
			shelters.get(i).setSheltersValue(value);
		}
	}
	
	private int no_Between (ABObject o1, ABObject o2)
	{
		double minX = o1.getMaxX();
		double maxX = o2.getMinX();
		int no = 0;
		for (int i = 0; i < shelters.size(); i++)
		{
			ABObject o = shelters.get(i);
			double _minX = o.getMinX();
			double _maxX = o.getMaxX();
			if (_minX <= maxX && _minX >= minX && _maxX <= maxX && _maxX >= minX)
				no++;
		}
		return no;
	}
	
	public double FindShelValueByObject (ABObject obs)
	{
		for (int i = 0; i < shelters.size(); i++)
		{
			ABObject o = shelters.get(i);
			if (o.getLocation() == obs.getLocation())
				return o.getSheltersValue();
		}
		return 0;
	}

	public ABObject getTarget() {
		return target;
	}

	public void setTarget(ABObject target) {
		this.target = target;
	}

	public List<ABObject> getShelters() {
		return shelters;
	}

	public void setShelters(List<ABObject> shelters) {
		this.shelters = shelters;
	}
	
}
