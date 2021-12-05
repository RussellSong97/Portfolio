package dao;

import static db.JdbcUtil.*;
import java.sql.*;
import vo.*;

public class DupIDDao {
// 회원 아이디 중복 확인용 클래스
	private static DupIDDao dupIDDao;
	private Connection conn;
	private DupIDDao() {}

	public static DupIDDao getInstance() {
		if (dupIDDao == null) {
			dupIDDao = new DupIDDao();
		}
		return dupIDDao;
	}
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public int chkDupID(String uid) {
		Statement stmt = null;
		ResultSet rs = null;
		int chkPoint = 0;	// 중복된 아이디의 개수를 저장할 변수(중복:1, 미중복:0)

		try {
			String sql = "select count(*) from t_member_info where mi_id = '" + uid + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())	chkPoint = rs.getInt(1);
		} catch(Exception e) {
			System.out.println("chkDupID() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return chkPoint;
	}
}
