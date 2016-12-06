package module6;

import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PGraphics;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Carol D. Toro
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.

	/*Track c02 emissions by country*/
	Map<String, Float> co2ByCountry;

	private String[] rangeValues;
	private float[] rangeLimits;
	private Color [] rangeColors;
	private int rangeCount;
	private int[] rangeStrSizes;

	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = true;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	

	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Microsoft.RoadProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";
		
		// Uncomment this line to take the quiz
		//earthquakesURL = "quiz2.atom";
		/*Load Co2 Emission data by country*/

		/*Load Co2 Emission Data*/
		co2ByCountry = loadCo2FromCSV("emissions.csv");

		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();

		/*Shade countries by emmission of CO2*/
		rangeCount = 5;
		loadIntervals(rangeCount);
		rangeColors = generateColors(rangeCount);
		shadeCountries();
		/*Add country markers*/
		map.addMarkers(countryMarkers);
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
		


	    
	    /*Test sort and print*/
	    sortAndPrint(5);
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		addEmissionKey();
		
	}
	
	
	// TODO: Add the method:
	//   private void sortAndPrint(int numToPrint)
	// and then call that method from setUp
	private void sortAndPrint(int numToPrint){

		/*Create new array from eq markers lists*/
		EarthquakeMarker[] eqArray = new EarthquakeMarker[quakeMarkers.size()];
		eqArray = quakeMarkers.toArray(eqArray);

		/*Sort in reverse order of magnitude*/
		Arrays.sort(eqArray, Collections.reverseOrder());

		/*Ensure that earthquakes printed never more than # of markers*/
		int toPrint = quakeMarkers.size();
		if (numToPrint < toPrint)
		{
			toPrint = numToPrint;
		}

		for(int i = 0; i < toPrint; i++){
			System.out.println(eqArray[i]);
		}

	}
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
		//loop();
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkEarthquakesForClick();
			if (lastClicked == null) {
				checkCitiesForClick();
			}
		}
	}
	/*Helper method to check if emissions was clicked off*/
	private void checkEmForClick()
	{
		
		if(lastClicked != null) return;
		
		for(Marker marker: countryMarkers)
		{
			if(!marker.isHidden() && marker.isInside(map, mouseX, mouseY)){
				lastClicked = (CommonMarker)marker;
				//Hide all other countries
				for(Marker chide: countryMarkers)
				{
					if(chide != lastClicked)
					{
						chide.setHidden(true);
					}
				}
				return;
			}
		}
	}
	
	// Helper method that will check if a city marker was clicked on
	// and respond appropriately
	private void checkCitiesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker marker : cityMarkers) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				// Hide all the other earthquakes and hide
				for (Marker mhide : cityMarkers) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : quakeMarkers) {
					EarthquakeMarker quakeMarker = (EarthquakeMarker)mhide;
					if (quakeMarker.getDistanceTo(marker.getLocation()) 
							> quakeMarker.threatCircle()) {
						quakeMarker.setHidden(true);
					}
				}
				return;
			}
		}		
	}
	
	// Helper method that will check if an earthquake marker was clicked on
	// and respond appropriately
	private void checkEarthquakesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker m : quakeMarkers) {
			EarthquakeMarker marker = (EarthquakeMarker)m;
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				// Hide all the other earthquakes and hide
				for (Marker mhide : quakeMarkers) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : cityMarkers) {
					if (mhide.getDistanceTo(marker.getLocation()) 
							> marker.threatCircle()) {
						mhide.setHidden(true);
					}
				}
				return;
			}
		}
	}
	
	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Land Quake", xbase+50, ybase+70);
		text("Ocean Quake", xbase+50, ybase+90);
		text("Size ~ Magnitude", xbase+25, ybase+110);
		
		fill(255, 255, 255);
		ellipse(xbase+35, 
				ybase+70, 
				10, 
				10);
		rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);
		
		
	}

	/*Helper Method to add the keys pertaining to C02 Emmisions*/
	private void addEmissionKey()
	{
		/*Create rectangle to store Emission key*/
		fill(224,224,224);
		rect(20,350,150,250);

		/*Add Header*/
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("CO2 Emissions",40, 380);
		int yCoord = 400;
		
		/*Add Key for missing data*/
		addKeys(yCoord, rangeColors[rangeCount].getRGB(),rangeValues[rangeCount].length(), rangeValues[rangeCount]);

		/*Add Key Items*/
		yCoord += 20;
		for (int i=0; i < this.rangeCount; i++)
		{
			addKeys(yCoord, rangeColors[i].getRGB(), rangeStrSizes[i], rangeValues[i]);
			yCoord += 20;
		}
		
		text("Metric Tons per Capita", 30, yCoord+10);
		
	}

	private void addKeys(int yCoord, int keyColor, int keySize, String keyName)
	{
		/*Draw Square with color*/
		fill(keyColor);
		rect(40,yCoord,12,12);

		/*Add key Text*/
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text(keyName, 60, yCoord+2);
	}
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	// You will want to loop through the country markers or country features
	// (either will work) and then for each country, loop through
	// the quakes to count how many occurred in that country.
	// Recall that the country markers have a "name" property, 
	// And LandQuakeMarkers have a "country" property set.
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}


	/*Helper method to load data into MAP */
	private Map<String, Float> loadCo2FromCSV(String fileName)
	{
		Map<String, Float> co2EmissionMap = new HashMap<String, Float>();
		String[] rows = loadStrings(fileName);

		/*Iterate over rows*/
		for (String row: rows){
			/*Remove the commas*/
			String [] columns = row.split(",");
			if ( columns.length == 58 && !columns[57].equals("")){
				float value = Float.parseFloat(columns[57]);
				//System.out.println("Emission is :" + value);
				co2EmissionMap.put(columns[1], (float)value);
				//System.out.println(columns[1] + " " + value);
			}

		}

		return co2EmissionMap;

	}

	private void loadIntervals(int n){
		float min = Collections.min(co2ByCountry.values());
		float max = Collections.max(co2ByCountry.values());

		float totalLength = max - min;
		float subrange_lenth = totalLength / n;
		float [] rangeLimit =new float[n];
		String [] rangeIntervals = new String[n+1];

		float curr_start = min;
		for (int i=0; i < n; i++){
			//System.out.println("Range: [" + curr_start +", " + (curr_start+subrange_lenth) + "]");
			String rangeVal = "[" + round(curr_start, 2) + ", " + round((curr_start + subrange_lenth), 2) + "]";
			rangeIntervals[i] = rangeVal;
			curr_start += subrange_lenth;
			rangeLimit[i] = curr_start;
		}
		/*Add n/a for missing data*/
		rangeIntervals[n] = "n/a";
		rangeValues = rangeIntervals;
		rangeLimits = rangeLimit;
		rangeStrSizes = loadValSizes();
	}
	/*Helper function to round float to decimal places*/
	private static float round(float d, int decimalPlace)
	{
		BigDecimal bd = new BigDecimal((Float.toString(d)));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	/*Helper Function to load Interval Values */
	private int[] loadValSizes()
	{
		int[] sizes = new int[this.rangeCount];
		for (int i=0; i < this.rangeCount; i++)
		{
			int size = rangeValues[i].length();
			sizes[i] = size;
		}

		return sizes;
	}
	
	/*Generate n colors with the n+1 being gray*/
	private Color[] generateColors(int n)
	{
		double alpha = 0.4;
		Color[] colors = new Color[n+1];
		for (int i=0; i < n; i++)
		{
			Color c2 = Color.getHSBColor((float)i / (float) n, 0.85f, 1.0f);
			/*Make the color transparent*/
			colors[i]= new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), 50);

		}
		
		/*Add gray color*/
		colors[n] = new Color(0,0,0,50);

		return colors;
	}
	/*Helper method to print the colors*/
	private void printColors()
	{
		for( Color c: rangeColors){
			System.out.println(c);
		}
	}
	
	/*Helper method to shade countries based off emission values*/
	private void shadeCountries(){
		/*Iterate over the countryMarkers*/
		float min = Collections.min(co2ByCountry.values());
		float max = Collections.max(co2ByCountry.values());
		System.out.println("Min: " + min + " Max:" + max);
		for (Marker marker: countryMarkers){
			String countryID = marker.getId();
			if (co2ByCountry.containsKey(countryID))
			{
				//System.out.println(co2ByCountry.get(countryID));
				float emission = co2ByCountry.get(countryID);
				System.out.println("Emission for country is: " + emission);
				Color col = getColor(emission);
				//Color col3 = new Color(col.getRed(), col.getGreen(), col.getBlue(),50);
				marker.setColor(col.getRGB());
			}
			else{
				//Set as gray
				Color col = rangeColors[rangeCount];
				marker.setColor(col.getRGB());
			}
		}
		printColors();
	}
	
	/*Helper method to return the color based on country's emission*/
	private Color getColor(float emission){

		for (int i=0; i < rangeCount; i++)
		{
			if (emission <= rangeLimits[i])
			{
				System.out.println(rangeColors[i].getRGB());
				return rangeColors[i];
			}
		}

		return rangeColors[rangeCount];
	}
}
