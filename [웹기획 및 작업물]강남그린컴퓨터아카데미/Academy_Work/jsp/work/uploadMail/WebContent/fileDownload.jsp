<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%
request.setCharacterEncoding("utf-8");
String fileName= request.getParameter("file");
String savePath = "upload";

ServletContext context = getServletContext();
String downPath = context.getRealPath(savePath);
String filePath = downPath + "\\" + fileName;
// 다운받을 파일의 실제 경로 및 파일명을 정의

byte[] b = new byte[4096];	// 한 번에 다운 받을 크기로 4KB로 지정
FileInputStream in = new FileInputStream(filePath);
String mimeType = getServletContext().getMimeType(filePath);
// 다운 받을 파일의 종류를 추출

System.out.println("Mime Type >>> " + mimeType);

if (mimeType == null) mimeType = "application/octet-stream";
// 기본값으로 다운받을 수 있는 mimeType을 지정

response.setContentType(mimeType);
String agent = request.getHeader("User-Agent");	// 브라우저 종류
boolean ie = ( agent.indexOf("MSIE") > -1 || agent.indexOf("Trident") > -1 ) ;	
// 사용자 브라우저가 IE인지 여부를 저장

if (ie) {
	fileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");	
	// 파일명에 띄어쓰기가 있으면 IE는 "+"로 변경하나 파일 다운로드시 "+"를 사용할 수 없으므로 다시 띄어쓰기로 변환
} else {
	fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
}

response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
// 현재 파일이 열릴 때 지정한 파일을 다운로드 시키라는 의미

ServletOutputStream out2 = response.getOutputStream();
int numRead;
while ((numRead = in.read(b, 0, b.length)) != -1) {
	// b 배열에 다운받을 데이터가 들어있으면...
	out2.write(b, 0, numRead);
	// 읽어들일 데이터를 out2를 통해 사용자에게 다운로드 시킴
}

out2.flush();		// out2에 남아 있는 데이터가 있으면 모두 다운로드 시키라는 의미
out2.close();		// out2를 닫음
in.close();			// in을 닫음

%> 