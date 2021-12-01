package ctrl;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import action.*;
import vo.*;
 
@WebServlet("*.pdt")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ProductServlet() {
        super(); 
    }
    
   	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
   		request.setCharacterEncoding("utf-8");
   		String requestUri 	= request.getRequestURI();
   		System.out.println("requestUri : " + requestUri);
   		// URI(도메인과 쿼리 스트링을 뺀 주소 문자열) : /mvcSite/brd_list.ntc
   		
   		String contextPath 	= request.getContextPath();
   		System.out.println("contextPath : " + contextPath);
   		// URI에서 파일명 부분을 제외한 문자열 :  /mvcSite
   		
   		String command 		= requestUri.substring(contextPath.length());
   		System.out.println("command : " + command);
        
        ActionForward forward = null;
        Action action = null;

        switch (command) {
	        case "/pdt_list.pdt" :    // 상품 목록  
	        	action = new ProductAction();  
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
