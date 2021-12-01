import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/partUploadProc2")
@MultipartConfig(
	fileSizeThreshold = 0, 
	location = "D:/zoom/jsp/work/uploadMail/WebContent/upload"
)
public class PartUploadProc2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public PartUploadProc2() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		String name = request.getParameter("name");
		String uploadFileNameList = "";	// 파일 이름들을 저장할 변수

		for (Part part : request.getParts()) {
		// getParts() : 업로드된 객체들을 Collection<Part>에 담아 리턴하는 메소드
		// getParts()로 받아온 Part객체들을 하나씩 차례대로 Part 형 인스턴스 part에 담아 루프를 돔
			if (!part.getName().equals("name")) {
			// part로 받아온 컨트롤의 이름이 "name"이 아닌 경우(file컨트롤만 작업하겠다는 의미)
				String contentDisposition = part.getHeader("content-disposition");
		    	String uploadFileName = getUploadFileName(contentDisposition);

		    	uploadFileNameList += ", " + uploadFileName;
		    	part.write(uploadFileName);
			}
		}
		if (!uploadFileNameList.equals(""))
			uploadFileNameList = uploadFileNameList.substring(2);
		out.println("업로더 " + name + "님이 " + uploadFileNameList + "파일들을 업로드 했습니다.");
	}

    private String getUploadFileName(String contentDisposition) {
    	String uploadFileName = null;
    	String[] contentSplitStr = contentDisposition.split(";");

    	int firstQutosIndex = contentSplitStr[2].indexOf("\"");
    	int lastQutosIndex = contentSplitStr[2].lastIndexOf("\"");
    	uploadFileName = contentSplitStr[2].substring(firstQutosIndex + 1, lastQutosIndex);

    	return uploadFileName;
    }
}
