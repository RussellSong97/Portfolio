<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="mem.*"%>
<%
	MemberInfo login_info = (MemberInfo) session.getAttribute("login_info");
	if (login_info == null) {
%>
<script>
	alert("잘못된 경로로 들어오셨습니다.");
	history.back();
</script>
<%
	out.close(); // 파일 실행을 강제로 종료시킴
	}

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
	url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String sql = "select * from t_member_addr where mi_id = '" + login_info.getMi_id() + "' order by ma_basic desc";

	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234");
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);

	} catch (Exception e) {
		out.println("주소록 목록 호출 시 오류가 발생했습니다.");
		e.printStackTrace();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
#addrForm {
	width: 600px;
	border: 1px black solid;
	display: none;
}
</style>
<script>
	function fnBasic(chk) {
		if (confirm("선택하신 주소를 '기본주소'로 등록하시겠습니까?")) {
			location.href = "addrProc.jsp?wtype=basic&idx=" + chk;
		}
	}
	function fnDel(idx) {
		if (confirm("선택하신 주소를 삭제하시겠습니까?")) {
			location.href = "addrProc.jsp?wtype=del&idx=" + idx;
		}
	}
	
	var isView = false;
	function showAddr(kind, idx) {
		var frm = document.frmAddr;
		var obj = document.getElementById("addrForm");
	
		frm.zip.value = "";		frm.addr1.value = "";	frm.addr2.value = "";
		if (kind == "up") {
			isView = false;			// 수정 상황이면 무조건 폼이 보이게 함
			frm.idx.value = idx;	// 수정할 주소의 idx 값 지정
			frm.zip.value = eval("frm.zip" + idx + ".value");
			frm.addr1.value = eval("frm.addr1" + idx + ".value");
			frm.addr2.value = eval("frm.addr2" + idx + ".value");
		}
		if (!isView) {
			isView = true;
			obj.style.display = "block";
			frm.wtype.value = kind;
		} else {
			isView = false;
			obj.style.display = "none";
			frm.wtype.value = "";
		}
	}
</script>
</head>
<body>
	<h2><%=login_info.getMi_id()%>님의 주소록
	</h2>
	<form name="frmAddr" action="addrProc.jsp" method="post">
		<input type="hidden" name="wtype" value="" />
		<input type="hidden" name="idx" value="" />			
		<table width="600" cellpadding="5" border="1">
			<tr>
				<th width="15%">우편번호</th>
				<th width="30%">주소1</th>
				<th width="*">주소2</th>
				<th width="10%">기본</th>
				<th width="10%">삭제</th>
			</tr>
			<%
				if (rs.next()) { // 해당 회원에 등록된 주소가 있으면
					int idx = 0;
					do {
						idx = rs.getInt("ma_idx");
						String lnk = "<a href='#' onclick='showAddr(\"up\", " + idx + ");'>";
						String chk = " onclick='fnBasic(this.value);'";
						String isDel = "fnDel(" + idx + ");";
						if (rs.getString("ma_basic").equals("y")) {
							chk = " checked='checked'";
							isDel = "alert('기본 주소는 삭제할 수 없습니다.\\n삭제하려면 다른 주소를 기본 주소로 설정한 후 삭제 해야 합니다.');";
						}
			%>
			<tr align="center">
				<td> 
					<input type="hidden" name="zip<%=idx%>" value="<%=rs.getString("ma_zip")%>" />
					<input type="hidden" name="addr1<%=idx%>" value="<%=rs.getString("ma_addr1")%>" />
					<input type="hidden" name="addr2<%=idx%>" value="<%=rs.getString("ma_addr2")%>" />
					<%=lnk + rs.getString("ma_zip")%></a>
				</td>
				<td align="left"><%=lnk + rs.getString("ma_addr1")%></a></td>
				<td align="left"><%=lnk + rs.getString("ma_addr2")%></a></td>
				<td><input type="radio" name="basic" value="<%=idx%>" <%=chk%> /></td>
				<td><input type="button" value="삭제" onclick="<%=isDel%>" /></td>
			</tr>
			<%
					} while (rs.next());
				} else { // 해당 회원에 등록된 주소가 없으면
					out.println("<tr><td colspan='5' align='center'>등록된 주소가 없습니다.</td></tr>");
				}
			%>
		</table>
		<p style="width: 600px; text-align: right;">
			<input type="button" value="새주소 등록" onclick="showAddr('in', 0);" />
		</p>
		<div id="addrForm"> 
			<table width="600" cellpadding="5">
				<tr>
					<th width="20%">우편번호</th>
					<td width="*"><input type="text" name="zip" size="5" maxlength="5" /></td>
				</tr>
				<tr>
					<th>주소</th>
					<td>
						<input type="text" name="addr1" size="29" />&nbsp; 
						<input type="text" name="addr2" size="29" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="submit" value="주소 등록" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					<input type="reset" value="다시 입력" /></td>
				</tr>
			</table>
		</div>
	</form>
	
	<hr />
	<a href="../index.jsp">Index</a>
</body>
</html>
