
/**
 * Created by carol on 11/9/2016.
 */
public class SimpleLocation {
    /*Member Variables*/
    private double latitude;
    private double longitude;

    /*Default Constructor*/
    public SimpleLocation()
    {
        this.latitude = 32.9;
        this.longitude = -117.2;
    }
    /*Overloaded constructor*/
    public SimpleLocation(double lat, double lon)
    {
        this.latitude = lat;
        this.longitude = lon;
    }
    /*Getters & Setters*/
    public double getLatitude()
    {
        return this.latitude;
    }
    public void setLatitude(double lat)
    {
        /*Ensure valid values*/
        if (lat < -180 || lat > 180) {
            System.out.println("Illegal value for latitude");
        } else {
            this.latitude = lat;
        }
    }
    public double getLongitude()
    {
        return this.longitude;
    }
    public void setLongitude(double lon)
    {
        this.longitude = lon;
    }
    public double distance(SimpleLocation other)
    {
        return getDist(this.latitude, this.longitude, other.latitude, other.longitude);

    }

    /*overloaded method*/
    public double distance(double otherLat, double otherLon)
    {
        return getDist(this.latitude, this.longitude, otherLat, otherLon);
    }
}

