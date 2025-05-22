package main.List;
import java.util.List;
import java.util.Optional;
import main.Data.Contract;

public interface ContractList {

	/**
	 * 
	 * @param contractID
	 */
	public int delete(int contractID);

	/**
	 * 
	 * @param contract
	 */
	public int insert(Contract contract);

	/**
	 * 
	 * @param contractID
	 */
	public Optional<Contract> search(int contractID);

	/**
	 * 
	 * @param contract
	 */
	public int update(Contract contract);

	public List<Contract> findAll();
}