package svc;

import static db.JdbcUtil.*; 
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class NoticeViewSvc {
	// 게시글 보기의 비즈니스 로직을 처리하는 클래스 
	
	public NoticeInfo getArticle(int idx) {
		// 받아온 idx에 해당하는 게시글의 데이터들을 NoticeInfo 형 인스턴스로 리턴하는 메소드
		
		NoticeInfo article = null;
		// 게시글목록을 담을 ArrayList 인스턴스 선언
		Connection conn = getConnection();
		NoticeDao noticeDao = NoticeDao.getInstance();
		noticeDao.setConnection(conn);
		
		// 조회수 증가
		int result = noticeDao.readCountUp(idx);	// 조회수 증가
		if (result > 0) commit(conn);		// update된 레코드가 없으면 commit 시킴
		else			rollback(conn);		// update된 레코드가 없으면 rollback 시킴
		// 조회수 증가는 게시판 운영에 큰 영향을 미치지 않으므로 rollback 되어도 계속 작업을 이어감
		
		article = noticeDao.getArticle(idx);
		// 사용자가 선택한 게시글의 정보를 받아와 article에 저장
		
		close(conn);
		
		return article;
	}
}
