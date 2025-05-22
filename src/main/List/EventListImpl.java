
package main.List;
import java.util.ArrayList;
import java.util.List;
import main.Data.Compensation;
import main.Data.Evaluation;
import main.Data.Event;
import main.Enum.ProcessState;

public class EventListImpl implements EventList {

	public ArrayList<Event> Events;

	public EventListImpl(){
		Events = new ArrayList<>();
	}

	/**
	 * 
	 * @param eventID
	 */
	public boolean delete(String eventID){
		for (Event event : Events) {
			if (String.valueOf(event.getEventID()).equals(eventID)) {
				Events.remove(event);
				return true;
			}
		}
		return false;
	}

	public boolean delete(Event tragetEvent){
		for (Event event : Events) {
			if (event.equals(tragetEvent)) {
				Events.remove(event);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean insert(Event event) {
		if(event==null)return false;
		return Events.add(event);
	}


//Search
	@Override
	public List<Event> search(String key, String value) {
		ArrayList<Event> result = new ArrayList<>();
		for(Event event : this.Events){
			switch (key.toLowerCase()){
				case "keyword":
					if(event.toString().toLowerCase().contains(value.toLowerCase())) result.add(event);
					break;
				case "state":
					if(event.getEvaluation().getResultOfEvaluation() == ProcessState.fromString(value)) result.add(event);
					break;
				case "id":
					if(event.getEventID().equals(value)) result.add(event);
					break;
				case "all":
					result.add(event);
					break;
				default:
					System.out.println("지원하지 않는 검색 필드입니다: " + key);
					return result;
			}
		}
		return result;
	}

	public ArrayList<Event> searchEvaluation(String key, String value) {
		ArrayList<Event> result = new ArrayList<>();
		for(Event event : this.Events){
			Evaluation evaluation = event.getEvaluation();
			if(evaluation==null) continue;
			switch (key.toLowerCase()){
				case "keyword":
					if(evaluation.toString().toLowerCase().contains(value.toLowerCase())) result.add(event);
					break;
				case "state":
					if(evaluation.getResultOfEvaluation() == ProcessState.fromString(value)) result.add(event);
					break;
				case "id":
					if(evaluation.getEvaluationID().equals(value)) result.add(event);
					break;
				case "all":
					result.add(event);
					break;
				default:
					System.out.println("지원하지 않는 검색 필드입니다: " + key);
					return result;
			}
		}
		return result;
	}


	public ArrayList<Event> searchCompensation(String key, String value) {
		ArrayList<Event> result = new ArrayList<>();
		for(Event event : this.Events){
			Compensation compensation = event.getEvaluation().getCompensation();
			if(compensation==null) continue;
			switch (key.toLowerCase()){
				case "keyword":
					if(compensation.toString().toLowerCase().contains(value.toLowerCase())) result.add(event);
					break;
				case "state":
					if(compensation.getState() == ProcessState.fromString(value)) result.add(event);
					break;
				case "id":
					if(compensation.getCompensationID().equals(value)) result.add(event);
					break;
				case "all":
					result.add(event);
					break;
				default:
					System.out.println("지원하지 않는 검색 필드입니다: " + key);
					return result;
			}
		}
		return result;
	}


//Update
	@Override
	public boolean update(Event updatedEvent) {
		for (int i = 0; i < Events.size(); i++) {
			Event existingEvents = Events.get(i);
			if (existingEvents.getEventID().equals(updatedEvent.getEventID())) {
				this.Events.set(i, updatedEvent);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean update(Evaluation updatedEvaluation) {
		// 수정된 코드
    // 리스트에서 Event 객체를 가져옵니다.
    for (Event eventInList : Events) {
      Evaluation existingEvaluation = eventInList.getEvaluation();
      if (existingEvaluation.getEvaluationID().equals(updatedEvaluation.getEvaluationID())) {
        eventInList.setEvaluation(updatedEvaluation);
        return true;
      }
    }
		return false;
	}

	@Override
	public boolean update(Compensation compensation) {
    for (Event event : Events) {
      Compensation existingCompensation = event.getEvaluation().getCompensation();
      if (existingCompensation.getCompensationID().equals(compensation.getCompensationID())) {
        event.getEvaluation().setCompensation(compensation);
        return true;
      }
    }
		return false;
//update compensation
	}
}//end EventListImpl