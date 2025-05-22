package main.Employee;

import main.Data.Contract;
import main.List.ContractList;
import main.List.CustomerList;

/**
 * @author �ڼֹ�
 * @version 1.0
 * @created 20-5-2025 ���� 3:19:59
 */
public class UnderWriter extends Employee {

	public CustomerList customerList;
	public ContractList contractList;

	public UnderWriter(int numOfEmployees, EmployeeType employeeType,CustomerList customerList, ContractList contractList){
    super(numOfEmployees, employeeType);
		this.contractList = contractList;
		this.customerList = customerList;
  }

	/**
	 * 
	 * @param approve
	 * @param ContractID
	 */
	public void underwrite(int ContractID , boolean approve){
		contractList.update(contractList.search(ContractID).get().receiptEvaluation(approve));
	}
}//end UnderWriter