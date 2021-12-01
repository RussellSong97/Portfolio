package svc;

import static db.JdbcUtil.*; 
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class NoticeListSvc {
	// 게시글 목록 보기의 비즈니스 로직을 처리하는 클래스(전체 게시글 개수와 특정 페이지의 목록을 추출)
	
	public int getArticleCount(String where) {
		// 목록에서 보여줄 전체 게시글의 개수를 리턴하는 메소드
		int rcnt = 0;		// 전체 게시글 개수를 저장할 변수
		Connection conn = getConnection();
		NoticeDao noticeDao = NoticeDao.getInstance();
		// NoticeDao 형 인스턴스를 getInstance() 메소드를 통해 생성 
		noticeDao.setConnection(conn);
		// NoticeDao 형 인스턴스 noticeDao에 Connection객체를 지정
		rcnt = noticeDao.getArticleCount(where);
		// 게시글의 전체 개수를 구할 getArticleCount() 메소드 호출
		close(conn);
		
		return rcnt;
	} 	
	
	public ArrayList<NoticeInfo> getArticleList(String where, int cpage, int psize) {
												// 매개변수 : 조건절, 현재 페이지 번호, 페이지 크기로 가져올 레코드 개수로 사용 
		ArrayList<NoticeInfo> articleList = null;
		// 게시글목록을 담을 ArrayList 인스턴스 선언
		Connection conn = getConnection();
		NoticeDao noticeDao = NoticeDao.getInstance();
		noticeDao.setConnection(conn);
		articleList = noticeDao.getArticleList(where, cpage, psize);
		close(conn);
				
		return articleList;
	}
}
