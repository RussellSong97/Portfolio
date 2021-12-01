
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/loginProc")
public class LoginProc extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginProc() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		// 받아오는 데이터의 캐릭터인코딩을 유니코드로 지정(한글이 깨지지 않게 하기 위해)
		String idGet = request.getParameter("idGet");
		String pwGet = request.getParameter("pwGet");
		// 지정된 이름의 컨트롤 값을 변수에 저장
		
		response.setContentType("text/html; charset=utf-8");
		// 사용자에게 응답하는 내용의 형식을 지정 (태그를 사용할 수 있는 html 형식에 인코딩은 유니코드로 지정)
		
		PrintWriter out = response.getWriter();
		// 사용자의 요청에 대한 응답을 할 수 있게 해주는 객체를 생성 (이왕이면 jsp와 같게 out으로 지정)
		
		out.println("아이디 : " + idGet + "<br />");
		out.println("비밀번호 : " + pwGet + "<br />");
		// 사용자에게 응답함
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// doGet(request, response);
		request.setCharacterEncoding("utf-8");
		String idpw = request.getParameter("idpw");		// 아이디/비밀번호 찾기 구분
		String uname = request.getParameter("uname");	// 사용자 이름
		String uid = request.getParameter("uid");		// 사용자 아이디
		String e1 = request.getParameter("e1");			// 이메일 아이디 부분
		String e2 = request.getParameter("e2");			// 이메일 도메인 부분

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();			// 사용자에게 보여줄 준비
		
		if (idpw.equals("id")) {	// 아이디 찾기이면
			out.println("<h3>아이디 찾기</h3>");
			out.println("이름 : " + uname + "<br />");
			out.println("이메일 : " + e1 + "@" + e2 + "<br />");
		} else {			// 비밀번호 찾기이면
			out.println("<h3>비밀번호 찾기</h3>");
			out.println("이름 : " + uname + "<br />");
			out.println("아이디 : " + uid + "<br />");
			out.println("이메일 : " + e1 + "@" + e2 + "<br />");
		}
			
		//System.out.println(uid + ":" + uname + ":" + e1 + ":" + e2);		// 개발자 확인
		
	}

}
