package action;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class ProductAction implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<ProductInfo> productList = new ArrayList<ProductInfo>();
		request.setCharacterEncoding("utf-8");
		int cpage = 1, psize = 8, bsize = 10;
		if (request.getParameter("cpage") != null)
			cpage = Integer.parseInt(request.getParameter("cpage"));
		
		ProductService productSerevice = new ProductService(); 
		String where = "";
		int rcnt = productSerevice.getProductCount(where);  
		productList = productSerevice.getProductList(where, cpage, psize);
		
		int pcnt = rcnt / psize;
		if (rcnt % psize > 0)
			pcnt++; /// 전체 페이지 수
		int spage = (cpage - 1) / bsize * bsize + 1; // 블록 시작 페이지 번호
		int epage = spage + bsize - 1;
		if (epage > pcnt)
			epage = pcnt; // 블록 종료 페이지 번호
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setCpage(cpage); // 현재 페이지 번호
		pageInfo.setRcnt(rcnt); // 전체 게시글 개수
		pageInfo.setPcnt(pcnt); // 전체 페이지 개수
		pageInfo.setSpage(spage); // 블록 시작 페이지 번호
		pageInfo.setEpage(epage); // 블록 종료 페이지 번호 
		pageInfo.setPsize(psize); // 페이지크기
		pageInfo.setBsize(bsize); // 블록크기
		  
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("productList", productList); 
	  
		ActionForward forward = new ActionForward(); 
		forward.setPath("/product/product_list.jsp");

		return forward;
	}
}
