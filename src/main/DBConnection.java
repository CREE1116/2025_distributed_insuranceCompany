package main;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
  private static final String URL = "jdbc:mysql://root51.duckdns.org:3306/distributed_prgramming_1";
  private static final String USER = "soyun";
  private static final String PASSWORD = "distributed";
  private static Connection con = null;

  public static Connection getConnection() throws SQLException {
    // Class.forName("com.mysql.cj.jdbc.Driver");
    try{
      con = DriverManager.getConnection(URL, USER, PASSWORD);
      System.out.println("연결 성공");
    }catch(SQLException e){
      System.out.println("연결 실패");
      e.printStackTrace();
    }
    return con;
  }

}
