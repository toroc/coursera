package week3;

import java.applet.*;
import java.awt.*;
import java.util.Map;
//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.*;

//Parsing library
import parsing.ParseFeed;
public class LifeExpectancy extends PApplet{
	UnfoldingMap map;
	//Store Data
	//Map: Keys -> Values .... countryID -> lifeExp
	Map<String, Float> lifeExpByCountry;
	//Store feature per Country
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	public void setup(){
		//size of canvas
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Microsoft.RoadProvider());
		//Allow map interaction
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//Load life expectancy data
		lifeExpByCountry = loadLifeExpectancyFromCSV("LifeExpectancyWorldBankModule3.csv");
				
		//Load country shapes and add them as markers
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		//Add markers to map
		map.addMarkers(countryMarkers);
		
		//helper method to shade countries
		shadeCountries();
	}
	
	public void draw(){
		map.draw();
		
	}
	//Helper method to load data into MAP
	private Map<String, Float> loadLifeExpectancyFromCSV(String fileName){
		//Create new map
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();
		String [] rows = loadStrings(fileName);
		
		//Iterate over rows
		for (String row: rows){
			//Split the comma separated file
			String [] columns = row.split(",");
			if (columns.length == 6 && !columns[5].equals("..")){
				float value = Float.parseFloat(columns[5]);
				lifeExpMap.put(columns[4], value);
			}
		}
		
		//Return the filled map
		return lifeExpMap;
	}
	
	//Helper method to shade countries
	private void shadeCountries(){
		
		//iterate over markers
		//for each marker change the color
		for (Marker marker: countryMarkers){
			String countryID = marker.getId();
			
			if (lifeExpByCountry.containsKey(countryID)){
				float lifeExp = lifeExpByCountry.get(countryID);
				//take within range and map within other range of 10-255
				//cast float as integer
				int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel,100, colorLevel));
			}
			else{
				marker.setColor(color(150,150,150));
			}
			
		}
	}
}
