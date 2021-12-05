package admin.svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import admin.dao.*;
import vo.*;

public class AdminPdtListSvc {
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

	public int getPdtCount(String where) {
	// 목록에서 보여줄 전체 게시글의 개수를 리턴하는 메소드
		int rcnt = 0;	// 전체 게시글 개수를 저장할 변수
		Connection conn = getConnection();	// DB에 연결
		AdminPdtDao adminProductDao = AdminPdtDao.getInstance();
		adminProductDao.setConnection(conn);
		rcnt = adminProductDao.getPdtCount(where);
		close(conn);

		return rcnt;
	}

	public ArrayList<ProductInfo> getPdtList(String where, String orderBy, int cpage, int psize) {
	// 검색된 상품 목록을 ArrayList<ProductInfo> 형 인스턴스로 리턴하는 메소드
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();
		Connection conn = getConnection();
		AdminPdtDao adminProductDao = AdminPdtDao.getInstance();
		adminProductDao.setConnection(conn);
		pdtList = adminProductDao.getPdtList(where, orderBy, cpage, psize);
		close(conn);

		return pdtList;
	}
}
