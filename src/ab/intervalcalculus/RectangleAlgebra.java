/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ab.intervalcalculus;

import ab.utils.ABUtil;
import ab.vision.ABObject;
import ab.vision.real.shape.*;
import ab.intervalcalculus.IntervalRelations.ERA;
import ab.intervalcalculus.StabilityChecker.ContactDimension;
import ab.intervalcalculus.StabilityChecker.ContactRelation;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nhat Tran
 * The University of Queensland
 */
public class RectangleAlgebra 
{ 
	// Relation Algebra (RA)
    private static HashMap<ABObject[], ERA[]> RADictionary;
    
    // Contact Relation (CR)
    private static HashMap<ABObject[], ContactRelation> CRDictionary;
    
    // Contact Dimension (CD)
    private static HashMap<ABObject[], ContactDimension> CDDictionary;
    
    // Stable Objects
    private static HashMap<ABObject, Boolean> StabilityDictionary;


    /**
     * Iterate through the list and find the relation between each object
     * @param listObject: All block in game
     */
    public static HashMap<ABObject[], ERA[]> TranslateToRA(List<ABObject> listObject)
    {
    	RADictionary = new HashMap<ABObject[], ERA[]>();
        //InitalizeHashMap(dictionary, listObject);
        for(int i = 0; i < listObject.size(); i++)
        {
            ABObject object1 = listObject.get(i);
            for(int j = 0; j < listObject.size(); j++)
            {
                if(j != i)
                {
                    ABObject object2 = listObject.get(j);
                    
                    // Centre, start and end point of each coordinate of Object 1
                    double[] points = GetObjectPoints(object1);
                    double object1_c_x = points[0];
                    double object1_c_y = points[1];
                    double object1_s_x = points[2];
                    double object1_s_y = points[3];
                    double object1_e_x = points[4];
                    double object1_e_y = points[5];
                    
                    // Centre, start and end point of each coordinate of Object 2
                    points = GetObjectPoints(object2);
                    double object2_c_x = points[0];
                    double object2_c_y = points[1];
                    double object2_s_x = points[2];
                    double object2_s_y = points[3];
                    double object2_e_x = points[4];
                    double object2_e_y = points[5];
                    
                    // Check the relation between 2 rectangles
                    ERA rel1 = ERA.NULL;
                    ERA rel2 = ERA.NULL;
                    
                    // Set Relation (native java run)
                    rel1 = GetRA (object1_s_x, object2_s_x, object1_c_x, object2_c_x, object1_e_x, object2_e_x);
                    rel2 = GetRA (object1_s_y, object2_s_y, object1_c_y, object2_c_y, object1_e_y, object2_e_y);               
                    
                    // Set relation for RA(object1, object 2)
                    RADictionary.put(new ABObject[]{object1,object2}, new ERA[]{rel1,rel2});               
                }
            }
        }
        return RADictionary;
    }
    
    /**
     * Get the angular and regular polygon position
     * @return List of points
     */
    public static double[] GetObjectPoints (ABObject ob)
    {
    	double[] points = new double[6];
    	double angle = Math.toDegrees(ob.angle);
		points[0] = ob.getCenterX();
		points[1] = ob.getCenterY();
		
		points[2] = ob.getMinX();
		points[3] = ob.getMinY();
		points[4] = ob.getMaxX();
		points[5] = ob.getMaxY();
    	
    	return points;
    }
    
    /**
     * Iterate the Relational Algebra to check contact relation
     * @return Hash map contains CR
     */ 
    public static HashMap<ABObject[], ContactRelation> ExtractContact()
    {
    	CRDictionary = new HashMap<ABObject[], ContactRelation>();
    	for (Map.Entry<ABObject[], ERA[]> entry : RADictionary.entrySet()) 
    	{
    		// Get the key pair
    	    ABObject[] key = entry.getKey();
    	    ERA[] value = entry.getValue();
    	    ContactRelation contact = CheckContact(value);
    	    if (ABUtil.isSupport(key[0], key[1]) || ABUtil.isSupport(key[1], key[0]))
    	    	contact = ContactRelation.SURFACE_TO_SURFACE;
    	    CRDictionary.put(key, contact);
    	}
    	return CRDictionary;
    }
    
    /**
     * Check Contact Relation between 2 Object
     * @param rel: Algebra Relations between 2 objects
     * @return contact: Contact Relation
     */
    private static ContactRelation CheckContact(ERA[] rel)
    {
    	ContactRelation contact = ContactRelation.NULL;
    	ERA rel1 = rel[0];
    	ERA rel2 = rel[1];
    	if (rel1 != ERA.TAKES_PLACE_BEFORE && rel1 != ERA.INVERSE_TAKES_PLACE_BEFORE
    			&& rel2 != ERA.TAKES_PLACE_BEFORE && rel2 != ERA.INVERSE_TAKES_PLACE_BEFORE)
    	{
    		contact = ContactRelation.SURFACE_TO_SURFACE;
    	}
    	return contact;
    }
    
    /**
     * Check the dimension of contact between 2 objects
     * @return: Contact Dimension
     */
    public static HashMap<ABObject[], ContactDimension> ExtractDimentsion()
    {
    	CDDictionary = new HashMap<ABObject[], ContactDimension>();
    	for (Map.Entry<ABObject[], ERA[]> entry : RADictionary.entrySet())
    	{
    		ABObject[] key = entry.getKey();
			ContactDimension cd = ContactDimension.NULL;
    		if (CRDictionary.get(key) != ContactRelation.NULL)
    		{
    			ERA[] value = entry.getValue();
    			if (value[0] == ERA.MEETS || value[0] == ERA.INVERSE_MEETS)
    			{
    				cd = ContactDimension.HORIZONTAL;
    			}
    			else
    			{
    				cd = ContactDimension.VERTICAL;
    			}
    			CDDictionary.put(key, cd);
    		}
    	}
    	return CDDictionary;
    }
    
    public static void CheckStability (List<ABObject> objects, List<ABObject> hills)
    {
    	StabilityDictionary = new HashMap<ABObject, Boolean>();
    	
    	// Find all object lies on the ground - Rule 1.1
    	for (int i = 0; i < objects.size(); i++)
    	{
    		double angle = Math.toDegrees(objects.get(i).angle);
    		if (angle == 0 || angle == 90 || angle == 180 || angle == 270 || angle == 360)
    		{
    			if (ABUtil.getSupporters(objects.get(i), objects).size() == 0 || CheckHill(hills, objects.get(i)))
    			{
    				System.out.println("Stand on the ground " + i);
    				StabilityDictionary.put(objects.get(i), true);
    			}
    			else
    			{
    				StabilityDictionary.put(objects.get(i), false);
    			}
    		}
    	}
    	
    	while (CheckAllStable())
    	{
    		// Regular Rectangle
	    	for (Map.Entry<ABObject[], ContactDimension> entry : CDDictionary.entrySet())
	    	{
	    		ABObject[] key = entry.getKey();
	    		ContactDimension contact = entry.getValue();
	    		
	    		// Rule 1.3
	    		if (contact == ContactDimension.VERTICAL && StabilityDictionary.get(key[0]) && !StabilityDictionary.get(key[1]))
	    		{
	    			ABObject[] newKey = {key[1], key[0]};
	    			ERA relation = GetRADictionary(newKey)[0];
	    			if (StabilityRules.CheckRule(relation, 13))
	    			{
	    				StabilityDictionary.put(key[1], true);
	    				System.out.println(key[1].id + " is stable by " + key[0].id);
	    			}
	    		}
	    		
	    		// Rule 1.2 and 1.4
	    		for (Map.Entry<ABObject[], ContactDimension> _entry : CDDictionary.entrySet())
	    		{
	    			ABObject[] _key = _entry.getKey();
	    			ContactDimension _contact = _entry.getValue();
	    			if (key != _key)
	    			{
	    				// Rule 1.2
	    				if (key[1] == _key[1] && contact == _contact && contact == ContactDimension.VERTICAL && StabilityDictionary.get(_key[0])
	    						&& StabilityDictionary.get(key[0]) && !StabilityDictionary.get(_key[1])
	    						&& !StabilityDictionary.get(key[1]))
	    				{
	    					ABObject[] newKey = {key[1], key[0]};
	    	    			ERA relation = GetRADictionary(newKey)[0];
	    	    			ABObject[] _newKey = {_key[1], _key[0]};
	    	    			ERA _relation = GetRADictionary(_newKey)[0];
	    	    			if (StabilityRules.CheckRule(relation, 121) && StabilityRules.CheckRule(_relation, 122))
	    	    			{
	    	    				StabilityDictionary.put(key[1], true);
	    	    				System.out.println(key[1].id + " is stable by " + key[0].id + "," + _key[0].id);
	    	    			}
	    				}
	    				
	    				// Rule 1.4
	    				if (key[0] == _key[0] && contact == _contact && contact == ContactDimension.HORIZONTAL && StabilityDictionary.get(key[1])
	    						&& StabilityDictionary.get(_key[1]) && !StabilityDictionary.get(key[0])
	    						&& !StabilityDictionary.get(_key[0]))
	    				{
	    					ERA relation = GetRADictionary(key)[0];
	    					ERA _relation = GetRADictionary(_key)[0];
	    					if ( (StabilityRules.CheckRule(relation, 141) && StabilityRules.CheckRule(_relation, 142))
	    						|| (StabilityRules.CheckRule(relation, 143) && StabilityRules.CheckRule(_relation, 144)) )
	    					{
	    						StabilityDictionary.put(key[0], true);
	    						System.out.println(key[0].id + " is stable by " + key[1].id + "," + _key[1].id);
	    					}
	    				}
	    			}
	    		}
	    	}
    	}
    	
    	// Angular Rectangle
    	
    }
    
    private static boolean CheckAllStable ()
    {
    	for (Map.Entry<ABObject, Boolean> _entry : StabilityDictionary.entrySet())
    	{
    		if (!_entry.getValue())
    		{
    			//System.out.println(_entry.getKey().id);
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Calculate and return Relational Algebra
     * @param object1_start: start point of object 1
     * @param object2_start: start point of object 2
     * @param object1_centre: centre point of object 1
     * @param object2_centre: centre point of object2
     * @param object1_end: end point of object 1
     * @param object2_end: end point of object 2
     * @return
     */
    private static ERA GetRA (double object1_start, double object2_start, double object1_centre, double object2_centre, double object1_end, double object2_end)
    {
    	ERA rel = ERA.NULL;
    	// Relation Takes Place Before
    	if(object1_start < object2_start && object1_end < object2_start)
        {
            rel = ERA.TAKES_PLACE_BEFORE;
        }
        else if(object2_start < object1_start && object2_end < object1_start)
        {
            rel = ERA.INVERSE_TAKES_PLACE_BEFORE;
        }
        
        // Relation MEETS
        else if(object1_end == object2_start && object1_start < object2_start)
        {
            rel = ERA.MEETS;
        }
        else if (object2_end == object1_start && object2_start < object1_start)
        {
            rel = ERA.INVERSE_MEETS;
        }
        
        // Relation OVERLAPS
        else if(object1_start < object2_start && object1_centre >= object2_start && object1_end >= object2_centre && object1_end < object2_end)
        {
        	rel = ERA.MOST_OVELAP_MOST;
        }
        else if(object2_start < object1_start && object2_centre >= object1_start && object2_end >= object1_centre && object2_end < object1_end)
        {
        	rel = ERA.INVERSE_MOST_OVERLAP_MOST;
        }
    	
        else if(object1_start < object2_start && object1_centre >= object2_start && object1_end < object2_end)
        {
        	rel = ERA.MOST_OVERLAP_LESS;
        }
        else if(object2_start < object1_start && object2_centre >= object1_start && object2_end < object1_end)
        {
        	rel = ERA.INVERSE_MOST_OVERLAP_LESS;
        }
    	
        else if(object1_centre < object2_start && object1_end >= object2_start && object1_end < object2_end)
        {
        	rel = ERA.LESS_OVERLAP_MOST;
        }
        else if(object2_centre < object1_start && object2_end >= object1_start && object2_end < object1_end)
        {
        	rel = ERA.INVERSE_LESS_OVERLAP_MOST;
        }
    	
        else if(object1_centre < object2_start && object1_end > object2_start && object1_end < object2_centre)
        {
        	rel = ERA.LESS_OVERLAP_LESS;
        }
        else if(object2_centre < object1_start && object2_end > object1_start && object2_end < object1_centre)
        {
        	rel = ERA.INVERSE_LESS_OVERLAP_LESS;
        }
        
        // Relation STARTS
        else if(object1_start == object2_start && object1_end >= object2_centre)
        {
        	rel = ERA.START_COVER_MOST;
        }
        else if(object2_start == object1_start && object2_end >= object1_centre)
        {
        	rel = ERA.INVERSE_START_COVER_MOST;
        }
    	
        else if(object1_start == object2_start && object1_end > object2_start && object1_end < object2_centre)
        {
        	rel = ERA.START_COVER_LESS;
        }
        else if(object2_start == object1_start && object2_end > object1_start && object2_end < object1_centre)
        {
        	rel = ERA.INVERSE_START_COVER_LESS;
        }
        
        // Relation DURING
        else if(object1_start > object2_start && object1_end <= object2_centre)
        {
        	rel = ERA.DURING_LEFT;
        }
        else if(object2_start > object1_start && object2_end <= object1_centre)
        {
        	rel = ERA.INVERSE_DURING_LEFT;
        }
    	
        else if(object1_start > object2_start && object1_start < object2_centre && object1_end > object2_centre && object1_end < object2_end)
        {
        	rel = ERA.DURING_MIDPERPENDICULAR;
        }
        else if(object2_start > object1_start && object2_start < object1_centre && object2_end > object1_centre && object2_end < object1_end)
        {
        	rel = ERA.INVERSE_DURING_MIDPERPENDICULAR;
        }
    	
        else if(object1_start >= object2_centre && object1_end < object2_end)
        {
        	rel = ERA.DURING_RIGHT;
        }
        else if(object2_start >= object1_centre && object2_end < object1_end)
        {
        	rel = ERA.INVERSE_DURING_RIGHT;
        }
        
        // Relation FINISHES
        else if(object1_start > object2_start && object1_start <= object2_centre && object1_end == object2_end)
        {
        	rel = ERA.FINISH_COVER_MOST;
        }
    	else if(object2_start > object1_start && object2_start <= object1_centre && object2_end == object1_end)
        {
        	rel = ERA.INVERSE_FINISH_COVER_MOST;
        }
    	
    	else if(object1_start > object2_centre && object1_start < object2_end && object1_end == object2_end)
        {
        	rel = ERA.FINISH_COVER_LESS;
        }
    	else if(object2_start > object1_centre && object2_start < object1_end && object2_end == object1_end)
        {
        	rel = ERA.INVERSE_FINISH_COVER_LESS;
        }
        
        // Relation EQUAL
        else if(object1_start == object2_start && object1_end == object2_end)
        {
            rel = ERA.EQUAL;
        }
    	  	
        return rel;
    }
    
    private static boolean CheckHill (List<ABObject> hills, ABObject ob)
    {
    	for (int i = 0; i < hills.size(); i++)
    	{
    		if (ABUtil.isSupport(ob, hills.get(i)))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public static ERA[] GetRADictionary (ABObject[] objects)
    {
    	for (ABObject[] ab: RADictionary.keySet())
    	{
    		if(ab[0].id == objects[0].id && ab[1].id == objects[1].id)
    			return RADictionary.get(ab);
    	}
    	return null;
    }
    
    public static ContactRelation GetCRDictionary (ABObject[] objects)
    {
    	for (ABObject[] ab: CRDictionary.keySet())
    	{
    		if(ab[0] == objects[0] && ab[1] == objects[1])
    		{
    			return CRDictionary.get(ab);
    		}
    	}
    	return null;
    }
    
    public static ContactDimension GetCDDictionary (ABObject[] objects)
    {
    	for (ABObject[] ab: CDDictionary.keySet())
    	{
    		if(ab[0] == objects[0] && ab[1] == objects[1])
    		{
    			return CDDictionary.get(ab);
    		}
    	}
    	return null;
    }
    
   /**
    * Print the list of Relational Algebra
    */
    public static void PrintRA()
    {
        for (ABObject[] ab: RADictionary.keySet())
        {
            int id1 = ab[0].id;
            int id2 = ab[1].id;
            ERA rel1 = RADictionary.get(ab)[0];
            ERA rel2 = RADictionary.get(ab)[1];
            System.out.println("RA(" + id1 + ", " + id2 + ")" + " = " + "(" + rel1 + ", " + rel2 + ")");
        } 
    }
    
    /**
     * Print the list of Contact Relation
     */
    public static void PrintCR()
    {
    	for (ABObject[] ab: CRDictionary.keySet())
    	{
    		int id1 = ab[0].id;
            int id2 = ab[1].id;
            ContactRelation contact = CRDictionary.get(ab);
            System.out.println("CR(" + id1 + ", " + id2 + ")" + " = " + contact);
    	}
    }
    
    public static void PrintCD ()
    {
    	for (ABObject[] ab: CDDictionary.keySet())
    	{
    		int id1 = ab[0].id;
            int id2 = ab[1].id;
            ContactDimension contact = CDDictionary.get(ab);
            System.out.println("CD(" + id1 + ", " + id2 + ")" + " = " + contact);
    	}
    }
}
