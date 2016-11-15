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
        //Calls method1 in Student
        //Inside student's method1
        //Student 1 gets printed and super class method 1 is called
        //Inside super class method 1:
        //Person 1 gets printed and returns to student's method1
        //method 2 is called since Undergrad has a method 2 this one gets called
        // Undergrad 2 is printed

    }

}
