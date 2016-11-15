package week4;

public class Faculty extends Person{
	
	private String employeeID;
	
	public Faculty(String n, String id){
		super(n);
		this.employeeID = id;
		System.out.println("Faculty constructor with n and id args");
	}
	public String getEID()
	{
		return employeeID;
	}

	public String toString(){
		return this.employeeID + ": " + super.getName();
	}
}
