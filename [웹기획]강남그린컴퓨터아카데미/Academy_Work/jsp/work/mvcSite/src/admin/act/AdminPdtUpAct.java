package admin.act;

import javax.servlet.http.*;
import java.util.*;
import admin.svc.*;
import vo.*;

public class AdminPdtUpAct implements action.Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProductInfo pdtInfo = new ProductInfo();	// 수정할 상품정보를 저장할 인스턴스

		request.setCharacterEncoding("utf-8");
		int cpage = Integer.parseInt(request.getParameter("cpage"));
		String id = request.getParameter("id");	// 수정할 상품 ID

		// 검색조건(검색어, 대/소분류, 브랜드, 가격대) 쿼리스트링
		String isview	= request.getParameter("isview");	// 게시여부(y, n)
		String schtype	= request.getParameter("schtype");	// 검색조건(id, 상품명)
		String keyword	= request.getParameter("keyword");	// 검색어
		String bcata	= request.getParameter("bcata");	// 대분류
		String scata	= request.getParameter("scata");	// 소분류
		String brand	= request.getParameter("brand");	// 브랜드
		String sprice	= request.getParameter("sprice");	// 가격대 중 시작 가격
		String eprice	= request.getParameter("eprice");	// 가격대 중 종료 가격
		String sdate	= request.getParameter("sdate");	// 등록기간 중 시작일
		String edate	= request.getParameter("edate");	// 등록기간 중 종료일
		String stock	= request.getParameter("stock");	// 재고량(이상)
		String ord 		= request.getParameter("ord");		// 정렬조건

		PdtPageInfo pageInfo = new PdtPageInfo();
		pageInfo.setCpage(cpage);		// 현재 페이지 번호
		pageInfo.setSchtype(schtype);	// 검색조건(아이디 or 상품명)
		pageInfo.setKeyword(keyword);	// 검색어
		pageInfo.setBcata(bcata);		// 대분류
		pageInfo.setScata(scata);		// 소분류
		pageInfo.setBrand(brand);		// 브랜드
		pageInfo.setSprice(sprice);		// 가격대(시작가)
		pageInfo.setEprice(eprice);		// 가격대(종료가)
		pageInfo.setIsview(isview);		// 게시여부
		pageInfo.setSdate(sdate);		// 등록기간 중 시작일
		pageInfo.setEdate(edate);		// 등록기간 중 종료일
		pageInfo.setStock(stock);		// 재고량
		pageInfo.setOrd(ord);			// 정렬조건

		AdminPdtUpSvc adminPdtUpSvc = new AdminPdtUpSvc();
		pdtInfo = adminPdtUpSvc.getPdtInfo(id);

		AdminPdtListSvc adminPdtListSvc = new AdminPdtListSvc();
		ArrayList<CataBigInfo> cataBigList = adminPdtListSvc.getCataBigList();
		ArrayList<CataSmallInfo> cataSmallList = adminPdtListSvc.getCataSmallList();
		ArrayList<BrandInfo> brandList = adminPdtListSvc.getBrandList();

		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("pdtInfo", pdtInfo);
		request.setAttribute("cataBigList", cataBigList);
		request.setAttribute("cataSmallList", cataSmallList);
		request.setAttribute("brandList", brandList);

		ActionForward forward = new ActionForward();
		forward.setPath("/admin/product/pdt_up_form.jsp");

		return forward;
	}
}
