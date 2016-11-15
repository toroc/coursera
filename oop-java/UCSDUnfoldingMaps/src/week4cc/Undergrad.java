package week4cc;

/**
 * Created by carol on 11/14/2016.
 */
public class Undergrad extends Student {

    public void method2(){
        System.out.print("Undergrad 2");
    }

    public static void main(String [] args)
    {
        Person u = new Undergrad();
        u.method1();
        //Type of object is Undergrad, so at runtime java wil look for this method in the undergrad class
        //Since undergrad does not have method1 java will look in Student class
        //Calls method1 in Student
        //Inside student's method1
        //Student 1 gets printed and super class method 1 is called
        //Inside super class method 1:
        //Person 1 gets printed and returns to student's method1
        //Back in Student's method1, method 2 is called & since Undergrad has a method 2 this one gets called
        // Undergrad 2 is printed

    }

}
