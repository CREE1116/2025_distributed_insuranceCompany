package main;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

  // config.properties 파일에서 읽어올 속성들을 저장할 Properties 객체
  private static Properties properties = new Properties();
  private static Connection con = null; // Connection 객체는 한 번만 생성되도록 static으로 유지

  // 클래스가 로드될 때 config.properties 파일을 한 번만 읽어옵니다.
  static {
    try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        // 파일을 찾을 수 없는 경우
        System.err.println("오류: 'config.properties' 파일을 찾을 수 없습니다. src/main/resources 디렉토리에 있는지 확인하세요.");
        // 애플리케이션 시작을 중단할 수 있도록 RuntimeException 발생
        throw new RuntimeException("설정 파일 로드 실패: config.properties를 찾을 수 없음");
      }
      // Properties 객체에 파일 내용 로드
      properties.load(input);
      System.out.println("config.properties 파일 로드 성공.");
    } catch (Exception e) {
      // 파일 로드 중 예외 발생 시 처리
      System.err.println("오류: 'config.properties' 파일을 로드하는 중 문제가 발생했습니다: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("DB 설정 초기화 실패", e);
    }
  }

  public static Connection getConnection() throws SQLException {
    // Properties 객체에서 DB 연결 정보 가져오기
    String url = properties.getProperty("db.url");
    String user = properties.getProperty("db.username");
    String password = properties.getProperty("db.password");

    // 연결 정보가 제대로 로드되었는지 확인
    if (url == null || user == null || password == null) {
      throw new SQLException("config.properties 파일에 db.url, db.username, db.password 중 하나 이상이 누락되었습니다.");
    }

    // 이미 연결이 되어 있다면 기존 연결 반환 (싱글톤 패턴과 유사)
    if (con != null && !con.isClosed()) {
      System.out.println("기존 데이터베이스 연결 재사용.");
      return con;
    }

    try {
      // DriverManager를 사용하여 데이터베이스 연결
      con = DriverManager.getConnection(url, user, password);
      System.out.println("데이터베이스 연결 성공!");
    } catch (SQLException e) {
      System.err.println("데이터베이스 연결 실패!");
      System.err.println("에러 메시지: " + e.getMessage());
      // 디버깅을 위해 스택 트레이스 출력
      e.printStackTrace();
      // 연결 실패 시 호출자에게 SQLException 다시 던지기
      throw e;
    }
    return con;
  }

  // 필요하다면 연결을 닫는 메서드도 추가할 수 있습니다. (권장)
  public static void closeConnection() {
    if (con != null) {
      try {
        con.close();
        System.out.println("데이터베이스 연결 종료.");
      } catch (SQLException e) {
        System.err.println("데이터베이스 연결 종료 중 오류 발생: " + e.getMessage());
        e.printStackTrace();
      } finally {
        con = null; // 연결 객체 초기화
      }
    }
  }


}
