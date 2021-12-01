package ctrl;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import action.*;
import vo.*;


@WebServlet("*.brd")
public class TestBrdCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
        
    public TestBrdCtrl() {
        super(); 
    }
    
   	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
   		
   		request.setCharacterEncoding("utf-8");

   		String requestUri 	= request.getRequestURI();	 
   		System.out.println("requestUri : " + requestUri);
   		
   		String contextPath 	= request.getContextPath();	 
   		System.out.println("contextPath : " + contextPath);
   		
   		String command 		= requestUri.substring(contextPath.length()); 
   		System.out.println("command : " + command);
   		
   	
   		ActionForward forward = null;	
   		Action action = null;			
   	
   		switch (command) {
			case "/test_list.brd":		// 목록
				action = new TestListAct(); 
				break;
			case "/test_view.brd":		// 보기
				action = new TestViewAct(); 
				break;
			case "/test_form.brd":		// 등록
				action = new TestFormAct();
				break; 
			case "/test_proc.brd":		// 처리
				action = new TestProcAct();
				break; 
		}
   		
   		try {
   			forward = action.execute(request, response); 
   		} catch(Exception e) {
   			e.printStackTrace();
   		}
   		
   		if (forward != null) {
   			if (forward.isRedirect()) {
   				response.sendRedirect(forward.getPath());
   			} else {
   				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
   				dispatcher.forward(request, response); 
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
