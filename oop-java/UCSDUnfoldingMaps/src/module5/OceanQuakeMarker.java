package module5;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

import java.util.List;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Carol D. Toro
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	

	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {

		pg.rect(x-radius, y-radius, 2*radius, 2*radius);

	}
}
