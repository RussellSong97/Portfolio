import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/partUploadProc1")
@MultipartConfig(
		fileSizeThreshold = 0 , 
		location = "D:/project/greenart_study/jsp/work/uploadMail/WebContent/upload"
)
public class PartUploadProc1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
        
    public PartUploadProc1() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String name = request.getParameter("name");
		// cos.jar와 다르게 request객체를 사용할 수 있음
		Part part = request.getPart("file1");
		// 업로드되는 파일을 Part형 인스턴스에 담음
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		
		String contentDisposition = part.getHeader("content-disposition");
		System.out.println(contentDisposition);	// 콘솔창에서 디버깅		// 예) form-data; name="file1"; filename="1. 회원테이블작성.PNG"
		
		String uploadFileName = getUploadFileName(contentDisposition);	// 업로드할 파일의 이름을 추출.		예) 1. 회원테이블작성.PNG
		System.out.println(uploadFileName);		// 콘솔창에서 디버깅		
		
		part.write(uploadFileName);
		out.println("작성자 " + name + "님이 " + uploadFileName + " 파일을 업로드 하였습니다.");
		
	}

	/**  파일 이름 추출 	 */
	private String getUploadFileName(String contentDisposition) {
		String uploadFileName = null;
		String[] contentSplitStr = contentDisposition.split(";");
		// contentDisposition 문자열을 세미콜론을 기준으로 String 배열로 생성
		
		// contentSplitStr[2] 에 들어있는 값 - filename="1. 회원테이블작성.PNG"
		int firstQutosIndex = contentSplitStr[2].indexOf("\"");
		// contentSplitStr[2] 에 들어있는 값에서 첫번째 큰 따옴표의 위치 저장 (문자열 자르기 시작위치)
		int lastQutosIndex = contentSplitStr[2].lastIndexOf("\"");
		// contentSplitStr[2] 에 들어있는 값에서 마지막 큰 따옴표의 위치 저장 (문자열 자르기 종료위치)
		
		uploadFileName = contentSplitStr[2].substring(firstQutosIndex + 1, lastQutosIndex);
		// contentSplitStr[2] 에 들어있는 값에서 파일이름 부분만 추출
				
		return uploadFileName;
		
	}
}
