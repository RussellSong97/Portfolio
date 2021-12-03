package svc;

import static db.JdbcUtil.*; 
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class TestListSvc {

	public int getBoardCount(String where) {
		// 목록에서 보여줄 전체 게시글의 개수를 리턴하는 메소드
		int rcnt = 0;		// 전체 게시글 개수를 저장할 변수
		Connection conn = getConnection();
		TestBrdDao testDao = TestBrdDao.getInstance();
		// NoticeDao 형 인스턴스를 getInstance() 메소드를 통해 생성 
		testDao.setConnection(conn);
		// NoticeDao 형 인스턴스 noticeDao에 Connection객체를 지정
		rcnt = testDao.getBoardCount(where);
		// 게시글의 전체 개수를 구할 getArticleCount() 메소드 호출
		close(conn);
		
		return rcnt;
	} 	
	
	public ArrayList<TestInfo> getBoardList(String where, int cpage, int psize) {
												// 매개변수 : 조건절, 현재 페이지 번호, 페이지 크기로 가져올 레코드 개수로 사용 
		ArrayList<TestInfo> articleList = null;
		// 게시글목록을 담을 ArrayList 인스턴스 선언
		Connection conn = getConnection();
		TestBrdDao testDao = TestBrdDao.getInstance();
		testDao.setConnection(conn);
		articleList = testDao.getBoardList(where, cpage, psize);
		close(conn);
				
		return articleList;
	}
}
