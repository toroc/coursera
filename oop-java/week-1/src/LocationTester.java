/**
 * Created by carol on 11/9/2016.
 */
public class LocationTester {

    public static void main(String[] args)
    {
        /*Call constructer to create new objects*/
        SimpleLocation ucsd = new SimpleLocation(32.9, -117.2);
        SimpleLocation lima = new SimpleLocation(-12.0, -77.0);

        System.out.println(ucsd.distance(lima));
    }
}
