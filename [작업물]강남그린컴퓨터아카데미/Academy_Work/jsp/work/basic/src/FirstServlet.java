

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FirstServlet
 */
@WebServlet(description = "ó���� ���� ����", urlPatterns = { "/first" })
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
		// ����ڰ� get ������� ���� �����͵��� request ��� �Ű������� �޾� response �� ó���ϴ� �޼ҵ� 
		response.getWriter().append("Served at: ").append(request.getContextPath());
		// border�� 1�� 3�� 2��¥�� ǥ�� ���
		response.getWriter().append("Served at: ").append("<table border = '1'><tr><td>o</td><td>o</td></tr><tr><td>o</td><td>o</td></tr><tr><td>o</td><td>o</td></tr></table>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// ����ڰ� post ������� ���� �����͵��� request ��� �Ű������� �޾� response �� ó���ϴ� �޼ҵ� 
		doGet(request, response);
	}

}
