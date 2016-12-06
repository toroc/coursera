package module6;

import java.awt.*;
import java.lang.reflect.Array;
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

/**
 * Created by carol on 11/30/2016.
 */
public class CO2Map extends PApplet{
    UnfoldingMap map;
    private List<Marker> countriesList;
    /*Track c02 emissions by country*/
    private Map<String, Integer> co2ByCountry;
    private String[] rangeValues;
    private int[] rangeLimits;
    private Color [] rangeColors;
    private int rangeCount;

    // A List of country markers
    private List<Marker> countryMarkers;

    // The files containing city names and info and country names and info
    private String cityFile = "city-data.json";
    private String countryFile = "countries.geo.json";

    public void setup()
    {
        size(800, 600, OPENGL);

        /*Create new Map*/
        map = new UnfoldingMap(this, 200, 50, 650, 600, new Microsoft.RoadProvider());
        MapUtils.createDefaultEventDispatcher(this, map);

        co2ByCountry = loadCo2FromCSV("emissions.csv");

        //     STEP 1: load country features and markers
        List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
        countryMarkers = MapUtils.createSimpleMarkers(countries);
        rangeCount = 5;
        loadIntervals(rangeCount);
        rangeColors = generateColors(rangeCount);
        shadeCountries();
    }
    public void draw() {
        background(0);
        map.draw();
        /*addKey();*/

    }

    /*Helper method to load data into MAP */
    private Map<String, Integer> loadCo2FromCSV(String fileName)
    {
        Map<String, Integer> co2EmissionMap = new HashMap<String, Integer>();
        String[] rows = loadStrings(fileName);

		/*Iterate over rows*/
        for (String row: rows){
			/*Remove the commas*/
            String [] columns = row.split(",");
            if ( columns.length == 58 && !columns[57].equals("")){
                float value = Float.parseFloat(columns[57]);
                //System.out.println("Emission is :" + value);
                co2EmissionMap.put(columns[1], (int)value);
                //System.out.println(columns[1] + " " + value);
            }

        }

        return co2EmissionMap;

    }

    private void loadIntervals(int n){
        int min = Collections.min(co2ByCountry.values());
        int max = Collections.max(co2ByCountry.values());

        int totalLength = max - min;
        int subrange_lenth = totalLength / n;
        int [] rangeLimit =new int[n];
        String [] rangeIntervals = new String[n];

        int curr_start = min;
        for (int i=0; i < n; i++){
            //System.out.println("Range: [" + curr_start +", " + (curr_start+subrange_lenth) + "]");
            rangeIntervals[i] = "Range: [" + curr_start +", " + (curr_start+subrange_lenth) + "]";
            curr_start += subrange_lenth;
            rangeLimit[i] = curr_start;
        }
        rangeValues = rangeIntervals;
        rangeLimits = rangeLimit;
    }

    private Color[] generateColors(int n)
    {
        Color[] colors = new Color[n];
        for (int i=0; i < n; i++)
        {
            colors[i]= Color.getHSBColor((float)i / (float) n, 0.85f, 1.0f);
        }
        return colors;
    }

    private void shadeCountries(){
		/*Iterate over the countryMarkers*/
        int min = Collections.min(co2ByCountry.values());
        int max = Collections.max(co2ByCountry.values());
        System.out.println("Min: " + min + " Max:" + max);
        for (Marker marker: countryMarkers){
            String countryID = marker.getId();
            if (co2ByCountry.containsKey(countryID))
            {
                //System.out.println(co2ByCountry.get(countryID));
                int emission = co2ByCountry.get(countryID);
                System.out.println("Emission for country is: " + emission);
                int num = Math.abs((int)emission);
                Color col = getColor(num);
                marker.setColor(col.getRGB());
            }
            else{
                marker.setColor(color(150,150,150));
            }
        }
    }
    private Color getColor(int emission){

        for (int i=0; i < rangeCount; i++)
        {
            if (emission <= rangeLimits[i])
            {
                return rangeColors[i];
            }
        }

        return new Color(150,150,150);
    }

}
