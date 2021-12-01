package dao;

import static db.JdbcUtil.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;
import vo.*;

public class MemberDao {
	private static MemberDao memberDao;
	private Connection conn;
	private MemberDao() {}

	public static MemberDao getInstance() {
		if (memberDao == null)	memberDao = new MemberDao();
		return memberDao;
	}
	
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	public int memberJoin(MemberInfo memberInfo) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			String sql = "insert into t_member_info (mi_id, mi_pwd, mi_name, " + 
				"mi_birth, mi_gender, mi_phone, mi_email)" +
					"values (?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberInfo.getMi_id());
			pstmt.setString(2, memberInfo.getMi_pwd());
			pstmt.setString(3, memberInfo.getMi_name());
			pstmt.setString(4, memberInfo.getMi_birth());
			pstmt.setString(5, memberInfo.getMi_gender());
			pstmt.setString(6, memberInfo.getMi_phone());
			pstmt.setString(7, memberInfo.getMi_email());
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			System.out.println("memberJoin() 메소드 오류.");
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}
	public int memberUpdate(MemberInfo memberInfo) {

		int result = 0;
		Statement stmt = null;
		
		try {
			String sql  = "update t_member_info set " ;
			
			if (!memberInfo.getMi_pwd().equals("")) {	// 비밀번호도 변경 시 
				   sql += " mi_pwd = '" + memberInfo.getMi_pwd() + "', "  ;
			}
				   sql += " mi_phone = '" + memberInfo.getMi_phone() + "', "  ;
				   sql += " mi_email = '" + memberInfo.getMi_email() + "', "  ;
				   sql += " mi_issns ='" + memberInfo.getMi_issns() + "', "  ;
				   sql += " where mi_id = '" + memberInfo.getMi_id() + "' ";
			System.out.println("memberUpdate sql >> " + sql);
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			
		} catch(Exception e) {
			System.out.println("memberUpdate() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(stmt);
		}

		return result + 2;
	}

	public int memberDelete(String miid) {
		
		int result = 0;
		Statement stmt = null;
		
		try {
			String sql  = " update t_member_info set mi_isactive = 'n' where mi_id = '" + miid + "' ";
			System.out.println("memberDelete sql >> " + sql);
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			
		} catch(Exception e) {
			System.out.println("memberDelete() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(stmt);
		}
		
		return result ;
	}
	
}
