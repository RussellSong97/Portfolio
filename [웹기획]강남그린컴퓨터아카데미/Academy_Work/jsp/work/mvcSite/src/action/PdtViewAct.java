package action;

import javax.servlet.http.*;
import java.util.*;
import svc.*;
import vo.*;

import vo.ActionForward;

public class PdtViewAct implements Action {
   public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
      request.setCharacterEncoding("utf-8");
      String id = request.getParameter("id");                     // 상품ID
      int cpage = Integer.parseInt(request.getParameter("cpage"));   // 현재 페이지 번호
      int psize = Integer.parseInt(request.getParameter("psize"));   // 페이지크기&목록보기방식

      // 검색조건(검색어, 대/소분류, 브랜드, 가격대) 및 정렬조건 쿼리스트링
      // 목록으로 돌아갈때 필요..
      String keyword, bcata, scata, brand, sprice, eprice, ord;
      keyword   = request.getParameter("keyword");   // 검색어
      bcata   = request.getParameter("bcata");   // 대분류
      scata   = request.getParameter("scata");   // 소분류
      brand   = request.getParameter("brand");   // 브랜드
      sprice   = request.getParameter("sprice");   // 가격대 중 시작 가격
      eprice   = request.getParameter("eprice");   // 가격대 중 종료 가격
      ord = request.getParameter("ord");         // 정렬조건
      
      PdtViewSvc pdtViewSvc = new PdtViewSvc();
      ProductInfo pdtInfo = pdtViewSvc.getPdtInfo(id);
      
       request.setAttribute("pdtInfo", pdtInfo);

       ActionForward forward = new ActionForward();
       forward.setPath("/product/pdt_view.jsp");

       return forward;
      
   }
      
}