package main.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import main.DBConnection;
import main.Data.Compensation;
import main.Data.Evaluation;
import main.Data.Event;
import main.Enum.ProcessState;

public class EventListDBImpl implements EventList{



  private Event mapRowToEvent(ResultSet rs)throws SQLException {
    Evaluation evaluation = mapRowToEvaluation(rs);
    return new Event.Builder(rs.getInt("event_id"),rs.getInt("customer_id"))
        .claimValue(rs.getInt("claim_value"))
        .documents(rs.getString("documents"))
        .eventDate(new Date(rs.getDate("event_date").getTime()))
        .eventDescription(rs.getString("event_description"))
        .eventLocation(rs.getString("event_location"))
        .receiptDate(new Date(rs.getDate("event_receipt_date").getTime()))
        .evaluation(evaluation)
        .build();
  }
  private Evaluation mapRowToEvaluation(ResultSet rs)throws SQLException {
    Compensation compensation = mapRowToCompensation(rs);
    return new Evaluation.Builder(rs.getInt("event_id"),rs.getInt("event_id"),rs.getInt("customer_id"))
        .resultOfEvaluation(ProcessState.fromString(rs.getString("state_of_evaluation")))
        .compensation(compensation)
        .build();
  }

  private Compensation mapRowToCompensation(ResultSet rs)throws SQLException {
    return new Compensation.Builder(rs.getInt("event_id"),rs.getInt("event_id"),rs.getInt("customer_id"))
        .claimsPaid(rs.getInt("amount_of_paid"))
        .paidState(ProcessState.fromString(rs.getString("state_of_compensation")))
        .build();
  }


  @Override
  public boolean delete(int eventID) {
    String sql = "DELETE FROM events WHERE event_id = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, eventID);
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(eventID + "번 사고내역 삭제 중 오류 발생", e);
    }
  }

  @Override
  public boolean insert(Event event) {
    String sql = "INSERT INTO events (event_id, customer_id, claim_value, documents, event_date, event_description, event_location, event_receipt_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, event.getEventID());
      pstmt.setInt(2, event.getCustomerID());
      pstmt.setInt(3, event.getClaimValue());
      pstmt.setString(4, event.getDocuments());
      pstmt.setDate(5, new java.sql.Date(event.getEventDate().getTime()));
      pstmt.setString(6, event.getEventDescription());
      pstmt.setString(7, event.getEventLocation());
      pstmt.setDate(8, new java.sql.Date(event.getReceiptDate().getTime()));
      int affectedRows = pstmt.executeUpdate();

      // Evaluation 및 Compensation 삽입 로직 (옵션)
      // 만약 Event 삽입 시 Evaluation과 Compensation도 함께 삽입해야 한다면 아래 로직을 추가합니다.
      // 이 예시에서는 Event만 삽입합니다.

      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("사고내역 삽입 중 오류 발생", e);
    }
  }

  @Override
  public List<Event> search(String key, String value) {
    String sql = "SELECT * FROM events WHERE " + key + " LIKE ?";
    List<Event> events = new ArrayList<>();
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, "%" + value + "%"); // 부분 일치를 위해 % 사용
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          events.add(mapRowToEvent(rs));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("사고내역 검색 중 오류 발생", e);
    }
    return events;
  }

  @Override
  public boolean update(Event event) {
    String sql = "UPDATE events SET customer_id = ?, claim_value = ?, documents = ?, event_date = ?, event_description = ?, event_location = ?, event_receipt_date = ? WHERE event_id = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, event.getCustomerID());
      pstmt.setInt(2, event.getClaimValue());
      pstmt.setString(3, event.getDocuments());
      pstmt.setDate(4, new java.sql.Date(event.getEventDate().getTime()));
      pstmt.setString(5, event.getEventDescription());
      pstmt.setString(6, event.getEventLocation());
      pstmt.setDate(7, new java.sql.Date(event.getReceiptDate().getTime()));
      pstmt.setInt(8, event.getEventID());
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(event.getEventID() + "번 사고내역 업데이트 중 오류 발생", e);
    }
  }

  @Override
  public boolean update(Evaluation evaluation) {
    String sql = "UPDATE events SET result_of_evaluation = ? WHERE evaluation_id = ?"; // 'evaluation_id'가 필요합니다.
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, evaluation.getResultOfEvaluation().toString());
      pstmt.setInt(2, evaluation.getEvaluationID()); // Evaluation 객체에 evaluationId가 있다고 가정합니다.
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(evaluation.getEvaluationID() + "번 평가내역 업데이트 중 오류 발생", e);
    }
  }

  @Override
  public boolean update(Compensation compensation) {
    String sql = "UPDATE events SET amount_of_paid = ?, state_of_compensation = ? WHERE compensation_id = ?"; // 'compensation_id'가 필요합니다.
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, compensation.getAmountOfPaid());
      pstmt.setString(2, compensation.getProcessOfCompensation().toString());
      pstmt.setInt(3, compensation.getCompensationID()); // Compensation 객체에 compensationId가 있다고 가정합니다.
      int affectedRows = pstmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(compensation.getCompensationID() + "번 보상내역 업데이트 중 오류 발생", e);
    }
  }

  @Override
  public List<Event> findAll() {
    String sql = "SELECT * FROM events";
    List<Event> events = new ArrayList<>();
    try (Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        events.add(mapRowToEvent(rs));
      }
    } catch (
        SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("모든 사고내역 조회 중 오류 발생", e);
    }
    return events;
  }
}
