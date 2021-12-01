package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class CartUpProcAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String kind = request.getParameter("kind");	// 수량(cnt) or 옵션(opt) 여부
		String op = request.getParameter("op");		// 수량이면 연산자, 옵션이면 옵션값
		int idx = Integer.parseInt(request.getParameter("idx")); // 카트 인덱스번호(PK)
		String piid = request.getParameter("piid");	// 상품ID로 옵션 변경시 필요

		HttpSession session = request.getSession();
		MemberInfo loginMember = (MemberInfo)session.getAttribute("loginMember");

		CartUpProcSvc cartUpProcSvc = new CartUpProcSvc();
		int result = cartUpProcSvc.cartUpdate(kind, op, idx, loginMember.getMi_id(), piid);

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.close();

		return null;
	}
}
