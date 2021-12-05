package dao;

import static db.JdbcUtil.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import vo.*;

public class ProductDao {
	private static ProductDao productDao;
	private Connection conn;

	private ProductDao() {
	}

	public static ProductDao getInstance() {
		if (productDao == null)
			productDao = new ProductDao();
		return productDao;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public ArrayList<CataBigInfo> getCataBigList() {
		// 대분류 목록을 리턴하는 메소드
		ArrayList<CataBigInfo> cataBigList = new ArrayList<CataBigInfo>();
		CataBigInfo bigInfo = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "select * from t_cata_big";
			System.out.println("getCataBigList : " + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				bigInfo = new CataBigInfo();
				bigInfo.setCb_id(rs.getString("cb_id"));
				bigInfo.setCb_name(rs.getString("cb_name"));
				cataBigList.add(bigInfo);
			}

		} catch (Exception e) {
			System.out.println("getCataBigList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}
		return cataBigList;
	}

	public ArrayList<CataSmallInfo> getCataSmallList() {
		// 소분류 목록을 리턴하는 메소드
		ArrayList<CataSmallInfo> cataSmallList = new ArrayList<CataSmallInfo>();
		CataSmallInfo smallInfo = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "select * from t_cata_small";
			System.out.println("getCataSmallList : " + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				smallInfo = new CataSmallInfo();
				smallInfo.setCs_id(rs.getString("cs_id"));
				smallInfo.setCb_id(rs.getString("cb_id"));
				smallInfo.setCs_name(rs.getString("cs_name"));
				cataSmallList.add(smallInfo);
			}

		} catch (Exception e) {
			System.out.println("getCataSmallList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}

		return cataSmallList;

	}

	public ArrayList<BrandInfo> getBrandList() {
		// 브랜드 목록을 리턴하는 메소드
		ArrayList<BrandInfo> brandList = new ArrayList<BrandInfo>();
		BrandInfo brandInfo = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "select * from t_brand";
			System.out.println("getBrandList : " + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				brandInfo = new BrandInfo();
				brandInfo.setB_id(rs.getString("b_id"));
				brandInfo.setB_name(rs.getString("b_name"));
				brandInfo.setB_company(rs.getString("b_company"));
				brandList.add(brandInfo);
			}

		} catch (Exception e) {
			System.out.println("getBrandList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}

		return brandList;

	}

	public int getPdtCount(String where) {
		// 검색된 상품의 총 개수를 리턴하는 메소드
		Statement stmt = null;
		ResultSet rs = null;
		int rcnt = 0;

		try {
			String sql = "select count(*) from t_product_info a, t_cata_big b, t_cata_small c, t_brand d " + where;
			System.out.println("getPdtCount : " + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
				rcnt = rs.getInt(1);
		} catch (Exception e) {
			System.out.println("getPdtCount() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}

		return rcnt;
	}

	public ArrayList<ProductInfo> getPdtList(String where, String orderBy, int cpage, int psize) {
		// 검색된 상품 목록을 ArrayList<ProductInfo> 형 인스턴스로 리턴하는 메소드
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();
		// 상품목록을 저장할 ArrayList로 오직 ProductInfo형 인스턴스만 저장됨
		Statement stmt = null;
		ResultSet rs = null;
		ProductInfo productInfo = null;
		// pdtList에 담을 데이터인 ProductInfo형 인스턴스 선언
		int snum = (cpage - 1) * psize;
		// 쿼리의 limit 명령에서 데이터를 가져올 시작 인덱스 번호

		try {
			String sql = "select a.*, b.cb_id, b.cb_name, c.cs_name, d.b_name "
					+ " from t_product_Info a, t_cata_big b, t_cata_small c, t_brand d " + where + orderBy + " limit "
					+ snum + ", " + psize;
			System.out.println("getPdtList : " + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// rs에 보여줄 상품이 있을 경우
				productInfo = new ProductInfo();
				// pdtList에 저장할 한 상품 정보를 담을 인스턴스 생성

				productInfo.setPi_id(rs.getString("pi_id"));
				productInfo.setPi_name(rs.getString("pi_name"));
				productInfo.setCs_id(rs.getString("cs_id"));
				productInfo.setB_id(rs.getString("b_id"));
				productInfo.setPi_origin(rs.getString("pi_origin"));
				productInfo.setPi_option(rs.getString("pi_option"));
				productInfo.setPi_img1(rs.getString("pi_img1"));
				productInfo.setPi_img2(rs.getString("pi_img2"));
				productInfo.setPi_img3(rs.getString("pi_img3"));
				productInfo.setPi_desc(rs.getString("pi_desc"));
				productInfo.setPi_isview(rs.getString("pi_isview"));
				productInfo.setPi_date(rs.getString("pi_date"));
				productInfo.setCb_id(rs.getString("cb_id"));
				productInfo.setCb_name(rs.getString("cb_name"));
				productInfo.setCs_name(rs.getString("cs_name"));
				productInfo.setB_name(rs.getString("b_name"));
				productInfo.setPi_cost(rs.getInt("pi_cost"));
				productInfo.setPi_price(rs.getInt("pi_price"));
				productInfo.setPi_discount(rs.getInt("pi_discount"));
				productInfo.setPi_stock(rs.getInt("pi_stock"));
				productInfo.setPi_salecnt(rs.getInt("pi_salecnt"));
				productInfo.setPi_review(rs.getInt("pi_review"));
				productInfo.setAi_idx(rs.getInt("ai_idx"));
				productInfo.setPi_star(rs.getFloat("pi_star"));

				pdtList.add(productInfo);
				// 한 게시글의 정보를 담은 noticeInfo 인스턴스를 articleList에 저장
			}
		} catch (Exception e) {
			System.out.println("getPdtList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}

		return pdtList;
	}

	public ProductInfo getPdtInfo(String id) {
		// 지정한 id에 해당하는 특정 상품 정보를 productInfo형 인스턴스로 리턴하는 메소드
		ProductInfo pdtInfo = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "select a.*, b.cb_id, b.cb_name, c.cs_name, d.b_name "
					+ " from t_product_info a, t_cata_big b, t_cata_small c, t_brand d "
					+ " where  a.cs_id = c.cs_id and b.cb_id = c.cb_id  and a.b_id = d.b_id " + " and a.pi_id = '" + id
					+ "' and a.pi_isview = 'y' and a.pi_stock <> 0";
			System.out.println("getPdtInfo >>" + sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				pdtInfo = new ProductInfo();

				pdtInfo.setPi_id(rs.getString("pi_id"));
				pdtInfo.setPi_name(rs.getString("pi_name"));
				pdtInfo.setCs_id(rs.getString("cs_id"));
				pdtInfo.setB_id(rs.getString("b_id"));
				pdtInfo.setPi_origin(rs.getString("pi_origin"));
				pdtInfo.setPi_option(rs.getString("pi_option"));
				pdtInfo.setPi_img1(rs.getString("pi_img1"));
				pdtInfo.setPi_img2(rs.getString("pi_img2"));
				pdtInfo.setPi_img3(rs.getString("pi_img3"));
				pdtInfo.setPi_desc(rs.getString("pi_desc"));
				pdtInfo.setPi_isview(rs.getString("pi_isview"));
				pdtInfo.setPi_date(rs.getString("pi_date"));
				pdtInfo.setPi_cost(rs.getInt("pi_cost"));
				pdtInfo.setPi_price(rs.getInt("pi_price"));
				pdtInfo.setPi_discount(rs.getInt("pi_discount"));
				pdtInfo.setPi_stock(rs.getInt("pi_stock"));
				pdtInfo.setPi_salecnt(rs.getInt("pi_salecnt"));
				pdtInfo.setPi_review(rs.getInt("pi_review"));
				pdtInfo.setAi_idx(rs.getInt("ai_idx"));
				pdtInfo.setPi_star(rs.getFloat("pi_star"));
				pdtInfo.setCb_id(rs.getString("cb_id"));
				pdtInfo.setCb_name(rs.getString("cb_name"));
				pdtInfo.setCs_name(rs.getString("cs_name"));
				pdtInfo.setB_name(rs.getString("b_name"));
				// 받아온 레코드들로 상품 정보를 저장

			}
		} catch (Exception e) {
			System.out.println("getPdtInfo() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmt);
		}

		return pdtInfo;
	}
}