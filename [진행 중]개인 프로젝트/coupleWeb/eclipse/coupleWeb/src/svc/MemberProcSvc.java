package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;


public class MemberProcSvc {
	public int memberProc(MemberInfo memberInfo, String wtype) {
		int result = 0;
		Connection conn = getConnection();
		MemberDao memberDao = MemberDao.getInstance();
		memberDao.setConnection(conn);
		
		if (wtype.equals("in")) {
			result = memberDao.memberJoin(memberInfo);			
		} else if (wtype.equals("up")) {
			result = memberDao.memberUpdate(memberInfo);
		}
		
		if (result == 3)	commit(conn);	// 추가된 레코드(가입된 회원)가 있으면 쿼리를 적용시킴
		else				rollback(conn);	// 추가된 레코드가 없으면(회원 가입 실패) 원래대로 돌림
		close(conn);

		return result;
	} 
		
	public int memberDelete(String miid) {
		int result = 0;

		Connection conn = getConnection();
		MemberDao memberDao = MemberDao.getInstance();
		memberDao.setConnection(conn);
		result = memberDao.memberDelete(miid);		 
		
		if (result == 1)	commit(conn);	// 변경된 레코드(회원 탈퇴 성공 )가 있으면 쿼리를 적용시킴
		else				rollback(conn);	// 변경된 레코드(회원 탈퇴 실해 )가 없으면 원래대로 돌림
		close(conn);
		
		return result;
	}

}
