package guimodule;

import processing.core.PApplet;

public class MyDisplay extends PApplet {
	
	
	public void setup()
	{
		size(400, 400);
		background(160, 160, 160);
	}
	
	public void draw()
	{

		//	draw the head
		fill(255, 255, 0);
		ellipse(200, 200, 390, 390);
		// draw left eye
		fill(0, 0, 0);
		ellipse(140,125,50,100);
		// draw right eye
		fill(0, 0, 0);
		ellipse(260,125,50,100);
		// draw smile
		arc(200,280,220,160,0,PI);
	}

}
