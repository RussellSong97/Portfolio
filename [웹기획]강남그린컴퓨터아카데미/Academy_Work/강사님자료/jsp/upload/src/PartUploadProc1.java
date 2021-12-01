import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/partUploadProc1")
@MultipartConfig(
	fileSizeThreshold = 0, 
	location = "D:/zoom/jsp/work/uploadMail/WebContent/upload"
)
public class PartUploadProc1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public PartUploadProc1() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("utf-8");
    	String name = request.getParameter("name");
    	// cos.jar�� �ٸ��� request��ü�� ����� �� ����
    	Part part = request.getPart("file1");
    	// ���ε�Ǵ� ������ Part�� �ν��Ͻ��� ����

    	response.setContentType("text/html; charset=utf-8");
    	PrintWriter out = response.getWriter();
    	
    	String contentDisposition = part.getHeader("content-disposition");
    	// ��) form-data; name="file1"; filename="����Ʈ���ۼ���.txt"

    	String uploadFileName = getUploadFileName(contentDisposition);
    	// ���ε��� ������ �̸��� ����. ��) ����Ʈ���ۼ���.txt

    	part.write(uploadFileName);
    	out.println("�ۼ��� " + name + "���� " + uploadFileName + " ������ ���ε� �Ͽ����ϴ�.");
    }

    private String getUploadFileName(String contentDisposition) {
    	String uploadFileName = null;
    	String[] contentSplitStr = contentDisposition.split(";");
    	// contentDisposition���ڿ��� �����ݷ��� �������� String�迭�� ����

    	// contentSplitStr[2] �� ����ִ� �� - filename="����Ʈ���ۼ���.txt"
    	int firstQutosIndex = contentSplitStr[2].indexOf("\"");
    	// contentSplitStr[2] �� ����ִ� ������ ù��° ū ����ǥ�� ��ġ ����(���ڿ� �ڸ��� ������ġ)
    	int lastQutosIndex = contentSplitStr[2].lastIndexOf("\"");
    	// contentSplitStr[2] �� ����ִ� ������ ������ ū ����ǥ�� ��ġ ����(���ڿ� �ڸ��� ������ġ)

    	uploadFileName = contentSplitStr[2].substring(firstQutosIndex + 1, lastQutosIndex);
    	// contentSplitStr[2] �� ����ִ� ������ �����̸� �κи� ����

    	return uploadFileName;
    }
}
