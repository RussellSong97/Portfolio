package admin.svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import admin.dao.*;
import vo.*;

public class AdminPdtInSvc {

	public ArrayList<CataBigInfo> getCataBigList() {
		ArrayList<CataBigInfo> cataBigList = null;
		Connection conn = getConnection();
		AdminPdtDao adminProductDao = AdminPdtDao.getInstance();
		adminProductDao.setConnection(conn);
		cataBigList = adminProductDao.getCataBigList();
		close(conn);

		return cataBigList;
	}

	public ArrayList<CataSmallInfo> getCataSmallList() {
		ArrayList<CataSmallInfo> cataSmallList = null;
		Connection conn = getConnection();
		AdminPdtDao adminProductDao = AdminPdtDao.getInstance();
		adminProductDao.setConnection(conn);
		cataSmallList = adminProductDao.getCataSmallList();
		close(conn);

		return cataSmallList;
	}

	public ArrayList<BrandInfo> getBrandList() {
		ArrayList<BrandInfo> brandList = null;
		Connection conn = getConnection();
		AdminPdtDao adminProductDao = AdminPdtDao.getInstance();
		adminProductDao.setConnection(conn);
		brandList = adminProductDao.getBrandList();
		close(conn);

		return brandList;
	}
}
