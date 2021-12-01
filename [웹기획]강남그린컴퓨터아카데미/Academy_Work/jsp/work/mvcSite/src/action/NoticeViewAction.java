package action;

import javax.servlet.http.*;
import java.util.*;
import svc.*;
import vo.*;

public class NoticeViewAction implements Action {
	// 특정 게시글을 볼 때 연결시켜주는 클래스

	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");

		int idx = Integer.parseInt(request.getParameter("idx"));
		int cpage = Integer.parseInt(request.getParameter("cpage"));
		// 글 번호와 페이지번호는 필수이므로 바로 정수형으로 변환하여 받아옴

		String schtype = request.getParameter("schtype");
		String keyword = request.getParameter("keyword");

		NoticeViewSvc noticeViewSvc = new NoticeViewSvc();
		NoticeInfo article = noticeViewSvc.getArticle(idx);
		// 특정 게시글의 대아토둘울 NoticeInfo 형 인스턴스 article에 저장

		request.setAttribute("article", article);
		// 이동할 페이지의 request 객체의 개시글 객체를 담아 넘겨줌(dispatch 방식으로 이동하므로 request 사용가능)

		ActionForward forward = new ActionForward();
		forward.setPath("/bbs/notice_view.jsp");
		// forward 인스턴스의 redirect 멤버변수의 값을 true로 지정하지 않았으므로 dispatch 방식으로 이동함

		return forward;
	}
}
