package week6;

/**
 * Created by carol on 11/16/2016.
 */
public class Airport implements Comparable<Airport> {
    private int id;
    private String name;
    private String city;
    private String country;
    private String code3;

    public Airport(int id, String name, String cityName, String countryName, String code){
        this.id = id;
        this.name = name;
        this.city = cityName;
        this.country = countryName;
        this.code3 = code;
    }
    /*Getters for data*/
    public int getId(){return  this.id;}
    public String getName(){return this.name;}
    public String getCity(){
        return this.city;
    }
    public String getCountry(){
        return this.country;
    }
    public String getCode(){
        return this.code3;
    }
    public int compareTo(Airport other)
    {
        return (this.getCity()).compareTo(other.getCity());
    }

}
