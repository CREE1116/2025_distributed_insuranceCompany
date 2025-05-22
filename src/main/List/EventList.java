package main.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.DBConnection;
import main.Data.Compensation;
import main.Data.Contract;
import main.Data.Evaluation;
import main.Data.Event;

/**
 * @author �ڼֹ�
 * @version 1.0
 * @created 11-5-2025 ���� 11:25:09
 */
public interface EventList {


	public boolean delete(int eventID);

	public boolean insert(Event event);

	//search
	public List<Event> search(String key, String value);

	//update
	public boolean update(Event event);

	public boolean update(Evaluation evaluation);

	public boolean update(Compensation compensation);

	public List<Event> findAll();
}