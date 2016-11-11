
package guimodule;
import processing.core.PApplet;
import processing.core.PImage;

public class MyPApplet extends PApplet {
	
	PImage img;
	
	public void setup()
	{
		//setup code for class
		size(400,400);
		//set canvas color
		background(160,160,160);
		//set pen color
		stroke(0);
		img = loadImage("http://1.bp.blogspot.com/-fvEGBwa_N5g/UD7qvlOfNBI/AAAAAAAAEQA/421P5hPI45U/s1600/Miami-Beach.jpg");
		//resize image
		img.resize(0, height);
		image(img, 0, 0);
	}
	
	public void draw()
	{
		//populate color for sun
		int[] color = sunColorSec(second());
		//set the color
		fill(color[0],color[1],color[2]);
		//draw the sun
		ellipse(width/4, height/5, width/4,height/5);
	}
	
	public int[] sunColorSec(float seconds)
	{
		int[] rgb = new int[3];
		//scale brightness of yellow based on seconds
		float diffFrom30 = Math.abs(30-seconds);
		
		float ratio = diffFrom30/30;
		//cast floats to int
		rgb[0] = (int)(255*ratio);
		rgb[1] = (int)(255*ratio);
		rgb[2] = 0;
		
		return rgb;
		
		
	}

}
