package ctrl;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;   // request, response, session 등의 객체를 사용하기 위함
import svc.*;
import vo.*;

@WebServlet("/login")
public class LoginCtrl extends HttpServlet {
   private static final long serialVersionUID = 1L;
    public LoginCtrl() {
        super();
    }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // post 방식으로 데이터를 보냇을 경우 동작하는 서블릿의 메소드
      request.setCharacterEncoding("utf-8");
      // 사용자의 요청으로 받은 데이터들의 인코딩 방식을 유니코드로 지정
      String uid = request.getParameter("uid");
      String pwd = request.getParameter("pwd");
      // 사용자가 보낸 값들이 들어있는 컨트롤(uid, pwd)들의 값을 각각 uid, pwd로 받음
      
      LoginSvc loginSvc = new LoginSvc();
      // 로그인에 필요한 작업을 위해 로그인 기능의 메소드를 가진 클래스의 인스턴스 생성
      MemberInfo loginMember = loginSvc.getLoginMember(uid,pwd);
      // 사용자에게 입력받은 아이디와 비밀번홀ㄹ 인수로 하여 loginSvc 인스턴스의 getLoginMember() 메소드를 실행하고 그 결과를
      // MemberInfo형 인스턴스 loginMember에 저장.
      // 받아온 loginMember 인스턴스에는 로그인한 회원의 정보들이 들어있음.
      // 단, 로그인 실패시 loginMember 인스턴에는 null값이 들어있게 됨.
      
      HttpSession session = request.getSession();
      // JSP와 다르게 세션 객체를 사용하려면 직접 HttpSession 인스턴스를 생성해야 함. 
      
      response.setContentType("text/html; charset=utf-8");
      // 응답하는 페이지의 타입을 text나 html로 지정 - 인코딩은 유니코드로 지정
      PrintWriter out = response.getWriter();
      
      if(loginMember != null) {      //로그인 성공시
         session.setAttribute("loginMember", loginMember);
         // 로그인 상태를 유지하고, 다른 페이지에서도 로그인 정보를 사용할 수 있도록 세션에 저장
         /*
         out.println("<script>");
         out.println("location.href = 'index.jsp';");
         out.println("</script>");
         */
         response.sendRedirect("index.jsp");
      } else {      //로그인 실패시
         out.println("<script>");
         out.println("alert('로그인에 실패했습니다.');");
         out.println("history.back();");
         out.println("</script>");
      }
   }
}