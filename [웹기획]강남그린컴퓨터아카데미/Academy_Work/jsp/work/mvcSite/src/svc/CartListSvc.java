package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class CartListSvc {
	public ArrayList<CartInfo> getCartList(String miid) {
		ArrayList<CartInfo> cartList = null;
		Connection conn = getConnection();
		OrderDao orderDao = OrderDao.getInstance();
		orderDao.setConnection(conn);
		cartList = orderDao.getCartList(miid, "");
		close(conn);

		return cartList;
	}
}
