package main.Employee;

import main.DAO.DAO;
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
  public boolean underwrite(String ContractID , boolean approve){
    try {
      DAO.executeQuery("UPDATE contract SET state = ? WHERE contract_id = ?",
          approve? 1 : -1, ContractID);
      return true;
    }catch (Exception e) {
      return false;
    }
  }
}//end UnderWriter