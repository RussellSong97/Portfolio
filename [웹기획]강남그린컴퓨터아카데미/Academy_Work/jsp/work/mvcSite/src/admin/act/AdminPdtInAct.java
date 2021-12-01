package admin.act;

import javax.servlet.http.*;
import java.util.*;
import admin.svc.*;
import vo.*;

public class AdminPdtInAct implements action.Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");

		AdminPdtInSvc adminPdtInSvc = new AdminPdtInSvc();

		ArrayList<CataBigInfo> cataBigList = adminPdtInSvc.getCataBigList();
		ArrayList<CataSmallInfo> cataSmallList = adminPdtInSvc.getCataSmallList();
		ArrayList<BrandInfo> brandList = adminPdtInSvc.getBrandList();

		request.setAttribute("cataBigList", cataBigList);
		request.setAttribute("cataSmallList", cataSmallList);
		request.setAttribute("brandList", brandList);

		ActionForward forward = new ActionForward();
		forward.setPath("/admin/product/pdt_in_form.jsp");

		return forward;
	}
}
