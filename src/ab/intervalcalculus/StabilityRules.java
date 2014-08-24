package ab.intervalcalculus;

import ab.intervalcalculus.IntervalRelations.ERA;

public class StabilityRules 
{
	/** Rule 1.3 and 1.6
	 *  ERA (x,y) on x = {ms, mf, msi, ls, mfi, lf, cd, cdi, ld, rd, mom, momi, lomi, mol}
	 */
	public static ERA[] rule13 = {ERA.START_COVER_MOST, ERA.FINISH_COVER_MOST, ERA.INVERSE_START_COVER_MOST,
		ERA.START_COVER_LESS, ERA.INVERSE_FINISH_COVER_MOST, ERA.FINISH_COVER_LESS, ERA.DURING_MIDPERPENDICULAR,
		ERA.INVERSE_DURING_MIDPERPENDICULAR, ERA.DURING_LEFT, ERA.DURING_RIGHT, ERA.MOST_OVELAP_MOST, ERA.INVERSE_MOST_OVERLAP_MOST,
		ERA.MOST_OVERLAP_LESS, ERA.INVERSE_LESS_OVERLAP_MOST
	};
	
	/** Rule 1.2 and 1.5
	 *  ERA (x,y) on x = {momi, moli, lomi, loli, msi, lsi, ldi}
	 *  ERA (x,z) on x = {mom, mol, lom, lol, mfi, lfi, rdi}
	 */
	public static ERA[] rule121 = {ERA.INVERSE_MOST_OVERLAP_MOST, ERA.INVERSE_MOST_OVERLAP_LESS, ERA.INVERSE_LESS_OVERLAP_MOST,
		ERA.INVERSE_LESS_OVERLAP_LESS, ERA.INVERSE_START_COVER_MOST, ERA.INVERSE_START_COVER_LESS, ERA.INVERSE_DURING_LEFT
		};
	public static ERA[] rule122 = {ERA.MOST_OVELAP_MOST.DURING, ERA.MOST_OVERLAP_LESS, ERA.LESS_OVERLAP_MOST, ERA.LESS_OVERLAP_LESS,
		ERA.INVERSE_FINISH_COVER_MOST, ERA.INVERSE_FINISH_COVER_LESS, ERA.INVERSE_DURING_RIGHT
	};
	
	/** Rule 1.4 and 1.7
	 * ERA (x,y) on x = {mi}
	 * ERA (x,z) on x = {mom, mol, lom, lol, mfi, lfi, rdi}
	 * ERA (x,y) on x = {m}
	 * ERA (x,z) on x = {momi, moli, lomi, loli, msi, lsi, ldi}
	 */
	public static ERA rule141 = ERA.INVERSE_MEETS;
	public static ERA[] rule142 = {ERA.MOST_OVELAP_MOST, ERA.MOST_OVERLAP_LESS, ERA.LESS_OVERLAP_MOST, ERA.LESS_OVERLAP_LESS
		, ERA.INVERSE_FINISH_COVER_MOST, ERA.INVERSE_FINISH_COVER_LESS, ERA.INVERSE_DURING_RIGHT
	};
	public static ERA rule143 = ERA.MEETS;
	public static ERA[] rule144 = {ERA.INVERSE_MOST_OVERLAP_MOST, ERA.INVERSE_MOST_OVERLAP_LESS, ERA.INVERSE_LESS_OVERLAP_MOST,
		ERA.INVERSE_LESS_OVERLAP_LESS, ERA.INVERSE_START_COVER_MOST, ERA.INVERSE_START_COVER_LESS, ERA.INVERSE_DURING_LEFT
	};
	
	/**
	 * Rule Check Support 3.1
	 * RA (x,y) on x = {d, di, o, oi, s, si, f, fi, eq}
	 * ERA (x,y) on x = {ld, rd, cd, ldi, rdi, cdi, mom, lol, mol, lom, momi, loli, moli, lomi, ms, ls, msi, lsi, mf, lf, mfi, lfi, eq}
	 */
	public static ERA[] rule31 = {ERA.DURING_LEFT, ERA.DURING_RIGHT, ERA.DURING_MIDPERPENDICULAR, ERA.INVERSE_DURING_LEFT, ERA.INVERSE_DURING_RIGHT,
		ERA.INVERSE_DURING_MIDPERPENDICULAR, ERA.MOST_OVELAP_MOST, ERA.LESS_OVERLAP_LESS, ERA.MOST_OVERLAP_LESS, ERA.LESS_OVERLAP_MOST,
		ERA.INVERSE_MOST_OVERLAP_MOST, ERA.INVERSE_LESS_OVERLAP_LESS, ERA.INVERSE_MOST_OVERLAP_LESS, ERA.INVERSE_LESS_OVERLAP_MOST,
		ERA.START_COVER_MOST, ERA.START_COVER_LESS, ERA.INVERSE_START_COVER_MOST, ERA.INVERSE_START_COVER_LESS, ERA.FINISH_COVER_MOST,
		ERA.FINISH_COVER_LESS, ERA.INVERSE_FINISH_COVER_MOST, ERA.INVERSE_FINISH_COVER_LESS, ERA.EQUAL
		};
	
	/** Rule 1.8
	 * ERA (x,y) on x = {m}
	 * ERA (x,z) on x = {mi}
	 */
	public static ERA rule181 = ERA.MEETS;
	public static ERA rule182 = ERA.INVERSE_MEETS;
	
	public static boolean CheckRule(ERA relation, int rule)
	{
		boolean check = false;
		ERA[] temp = {};
		if (rule == 141 || rule == 182 || rule == 171)
		{
			if (relation == rule141)
			{
				check = true;
				return check;
			}
		}
		else if (rule == 143 || rule == 181 || rule == 173)
		{
			if (relation == rule143)
			{
				check = true;
				return check;
			}
		}
		else if (rule == 13 || rule == 16)
			temp = rule13;
		else if (rule == 121 || rule == 151)
			temp = rule121;
		else if (rule == 122 || rule == 152)
			temp = rule122;
		else if (rule == 142 || rule == 172)
			temp = rule142;
		else if (rule == 144 || rule == 174)
			temp = rule144;
		else if (rule == 31)
			temp = rule31;
		for (int i = 0; i < temp.length; i++)
		{
			if (relation == temp[i])
			{
				check = true;
				break;
			}
		}
		return check;
	}
}
