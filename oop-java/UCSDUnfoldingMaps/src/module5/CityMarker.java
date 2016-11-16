package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
// TODO: Change SimplePointMarker to CommonMarker as the very first thing you do 
// in module 5 (i.e. CityMarker extends CommonMarker).  It will cause an error.
// That's what's expected. //Common Marker overrieds draw() and calls drawMarker()?
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	
	/**
	 * Implementation of method to draw marker on the map.
	 */
	//Fixed from draw to drawMarker
	public void drawMarker(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		
		// TODO: Implement this method
		/*Get City Name, Country, and Population*/
		String title = getCity() + ", " + getCountry() + " ";
		String pop = "Pop: " + getPopulation() + " (unit)";
		int offset = 40;
		int x_offset = TRI_SIZE * 2;
		int y_offset = 15;
		float max_text = Math.max(pg.textWidth(title), pg.textWidth(pop));
		pg.pushStyle();

		/*Change Text Sizes and Color*/
		pg.fill(255,255,255);
		pg.textSize(12);
		/*Create a rectangle on screen*/
		pg.rectMode(PConstants.CORNER); //modify location to corners
		/*Make rectangle width the length of max text plus the y-offset and triangle size*/
		/*Height of rectangle can be the total offset of 40*/
		pg.rect(x, y -TRI_SIZE - offset, max_text + y_offset+x_offset, offset);
		pg.fill(0,0,0);
		/*Align to the top left of rect*/
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		/*Add the text*/
		pg.text(title, x + x_offset, y - TRI_SIZE - offset+2);
		pg.text(pop, x+x_offset, y-TRI_SIZE-(offset/2));
		pg.popStyle();
	}
	
	
	
	/* Local getters for some city properties.  
	 */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
}
