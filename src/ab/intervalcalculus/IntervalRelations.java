/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ab.intervalcalculus;

/**
 *
 * @author Nhat
 */
public class IntervalRelations
{
    public static enum ERA
    {
        TAKES_PLACE_BEFORE("b"),
        INVERSE_TAKES_PLACE_BEFORE("a"),
        MEETS("m"),
        INVERSE_MEETS("mi"),
        OVERLAPS("o"),
        INVERSE_OVERLAPS("oi"),
        STARTS("s"),
        INVERSE_STARTS("si"),
        DURING("d"),
        INVERSE_DURING("di"),
        FINISHES("f"),
        INVERSE_FINISHES("fi"),
        NULL("nl"),
        EQUAL("eq");
        private String value;

        private ERA(String value) 
        {
                this.value = value;
        }
        
        public String getCode ()
        {
        	return this.value;
        }
    }
}
