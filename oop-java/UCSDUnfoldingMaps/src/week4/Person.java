package week4;

public class Person extends Object{
	private String name;
	//1 arg constructor
	public Person(String n){
		super();
//		this.name = n;
		//super();//Error super has to be the first line!
		
		this.name = n;
//		System.out.print("#1 ");
	}
	
	
	public void setName(String n)
	{
		this.name = n;
	}
	
	public String getName(){return name;}
	
	public String toString(){
		return this.getName();
		
	}
	
	public static void main (String [] args){
		Person p = new Person("Tim");
		System.out.println(p.toString());
		//Can also be called as
		//System.out.println(p);
	}
}
