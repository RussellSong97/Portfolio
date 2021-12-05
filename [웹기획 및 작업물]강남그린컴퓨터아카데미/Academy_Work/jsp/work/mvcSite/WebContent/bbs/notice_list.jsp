<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="vo.*"%>
<%   
	ArrayList<NoticeInfo> articleList  = (ArrayList<NoticeInfo>)request.getAttribute("articleList");
	// 화면에서 보여줄 게시글 목록을 ArrayList로 받아옴
	// attribute에 들어있는 데이터들은 Object로 되어 있기 때문에 사용하려면 해당 자료형으로 형변환을 해야 함
	
	PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
	// 페이징에 필요한 각종 데이터들을 PageInfo형 인스턴스에 담음.
	
	int cpage = pageInfo.getCpage();	// 현재 페이지 번호
	int pcnt  = pageInfo.getPcnt();		// 전체 페이지 개수
	int rcnt  = pageInfo.getRcnt();		// 전체 게시글 개수
	int spage = pageInfo.getSpage();	// 블록 시작 페이지 번호
	int epage = pageInfo.getEpage();	// 블록 종료 페이지 번호
	int psize = pageInfo.getPsize();	// 페이지크기
	int bsize = pageInfo.getBsize();	// 블록크기
	
	String schtype = "", keyword = "", schargs = "", args = "";
	// 검색조건, 검색어, 검색쿼리스트링, 전체쿼리스트링
	schtype = pageInfo.getSchtype();	// 검색조건
	keyword = pageInfo.getKeyword();	// 검색어
	
	if (schtype == null || keyword == null) {	// 검색을 하지 않은 경우
		schtype = "";	keyword = "";	
	} else if (!keyword.equals("")) {			// 검색어가 있으면
		schargs = "&schtype=" + schtype + "&keyword=" + keyword;
		// 검색어가 있을 경우 검색관련 데이터를 쿼리스트링으로 지정
	}
	
	args = "&cpage=" + cpage + schargs;
		 	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
#brd th {
	border-bottom: 3px black double;
}

#brd td {
	border-bottom: 1px black solid;
}
</style>
</head>
<body>
	<h2>공지사항 목록 화면</h2>
	<table width="700" cellpadding="5" cellspacing="0" id="brd">
		<tr>
			<th width="10%">번호</th>
			<th width="*">제목</th>
			<th width="10%">조회수</th>
			<th width="15%">작성일</th>
		</tr>
	<%	
	
		if (articleList.size() > 0 && rcnt > 0) {	// 보여줄 목록이 있으면
			int num = rcnt - (psize * (cpage - 1));
			String title = "", lnk = "", date = "";
			int idx;
			for (int i = 0 ; i < articleList.size(); i++) {
				idx = articleList.get(i).getBn_idx();
				title = articleList.get(i).getBn_title();
				date = articleList.get(i).getBn_date().substring(2, 11).replace("-", ".");
				lnk = "<a href='brd_view.ntc?idx'" + idx + args + " title='" + title + "'>";
				if (title.length() > 28) title = title.substring(0, 26) + "...";
				
	%> 
		<tr align="center">
			<td><%=idx%></td>
			<td align="left"><%=lnk + title %></a></td>
			<td><%=articleList.get(i).getBn_read() %></td>
			<td><%=date %></td>
		</tr>
	<% 
				}
			}
	%>	
	</table>
	<br />
	<table width="700" cellpadding="5" cellspacing="0">
		<tr>
			<td align="center">
			<!-- &lt;&lt;&nbsp;&nbsp;&lt;&nbsp;&nbsp;1 2 3 4
				5&nbsp;&nbsp;&gt;&nbsp;&nbsp;&gt;&gt; -->
				
		<% 
			if (rcnt > 0) {	// 게시글이 있을 경우
				if (cpage == 1) {	// 현재 페이지 번호가 1이면
					out.println("[&lt;&lt;]&nbsp;&nbsp;[&lt;]&nbsp;&nbsp;");
				} else {
					out.print("<a href='brd_list.ntc?cpage=1" + schargs + "'>");
					out.println("[&lt;&lt;]</a>&nbsp;&nbsp;");
					out.print("<a href='brd_list.ntc?cpage=" + (cpage - 1) + schargs + "'>");
					out.println("[&lt;]</a>&nbsp;&nbsp;");
				}	// 첫 페이지와 이전 페이지 링크 

				// i => 루프돌릴 횟수를 검사하는 용도의 변수
				// j => 페이지 시작되는 페이지 번호 출력용 변수
				// 조건 => bsize만큼 루프를 도는데 마지막 페이지일 경우 bsize보다 작아도 멈춤
				for (int i = 1, j = spage; j <= bsize && j <= epage; i++, j++) {
					if (cpage == j) {	// 현재 페이지 번호일 경우 링크없이 강조만 함
						out.print("&nbsp;<strong>" + j + "</strong>&nbsp;");
					} else {
						out.print("&nbsp;<a href='brd_list.ntc?cpage=" + j + schargs + "'>");
						out.print(j + "</a>&nbsp;");
					}
				}
				
				if (cpage == pcnt) {	// 현재 페이지 번호가 마지막 페이지번호이면 
					out.println("&nbsp;&nbsp;[&gt;]&nbsp;&nbsp;[&gt;&gt;]");					
				} else {
					out.print("&nbsp;&nbsp;<a href='brd_list.ntc?cpage=" + (cpage + 1) + schargs + "'>[&gt;]</a>");
					out.print("&nbsp;&nbsp;<a href='brd_list.ntc?cpage=" + pcnt + schargs + "'>[&gt;&gt;]</a>&nbsp;&nbsp;");
				}
				
			
			} else {		// 게시글이 없을 경우 : 페이지 생략 
				out.println("검색 결과가 없습니다.");
			}
		 
			
		%> 	
			</td>
		</tr>
		<tr align="right">
			<td>
				<form name="searchfrm" method="get">
					<select name="schtype">
							<option value="title" <%=(schtype.equals("title"))? "selected" : "" %>>제목</option>
							<option value="content" <%=(schtype.equals("content"))? "selected" : "" %>>내용</option>
							<option value="tc" <%=(schtype.equals("tc"))? "selected" : "" %>>제목+내용</option>
					</select> 
					<input type="text" name="keyword" value="<%=keyword %>"/> 
					<input type="submit" value="검 색"/>
				</form> 
			</td>
		</tr>
	</table>
</body>
</html>
