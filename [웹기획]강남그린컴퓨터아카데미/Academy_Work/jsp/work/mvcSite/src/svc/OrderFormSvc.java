package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class OrderFormSvc {
	public ArrayList<CartInfo> getCartList(String miid, String where) {
		ArrayList<CartInfo> cartList = null;
		Connection conn = getConnection();
		OrderDao orderDao = OrderDao.getInstance();
		orderDao.setConnection(conn);
		cartList = orderDao.getCartList(miid, where);
		close(conn);

		return cartList;
	}

	public ProductInfo getPdtInfo(String id) {
		ProductInfo pdtInfo = null;
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		pdtInfo = productDao.getPdtInfo(id);

		return pdtInfo;
	}
}
