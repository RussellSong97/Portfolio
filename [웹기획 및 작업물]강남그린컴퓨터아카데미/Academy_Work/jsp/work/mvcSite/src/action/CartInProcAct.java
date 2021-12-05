package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class CartInProcAct implements Action {
// 특정 상품을 장바구니에 담는 작업을 처리하는 클래스
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String piid = request.getParameter("piid");
		int occnt = Integer.parseInt(request.getParameter("occnt"));
		String ocoption = request.getParameter("ocoption");

		HttpSession session = request.getSession();
		MemberInfo loginMember = (MemberInfo)session.getAttribute("loginMember");
		
		CartInfo cart = new CartInfo();
		cart.setMi_id(loginMember.getMi_id());
		cart.setPi_id(piid);
		cart.setOc_cnt(occnt);
		cart.setOc_option(ocoption);
		
		CartInProcSvc cartInProcSvc = new CartInProcSvc();
		int result = cartInProcSvc.cartInsert(cart);
		if (result == 0) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('장바구니에 추가시 문제가 발생했습니다.\n다시 시도해 주세요.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
		}

		ActionForward forward = new ActionForward();
		forward.setRedirect(true);	// dispatch가 아닌 sendRedirect 방식으로 이동
		forward.setPath("cart_list.ord");

		return forward;
	}
}
