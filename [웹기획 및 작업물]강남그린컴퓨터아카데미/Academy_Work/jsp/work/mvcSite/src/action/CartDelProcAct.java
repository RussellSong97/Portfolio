package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class CartDelProcAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String kind = request.getParameter("kind");	// 전체(0), 일부(-1), 하나의 삭제 여부
		String idx = request.getParameter("idx");	// 삭제할 카트인덱스번호(들)

		HttpSession session = request.getSession();
		MemberInfo loginMember = (MemberInfo)session.getAttribute("loginMember");

		String where = " where mi_id = '" + loginMember.getMi_id() + "' ";
		if (kind.equals("-1")) {	// 선택한 상품(들)을 삭제할 경우
			String[] arr = idx.split(",");
			String tmpWhere = "";
			for (int i = 0 ; i < arr.length ; i++) {
				tmpWhere += " or oc_idx = " + arr[i];
			}
			where += " and (" + tmpWhere.substring(4) + ")";
		} else if (!kind.equals("0")) {	// 한 상품만 삭제할 경우
			where += " and oc_idx = " + idx;
		}

		CartDelProcSvc cartDelProcSvc = new CartDelProcSvc();
		int result = cartDelProcSvc.cartDelete(where);

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.close();

		return null;
	}
}
