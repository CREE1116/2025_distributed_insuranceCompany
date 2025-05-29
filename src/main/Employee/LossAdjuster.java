package main.Employee;

import java.util.Objects;
import main.DAO.DAO;
import main.Data.Compensation;
import main.Data.Evaluation;
import main.Data.Event;
import main.Enum.ProcessState;
import main.List.EventList;
import main.List.EventListImpl;

/**
 * @author �ڼֹ�
 * @version 1.0
 * @created 11-5-2025 ���� 11:25:09
 */
 public class LossAdjuster extends Employee {

	private final EventList EventList;

	public LossAdjuster(int numOfEmployees, EmployeeType employeeType) {
		super(numOfEmployees, employeeType);
		this.EventList = new EventListImpl();
	}

	/**
	 * 이벤트ID받고, 심사의 상태를 변경한다.
	 *
	 * @param eventID   심사할 사건의 ID
	 * @param isReceipt 심사 결과 (true: 승인, false: 거절)
	 * @return 심사 성공 여부 (true: 성공, false: 실패 - 해당 사건 ID가 없는 경우)
	 */
	public boolean evaluateCompensation(String eventID, boolean isReceipt) {
		try {
			DAO.executeQuery("UPDATE event SET state_of_evaluation = ? WHERE event_id = ?",
					isReceipt? 1 : -1, eventID);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	/**
	 * 심사가 완료된 사고들의 보상금을 지급하거나 거부합니다.
	 *
	 * @param eventID 지급 또는 거부할 보상금의 ID
	 * @param isPaid         true면지급, false면 거부
	 * @return 보상금 지급 성공시 true를, 보상금 ID가 유효하지 않거나 업데이트에 실패한 경우 false
	 */
	public boolean payCompensation(String eventID, boolean isPaid) {
		try {
			DAO.executeQuery("UPDATE event SET state_of_compensation = ? WHERE event_id = ?",
					isPaid? 1 : -1, eventID);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}