package week6;
import java.util.*;
/**
 * Created by carol on 11/16/2016.
 */
public class MyBuiltInSortingTest {

    public static void main(String [] args)
    {
        Random random = new Random();
        List<Integer> numsToSort = new ArrayList<>();

        /*Load the array with random numbers*/
        for (int i = 0; i < 20; i++){
            numsToSort.add(random.nextInt(100));
        }

        /*Use Built in Sort*/
        Collections.sort(numsToSort);
        System.out.println("Array after built in sort: " + numsToSort.toString());
    }

}
