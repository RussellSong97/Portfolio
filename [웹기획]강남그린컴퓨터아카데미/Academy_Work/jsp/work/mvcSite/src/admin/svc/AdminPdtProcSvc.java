package admin.svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import admin.dao.*;
import vo.*;

public class AdminPdtProcSvc {
	public boolean pdtInsert(ProductInfo pdtInfo) {
		boolean isSuccess = false;
		Connection conn = getConnection();
		AdminPdtDao adminPdtDao = AdminPdtDao.getInstance();
		adminPdtDao.setConnection(conn);
		int result = adminPdtDao.pdtInsert(pdtInfo);
		if (result > 0) {
			isSuccess = true;
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);

		return isSuccess;
	}

	public boolean pdtUpdate(ProductInfo pdtInfo) {
		boolean isSuccess = false;
		Connection conn = getConnection();
		AdminPdtDao adminPdtDao = AdminPdtDao.getInstance();
		adminPdtDao.setConnection(conn);
		int result = adminPdtDao.pdtUpdate(pdtInfo);
		if (result > 0) {
			isSuccess = true;
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);

		return isSuccess; 
	}
}
