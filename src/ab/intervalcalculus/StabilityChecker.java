/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ab.intervalcalculus;

import ab.intervalcalculus.IntervalRelations.ERA;
import ab.vision.ABObject;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Nhat Tran The University of Queensland
 */
public class StabilityChecker {

    public static enum ContactRelation 
    {
        POINT_TO_SURFACE,
        SURFACE_TO_SURFACE,
        POINT_TO_POINT,
        NULL
    }
    
    public static enum ContactDimension
    {
    	HORIZONTAL,
    	VERTICAL,
    	NULL
    }
}
