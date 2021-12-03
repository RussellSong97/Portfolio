package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class OrderFormAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<CartInfo> cartList = new ArrayList<CartInfo>();

		request.setCharacterEncoding("utf-8");
		String kind = request.getParameter("kind");
		String where = "";

		HttpSession session = request.getSession();
		MemberInfo loginMember = (MemberInfo)session.getAttribute("loginMember");
		OrderFormSvc orderFormSvc = new OrderFormSvc();

		if (kind.equals("cart")) {	// 장바구니를 통해 구매할 경우
			String[] arrIdx = request.getParameterValues("chk");
			// 체크되어 있는 체크박스의 value 값들을 문자열 배열로 받아 옴
			for (int i = 0 ; i < arrIdx.length ; i++)
				where += " or a.oc_idx = " + arrIdx[i];
			where = " and (" + where.substring(4) + ") ";	// 구매할 상품들만 추출하기 위한 조건
			cartList = orderFormSvc.getCartList(loginMember.getMi_id(), where);
			request.setAttribute("cartList", cartList);
		} else {	// 상품 상세화면에서 '바로 구매' 클릭 시
			String piid = request.getParameter("piid");		// 상품ID
			String occnt = request.getParameter("occnt");	// 구매수량
			String ocoption = request.getParameter("ocoption");	// 상품 옵션
			ProductInfo pdtInfo = orderFormSvc.getPdtInfo(piid);
			request.setAttribute("pdtInfo", pdtInfo);
			request.setAttribute("occnt", occnt);
			request.setAttribute("ocoption", ocoption);
		}

		ActionForward forward = new ActionForward();
		forward.setPath("/order/order_form.jsp");

		return forward;
	}
}
