package main.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import main.Data.Contract;
import main.DBConnection;
import main.Enum.ProcessState;

public class ContractListDBImpl implements ContractList {


  @Override
  public int insert(Contract contract) {
    String sql = "INSERT INTO contracts (contract_date, customer_id, expiration_date, product_id, sales_id, state) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) { // contract_id가 DB 자동생성이 아니라면 RETURN_GENERATED_KEYS 필요 없음

      pstmt.setDate(1, new java.sql.Date(contract.getContractDate().getTime())); // java.util.Date -> java.sql.Date
      pstmt.setString(2, contract.getCustomerID());
      pstmt.setDate(3, java.sql.Date.valueOf(contract.getExpirationDate())); // LocalDate -> java.sql.Date
      pstmt.setString(4, contract.getProductID());
      pstmt.setString(5, contract.getSalesID());
      pstmt.setString(6, contract.getState().name()); // ENUM을 String으로 저장

      int affectedRows = pstmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("계약 저장 실패, 영향을 받은 행 없음.");
      }
      return contract.getContractID(); // 저장된 ID 반환

    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("계약 저장 중 오류 발생", e); // 예외를 런타임 예외로 감싸서 던짐
    }
  }


  @Override
  public Optional<Contract> search(int contractID) {
    String sql = "SELECT contract_id, contract_date, customer_id, expiration_date, product_id, sales_id, state FROM contracts WHERE contract_id = ?";
    Contract contract = null;
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, contractID);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        contract = mapRowToContract(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("ID로 계약 조회 중 오류 발생", e);
    }
    return Optional.ofNullable(contract);
  }


  public List<Contract> findAll() {
    String sql = "SELECT contract_id, contract_date, customer_id, expiration_date, product_id, sales_id, state FROM contracts";
    List<Contract> contracts = new ArrayList<>();
    try (Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        contracts.add(mapRowToContract(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("모든 계약 조회 중 오류 발생", e);
    }
    return contracts;
  }

  @Override
  public int update(Contract contract) {
    String sql = "UPDATE contracts SET contract_date = ?, customer_id = ?, expiration_date = ?, product_id = ?, sales_id = ?, state = ? WHERE contract_id = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setDate(1, new java.sql.Date(contract.getContractDate().getTime()));
      pstmt.setString(2, contract.getCustomerID());
      pstmt.setDate(3, java.sql.Date.valueOf(contract.getExpirationDate()));
      pstmt.setString(4, contract.getProductID());
      pstmt.setString(5, contract.getSalesID());
      pstmt.setString(6, contract.getState().name());
      pstmt.setInt(7, contract.getContractID());
      pstmt.executeUpdate();
      return contract.getContractID();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("계약 업데이트 중 오류 발생", e);
    }
  }

  @Override
  public int delete(int contractID) {
    String sql = "DELETE FROM contracts WHERE contract_id = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, contractID);
      pstmt.executeUpdate();
      return contractID;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("계약 삭제 중 오류 발생", e);
    }
  }

  public List<Contract> searchByCustomerID(String customerID) {
    String sql = "SELECT contract_id, contract_date, customer_id, expiration_date, product_id, sales_id, state FROM contracts WHERE customer_id = ?";
    List<Contract> contracts = new ArrayList<>();
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, customerID);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        contracts.add(mapRowToContract(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("고객 ID로 계약 조회 중 오류 발생", e);
    }
    return contracts;
  }

  // ResultSet의 현재 행을 Contract 객체로 매핑하는 헬퍼 메서드
  private Contract mapRowToContract(ResultSet rs) throws SQLException {
    return new Contract.Builder()
        .contractID(rs.getInt("contract_id"))
        .contractDate(new Date(rs.getDate("contract_date").getTime())) // java.sql.Date -> java.util.Date
        .customerID(rs.getString("customer_id"))
        .expirationDate(rs.getDate("expiration_date").toLocalDate()) // java.sql.Date -> LocalDate
        .productID(rs.getString("product_id"))
        .salesID(rs.getString("sales_id"))
        .state(ProcessState.valueOf(rs.getString("state"))) // String -> ENUM
        .build();
  }
}
