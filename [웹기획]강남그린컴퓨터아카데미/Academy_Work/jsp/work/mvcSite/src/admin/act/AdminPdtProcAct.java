package admin.act;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import admin.svc.*;
import vo.*;

public class AdminPdtProcAct implements action.Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String uploadPath = "D:/zoom/jsp/work/mvcSite/WebContent/product/pdt_img";
		// 상품, 설명 이미지들을 저장할 위치를 절대경로로 지정
		int maxSize = 5 * 1024 * 1024;	// 업로드 최대용량은 5MB로 지정

		MultipartRequest multi = new MultipartRequest(
			request, 	// request객체로 multi인스턴스로 전송된 데이터들을 받기 위함
			uploadPath, // 서버에 실제로 파일이 저장될 위치 지정
			maxSize, 	// 한 번에 업로드할 수 있는 최대크기 지정(byte단위)
			"utf-8", 	// 파일의 인코딩 방식
			new DefaultFileRenamePolicy());	// 파일 이름의 중복 처리

		String wtype	= multi.getParameter("wtype");	// 상품정보의 등록/수정 여부
		String id		= multi.getParameter("id");		// 상품정보 수정일 경우 사용할 상품ID
		String csid		= multi.getParameter("csid");
		String bid		= multi.getParameter("bid");
		String origin	= multi.getParameter("origin");
		String name		= multi.getParameter("name");
		String price	= multi.getParameter("price");
		if (price == null || price.equals(""))	price = "0";
		String cost		= multi.getParameter("cost");
		if (cost == null || cost.equals(""))	cost = "0";
		String discount	= multi.getParameter("discount");
		if (discount == null || discount.equals(""))	discount = "0";
		String option	= multi.getParameter("option");
		String pstock	= multi.getParameter("pstock");
		String pisview	= multi.getParameter("pisview");

		Enumeration files = multi.getFileNames();
		// 업로드할 파일명들을 Enumeration형으로 받아옴(빈 file컨트롤은 받아오지 않음)
		String img1 = "", img2 = "", img3 = "", desc = "";
		while (files.hasMoreElements()) {
			String f = (String)files.nextElement();
			switch (f) {
				case "img1" : img1 = multi.getFilesystemName(f);	break;
				case "img2" : img2 = multi.getFilesystemName(f);	break;
				case "img3" : img3 = multi.getFilesystemName(f);	break;
				case "desc" : desc = multi.getFilesystemName(f);	break;
			}
		}

		if (wtype.equals("in")) {
			if (img1 == null || img1.equals("null"))	img1 = "";
			if (img2 == null || img2.equals("null"))	img2 = "";
			if (img3 == null || img3.equals("null"))	img3 = "";
			if (desc == null || desc.equals("null"))	desc = "";
		} else {
		// 상품정보 수정일 경우 이미지 업로드를 하지 않으면 기존의 이미지명을 받아옴
			if (img1 == null || img1.equals("null"))
				img1 = multi.getParameter("oldImg1");
			if (img2 == null || img2.equals("null"))
				img2 = multi.getParameter("oldImg2");
			if (img3 == null || img3.equals("null"))
				img3 = multi.getParameter("oldImg3");
			if (desc == null || desc.equals("null"))
				desc = multi.getParameter("oldDesc");
		}

		ProductInfo pdtInfo = new ProductInfo();
		pdtInfo.setPi_id(id);	// 수정일 경우에만 사용되고, 등록일 경우 사용치 않음
		pdtInfo.setB_id(bid);			pdtInfo.setCs_id(csid);
		pdtInfo.setPi_origin(origin);	pdtInfo.setPi_name(name);
		pdtInfo.setPi_option(option);	pdtInfo.setPi_isview(pisview);
		pdtInfo.setPi_img1(img1);		pdtInfo.setPi_img2(img2);
		pdtInfo.setPi_img3(img3);		pdtInfo.setPi_desc(desc);
		pdtInfo.setPi_price(Integer.parseInt(price));
		pdtInfo.setPi_cost(Integer.parseInt(cost));
		pdtInfo.setPi_discount(Integer.parseInt(discount));
		pdtInfo.setPi_stock(Integer.parseInt(pstock));
		// 사용자가 입력한 정보를 상품정보 저장용 인스턴스에 넣음

		AdminPdtProcSvc adminPdtProcSvc = new AdminPdtProcSvc();
		boolean isSuccess = false;
		String failMsg = "";
		if (wtype.equals("in")) {	// 상품등록일 경우
			HttpSession session = request.getSession();
			AdminInfo ai = (AdminInfo)session.getAttribute("adminInfo");
			pdtInfo.setAi_idx(ai.getAi_idx());	// 등록 관리자 번호 저장

			isSuccess = adminPdtProcSvc.pdtInsert(pdtInfo);
			failMsg = "상품 등록에 실패했습니다.";
		} else {	// 상품정보 수정일 경우
 			isSuccess = adminPdtProcSvc.pdtUpdate(pdtInfo);
			failMsg = "상품 정보 수정에 실패했습니다.";
		}
		if (!isSuccess) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('" + failMsg + "');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
		}

		// 페이지 정보와 검색관련 정보들
		int cpage = Integer.parseInt(multi.getParameter("cpage"));
		String isview	= multi.getParameter("isview");		// 게시여부(y, n)
		String schtype	= multi.getParameter("schtype");	// 검색조건(id, 상품명)
		String keyword	= multi.getParameter("keyword");	// 검색어
		String bcata	= multi.getParameter("bcata");		// 대분류
		String scata	= multi.getParameter("scata");		// 소분류
		String brand	= multi.getParameter("brand");		// 브랜드
		String sprice	= multi.getParameter("sprice");		// 가격대 중 시작 가격
		String eprice	= multi.getParameter("eprice");		// 가격대 중 종료 가격
		String sdate	= multi.getParameter("sdate");		// 등록기간 중 시작일
		String edate	= multi.getParameter("edate");		// 등록기간 중 종료일
		String stock	= multi.getParameter("stock");		// 재고량(이상)

		// 검색관련 쿼리스트링 제작
		String args = "";
		if (isview == null) args += "&isview=";	else args += "&isview=" + isview;
		if (schtype == null) args += "&schtype="; else args += "&schtype=" + schtype;
		if (keyword == null) args += "&keyword="; else args += "&keyword=" + keyword;
		if (bcata == null) args += "&bcata="; else args += "&bcata=" + bcata;
		if (scata == null) args += "&scata="; else args += "&scata=" + scata;
		if (brand == null) args += "&brand="; else args += "&brand=" + brand;
		if (sprice == null) args += "&sprice="; else args += "&sprice=" + sprice;
		if (eprice == null) args += "&eprice="; else args += "&eprice=" + eprice;
		if (sdate == null) args += "&sdate="; else args += "&sdate=" + sdate;
		if (edate == null) args += "&edate="; else args += "&edate=" + edate;
		if (stock == null) args += "&stock="; else args += "&stock=" + stock;

		ActionForward forward = new ActionForward();
		forward.setRedirect(true);	// redirect 방식으로 이동하도록 지정
		forward.setPath("pdt_list.pdta?cpage=" + cpage + args);

		return forward;
	}
}
