package admin.act;

import javax.servlet.http.*;
import java.util.*;
import admin.svc.*;
import vo.*;

public class AdminPdtListAct implements action.Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();

		request.setCharacterEncoding("utf-8");
		int cpage = 1, psize = 15, bsize = 10, spage, epage, rcnt, pcnt;
		if (request.getParameter("cpage") != null)
			cpage = Integer.parseInt(request.getParameter("cpage"));

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

		// 검색 조건에 따른 where절 생성
		String where = " where 1=1 ";
		if (!isEmpty(isview))	where += " and pi_isview = '" + isview + "' ";
		else	isview = "";
		if (!isEmpty(keyword))	where += " and pi_" + schtype + " like '%" + keyword + "%' ";
		else { keyword = "";	schtype = ""; }
		if (!isEmpty(bcata))	where += " and cb_id = '" + bcata + "' ";
		else	bcata = "";
		if (!isEmpty(scata))	where += " and cs_id = '" + scata + "' ";
		else	scata = "";
		if (!isEmpty(brand))	where += " and b_id = '" + brand + "' ";
		else	brand = "";
		if (!isEmpty(sprice))	where += " and pi_price >= '" + sprice + "' ";
		else	sprice = "";
		if (!isEmpty(eprice))	where += " and pi_price <= '" + eprice + "' ";
		else	eprice = "";
		if (!isEmpty(sdate))	where += " and pi_date >= '" + sdate + " 00:00:00' ";
		else	sdate = "";
		if (!isEmpty(edate))	where += " and pi_date <= '" + edate + " 23:59:59' ";
		else	edate = "";
		if (!isEmpty(stock))	where += " and (pi_stock = -1 or pi_stock >= '" + stock + "') ";
		else	stock = "";

		// 정렬조건(판매량-내, 가격-오내, 등록-내(기본), 상품명-오, 리뷰-내) 쿼리스트링
		String ord = request.getParameter("ord");
		if (ord == null || ord.equals(""))	ord = "idd";	// 등록 역순으로 정렬이 기본값
		String orderBy = " order by a.pi_" + ord.substring(0, ord.length() - 1) + 
			(ord.charAt(ord.length() - 1) == 'a' ? " asc" : " desc");
		// 정렬조건에 따른 order by절 생성

		AdminPdtListSvc adminPdtListSvc = new AdminPdtListSvc();
		rcnt = adminPdtListSvc.getPdtCount(where);
		pdtList = adminPdtListSvc.getPdtList(where, orderBy, cpage, psize);

		pcnt = rcnt / psize;
		if (rcnt % psize > 0)	pcnt++;				// 전체 페이지 수
		spage = (cpage - 1) / bsize * bsize + 1;	// 블록의 시작 페이지 번호
		epage = spage + bsize - 1;
		if (epage > pcnt)		epage = pcnt;		// 블록의 종료 페이지 번호

		PdtPageInfo pageInfo = new PdtPageInfo();	// 페이징에 필요한 정보들을 저장할 인스턴스
		// 페이징에 필요한 정보들
		pageInfo.setCpage(cpage);		// 현재 페이지 번호
		pageInfo.setRcnt(rcnt);			// 전체 게시글 개수
		pageInfo.setPcnt(pcnt);			// 전체 페이지 개수
		pageInfo.setSpage(spage);		// 블록 시작 페이지 번호
		pageInfo.setEpage(epage);		// 블록 종료 페이지 번호
		pageInfo.setPsize(psize);		// 페이지 크기
		pageInfo.setBsize(bsize);		// 블록 크기

		// 검색관련 정보들
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

		ArrayList<CataBigInfo> cataBigList = adminPdtListSvc.getCataBigList();
		ArrayList<CataSmallInfo> cataSmallList = adminPdtListSvc.getCataSmallList();
		ArrayList<BrandInfo> brandList = adminPdtListSvc.getBrandList();

		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("pdtList", pdtList);
		request.setAttribute("cataBigList", cataBigList);
		request.setAttribute("cataSmallList", cataSmallList);
		request.setAttribute("brandList", brandList);

		ActionForward forward = new ActionForward();
		forward.setPath("/admin/product/pdt_list.jsp");

		return forward;
	}

	private boolean isEmpty(String str) {	// 매개변수에 어떤 값이든 들어 있는지 여부를 검사하는 메소드
		boolean empty = true;
		if (str != null && !str.equals(""))	empty = false;
		return empty;
	}
}
