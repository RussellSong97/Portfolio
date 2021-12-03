package mailtest;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/mailSend")
public class MailSendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public MailSendServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String sender = request.getParameter("sender");
		String receiver = request.getParameter("receiver");
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		try {
			Properties properties = System.getProperties();
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.naver.com");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.port", "587");
			
			Authenticator auth = new NaverAuthentication();
			Session s = Session.getDefaultInstance(properties, auth);
			Message msg = new MimeMessage(s);
			Address senderAddr = new InternetAddress(sender);
			Address receiverAddr = new InternetAddress(receiver);
			
			msg.setHeader("content-type", "text/html; charset=utf-8");
			msg.setFrom(senderAddr);
			msg.addRecipient(Message.RecipientType.TO, receiverAddr);
			msg.setSubject(title);
			msg.setContent(content, "text/html; charset=utf-8");
			msg.setSentDate(new java.util.Date());
			
			Transport.send(msg);
			out.println("<h3>메일이 정상적으로 전송되었습니다.</h3>");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
