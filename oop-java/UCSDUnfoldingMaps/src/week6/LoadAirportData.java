package week6;

import java.io.*;
import java.util.ArrayList;
import java.util.*;

/**
 * Created by carol on 11/16/2016.
 */
public class LoadAirportData {

    public static ArrayList<Airport> readData(String fileName){
        ArrayList<Airport> airports = new ArrayList<Airport>();
        /*Open the file*/
        try (FileInputStream fis = new FileInputStream(fileName)){
            //do work;
            /*Use a buffered reader for performance
            * http://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java*/
            try(BufferedReader br = new BufferedReader(new InputStreamReader(fis))){
                /*Do work*/

                String line;
                while((line = br.readLine()) != null){
                    /*Split the comma separated line into an array*/
                    String [] data = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    String name = removeQuotes(data[1]);
                    String city = removeQuotes(data[2]); //data[2] is "city" with the quotes
                    String country = removeQuotes(data[3]);
                    String code = removeQuotes(data[4]);
                    /*Create Airport*/
                    Airport a = new Airport(id, name, city, country, code);
                    System.out.println(a.getCode());
                    /*Append airport to the list*/
                    airports.add(a);
                }
                return airports;
            }catch(IOException e){

            }
        } catch(IOException e){

        }
        return airports;
    }
    private static String removeQuotes(String string){
        return string.substring(1, string.length()-1);
    }
    public static void main(String [] args)
    {
        String dataFile = "data/airports.dat";
        ArrayList<Airport> airports = readData(dataFile);
        System.out.println("Running linear search for Seattle Tacoma Intl airport code");
        String code = findAirportCode("Seattle Tacoma Intl", airports);
        System.out.println("Seattle Tacoma Intl code: " + code);

        /*Sort the airports with builtin mergesort*/
        Collections.sort(airports);


    }
    /*Linear Run Time Algorithm to find the Airport Code*/
    public static String findAirportCode(String cityName, ArrayList<Airport> airports)
    {
        int index = 0;
        while (index < airports.size()){
            Airport a = airports.get(index);
            /*Checj if City name equals city at current index*/
//            if(airports.get(index).getCity() == cityName){
            if(cityName.equals(a.getCity())){
                /*Using .equals() instead of == c will compare the string
                * representation of the two objects*/
                System.out.println("Found the city");
                System.out.println(a.getCode());
                return a.getCode();
            }
            /*increment counter*/
            index += 1;
        }
        /*Airport not found*/
        return null;

    }

    /*LogN Run Time Algorithm to find Airport Code*/
    public static String findAirportCodeBinarySearch(String cityName, ArrayList<Airport> airports)
    {
        int low_index = 0;
        int high_index = airports.size() -1;


        while (low_index <= high_index){
//            int mid_index = (low_index + high_index) / 2;
            int mid_index = low_index + ((high_index - low_index)/2);
            /*Compare to the middle element*/
            Airport a = airports.get(mid_index);
            /*Use Compare To*/
            int compare = cityName.compareTo(a.getCity());
            /*Do the strings match?*/
            if (compare == 0){
                /*Found the city*/
                return a.getCode();
            }
            else if(compare < 0){
                /*Returns -1 if calling object is less than param*/
                /*city is alphabetically less than city at mid index*/
                high_index = mid_index - 1;
                /*Look to the left half of the array*/
            }
            else{
                /*Look to the right half of the array*/
                low_index = mid_index + 1;
            }

        }
        /*Not found*/
        return null;
    }
    /*Selection sort for an integer array*/
    public static void selectionSort(int [] vals){
        int minIndex;

        for (int i = 0; i < vals.length - 1; i++){
            minIndex = i;
            /*Look for a smaller element to the left of element at i*/
            for (int j = i + 1; j < vals.length; j++)
            {
                if (vals[j] < vals[minIndex]){
                    /*Found a smaller element*/
                    minIndex = j;
                }
            }
            /*Swap the variables*/
            swap(vals, minIndex, i);
        }
    }

    private static void swap(int[] vals, int minIndex, int i) {
        int temp = vals[i];
        vals[i] = vals[minIndex];
        vals[minIndex] = temp;
    }

    /*Mystery Sort is Actually Insertion Sort*/
    public static void mysterySort(int [] vals){

        int currInd;
        for (int pos = 1; pos < vals.length; pos ++)
        {
            currInd = pos;
            while (currInd > 0 && vals[currInd] < vals[currInd-1]){
                /*Swap*/
                swap(vals, currInd, currInd - 1);
                currInd = currInd - 1;
            }
        }
    }
    public static void SelectionSort(ArrayList<Airport> airports){

        for (int i = 0; i < airports.size()-2; i++)
        {
            /*Find Smalles Eleement*/
            Airport cur = airports.get(i);
            Airport smallest;
            for (int j = i + 1; j < airports.size(); j++){
                Airport next = airports.get(j);
                int compare = cur.getCountry().compareTo(next.getCountry());
                if (compare > 0){

                }
            }
        }
    }
}
