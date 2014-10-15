package ab.QualitativePhysics;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import ab.vision.ABObject;
import ab.vision.ABType;

public class TestFunctions {

	public static void main(String[] args) 
	{
		Rectangle rec = new Rectangle (5, 5, 9, 4);
		Rectangle _rec = new Rectangle (10, 10, 9, 4);
		Rectangle __rec = new Rectangle (15, 15, 9, 4);
		ABObject ob = new ABObject(rec, ABType.Wood);
		ABObject _ob = new ABObject(_rec, ABType.Wood);
		ABObject __ob = new ABObject(__rec, ABType.Wood);
		List<Point2D> edge = QualitativeRep.UpperEdge(ob);
		List<ABObject> obs = new ArrayList<ABObject>();
		obs.add(ob);
		obs.add(_ob);
		obs.add(__ob);
		List<ABObject> above = new ArrayList<ABObject>();
		QualitativeRep.AboveRelation(ob, obs, above);
		
		for (int i = 0; i < above.size(); i++)
		{
			System.out.println("ABove ob: " + above.get(i).getLocation());
		}
		
		System.out.println(QualitativeRep.TouchRelation(_ob, __ob));
		System.out.println(QualitativeRep.RightPoint(ob, _ob));
		System.out.println(QualitativeRep.LeftPoint(ob, _ob));
		System.out.println(QualitativeRep.CenterPoint(ob, _ob));
		System.out.println(QualitativeRep.AboveRelation(__ob, _ob));
	}

}
