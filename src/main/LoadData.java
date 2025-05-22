package main;

import main.Data.Customer;
import main.Employee.Employee.EmployeeType;
import main.Employee.LossAdjuster;
import main.Employee.ProductManagement;
import main.Employee.Sales;
import main.Employee.UnderWriter;
import main.Enum.Sex;
import main.List.ContractList;
import main.List.CustomerListImpl;
import main.List.EmployeeListImpl;
import main.List.InsuranceProductList;

public class LoadData {
	private CustomerListImpl customerList;
	private EmployeeListImpl employeeList;
	private ContractList contractList;
	private InsuranceProductList insuranceProductList;
	
	public LoadData(CustomerListImpl customerList,EmployeeListImpl employeeList, ContractList contractList) {
		this.customerList = customerList;
		this.employeeList = employeeList;
		this.contractList = contractList;
	}
	public void loadCustomerData() {
		Customer customer1 = new Customer.Builder().accountNumber("333377645328").address("서울특별시 서대문구 북가좌동").age(23)
				.customerID(Integer.toString(0)).job("대학생").name("장소윤").rrn("60221340").sex(Sex.FEMALE)
				.phoneNumber("01077645328").build();
		Customer customer2 = new Customer.Builder().accountNumber("333346965328").address("서울특별시 서대문구 남가좌동").age(23)
				.customerID(Integer.toString(1)).job("대학생").name("박솔민").rrn("60221333").sex(Sex.FEMALE)
				.phoneNumber("01010001000").build();
		Customer customer3 = new Customer.Builder().accountNumber("333324555328").address("서울특별시 서대문구 북가좌동").age(25)
				.customerID(Integer.toString(2)).job("대학생").name("이종민").rrn("60211111").sex(Sex.FEMALE)
				.phoneNumber("01020002000").build();
		
		customerList.insert(customer1);
		customerList.insert(customer2);
		customerList.insert(customer3);
	}

	public void loadEmployeeData() {
		for (int i = 0; i < 3; i++) { // add temp three Sales
			int numOfEmployees = employeeList.employees.size();
			Sales sales = new Sales(numOfEmployees, EmployeeType.Sales, customerList);
			employeeList.insert(sales);
		}
		for (int i = 0; i < 3; i++) { // add temp three Sales
			int numOfEmployees = employeeList.employees.size();
			ProductManagement productManagement = new ProductManagement(numOfEmployees, EmployeeType.ProductManagement, customerList);
			employeeList.insert(productManagement);
		}
		for (int i = 0; i < 3; i++) { // add temp three lossAdjuster
			int numOfEmployees = employeeList.employees.size();
			LossAdjuster lossAdjuster = new LossAdjuster(numOfEmployees, EmployeeType.LossAdjuster);
			employeeList.insert(lossAdjuster);
		}
		for (int i = 0; i < 3; i++) { // add temp three lossAdjuster
			int numOfEmployees = employeeList.employees.size();
			UnderWriter uw = new UnderWriter(numOfEmployees, EmployeeType.UnderWriter, customerList,contractList);
			employeeList.insert(uw);
		}

	}
	public void loadInsuranceProductData() {
		
	}
	public void loadContractData() {
		
	}
}
