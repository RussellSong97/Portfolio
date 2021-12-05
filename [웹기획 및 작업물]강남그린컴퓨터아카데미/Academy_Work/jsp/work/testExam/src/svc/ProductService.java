package svc;

import static db.JdbcUtil.*; 
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class ProductService {

	public int getProductCount(String where) {
		int rcnt = 0;		// 전체 게시글 개수를 저장할 변수
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		rcnt = productDao.getProductCount(where);
		close(conn);
		
		return rcnt;
	} 	
	
	public ArrayList<ProductInfo> getProductList(String where, int cpage, int psize) {
		ArrayList<ProductInfo> articleList = null;
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		articleList = productDao.getProductList(where, cpage, psize);
		
		close(conn);
				
		return articleList;
	}
}
