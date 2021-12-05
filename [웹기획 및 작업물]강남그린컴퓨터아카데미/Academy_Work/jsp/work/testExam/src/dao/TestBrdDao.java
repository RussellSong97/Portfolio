package dao;

import static db.JdbcUtil.*;
import javax.sql.*;
import java.sql.*;
import java.util.*; // ArrayList ������
import vo.*;

public class TestBrdDao {

	private static TestBrdDao boardDao;
	private Connection conn;

	private TestBrdDao() {
	}

	public static TestBrdDao getInstance() {
		if (boardDao == null)
			boardDao = new TestBrdDao();
		return boardDao;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}
 
	public int getBoardCount(String where) {
		Statement stmt = null;
		ResultSet rs = null;
		int rcnt = 0;

		try {
			String sql = "select count(*) from t_board " + where;
			System.out.println("getBoardCount : " + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
				rcnt = rs.getInt(1);

		} catch (Exception e) {
			System.out.println("getBoardCount() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}

		return rcnt;
	}

	public ArrayList<TestInfo> getBoardList(String where, int cpage, int psize) {

		Statement stmt = null;
		ResultSet rs = null;

		ArrayList<TestInfo> boardList = new ArrayList<TestInfo>();

		TestInfo TestInfo = null;

		int snum = (cpage - 1) * psize;

		try {
			String sql = "select * from t_board " + where + " order by b_idx desc limit " + snum + ", " + psize;
			stmt = conn.createStatement();
			System.out.println("getBoardList : " + sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				TestInfo = new TestInfo();

				TestInfo.setB_idx(rs.getInt("b_idx"));
				TestInfo.setB_writer(rs.getString("b_writer"));
				TestInfo.setB_title(rs.getString("b_title"));
				TestInfo.setB_content(rs.getString("b_content"));
				TestInfo.setB_date(rs.getString("b_date"));
				TestInfo.setB_read(rs.getInt("b_read"));

				boardList.add(TestInfo);
			}

		} catch (Exception e) {
			System.out.println("getBoardList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}

		return boardList;
	}

	public int readCountUp(int idx) {
		Statement stmt = null;
		int result = 0;
		try {
			String sql = "update t_board set b_read = b_read + 1 where b_idx = " + idx;
			System.out.println("readCountUp : " + sql);

			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);

		} catch (Exception e) {
			System.out.println("readCountUp() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(stmt);
		}
		return result;
	}

	public TestInfo getArticle(int idx) { 
		Statement stmt = null;
		ResultSet rs = null;
		TestInfo article = null;
		try {
			String sql = "select * from t_board where b_idx =  " + idx;
			System.out.println("getArticle : " + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				// rs에 보여줄 게시글이 있을 경우
				article = new TestInfo();
				// articleList에 저장할 한 게시글의 정보를 담을 인스턴스 생성

				article.setB_idx(idx);
				article.setB_title(rs.getString("b_title"));
				article.setB_writer(rs.getString("b_writer"));
				article.setB_content(rs.getString("b_content"));
				article.setB_read(rs.getInt("b_read"));
				article.setB_date(rs.getString("b_date"));
				// 받아온 레코드들로 게시글 정보를 저장

			}
		} catch (Exception e) {
			System.out.println("getArticle() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}
		
		return article;
	}

	
	public int insert(TestInfo testInfo) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			String sql = "insert into t_board(b_writer, b_title, b_content) values (?, ?, ?)"; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, testInfo.getB_writer());
			pstmt.setString(2, testInfo.getB_title());
			pstmt.setString(3, testInfo.getB_content());  
			System.out.println(pstmt);
			result = pstmt.executeUpdate();
			 
		} catch(Exception e) {
			System.out.println("board insert() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}
	
	public int update(TestInfo testInfo) {
		Statement stmt = null;
		int result = 0;
		
		try {
			String sql = "update t_board set "
					+ "b_writer = '" + testInfo.getB_writer() + "', "
							+ "b_title = '" + testInfo.getB_title() + "', "
							+ "b_content = '" + testInfo.getB_content() + "' "
							+ " where b_idx = " + testInfo.getB_idx(); 
			System.out.println("update : " + sql);  
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			
		} catch (Exception e) {
			System.out.println("update() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(stmt);
		}
		
		return result;
	}
}
