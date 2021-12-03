package dao;

import static db.JdbcUtil.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;
import vo.*;

public class ProductDao {
	private static ProductDao productDao;
	private Connection conn;
	private ProductDao() { }
	public static ProductDao getInstance() {
		if (productDao == null)
			productDao = new ProductDao();
		return productDao;
	}
	public void setConnection(Connection conn) {
		this.conn = conn;
	} 
	public int getProductCount(String where) {
		Statement stmt = null;
		ResultSet rs = null;
		int rcnt = 0;
		try {
			String sql = "select count(*) from TBL_PRODUCT " + where;
			System.out.println("getProductCount : " + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
				rcnt = rs.getInt(1);
		} catch (Exception e) {
			System.out.println("getProductCount() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}
		return rcnt;
	}
	
	public ArrayList<ProductInfo> getProductList(String where, int cpage, int psize) {

		Statement stmt = null;
		ResultSet rs = null;

		ArrayList<ProductInfo> productList = new ArrayList<ProductInfo>();

		ProductInfo productInfo = null;

		int snum = (cpage - 1) * psize;

		try {
			String sql = "select * from TBL_PRODUCT " + where + " order by product_idx desc limit " + snum + ", " + psize;
			stmt = conn.createStatement();
			System.out.println("getProductList : " + sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				productInfo = new ProductInfo();

				productInfo.setProduct_idx(rs.getInt("product_idx"));
				productInfo.setProduct_id(rs.getString("product_id"));  
				productInfo.setProduct_name(rs.getString("product_name"));  
				productInfo.setProduct_price(rs.getInt("product_price"));
				productInfo.setProduct_img(rs.getString("product_img"));   
				
				productList.add(productInfo);
			}

		} catch (Exception e) {
			System.out.println("getProductList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}

		return productList;
	}
  
}
