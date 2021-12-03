package action;

import javax.servlet.http.*;
import java.util.*;
import svc.*;
import vo.*;

//게시글 목록을 받아와 연결하는 클래스
public class NoticeListAction implements Action {

	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Action 인터페이스의 abstract 메소드인 execute()를 오버라이딩
		ArrayList<NoticeInfo> articleList = new ArrayList<NoticeInfo>();
		// 공지사항 게시글의 목록을 저장할 ArrayList 인스턴스로 NoticeInfo형 인스턴스만 저장함

		request.setCharacterEncoding("utf-8");
		int cpage = 1, psize = 10, bsize = 10;
		// 현재 페이지 번호와 한 페이지에서 보여줄 게시글 개수, 한 블록에서 보여줄 페이지 개수를 저장할 변수

		if (request.getParameter("cpage") != null)
			cpage = Integer.parseInt(request.getParameter("cpage"));
		// 현재 페이지번호가 있으면 받아온 페이지 번호를 정수형으로 형변환하여 저장
		// DB 인젝션 공격을 막기 위함과 cpage로 산술연산을 해야 하기 때문에 int형으로 형변환
		
		String schtype = request.getParameter("schtype");	// 검색조건으로 제목, 내용, 제목+내용
		String keyword = request.getParameter("keyword");	// 검색어
		
		String where = "";	// 검색어가 있을 경우에만 값이 들어감
		if (keyword != null && !keyword.equals("")) {	// 검색어가 있을 경우에만 where 설정
			if (schtype.equals("tc")) {
				where = " where bn_title like '%" + keyword + "%' or bn_content like '%" + keyword + "%' ";
			} else {
				where = " where bn_"+ schtype +" like '%" + keyword + "%' ";	
			}
			
		}

		NoticeListSvc noticeListSvc = new NoticeListSvc();
		// 게시판 목록을 위한 비즈니스 로직을 처리하기 위한 NoticeListSvc 인스턴스 생성

		int rcnt = noticeListSvc.getArticleCount(where);
		// 총 레코드 개수를 받아옴(전체 페이지수를 알기 위해)
		
		articleList = noticeListSvc.getArticleList(where, cpage, psize);
		// 목록 화면에서 보여줄 게시글 목록을 ArrayList로 받아옴
		
		int pcnt = rcnt / psize; 
		if (rcnt % psize > 0) 	pcnt++;		/// 전체 페이지 수 
		
		int spage = (cpage - 1) / bsize * bsize + 1;	// 블록 시작 페이지 번호 
		int epage = spage + bsize - 1 ;					
		if (epage > pcnt)	epage = pcnt;				// 블록 종료 페이지 번호 
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setCpage(cpage);		// 현재 페이지 번호
		pageInfo.setRcnt(rcnt);			// 전체 게시글 개수 
		pageInfo.setPcnt(pcnt);			// 전체 페이지 개수 
		pageInfo.setSpage(spage);		// 블록 시작 페이지 번호 
		pageInfo.setEpage(epage);		// 블록 종료 페이지 번호 
		pageInfo.setSchtype(schtype);	// 검색조건  
		pageInfo.setKeyword(keyword);	// 검색 
		pageInfo.setPsize(psize);		// 페이지크기  
		pageInfo.setBsize(bsize);		// 블록크기
		// 목록 페이지 구성에 필요한 값들을 목록 화면으로 보내기 쉽게 하기 위해 하나의 인스턴스에 담음 

		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("articleList", articleList);
		// notice_listjsp로 request 를 통해 전달해줄 객체를 request에 저장 
		// dispatch방식으로 이동할 것이므로 이동하는 곳에서 기존 파일의 request와 response를 사용할 수 있음

		ActionForward forward = new ActionForward();
		forward.setPath("/bbs/notice_list.jsp");
		// forward 인스턴스의 setPath() 메소드를 이용하여 forward 인스턴스의 path 멤버변수의 값을 지정
		
		return forward;
	}
}
