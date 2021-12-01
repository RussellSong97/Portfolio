package dao;

import static db.JdbcUtil.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;
import vo.*;

public class MemberDao {
// 회원 관련 DB작업을 실제로 처리하는 메소드들을 담은 클래스
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
				"mi_birth, mi_gender, mi_phone, mi_email, mi_issns, mi_ismail, " + 
				"mi_point) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberInfo.getMi_id());
			pstmt.setString(2, memberInfo.getMi_pwd());
			pstmt.setString(3, memberInfo.getMi_name());
			pstmt.setString(4, memberInfo.getMi_birth());
			pstmt.setString(5, memberInfo.getMi_gender());
			pstmt.setString(6, memberInfo.getMi_phone());
			pstmt.setString(7, memberInfo.getMi_email());
			pstmt.setString(8, memberInfo.getMi_issns());
			pstmt.setString(9, memberInfo.getMi_ismail());
			pstmt.setInt(10, 1000);
			result = pstmt.executeUpdate();
			if (result > 0) {
				sql = "insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2) " + 
					"values (?, ?, ? , ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberInfo.getMi_id());
				pstmt.setString(2, memberInfo.getMa_zip());
				pstmt.setString(3, memberInfo.getMa_addr1());
				pstmt.setString(4, memberInfo.getMa_addr2());
				result += pstmt.executeUpdate();
				
				sql = "insert into t_member_point (mi_id, mp_point, mp_content) " + 
					"values (?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberInfo.getMi_id());
				pstmt.setInt(2, 1000);
				pstmt.setString(3, "가입 축하금");
				result += pstmt.executeUpdate();
			}
		} catch(Exception e) {
			System.out.println("memberJoin() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	/**
	 * 회원정보를 수정하는 메소드 
	 * @param memberInfo
	 * @return
	 */
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
				   sql += " mi_ismail = '" + memberInfo.getMi_ismail() + "' "  ;
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
	
	
	/**
	 * 회원을 탈퇴시키는 메소드 
	 * @param miid
	 * @return
	 */
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
