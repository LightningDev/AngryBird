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
        EQUAL("eq"),
        MOST_OVELAP_MOST("mom"),
        INVERSE_MOST_OVERLAP_MOST("momi"),
        LESS_OVERLAP_LESS("lol"),
        INVERSE_LESS_OVERLAP_LESS("loli"),
        MOST_OVERLAP_LESS("mol"),
        INVERSE_MOST_OVERLAP_LESS("moli"),
        LESS_OVERLAP_MOST("lom"),
        INVERSE_LESS_OVERLAP_MOST("lomi"),
        START_COVER_MOST("ms"),
        INVERSE_START_COVER_MOST("msi"),
        START_COVER_LESS("ls"),
        INVERSE_START_COVER_LESS("lsi"),
        DURING_LEFT("ld"),
        INVERSE_DURING_LEFT("ldi"),
        DURING_RIGHT("rd"),
        INVERSE_DURING_RIGHT("rdi"),
        DURING_MIDPERPENDICULAR("cd"),
        INVERSE_DURING_MIDPERPENDICULAR("cdi"),
        FINISH_COVER_MOST("mf"),
        INVERSE_FINISH_COVER_MOST("mfi"),
        FINISH_COVER_LESS("lf"),
        INVERSE_FINISH_COVER_LESS("lfi");
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
