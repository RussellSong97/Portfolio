package action;

import javax.servlet.http.*;
import java.util.*;
import svc.*;
import vo.*;

public class TestListAct implements Action {

	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Action 인터페이스의 abstract 메소드인 execute()를 오버라이딩
		ArrayList<TestInfo> baordList = new ArrayList<TestInfo>();
		// 공지사항 게시글의 목록을 저장할 ArrayList 인스턴스로 TestInfo형 인스턴스만 저장함

		request.setCharacterEncoding("utf-8");
		int cpage = 1, psize = 10, bsize = 10;
		// 현재 페이지 번호와 한 페이지에서 보여줄 게시글 개수, 한 블록에서 보여줄 페이지 개수를 저장할 변수

		if (request.getParameter("cpage") != null)
			cpage = Integer.parseInt(request.getParameter("cpage"));
		// 현재 페이지번호가 있으면 받아온 페이지 번호를 정수형으로 형변환하여 저장
		// DB 인젝션 공격을 막기 위함과 cpage로 산술연산을 해야 하기 때문에 int형으로 형변환

		String schtype = request.getParameter("schtype"); // 검색조건으로 제목, 내용, 제목+내용
		String keyword = request.getParameter("keyword"); // 검색어

		String where = ""; // 검색어가 있을 경우에만 값이 들어감
		if (keyword != null && !keyword.equals("")) { // 검색어가 있을 경우에만 where 설정
			if (schtype.equals("tc")) {
				where = " where b_title like '%" + keyword + "%' or b_content like '%" + keyword + "%' ";
			} else {
				where = " where b_" + schtype + " like '%" + keyword + "%' ";
			} 
		}

		TestListSvc noticeListSvc = new TestListSvc(); 

		int rcnt = noticeListSvc.getBoardCount(where);  
		baordList = noticeListSvc.getBoardList(where, cpage, psize);
		// 목록 화면에서 보여줄 게시글 목록을 ArrayList로 받아옴

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
		pageInfo.setSchtype(schtype); // 검색조건
		pageInfo.setKeyword(keyword); // 검색
		pageInfo.setPsize(psize); // 페이지크기
		pageInfo.setBsize(bsize); // 블록크기

		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("brdList", baordList); 

		ActionForward forward = new ActionForward();
		forward.setPath("/test_list.jsp");

		return forward;
	}

}
