package ctrl;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import action.*;
import vo.*;

@WebServlet("*.mem")
public class MemberCtrl extends HttpServlet {
// 회원관련 작업(가입, 정보수정, 탈퇴)를 처리하는 컨트롤러
   
   private static final long serialVersionUID = 1L;
   
    public MemberCtrl() {
        super();
    }

   protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        
        // 해당 요청을 구분하기 위해 url을 잘라냄
        String requestUri    = request.getRequestURI();        
        String contextPath    = request.getContextPath();       
        String command       = requestUri.substring(contextPath.length());
        
        ActionForward forward = null;
        Action action = null;
        // 기능 별로 처리할 때 동일한 메소드로 작업하기 위해 선언된 인스턴스
        // 인터페이스이므로 인스턴스를 생성할 수는 없음
        
        // 사용자의 요청에 따라 각기 다른 action을 작업함
        switch (command) {
	        case "/member/proc.mem" :    // 회원관련 처리작업(가입, 수정, 탈퇴) 요청
	           action =  new MemberProcAct();
	           break;
	        case "/member/mypage.mem" :   // 회원 정보 수정 폼 요청
	           action = new MemberUpdateAct();
	           break;
        }
        
        try {
              forward = action.execute(request, response);
              // 각 요청에 따른 클래스 Action을  implements한 클래스의 execute()메소드를 실행
        } catch (Exception e) {
           e.printStackTrace();
        }
        
        if (forward != null) {
              if (forward.isRedirect()) {
                 response.sendRedirect(forward.getPath());
              } else {
                 RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
                 dispatcher.forward(request, response);
                 // RequestDispatcher를 통해 이동시키면 이동한 페이지의 URL이 변하지 않고, 
                 // 이동한 페이지로 현재 가지고 있는 request 와 response 객체를 그대로 넘겨줌
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