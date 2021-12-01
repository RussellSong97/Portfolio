package db;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class JdbcUtil {
// DB관련 연결 및 공용 메소드들을 정의한 클래스
	public static Connection getConnection() {
	// DB에 연결하여 컨넥션 객체를 리턴하는 메소드
		Connection conn = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context)initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQLDB");
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			// 쿼리가 자동으로 commit되는 것을 막아 트랜잭션을 시작시킴

		} catch(Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	public static void close(Connection conn) {	// DB와의 연결을 끊어주는 메소드
		try { conn.close(); }
		catch(Exception e) { e.printStackTrace(); }
	}

	public static void close(Statement stmt) {	// Statement를 닫는 메소드
	// PreparedStatement와 CallabledStatement는 Statement를 상속받으므로 따로 close()메소드를 안만들어도 됨
		try { stmt.close(); }
		catch(Exception e) { e.printStackTrace(); }
	}

	public static void close(ResultSet rs) {	// ResultSet을 닫는 메소드
		try { rs.close(); }
		catch(Exception e) { e.printStackTrace(); }
	}

	public static void commit(Connection conn) {	// transaction을 commit시키는 메소드
		try {
			conn.commit();
			System.out.println("쿼리 적용 성공");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void rollback(Connection conn) {	// transaction을 rollback시키는 메소드
		try {
			conn.rollback();
			System.out.println("쿼리 적용 실패");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
