package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class CartInProcSvc {
	public int cartInsert(CartInfo cart) {
		int result = 0;
		Connection conn = getConnection();
		OrderDao orderDao = OrderDao.getInstance();
		orderDao.setConnection(conn);
		result = orderDao.cartInsert(cart);

		if (result == 1)	commit(conn);
		else				rollback(conn);
		close(conn);

		return result;
	}
}
