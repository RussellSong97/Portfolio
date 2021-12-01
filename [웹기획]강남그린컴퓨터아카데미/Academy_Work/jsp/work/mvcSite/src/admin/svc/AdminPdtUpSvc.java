package admin.svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import admin.dao.*;
import vo.*;

public class AdminPdtUpSvc {
	public ProductInfo getPdtInfo(String id) {
	// 수정할 상품을 ProductInfo 형 인스턴스로 리턴하는 메소드
		ProductInfo pdtInfo = new ProductInfo();
		Connection conn = getConnection();
		AdminPdtDao adminPdtDao = AdminPdtDao.getInstance();
		adminPdtDao.setConnection(conn);
		pdtInfo = adminPdtDao.getPdtInfo(id);
		close(conn);

		return pdtInfo;
	}
}
