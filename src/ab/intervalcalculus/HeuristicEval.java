package ab.intervalcalculus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ab.vision.ABObject;
import ab.vision.ABType;

public class HeuristicEval 
{
	private List<Double> scores;
	private double HIT_RATIO_THRESHOLD = 2.0;
	private double TOPPLING_RATIO_THRESHOLD = 5.0;
	
	public HeuristicEval()
	{
		scores = new ArrayList<Double>();
	}
	
	public void Evaluation (ABType birds, List<ABObject> objects, List<Point> tp)
	{
		// Clear score list
		scores.clear();
		
		// Find out the objects from direct impact
		for (int i = 0; i < objects.size(); i++)
		{
			List<ABObject> affectedList = new ArrayList<ABObject>();
			ABObject target = objects.get(i);
			if (target.getType() != ABType.Hill)
			{
				affectedList.addAll(DirectImpact(birds, target, objects));
				int size = affectedList.size();
				for (int j = 0; j < size; j++)
				{
					ABObject _target = affectedList.get(j);
					double ratio = target.height / target.width;
				}
			}
		}
	}
	
	public void DirectHit (ABObject target, List<ABObject> objects)
	{
		// Regular Rectangle
		if (!target.IsAngular())
		{
			double ratio = target.height / target.width;
		}
		
		// Angular Rectangle
		else
		{
			
		}
	}
	
	public List<ABObject> DirectImpact (ABType birds, ABObject target, List<ABObject> objects)
	{
		List<ABObject> directList = new ArrayList<ABObject>();
		switch(birds)
		{
		case RedBird:
			directList.add(target);
			break;
		case YellowBird:
			FindObjectInRow(target, objects, directList);
			break;
		}
		return directList;
	}
	
	private void FindObjectInRow (ABObject target, List<ABObject> objects, List<ABObject> directList)
	{
		for (int i = 0; i < objects.size(); i++)
		{
			if (directList.size() <= 3)
			{
				ABObject x = objects.get(i);
				if (x.getCenterX() >= target.getMaxX() && x.getCenterX() <= target.getMaxX() + 10)
				{
					if (x.getCenterY() >= target.getMinY()-10 && x.getCenterY() <= target.getMaxY()+10)
					{
						directList.add(x);
					}
				}
			}
			else
				break;
		}
	}
}
