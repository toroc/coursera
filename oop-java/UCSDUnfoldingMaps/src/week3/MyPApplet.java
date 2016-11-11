package week3;
import processing.core.*;
import processing.core.PApplet;


public class MyPApplet extends PApplet {
	private String URL = "http://1.bp.blogspot.com/-fvEGBwa_N5g/UD7qvlOfNBI/AAAAAAAAEQA/421P5hPI45U/s1600/Miami-Beach.jpg";
	private PImage backgroundImg;
	
	public void setup()
	{
		size(200, 200);
		backgroundImg = loadImage(URL, "jpg");
		
	}
	
	public void draw()
	{
		backgroundImg.resize(0, height);
		image(backgroundImg,0,0);
		fill(255, 209, 0);
		ellipse(width/4, height/5, width/5, height/5);
		
	}
}
