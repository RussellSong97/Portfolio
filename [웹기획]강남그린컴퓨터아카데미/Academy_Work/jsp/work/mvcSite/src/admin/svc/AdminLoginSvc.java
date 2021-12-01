package admin.svc;

import static db.JdbcUtil.*;
import java.sql.*;
import admin.dao.*;
import vo.*;

public class AdminLoginSvc {
	public AdminInfo getLoginAdmin(String uid, String pwd) {
		AdminLoginDao adminLoginDao = AdminLoginDao.getInstance();
		Connection conn = getConnection();
		adminLoginDao.setConnection(conn);

		AdminInfo adminInfo = adminLoginDao.getLoginAdmin(uid, pwd);
		close(conn);

		return adminInfo;
	}
}
