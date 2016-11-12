package module3;

import java.applet.*;
import java.awt.*;
//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.*;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Carol D. Toro
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	public int [] colors;
	public int [] keySizes;
	public String [] keyNames;
	
	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.RoadProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    //load colors
	    this.colors = loadColors();
	    this.keyNames = loadKeyNames();
	    this.keySizes = loadKeySizes();
	    
	    
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
//	    if (earthquakes.size() > 0) {
//	    	PointFeature f = earthquakes.get(0);
//	    	System.out.println(f.getProperties());
//	    	Object magObj = f.getProperty("magnitude");
//	    	float mag = Float.parseFloat(magObj.toString());
//	    	// PointFeatures also have a getLocation method
//	    	System.out.println(f.getLocation());
//	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    
	    //TODO: Add code here as appropriate
	    //Add each point feature as a list of markers
	    for (PointFeature eq: earthquakes)
	    {
	    	markers.add(createMarker(eq));
	    	
	    }
	    
	    map.addMarkers(markers);
	    
	}
	// Added additional functions to make it easier to loop thru keys
	public int[] loadColors(){
		int[] colorkeys = new int[3];
		colorkeys[0] = color(51,51,255);
		colorkeys[1] = color(255,255,102);
		colorkeys[2] = color(255,0,0);
		
		return colorkeys;
	}
	
	public int[] loadKeySizes(){
		int[] sizes = new int[3];
		sizes[0] = 5;
		sizes[1] = 10;
		sizes[2] = 15;
		return sizes;
		
	}
	public String[] loadKeyNames(){
		String[] names = new String[3];
		names[0] = "Below 4.0";
		names[1] = "4.0+ Magnitude";
		names[2] = "5.0+ Magnitude";
		return names;
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{

		
		float mag =(float) feature.getProperty("magnitude");
//		System.out.println(feature.getProperty("magnitude"));
		SimplePointMarker mk = new SimplePointMarker(feature.getLocation(), feature.getProperties());
		//set color and weight
		if (mag < 4.0){
			mk.setColor(colors[0]);
			mk.setRadius(keySizes[0]);
		}
		else if(mag < 5.0){
			mk.setColor(colors[1]);
			mk.setRadius(keySizes[1]);
		}
		else{
			mk.setColor(colors[2]);
			mk.setRadius(keySizes[2]);
		}
		// finish implementing and use this method, if it helps.
		return mk;
	}

	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		//Create Rectangle to store the KEY
		fill(224,224,224);
		rect(10,50,180,300);
		
		//Add Header
		textSize(16);
		fill(0,102,153);
		text("Earthquake Key",40,80);
		
		//Add Key Items
		int yCoord = 250;
		for (int i = 0; i < 3; i++){
			addKeys(yCoord, colors[i], keySizes[i], keyNames[i]);
			yCoord -= 60;
		}

			
	
	}
	private void addKeys(int yCoord, int keyColor, int keySize, String keyName){
		
		// draw circle
		//color circle
		fill(keyColor);
		ellipse(35,yCoord,keySize,keySize);
		
		//add magnitude text
		textSize(14);
		fill(0);
		text(keyName,60,yCoord);
	}
	
}
	

