package dao;

import static db.JdbcUtil.*;
import java.sql.*;
import java.util.*;
import vo.*;

public class OrderDao {
	private static OrderDao orderDao;
	private Connection conn;
	private OrderDao() {}

	public static OrderDao getInstance() {
		if (orderDao == null) {
			orderDao = new OrderDao();
		}
		return orderDao;
	}
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public ArrayList<CartInfo> getCartList(String miid, String where) {
	// 장바구니에서 보여줄 특정 회원의 장바구니 목록 또는 결제폼에서 보여줄 결제할 상품 목록을 리턴하는 메소드
		ArrayList<CartInfo> cartList = new ArrayList<CartInfo>();
		Statement stmt = null;
		ResultSet rs = null;
		CartInfo cart = null;

		try {
			String sql = "select a.*, b.pi_name, b.pi_img1, c.b_name, b.pi_option, " + 
			"b.pi_price, b.pi_stock, b.pi_discount from t_order_cart a, t_product_info b, t_brand c " + 
			"where a.pi_id = b.pi_id and b.b_id = c.b_id and b.pi_isview = 'y' and " + 
			"(b.pi_stock >= a.oc_cnt or b.pi_stock = -1) and a.mi_id = '" + miid + 
			"' " + where + " order by a.pi_id";
			System.out.println(sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cart = new CartInfo();
				cart.setOc_idx(rs.getInt("oc_idx"));
				cart.setMi_id(rs.getString("mi_id"));
				cart.setOc_cnt(rs.getInt("oc_cnt"));
				cart.setOc_option(rs.getString("oc_option"));
				cart.setB_name(rs.getString("b_name"));
				cart.setPi_id(rs.getString("pi_id"));
				cart.setPi_name(rs.getString("pi_name"));
				cart.setPi_img1(rs.getString("pi_img1"));
				cart.setPi_option(rs.getString("pi_option"));
				cart.setPi_stock(rs.getInt("pi_stock"));
				int price = rs.getInt("pi_price");
				if (rs.getInt("pi_discount") > 0) {	// 할인율이 있으면
					float rate = (float)rs.getInt("pi_discount") / 100;	// 할인율
					price = Math.round(price - (price * rate));
					// 할인율이 있는 상품일 경우 할인율을 적용한 가격을 저장함
				}
				cart.setPi_price(price);

				cartList.add(cart);
			}
		} catch(Exception e) {
			System.out.println("getCartList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return cartList;
	}

	public int cartInsert(CartInfo cart) {
	// 장바구니에 특정 상품을 등록하는 메소드
		int result = 0;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			String sql = "select a.oc_idx, a.oc_cnt, b.pi_stock " + 
			"from t_order_cart a, t_product_info b where a.pi_id = b.pi_id and " + 
			"a.mi_id = '" + cart.getMi_id() + "' and a.pi_id = '" + 
			cart.getPi_id() + "' and a.oc_option = '" + cart.getOc_option() + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
			// 현재 추가하려는 상품과 동일한 상품이 이미 장바구니에 있을 경우
				sql = "update t_order_cart set oc_cnt = oc_cnt + " + cart.getOc_cnt() + 
				" where oc_idx = " + rs.getInt("oc_idx");
				if (rs.getInt("pi_stock") > 0)	// 재고가 무제한(-1)이 아니면
					sql += " and (oc_cnt + " + cart.getOc_cnt() + ") <= " + rs.getInt("pi_stock");
			} else {
				sql = "insert into t_order_cart (mi_id, pi_id, oc_option, oc_cnt) " + 
				"values ('" + cart.getMi_id() + "', '" + cart.getPi_id() + 
				"', '" + cart.getOc_option() + "', " + cart.getOc_cnt() + ")";
			}
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("cartInsert() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return result;
	}

	public int cartUpdate(String kind, String op, int idx, String uid, String piid) {
	// 장바구니내 수량 및 옵션 변경 메소드
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "";
			if (kind.equals("cnt")) {	// 수량 변경일 경우
				sql = "update t_order_cart set oc_cnt = oc_cnt " + op + " 1 " + 
					" where mi_id = '" + uid + "' and oc_idx = " + idx;
			} else {	// 옵션 변경일 경우
				sql = "select oc_idx, oc_cnt from t_order_cart where mi_id = '" + 
					uid + "' and pi_id = '" + piid + "' and oc_option = '" + op + "'";
				// 현재 회원의 장바구니 속에 동일한 상품의 동일한 옵션이 있는지 검사할 쿼리
				rs = stmt.executeQuery(sql);
				if (rs.next()) {	// 동일한 상품의 동일한 옵션이 있으면
					sql = "update t_order_cart set oc_option = '" + op + 
						"', oc_cnt = oc_cnt + " + rs.getInt("oc_cnt") + 
						" where oc_idx = " + idx;
					// 변경하는 상품의 개수를 기존 상품의 개수와 합침

					cartDelete(" where mi_id = '" + uid + "' and oc_idx = " + rs.getInt("oc_idx"));
					// 기존에 있던 동일상품 동일옵션의 카트 레코드를 삭제
				} else {	// 처음 선택되는 옵션이면
					sql = "update t_order_cart set oc_option = '" + 
						op + "' where oc_idx = " + idx;
				}
			}
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("cartUpdate() 메소드 오류");
			e.printStackTrace();
		} finally {
			if (kind.equals("opt"))	close(rs);	// 옵션변경일 경우에만 rs를 닫아줌
			close(stmt);
		}

		return result;
	}

	public int cartDelete(String where) {
	// 장바구니내 상품(들)을 삭제하는 메소드
		Statement stmt = null;
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "delete from t_order_cart " + where;
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("cartDelete() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(stmt);
		}

		return result;
	}
}
