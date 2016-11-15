package week4;

public class Student extends Person{
	private int studentID;
	
	
	//default constructor
	public Student(){
		//No reason to call superclass argument constructor, instead use same class constructor that is available
		super("Student");
//		this("Student");
		this.setName("Student");
		System.out.print("#2 ");		
	}
	public Student(String n, int id){
//		super();
//		this.name = n; // not allowed to say this.name since name member variable belongs to parent class Person
		//Since there is no setter, call the super class which takes an argument of name
		super(n);
		this.studentID = id;
		System.out.print("Student constructor with name and id");
		
	}
	public int getSID(){
		return studentID;
	}
	public String toString(){
//		return this.getSID() + ": " + this.getName();
		return this.getSID() + ": " + super.toString(); //super keyword indicates calling method from super class
	}
	public static void main(String [] args){
		//Student s = new Student();
		
		//Constructors called
		// Student(string n), which calls the super calls and prints out #1
		// Then prints out #3, since the Student() constructor calls Student(n)
		// and finally Student() constructor prints out #2
		// #1, #3, 2
		
		//Call no argument student constructor
		// which immediately calls the 1 arg constructor
		// which calls the Super class constructor
		//Super class constructor prints #1 and then returns to the
		// 1 arg student class constructor which prints #3
		// which then returns to the no arg student constructor and prints #2
		
		
		//Concept Challenge 2
		// Call the no arg student constructor which calls the setName method in the super class
		// The setName method in Person class
		// ERROR: COMPILE ERROR
		//Person does not have a no -arg constructor
		//Person Class has no default (no-argument) constructor, student constructor doesn't explicitly call
		// super with an argument, java will attempt to call Person's non-existent no-arg constructor
		
		//Person s = new Student("Cara", 1234);
		//System.out.println(s);
		
		Person p[] = new Person[3];
		p[0] = new Person("Tim");
		p[1] = new Student("Cara", 1234);
		p[2] = new Faculty("Mia", "ABCD");
		
		for(int i=0; i < p.length; i++){
			System.out.println(p[i]);
		}

	}
}
