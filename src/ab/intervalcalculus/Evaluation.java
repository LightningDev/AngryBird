package ab.intervalcalculus;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.StyledEditorKit.UnderlineAction;

import org.ini4j.jdk14.edu.emory.mathcs.backport.java.util.Arrays;

import ab.demo.other.Shot;
import ab.intervalcalculus.IntervalRelations.ERA;
import ab.utils.ABUtil;
import ab.vision.ABObject;
import ab.vision.ABType;
import ab.vision.Vision;

public class Evaluation 
{
	public static double HIT_RATIO_THRESHOLD = 2.0;
	public static double TOPPLING_RATIO_THRESHOLD = 5.0;
	public static List<ABObject> destroyedList;
	
	public static List<ABObject> getShelteringStructure (ABObject pig, ABObject[] list)
	{
		List<ABObject> leftList = new ArrayList<ABObject>();
		List<ABObject> rightList = new ArrayList<ABObject>();
		HashMap<ABObject, List<ABObject>> leftSupportee = new HashMap<ABObject, List<ABObject>>();
		HashMap<ABObject, List<ABObject>> rightSupportee = new HashMap<ABObject, List<ABObject>>();
		List<ABObject> shelteringList = new ArrayList<ABObject>();
		List<ABObject> supporterUnder = new ArrayList<ABObject>();
		
		// Get List of closest objects from right and left
		for (int i = 0; i < list.length; i++)
		{
			ERA[] rels = RectangleAlgebra.GetERA(list[i], pig);
			if (StabilityRules.CheckRule(rels[0], 321) && StabilityRules.CheckRule(rels[1], 322))
			{
				System.out.println(pig.id + " " + list[i].id + " : " + rels[0] + " , " + rels[1]);
				leftList.add(list[i]);
			}
			else if (StabilityRules.CheckRule(rels[0], 323) && StabilityRules.CheckRule(rels[1], 322))
			{
				System.out.println(pig.id + " " + list[i].id + " : " + rels[0] + " , " + rels[1]);
				rightList.add(list[i]);
			}
		}
		
		// Get closest objects of each side
		GetClosestObjects(leftList, true);
		GetClosestObjects(rightList, false);
		
		for (int i = 0; i < leftList.size(); i++)
		{
			System.out.println("Left: " + leftList.get(i).id);
			ABObject x = leftList.get(i);
			List<ABObject> temp =  ABUtil.getSupporters(x, Arrays.asList(list), 1);
			for (int j = 0; j < temp.size(); j++)
			{
				System.out.println("Supporter objects of " + x.id + " is " + temp.get(j).id + " depth " + temp.get(j).depth + " location " + temp.get(j).getLocation());
			}
			if (!shelteringList.contains(x))
			{
				shelteringList.add(x);
				//supporterUnder.addAll(ABUtil.getAllSupporter(temp, Arrays.asList(list)));
			}
		}
		for (int i = 0; i < rightList.size(); i++)
		{
			System.out.println("Right: " + rightList.get(i).id);
			ABObject x = rightList.get(i);
			List<ABObject> temp =  ABUtil.getSupporters(x, Arrays.asList(list), 1);
			for (int j = 0; j < temp.size(); j++)
			{
				System.out.println("Supporter objects of " + x.id + " is " + temp.get(j).id + " depth " + temp.get(j).depth + " location " + temp.get(j).getLocation());
			}
			if (!shelteringList.contains(rightList.get(i)))
			{
				shelteringList.add(rightList.get(i));
				//supporterUnder.addAll(ABUtil.getAllSupporter(temp, Arrays.asList(list)));
			}
		}
		
		for (int i = 0; i < leftList.size(); i++)
		{
			System.out.println("Left: " + leftList.get(i).id + leftList.get(i).getLocation());
		}
		for (int i = 0; i < rightList.size(); i++)
		{
			System.out.println("Right: " + rightList.get(i).id + rightList.get(i).getLocation());
		}
		
		// Get Supportee list of each side
		for (int i = 0; i < leftList.size(); i++)
		{
			ABObject x = leftList.get(i);
			List<ABObject> temp =  ABUtil.getSupportees(x, Arrays.asList(list), 1);
			for (int j = 0; j < temp.size(); j++)
			{
				System.out.println("Supportee objects of " + x.id + " is " + temp.get(j).id + " depth " + temp.get(j).depth + " location " + temp.get(j).getLocation());
			}
			List<ABObject> temp1 = ABUtil.getAllSupportees(temp, Arrays.asList(list));
			leftSupportee.put(x,temp1);
			for (int j = 0; j < temp1.size(); j++)
			{
				System.out.println("Supportee objects of " + x.id + " is " + temp1.get(j).id + " depth " + temp1.get(j).depth + " location " + temp1.get(j).getLocation());
			} 
		}
		for (int i = 0; i < rightList.size(); i++)
		{
			ABObject x = rightList.get(i);
			List<ABObject> temp =  ABUtil.getSupportees(x, Arrays.asList(list), 1);
			for (int j = 0; j < temp.size(); j++)
			{
				System.out.println("Supportee objects of " + x.id + " is " + temp.get(j).id + " depth " + temp.get(j).depth + " location " + temp.get(j).getLocation());
			}
			List<ABObject> temp1 = ABUtil.getAllSupportees(temp, Arrays.asList(list));
			rightSupportee.put(x,temp1);
			for (int j = 0; j < temp1.size(); j++)
			{
				System.out.println("Supportee objects of " + x.id + " is " + temp1.get(j).id + " depth " + temp1.get(j).depth + " location " + temp1.get(j).getLocation());
			}
		}
		
		// Get Roof Object
		List<ABObject> smallDepth = new ArrayList<ABObject>();
		ABObject roof = GetRoof(leftSupportee, rightSupportee);
		
		// If roof is found, out supportee of both side to sheltering structure
		if (roof != null)
		{
			System.out.println("Roof object is " + roof.id + " depth " + roof.depth);
			smallDepth.addAll(GetSupporteeShel(leftSupportee, roof));
			smallDepth.addAll(GetSupporteeShel(rightSupportee, roof));
			//shelteringList.addAll(supporterUnder);
			for (int i = 0; i < smallDepth.size(); i++)
			{
				System.out.println("Sheltering object is " + smallDepth.get(i).id + " depth " + smallDepth.get(i).depth);
				if (!shelteringList.contains(smallDepth.get(i)))
					shelteringList.add(smallDepth.get(i));
			}
			return shelteringList;
		}
		
		return null;
	}
	
	public static List<ABObject> ShelterFunction(ABObject target, Vision vision, Point releasePoint)
	{
		List<ABObject> shelters = new ArrayList<ABObject>();
		Point centerPoint = target.getCenter();
		shelters = ABUtil.Reachable(vision, centerPoint, releasePoint);
		return shelters;
	}
	
	public static List<ABObject> GetClosestObjects (List<ABObject> objectList, boolean left)
	{
		List<ABObject> removedList = new ArrayList<ABObject>();
		Iterator<ABObject> index = objectList.iterator();
		for (int i = 0; i < objectList.size(); i++)
		{
			ABObject y = objectList.get(i);
			for (int j = 0; j < objectList.size(); j++)
			{
				ABObject z = objectList.get(j);
				if (y.id != z.id)
				{
					ERA[] rels = RectangleAlgebra.GetERA(y, z);
					if (StabilityRules.CheckRule(rels[0], 331))
					{	
						if (left)
						{
							//System.out.println("delete " + y.id + " remain " + z.id);
							removedList.add(y);
						}
						else
						{
							//System.out.println("delete " + z.id + " remain " + y.id);
							removedList.add(z);
						}
					}
					else if (StabilityRules.CheckRule(rels[0], 332))
					{
						if (left)
						{
							//System.out.println("delete " + z.id + " remain " + y.id);
							removedList.add(z);
						}
						else
						{
							//System.out.println("delete " + y.id + " remain " + z.id);
							removedList.add(y);
						}
					}	
				}
			}
		}

		while (index.hasNext())
		{
			ABObject x = index.next();
			for (int i = 0; i < removedList.size(); i++)
			{
				ABObject y = removedList.get(i);
				if (x.id == y.id)
				{
					index.remove();
					break;
				}
			}
		}
		
		return objectList;
	}
	
	public static ABObject GetRoof (HashMap<ABObject, List<ABObject>> leftSupportee, HashMap<ABObject, List<ABObject>> rightSupportee)
	{
		List<ABObject> sameList = new ArrayList<ABObject>();
		for (ABObject ab: leftSupportee.keySet())
    	{
			List<ABObject> leftList = leftSupportee.get(ab);
			for (ABObject ab1 : rightSupportee.keySet())
			{
				List<ABObject> rightList = rightSupportee.get(ab1);
				for (int i = 0; i < leftList.size(); i++)
				{
					for (int j = 0 ; j < rightList.size(); j++)
					{
						if (leftList.get(i).id == rightList.get(j).id)
						{
							if (leftList.get(i).depth <= rightList.get(j).id)
								sameList.add(leftList.get(i));
							else
								sameList.add(rightList.get(i));
						}
					}
				}
			}
    	}
		int min = Integer.MAX_VALUE;
		ABObject roof = null;
		for (int i = 0; i < sameList.size(); i++)
		{
			ABObject x = sameList.get(i);
			if (x.depth <= min)
			{
				roof = x;
				min = x.depth;
			}
		}
		return roof;
	}
	
	private static List<ABObject> GetSupporteeShel (List<ABObject> left, ABObject roof)
	{
		List<ABObject> temp = new ArrayList<ABObject>();
		ABObject leftRoof = null;
		for (int i = 0; i < left.size(); i++)
		{
			if (left.get(i).id == roof.id)
			{
				leftRoof = left.get(i);
				break;
			}
		}
		if (leftRoof == null)
		{
			leftRoof = roof;
		}
		for (int i = 0; i < left.size(); i++)
		{
			if (left.get(i).depth < leftRoof.depth)
			{
				temp.add(left.get(i));				
			}
		}
		return temp;
	}
	
	private static List<ABObject> GetSupporteeShel (HashMap<ABObject, List<ABObject>> supportee, ABObject roof)
	{
		List<ABObject> temp = new ArrayList<ABObject>();
		for (ABObject ab : supportee.keySet())
		{
			temp.addAll(GetSupporteeShel(supportee.get(ab), roof));
		}
		return temp;
	}
	
	private static List<ABObject> GetNeighbour (ABObject obs, List<ABObject> objects, boolean left)
	{
		List<ABObject> neighbour = new ArrayList<ABObject>();
		
		// Get List of closest objects from right and left
		for (int i = 0; i < objects.size(); i++)
		{
			ERA[] rels = RectangleAlgebra.GetERA(objects.get(i), obs);
			//System.out.println(obs.id + " " + objects.get(i).id + " : " + rels[0] + " , " + rels[1]);
			if (StabilityRules.CheckRule(rels[0], 321) && StabilityRules.CheckRule(rels[1], 322) && left)
			{
				//System.out.println(obs.id + " " + objects.get(i).id + " : " + rels[0] + " , " + rels[1]);
				if (objects.get(i).getMaxX() >= (obs.getMinX() - obs.height))
				{
					if (!neighbour.contains(objects.get(i)))
						neighbour.add(objects.get(i));
				}
			}
			else if (StabilityRules.CheckRule(rels[0], 323) && StabilityRules.CheckRule(rels[1], 322) && !left)
			{
				//System.out.println(obs.id + " " + objects.get(i).id + " : " + rels[0] + " , " + rels[1]);
				if (objects.get(i).getMinX() <= (obs.getMaxX() + obs.height))
				{
					if (!neighbour.contains(objects.get(i)))
						neighbour.add(objects.get(i));
				}
			}
		}
		
		// Get closest objects of each side
		/*if (left)
			GetClosestObjects(neighbour, true);
		else
			GetClosestObjects(neighbour, false);*/
		
		return neighbour;
	}
	
	public static List<ABObject> Hit (ABObject bird, ABObject obs, List<ABObject> objects, List<Point> trajs)
	{
		// List of affected object
		List<ABObject> affectedList = new ArrayList<ABObject>();
		affectedList.add(obs);
		
		// Regular Rectangle
		double ratio = obs.height / obs.width;
				
		// Red bird - lowest damage
		if (bird.getType() == ABType.RedBird)
		{
			// Falling
			if (ratio >= HIT_RATIO_THRESHOLD)
			{		
				List<ABObject> temp =  ABUtil.getSupportees(obs, objects, 1);
				List<ABObject> temp1 = ABUtil.getAllSupportees(temp, objects);
				affectedList.addAll(temp);
				affectedList.addAll(temp1);
				temp =  ABUtil.getSupporters(obs, objects, 1);
				temp1 = ABUtil.getAllSupporter(temp, objects);
				affectedList.addAll(temp);
				affectedList.addAll(temp1);
				affectedList.addAll(GetNeighbour(obs, objects, false));
			}
			else
			{
				List<ABObject> temp =  ABUtil.getSupporters(obs, objects, 1);
				List<ABObject> temp1 = ABUtil.getAllSupporter(temp, objects);
				affectedList.addAll(temp);
				affectedList.addAll(temp1);
			}
		}
		
		else if (bird.getType() == ABType.YellowBird)
		{
			affectedList.add(obs);
			for (int i = 0; i < trajs.size(); i++)
			{
				Point traj = trajs.get(i);
				if (affectedList.size() <= 3)
				{
					for (int j = 0; j < objects.size(); j++)
					{
						ABObject x = objects.get(j);
						if (x.contains(traj) && !affectedList.contains(x))
							affectedList.add(x);
							
					}
				}
				else
					break;
			}
			List<ABObject> temp =  ABUtil.getSupportees(obs, objects, 1);
			List<ABObject> temp1 = ABUtil.getAllSupportees(temp, objects);
			affectedList.addAll(temp);
			affectedList.addAll(temp1);
		}
		
		for (int i = 0; i < affectedList.size(); i++)
		{
			//System.out.println(affectedList.get(i).id);
		}	
		return affectedList;
	}
	
	public static void CheckDuplicate (List<ABObject> objects)
	{
		HashSet hs = new HashSet();
		hs.addAll(objects);
		objects.clear();
		objects.addAll(hs);	
	}
	
	public static List<ABObject> SupporteeToppling (ABObject supportee, ABObject obs, List<ABObject> objects)
	{
		// List of affected object
		List<ABObject> affectedList = new ArrayList<ABObject>();
				
		// ratio
		double ratio = obs.height / obs.width;
		
		List<ABObject> temp =  ABUtil.getSupporters(obs, objects, 1);
		List<ABObject> temp1 = ABUtil.getAllSupporter(temp, objects);
		affectedList.addAll(temp);
		affectedList.addAll(temp1);
		
		// If not stable, add neighbors
		if (ratio >= TOPPLING_RATIO_THRESHOLD)
		{
			affectedList.addAll(GetNeighbour(obs, objects, false));
		}	
		
		return affectedList;
	}
	
	public static List<ABObject> SupporterToppling (ABObject supporter, ABObject obs, List<ABObject> objects, List<ABObject> hills) throws FileNotFoundException
	{
		// List of affected object
		List<ABObject> affectedList = new ArrayList<ABObject>();
		
		RectangleAlgebra.TranslateToRA(objects);
		RectangleAlgebra.ExtractContact();
		RectangleAlgebra.ExtractDimentsion();
		RectangleAlgebra.CheckStability(objects, hills);
		
		affectedList.add(supporter);
		
		// If not stable, add supportees and neighbours to affected list
		if (!RectangleAlgebra.StabilityDictionary.get(obs))
		{
			List<ABObject> temp =  ABUtil.getSupportees(obs, objects, 1);
			List<ABObject> temp1 = ABUtil.getAllSupportees(temp, objects);
			affectedList.addAll(temp);
			affectedList.addAll(temp1);
			affectedList.addAll(GetNeighbour(obs, objects, false));
		}
		
		return affectedList;
	}
	
	public static double EvaluationShot (ABObject bird, ABObject obs, List<Point> trajs, List<ABObject> objects, List<ABObject> hills) throws FileNotFoundException
	{
		// List of affected object
		List<ABObject> affectedList = new ArrayList<ABObject>();
		
		// Score
		double scores = 0.0;
		
		// Get the list of objects affected by target objects
		List<ABObject> temp = Hit(bird, obs, objects, trajs);
		affectedList.addAll(temp);
		
		// Get the list of objects that can be affected in the list
//		for (int i = 0; i < temp.size(); i++)
//		{
//			ABObject x = temp.get(i);
//			List<ABObject> supporterList = ABUtil.getSupporters(x, objects);
//			List<ABObject> supporteeList = ABUtil.getSupportees(x, objects);
//			boolean support = false;
//			ABObject y = null;
//			
//			for (int j = 0; j < supporterList.size(); j++)
//			{
//				if (affectedList.contains(supporterList.get(j)))
//				{
//					support = true;
//					y = supporterList.get(j);
//					break;
//				}
//			}
//			
//			for (int j = 0; j < supporteeList.size(); j++)
//			{
//				if (affectedList.contains(supporteeList.get(j)))
//				{
//					y = supporteeList.get(j);
//					break;
//				}
//			}
//			
//			if (support)
//			{
//				List<ABObject> lst = SupporterToppling(y, x, objects, hills);
//				for (int j = 0; j < lst.size(); j++)
//				{
//					System.out.println("Supporter Toppling of " + x.id + " : " + lst.get(j).id);
//					if (!affectedList.contains(lst.get(j)))
//					{						
//						affectedList.add(lst.get(j));
//					}
//				}
//			}
//			else
//			{
//				List<ABObject> lst = SupporteeToppling(y, obs, objects);
//				for (int j = 0; j < lst.size(); j++)
//				{
//					System.out.println("Supportee Toppling of " + y.id + " : " + lst.get(j).id);
//					if (!affectedList.contains(lst.get(j)))
//					{					
//						affectedList.add(lst.get(j));
//					}
//				}
//			}
//		}
		CheckDuplicate (affectedList);
		System.out.print("Affected objects: ");
		for (int i = 0; i < affectedList.size(); i++)
		{
			System.out.print(affectedList.get(i).id + " ");
			if (affectedList.get(i).getType() != ABType.Pig && affectedList.get(i).getType() != ABType.Hill)
			{
				scores += 1;
			}
			else
				scores += 10;
		}
		destroyedList = affectedList;
		System.out.println();
		System.out.println("Score: " + scores + "\n");
		return scores;
	}
	
	
}
