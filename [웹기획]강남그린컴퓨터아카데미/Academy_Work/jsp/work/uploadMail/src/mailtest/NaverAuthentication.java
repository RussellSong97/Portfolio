package mailtest;

import javax.mail.*;

public class NaverAuthentication extends Authenticator {
	PasswordAuthentication passAuth;

	public NaverAuthentication() {
		passAuth = new PasswordAuthentication("gimkeung", "k990113"); 
											// 네이버아이디, 네이버비밀번호
	}
	public PasswordAuthentication getPasswordAuthentication() {
		return passAuth;
	}
}


