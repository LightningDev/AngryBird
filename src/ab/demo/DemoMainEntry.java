package ab.demo;

import java.io.IOException;

import org.ini4j.InvalidFileFormatException;

import ab.vision.ShowSeg;

public class DemoMainEntry 
{
	public static void main(String args[]) throws InvalidFileFormatException, IOException
	{
		DemoAgent da = new DemoAgent ();
		da.Run();
	}
}
