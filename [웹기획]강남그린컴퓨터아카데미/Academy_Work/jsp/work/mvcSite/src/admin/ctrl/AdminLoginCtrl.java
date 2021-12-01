package admin.ctrl;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import admin.svc.*;
import vo.*;

@WebServlet("/adminLogin")
public class AdminLoginCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public AdminLoginCtrl() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String uid = request.getParameter("uid");
		String pwd = request.getParameter("pwd");
		String url = request.getParameter("url");
		if (url.equals(""))	url = "admin/index.jsp";

		AdminLoginSvc adminLoginSvc = new AdminLoginSvc();
		AdminInfo adminInfo = adminLoginSvc.getLoginAdmin(uid, pwd);
		HttpSession session = request.getSession();

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		if (adminInfo != null) {
			session.setAttribute("adminInfo", adminInfo);
			response.sendRedirect(url);
		} else {
			out.println("<script>");
			out.println("alert('로그인에 실패했습니다.');");
			out.println("history.back();");
			out.println("</script>");
		}
	}
}
