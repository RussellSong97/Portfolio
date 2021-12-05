package ctrl;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import action.*;
import vo.*;


@WebServlet("*.ntc")
// url이 뭐든 마지막 4글자가 '.ntc'인 url을 모두 매핑시켜 받아 옴(공지사항-notice 관련 기능을 모두 처리)
public class NoticeCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
        
    public NoticeCtrl() {
        super(); 
    }
    
   	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		// 사용자의 요청이 get이든 post든 모두 처리하는 메소드
   		request.setCharacterEncoding("utf-8");

   		
   		// ***** 해당 요청을 구분하기 위해 url을 잘라냄 *****
   		String requestUri 	= request.getRequestURI();
   		System.out.println("requestUri : " + requestUri);
   		// URI(도메인과 쿼리 스트링을 뺀 주소 문자열) : /mvcSite/brd_list.ntc
   		
   		String contextPath 	= request.getContextPath();
   		System.out.println("contextPath : " + contextPath);
   		// URI에서 파일명 부분을 제외한 문자열 :  /mvcSite
   		
   		String command 		= requestUri.substring(contextPath.length());
   		System.out.println("command : " + command);
   		// requestUri에서 contextPath를 뺀 문자열 :  /brd_list.ntc
   	
   		ActionForward forward = null;	// 처리 후 이동할 view에 해당하는 링크와 이동방식을 얻기 위한 인스턴스
   		Action action = null;			// 기능 별로 처리할 때 동일한 메소드로 작업하기 위해 선언된 인스턴스
   										// 인터페이스이므로 인스턴스를 생성할 수는 없음
   	
   		// 사용자의 요청에 따라 각기 다른 action을 작업
   		switch (command) {
		case "/brd_list.ntc":		// 공지사항 목록 요청
			action = new NoticeListAction();
			// Action을 implements하는 NoticeListAction클래스를 이용하여 인스턴스 생성
			break;
		case "/brd_form.ntc":		// 공지사항 폼 요청
			
			break;
		case "/brd_view.ntc":		// 공지사항 보기 요청
			action = new NoticeViewAction();
			break;
		case "/brd_proc.ntc":		// 공지사항 처리 요청
			
			break;
	
			default:
				break;
		}
   		
   		try {
   			forward = action.execute(request, response);
   			//각 요청에 따른 Action을 implements한 클래스의  execute() 메소드를 실행
   		} catch(Exception e) {
   			e.printStackTrace();
   		}
   		
   		if (forward != null) {
   			if (forward.isRedirect()) {
   				response.sendRedirect(forward.getPath());
   			} else {
   				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
   				dispatcher.forward(request, response);
   				// RequestDispatcher 를 통해 이동시키면 이동한 페이지의 URL이 변하지 않고, 
   				// 이동한 페이지로 현재 가지고 있는 request와 response 객체를 그대로 넘겨줌 
   			}
   		}
   	}
    
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		doProcess(request, response);
   	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
