package week4cc;

public class Student extends Person {
//	public Student(String name){
//		super(name);
//	}
//
//	//Overide the super class method
//	public boolean isAsleep(int hr){
//		return 2 < hr && 8 > hr;
//	}
//
//	public static void main(String [] args){
//		Person p;
//		p = new Student("Sally");
//		// object is a student and not just a person, students isAsleep method gets called at run time
//		p.status(1);
//	}
	public void method1(){
		System.out.print("Student 1");
		super.method1();
		method2();
	}
	public void method2(){
		System.out.print("Student 2");
	}
}
