package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class CartListAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<CartInfo> cartList = new ArrayList<CartInfo>();

		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		MemberInfo loginMember = (MemberInfo)session.getAttribute("loginMember");

		CartListSvc cartListSvc = new CartListSvc();
		cartList = cartListSvc.getCartList(loginMember.getMi_id());
		request.setAttribute("cartList", cartList);
		// dispatch방식으로 이동할 cart_list.jsp파일에서 사용할 수 있도록 request객체에 속성으로 저장

		ActionForward forward = new ActionForward();
		forward.setPath("/order/cart_list.jsp");

		return forward;
	}
}
