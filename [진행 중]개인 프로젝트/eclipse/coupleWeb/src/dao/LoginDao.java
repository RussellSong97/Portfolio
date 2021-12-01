package dao;

import static db.JdbcUtil.*;
import java.sql.*;
import vo.*;

public class LoginDao {
   private static LoginDao loginDao;
   private Connection conn;      
   private LoginDao() {}
   
   public static LoginDao getInstance() {
      if(loginDao==null) {
         loginDao = new LoginDao();
      }
      return loginDao;
   }
   public void setConnection(Connection conn) {
      this.conn = conn;
   }
   public MemberInfo getLoginMember(String uid, String pwd) {
      MemberInfo loginMember = null; 
      Statement stmt = null;
      ResultSet rs = null;
      try {
         stmt = conn.createStatement();
         String sql = "select a.*, b.ma_idx, b.ma_zip, b.ma_addr1, b.ma_addr2 "
               + "from t_member_info a, t_member_addr b where a.mi_id = b.mi_id and "
               + " a.mi_id = '" + uid + "' and a.mi_pwd = '" + pwd + "' and a.mi_isactive = 'y' and b.ma_basic = 'y'";
         rs = stmt.executeQuery(sql);
         
         if(rs.next()) {   // 로그인 성공시
            loginMember = new MemberInfo();
            loginMember.setMi_id(uid);
            loginMember.setMi_pwd(pwd);
            loginMember.setMi_name(rs.getString("mi_name"));
            loginMember.setMi_birth(rs.getString("mi_birth"));
            loginMember.setMi_gender(rs.getString("mi_gender"));
            loginMember.setMi_phone(rs.getString("mi_phone"));
            loginMember.setMi_email(rs.getString("mi_email"));
            loginMember.setMi_issns(rs.getString("mi_issns"));
            loginMember.setMi_date(rs.getString("mi_date"));
            loginMember.setMi_isactive(rs.getString("mi_isactive"));
            loginMember.setMi_lastlogin(rs.getString("mi_lastlogin"));
         }
         
      } catch(Exception e) {
         System.out.println("getLoginMember() 메소드 오류");
         e.printStackTrace();
      } finally {
         close(rs);
         close(stmt);
      }
      
      return loginMember;
   }
}