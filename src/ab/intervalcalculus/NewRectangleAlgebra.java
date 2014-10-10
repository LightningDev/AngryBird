package ab.intervalcalculus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ab.intervalcalculus.IntervalRelations.ERA;
import ab.intervalcalculus.StabilityChecker.ContactDimension;
import ab.intervalcalculus.StabilityChecker.ContactRelation;
import ab.utils.ABUtil;
import ab.vision.ABObject;
import ab.vision.ABShape;
import ab.vision.real.shape.Rect;

public class NewRectangleAlgebra 
{
	 /**
     * Get Relation between 2 objects
     * @param o1
     * @param o2
     * @return
     */
    public static ERA[] GetERA (ABObject o1, ABObject o2)
    {
    	// Centre, start and end point of each coordinate of Object 1
        double object1_c_x = o1.getCenterX();
        double object1_c_y = o1.getCenterY();
        double object1_s_x = o1.getMinX();
        double object1_s_y = o1.getMinY();
        double object1_e_x = o1.getMaxX();
        double object1_e_y = o1.getMaxY();
        
        // Centre, start and end point of each coordinate of Object 2
        double object2_c_x = o2.getCenterX();
        double object2_c_y = o2.getCenterY();
        double object2_s_x = o2.getMinX();
        double object2_s_y = o2.getMinY();
        double object2_e_x = o2.getMaxX();
        double object2_e_y = o2.getMaxY();
        
        // Check the relation between 2 rectangles
        ERA rel1 = ERA.NULL;
        ERA rel2 = ERA.NULL;
        
        // Set Relation
        rel1 = CheckERA (object1_s_x, object2_s_x, object1_c_x, object2_c_x, object1_e_x, object2_e_x);
        rel2 = CheckERA (object1_s_y, object2_s_y, object1_c_y, object2_c_y, object1_e_y, object2_e_y);
        
        return new ERA[] {rel1, rel2};
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
    private static ERA CheckERA (double object1_start, double object2_start, double object1_centre, double object2_centre, double object1_end, double object2_end)
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
    
    /**
     * Iterate through the list and find the relation between each object
     * @param listObject: All block in game
     */
    public static HashMap<ABObject[], ERA[]> TranslateToRA(List<ABObject> listObject)
    {
    	HashMap<ABObject[], ERA[]> RADictionary = new HashMap<ABObject[], ERA[]>();
        //InitalizeHashMap(dictionary, listObject);
        for(int i = 0; i < listObject.size(); i++)
        {
            ABObject object1 = listObject.get(i);
            for(int j = 0; j < listObject.size(); j++)
            {
                if(j != i)
                {
                    ABObject object2 = listObject.get(j);
                    
                    ERA[] rels = GetERA(object1, object2);
                    
                    // Set relation for RA(object1, object 2)
                    RADictionary.put(new ABObject[]{object1,object2}, rels);               
                }
            }
        }
        return RADictionary;
    }
    
    /**
     * Iterate the Relational Algebra to check contact relation
     * @return Hash map contains CR
     * @throws FileNotFoundException 
     */ 
    public static HashMap<ABObject[], ContactRelation> ExtractContact() throws FileNotFoundException
    {
    	HashMap<ABObject[], ContactRelation> CRDictionary = new HashMap<ABObject[], ContactRelation>();
    	HashMap<ABObject[], ERA[]> RADictionary = new HashMap<ABObject[], ERA[]>();
    	for (Map.Entry<ABObject[], ERA[]> entry : RADictionary.entrySet()) 
    	{
    		// Get the key pair
    	    ABObject[] key = entry.getKey();
    	    ERA[] value = entry.getValue();
    	    ContactRelation contact = null;
    	    
    	    // Object 1 and 2 are both regular rectangle
    	    // and they have to support each other
    	    // Contact relation = Surface to surface
    	    if (!key[0].IsAngular() && !key[1].IsAngular() && ABUtil.isSupport(key[1], key[0]))
    	    	contact = ContactRelation.SURFACE_TO_SURFACE;
    	    
    	    // Either object 1 or 2 is angular
    	    // and they must not takes place before
    	    // Contact relation = Point to surface | Point to point
    	    else if ((key[0].IsAngular() || key[1].IsAngular())  && (value[0] != ERA.TAKES_PLACE_BEFORE && value[0] != ERA.INVERSE_TAKES_PLACE_BEFORE
        			&& value[1] != ERA.TAKES_PLACE_BEFORE && value[1] != ERA.INVERSE_TAKES_PLACE_BEFORE) )
        	{
        		contact = CheckContactAngular(key[0], key[1]);		
        	}
    	    CRDictionary.put(key, contact);
    	}
    	return CRDictionary;
    }
    
    /**
     * Check contact between angular rectangle
     * @param obs1
     * @param obs2
     * @return Contact relation between them
     */
    private static ContactRelation CheckContactAngular(ABObject obs1, ABObject obs2)
    {
		if (obs1.shape != ABShape.Circle && obs2.shape != ABShape.Circle)
		{
	    	Rect ob1 = (Rect)obs1;
	    	Rect ob2 = (Rect)obs2;	    	
	    	int[] ob1_x = null;
	    	int[] ob2_x = null;
	    	int[] ob1_y = null;
	    	int[] ob2_y = null;
	    	if (ob1.IsAngular() && ob2.IsAngular())
	    	{
	    		ob1_x = ob1.p.xpoints;
		    	ob2_x = ob2.p.xpoints;
		    	ob1_y = ob1.p.ypoints;
		    	ob2_y = ob2.p.ypoints;
	    	}
	    	else if (ob1.IsAngular() && !ob2.IsAngular())
	    	{
	    		ob1_x = ob1.p.xpoints;
	    		ob1_y = ob1.p.ypoints;
		    	ob2_x = ob2.GetBoundX();
		    	ob2_y = ob2.GetBoundY();
	    	}
	    	else if (!ob1.IsAngular() && ob2.IsAngular())
	    	{
	    		ob1_x = ob1.GetBoundX();
	    		ob1_y = ob1.GetBoundY();
		    	ob2_x = ob2.p.xpoints;
		    	ob2_y = ob2.p.ypoints;
	    	}
	    	List<double[]> ob1LineEquations = new ArrayList<double[]>();
	    	List<double[]> ob2LineEquations = new ArrayList<double[]>();
	    	

			
	    	// Check corner-corner contact
	    	// Check corner-surface contact
	    	AddLineEquations(ob1_x, ob1_y, ob1LineEquations);
	    	AddLineEquations(ob2_x, ob2_y, ob2LineEquations);
	    	
            ContactRelation cr1 = CheckLineEquations(ob2_x, ob2_y, ob1LineEquations) ;
            ContactRelation cr2 = CheckLineEquations(ob1_x, ob1_y, ob2LineEquations);
            if (cr1 != ContactRelation.NULL)
            	return cr1;
            if (cr2 != ContactRelation.NULL)
            	return cr2;

	    	int size = 0;
	    	if (ob1_x.length > ob2_x.length)
	    		size = ob1_x.length;
	    	else
	    		size = ob2_x.length;
	    	for (int i = 0; i < size; i++)
	    	{
	    		if (i < ob1_x.length)
	    		{
	    			if ( Arrays.asList(ob2_x).contains(ob1_x[i]) && Arrays.asList(ob2_y).contains(ob1_y[i]) )
	    				return ContactRelation.POINT_TO_POINT;
	    		}
	    		if (i < ob2_x.length)
	    		{
	    			if ( Arrays.asList(ob1_x).contains(ob2_x[i]) && Arrays.asList(ob1_y).contains(ob2_y[i]) )
	    				return ContactRelation.POINT_TO_POINT;
	    		}
	    	}
	    	
		}
    	else
    		return ContactRelation.SURFACE_TO_SURFACE;
    	return ContactRelation.NULL;
    }
    
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
   public static double[] LineEquation (int x1, int y1, int x2, int y2)
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
	    	return arr;
   }
   
   /**
    * Add line equation of object to a given list
    * @param x
    * @param y
    * @param list
    */ 
   public static void AddLineEquations (int[] x, int[] y, List<double[]> list)
   {
	   int lines = 0;
	   while(true)
	   {
		   int x1 = x[lines];
		   int y1 = y[lines];
		   if (lines != x.length-1)
		   {
			   int x2 = x[lines+1];
			   int y2 = y[lines+1];
			   list.add(LineEquation(x1, y1, x2, y2));
		   }
		   else
		   {
			   int x2 = x[0];
			   int y2 = y[0];

			   list.add(LineEquation(x1, y1, x2, y2));
			   break;
		   } 		
		   lines++;
	   }
   }
   
   /**
    * Check any point lie on a given line equation
    * @param x
    * @param y
    * @param lineList
    * @return Contact relation
    */
   public static ContactRelation CheckLineEquations (int[] x, int[] y, List<double[]> lineList)
   {
	   double ERROR = 2.0;
	   for (int i = 0; i < lineList.size(); i++)
	   {
		   double a = lineList.get(i)[0];
		   double b = lineList.get(i)[1];
		   double type = lineList.get(i)[2];
		   for (int j = 0; j < x.length; j++)
		   {
			   int tempX = x[j];
			   int tempY = y[j];
			   if (type == 0)
			   {
				   double ax = a * tempX;
				   double result = ax + b;
				   if (result >= tempY - ERROR && result <= tempY + ERROR)
				   {
					   if (tempX >= lineList.get(i)[3] - ERROR && tempX <= lineList.get(i)[5] +ERROR
							   && tempY >= lineList.get(i)[4] -ERROR && tempY <= lineList.get(i)[6] + ERROR)
					   {
						   //System.out.println(" at " + tempX + ", " + tempY + " on " + "y = " + a + "x + " + b);
						   return ContactRelation.POINT_TO_SURFACE;
					   }
				   }

			   }
			   else
			   {
				   double ay = a * tempY;
				   double result = ay + b;            			 
				   if (result >= tempX - ERROR && result <= tempX + ERROR)
				   {   
					   if (tempX >= lineList.get(i)[3] - ERROR && tempX <= lineList.get(i)[5] + ERROR
							   && tempY >= lineList.get(i)[4] - ERROR && tempY <= lineList.get(i)[6] + ERROR)
					   {        
						   //System.out.println(" at " + tempX + ", " + tempY + " on " + "x = " + a + "y + " + b);
						   return ContactRelation.POINT_TO_SURFACE;
					   }
				   }
			   }
		   }
	   }
	   return ContactRelation.NULL;
   }
   
   /**
    * Check the dimension of contact between 2 objects
    * @return: Contact Dimension
    */
   public static HashMap<ABObject[], ContactDimension> ExtractDimentsion()
   {
	   HashMap<ABObject[], ContactDimension> CDDictionary = new HashMap<ABObject[], ContactDimension>();
	   HashMap<ABObject[], ERA[]> RADictionary = new HashMap<ABObject[], ERA[]>();
	   HashMap<ABObject[], ContactRelation> CRDictionary = new HashMap<ABObject[], ContactRelation>();
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
}
