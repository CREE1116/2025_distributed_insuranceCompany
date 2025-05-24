package main.List;
import java.util.List;
import java.util.Optional;
import main.Data.Contract;

public interface ContractList {

	/**
	 * 
	 * @param contractID
	 */
	public String delete(String contractID);

	/**
	 * 
	 * @param contract
	 */
	public String insert(Contract contract);

	/**
	 * 
	 * @param contractID
	 */
	public Optional<Contract> search(String contractID);

	/**
	 * 
	 * @param contract
	 */
	public String update(Contract contract);

	public List<Contract> findAll();
}