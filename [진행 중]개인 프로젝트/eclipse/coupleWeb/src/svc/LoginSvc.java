package svc;

import static db.JdbcUtil.*;
// db 패키지 내의 JdbcUtil 클래스가 가진 모든 멤버들을 자유롭게 사용하겠다는 의미
import java.sql.*;
import dao.*;
import vo.*;

public class LoginSvc {
	// 로그인에 대한 처리작업을 DB를 제외한 모든 처리를 하는 클래스
	   public MemberInfo getLoginMember(String uid, String pwd) {
	      // 사용자가 입력한 아이디와 비밀번호를 매개변수로 받아 로그인을 처리하는 메소드로 MemberInfo형 인스턴스로 결과를 리턴.
	      LoginDao loginDao = LoginDao.getInstance();
	      // 로그인의 DB작업을 위해 LoginDao형 인스턴스 loginDao를 생성
	      Connection conn = getConnection();
	      // JdbcUtil 클래스의 getConnection()메소드로 Connection 객체 conn을 생성
	      loginDao.setConnection(conn);
	      // loginDao인스턴스에서 사용할 Connection객체 전달
	      
	      MemberInfo loginMember = loginDao.getLoginMember(uid, pwd);
	      // loginDao 인스턴스의 getLoginMember() 메소드를 실행시키고
	      // 그 결과를 MemberInfo 형 인스턴스 loginMember에 저장
	      
	      close(conn);
	      
	      return loginMember;
	   }
}
