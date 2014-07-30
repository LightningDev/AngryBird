/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ab.intervalcalculus;

import ab.vision.ABObject;
import ab.vision.real.shape.Body;
import ab.intervalcalculus.IntervalRelations.ERA;
import ab.intervalcalculus.StabilityChecker.ContactDimension;
import ab.intervalcalculus.StabilityChecker.ContactRelation;

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
                    double object1_c_x = object1.getCenterX();
                    double object1_c_y = object1.getCenterY();
                    double object1_s_x = object1.getMinX();
                    double object1_s_y = object1.getMinY();
                    double object1_e_x = object1.getMaxX();
                    double object1_e_y = object1.getMaxY();
                    
                    // Centre, start and end point of each coordinate of Object 2
                    double object2_c_x = object2.getCenterX();
                    double object2_c_y = object2.getCenterY();
                    double object2_s_x = object2.getMinX();
                    double object2_s_y = object2.getMinY();
                    double object2_e_x = object2.getMaxX();
                    double object2_e_y = object2.getMaxY();
                    
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
        else if(object1_start < object2_start && object2_start < object1_end && object1_end < object2_end)
        {
            rel = ERA.OVERLAPS;
        }
        else if(object2_start < object1_start && object1_start < object2_end && object2_end < object1_end)
        {
            rel = ERA.INVERSE_OVERLAPS;
        }
        
        // Relation STARTS
        else if(object1_start == object2_start && object1_end < object2_end && object2_start < object1_end)
        {
            rel = ERA.STARTS;
        }
        else if(object1_start == object2_start && object2_end < object1_end && object1_start < object2_end)
        {
            rel = ERA.INVERSE_STARTS;
        }
        
        // Relation DURING
        else if(object1_start > object2_start && object1_end < object2_end && object1_start < object2_end && object1_end > object2_start)
        {
            rel = ERA.DURING;
        }
        else if(object2_start > object1_start && object2_end < object1_end && object2_start < object1_end && object2_end > object1_start)
        {
            rel = ERA.INVERSE_DURING;
        }
        
        // Relation FINISHES
        else if(object1_end == object2_end && object2_start < object1_start && object1_start < object2_end)
        {
            rel = ERA.FINISHES;
        }
        else if(object1_end == object2_end && object1_start < object2_start && object2_start < object1_end)
        {
            rel = ERA.INVERSE_FINISHES;
        }
        
        // Relation EQUAL
        else if(object1_start == object2_start && object1_end == object2_end)
        {
            rel = ERA.EQUAL;
        }
        return rel;
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
}
