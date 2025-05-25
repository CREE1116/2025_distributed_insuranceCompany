package main.Employee;

public class Employee {

	private String employeeID;
	private EmployeeType employeeType;
	DBConnection dbConnection;

	public enum EmployeeType {
		Sales, UnderWriter, ProductManagement, LossAdjuster
	}

	public Employee(int numOfEmployees, EmployeeType employeeType) {
		this.employeeID = Integer.toString(numOfEmployees+1);
		this.employeeType = employeeType;
		dbConnection = new DBConnection();
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public EmployeeType getEmployeeType() {
		return employeeType;
	}

}