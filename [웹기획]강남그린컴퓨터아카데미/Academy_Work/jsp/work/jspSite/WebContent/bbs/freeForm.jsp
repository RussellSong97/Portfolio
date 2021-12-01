<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="mem.*" %>

<%
MemberInfo login_info = (MemberInfo)session.getAttribute("login_info");
request.setCharacterEncoding("utf-8");
String wtype = request.getParameter("wtype"); //수정인지 작성인지 확인하기 위한 

String bf_ismem = "", bf_pwd = "", bf_writer = "";
String bf_title = "", bf_content = "", msg = null;
//컨트롤들에서 보여줄 데이터를 저장할 변수들(수정일 경우)
int idx = 0;
String args = "";

if (wtype.equals("in")) {          // 게시글 등록이면
   msg = "등록";
} else if (wtype.equals("in")) {   // 게시글 수정이면
   msg = "수정";
   String driver = "com.mysql.cj.jdbc.Driver";
   String url = "jdbc:mysql://localhost:3307/kbm_test?useUnicode=true&characterEncoding=UTF8";
   url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
   
   Connection conn = null; // 연결 역할
   Statement stmt = null;  // 쿼리를 보내는 역할
   ResultSet rs = null;    // select의 결과값을 저장
   
   idx = Integer.parseInt(request.getParameter("idx"));      // 글번호
   int cpage = Integer.parseInt(request.getParameter("cpage"));   // 페이지번호
   String schType = request.getParameter("schType");
   String keyword = request.getParameter("keyword");
   String pwd = request.getParameter("pwd");   // 사용자가 수정하기 위해 입력한 암호
   
   args = "?cpage=" + cpage;
   if (schType != null && keyword != null) {
      URLEncoder.encode(keyword, "UTF-8");
      args += "&schType=" + schType + "&keyword=" + keyword;
   }
   
   
   try {
      Class.forName(driver);
      conn = DriverManager.getConnection(url, "root", "1234");
      stmt = conn.createStatement();
      String sql = "select * from t_brd_free where bf_idx = " + idx;
      rs = stmt.executeQuery(sql);
      
      if (rs.next()) {
         bf_ismem = rs.getString("bf_ismem");
         bf_writer = rs.getString("bf_writer");
         bf_pwd = rs.getString("bf_pwd");
         bf_title = rs.getString("bf_title");
         bf_content = rs.getString("bf_content");
      } else {
         out.println("<script>");
         out.println("alert('잘못된 경로로 들어오셨습니다.');");
         out.println("history.back();");
         out.println("</script>");
         out.close();
      }

      // 수정권한 체크
      if (bf_ismem.equals("n")) {      // 비회원이 입력한 글이면
         if (pwd == null || !pwd.equals(bf_pwd)) {
         // 사용자가 입력한 암호가 없거나 게시글의 암호와 다르면
            out.println("<script>");
            out.println("alert('비밀번호가 틀렸습니다.');");
            out.println("history.back();");
            out.println("</script>");
            out.close();
         }
      } else {   // 회원이 입력한 글이면
         if (login_info == null || !login_info.getMi_id().equals(bf_writer)) {
         // 로그인 상태가 아니거나 현재 로그인한 회원이 게시글을 입력한 회원이 아닐 경우
            out.println("<script>");
            out.println("alert('잘못된 경로로 들어오셨습니다.');");
            out.println("history.back();");
            out.println("</script>");
            out.close();
         }
      }
   
   } catch(Exception e) {
      out.println("게시판 작성시 오류가 생겼습니다.");
      e.printStackTrace();
   } 
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>자유 게시판 <%=msg %> 폼</h2>
<form action="freeProc.jsp" method="post" name="frmForm">
<input type="hidden" name="wtype" value="<%=wtype %>" >
<input type="hidden" name="idx" value="<%=idx%>" >
<table width = "700" cellpadding="5" id="brd" cellspacing="0" >
   <tr>
   <%
   if (wtype.equals("in")) {   // 글 등록 상황이면
      if (login_info == null) {   // 로그인이 되어 있지 않을 경우
   %>
      <th width="15%" >이름</th>
      <td width="35%" ><input type="text" name="bfwriter" ></td>
      <th width="15%" >암호</th>
      <td width="35%" ><input type="password" name="bfpwd" ></td>
   <% } else {      // 로그인이 되어 있으면  %>
      <input type="hidden" name="bfwriter" value="" >
      <input type="hidden" name="bfpwd" value="" >
      <th width="15%" >이름</th>
      <td width="*" ><%=login_info.getMi_id() %></td>
   <%
      }
   } else {   // 글 수정 상황이면
      out.println("<th width='15%'>이름</th><td width='*'>" + bf_writer + "</td>");
   }
   %>
   </tr>
   <tr>
      <th>제목</th>
      <td colspan="3" ><input type="text" name="bftitle" size="70" value="<%=bf_title %>" ></td>
   </tr>
   <tr>
      <th>내용</th>
      <td colspan="3" >
         <textarea name="bfcontent" cols="80" rows="10" ><%=bf_content %></textarea>
      </td>
   </tr>
</table>
<p style="width:700px;"  text-align:center;" >
   <input type="submit" value="게시글 <%=msg %>"  >
   <input type="reset" value="다시 입력" >
</p>
</form>
</body>
</html>