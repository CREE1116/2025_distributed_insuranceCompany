package main;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {
  private static final String URL = "jdbc:mysql://root51.duckdns.org:3306/distributed_prgramming_1";
  private static final String USER = "soyun";
  private static final String PASSWORD = "distributed";
  private static Connection con = null;

  public static Connection getConnection() throws SQLException {
    // Class.forName("com.mysql.cj.jdbc.Driver");
    try{
      con = DriverManager.getConnection(URL, USER, PASSWORD);
    }catch(SQLException e){
      e.printStackTrace();
    }
    return con;
  }

}
