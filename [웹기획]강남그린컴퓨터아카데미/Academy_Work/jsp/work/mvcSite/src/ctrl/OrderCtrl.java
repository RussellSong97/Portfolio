package ctrl;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import action.*;
import vo.*;

@WebServlet("*.ord")
public class OrderCtrl extends HttpServlet {
// 주문관련 기능들에 대한 컨트롤러(장바구니, 결제 등)
	private static final long serialVersionUID = 1L;
    public OrderCtrl() {
        super();
    }

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

    	String requestUri = request.getRequestURI();
    	String contextPath = request.getContextPath();
    	String command = requestUri.substring(contextPath.length());

    	ActionForward forward = null;
    	Action action = null;

    	switch (command) {
    		case "/cart_list.ord" :		// 장바구니 폼 화면 처리
    			action = new CartListAct();
    			break;
    		case "/cart_in_proc.ord" :	// 장바구니 담기 처리
    			action = new CartInProcAct();
    			break;
    		case "/cart_up_proc.ord" :	// 장바구니 수정 처리
    			break;
    		case "/cart_del_proc.ord" :	// 장바구니 삭제 처리
    			break;
    		case "/order_form.ord" :	// 결제 폼 화면 처리
    			break;
    		case "/order_proc.ord" :	// 결제 처리
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
    			RequestDispatcher dispatcher = 
    				request.getRequestDispatcher(forward.getPath());
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
