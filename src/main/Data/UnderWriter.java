package main;

import main.Data.Contract;
import main.Employee.Employee;

/**
 * @author �ڼֹ�
 * @version 1.0
 * @created 20-5-2025 ���� 3:19:59
 */
public class UnderWriter extends Employee {

	public Contract m_Contract;

	public UnderWriter(){
    super();
  }
	/**
	 * 
	 * @param CustomerID
	 * @param ContractID
	 */
	public boolean underwrite(String CustomerID, String ContractID){
		return false;
	}
}//end UnderWriter