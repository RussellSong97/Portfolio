

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FirstServlet
 */
@WebServlet(description = "처음을 만든 서블릿", urlPatterns = { "/first" })
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FirstServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 사용자가 get 방식으로 보낸 데이터들을 request 라는 매개변수로 받아 response 로 처리하는 메소드 
		response.getWriter().append("Served at: ").append(request.getContextPath());
		// border가 1인 3행 2열짜리 표를 출력
		response.getWriter().append("Served at: ").append("<table border = '1'><tr><td>o</td><td>o</td></tr><tr><td>o</td><td>o</td></tr><tr><td>o</td><td>o</td></tr></table>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 사용자가 post 방식으로 보낸 데이터들을 request 라는 매개변수로 받아 response 로 처리하는 메소드 
		doGet(request, response);
	}

}
