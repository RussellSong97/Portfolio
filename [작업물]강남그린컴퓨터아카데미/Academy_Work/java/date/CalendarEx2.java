import java.util.*;

class CalendarEx2 {
	public static void main(String[] args) {
		final String[] DAY_OF_WEEK = {"", "일", "월", "화", "수", "목", "금", "토"};
		//요일이름을 출력하기 위한 상수 배열 - 요일번호는 1부터 시작하므로 첫번 째 요소를 비워둠

		Calendar date1 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		
		date1.set(2020, 1, 15);//date1의 날짜를 2020년 2월 15일로 지정
		System.out.println("date1은 " + toString(date1) + DAY_OF_WEEK[date1.get(Calendar.DAY_OF_WEEK)] + "요일이고, ");
		//date1은 2020년 2월 15일 토요일이고, 
		System.out.println("오늘(date2)은 " + toString(date2) + DAY_OF_WEEK[date2.get(Calendar.DAY_OF_WEEK)] + "요일이다 ");
		//	오늘(date2)은 2021년 3월 29일 월요일이다
		long diff = (date2.getTimeInMillis() - date1.getTimeInMillis()) / 1000;
		//getTimeInMillis() : 지정된 날짜 및 시간을 1970년 1월 1일 0시를 기준으로 밀리초 단위로 리턴하는 메소드
		System.out.println("그 날(date1) 부터 지금(date2)까지 " + diff + "초가 지났습니다.");
		//그 날(date1) 부터 지금(date2)까지 35251200초가 지났습니다.

		System.out.println("일(day)단위로 변경하면 " + (diff / (60 * 60 * 24) ) + "일입니다.");
	}
	public static String toString(Calendar date) {
		return date.get(Calendar.YEAR) + "년 " + (date.get(Calendar.MONTH) + 1) + "월 " + date.get(Calendar.DATE) + "일 ";
	}
	/*
	date1은 2020년 2월 15일 토요일이고,
	오늘(date2)은 2021년 3월 29일 월요일이다
	그 날(date1) 부터 지금(date2)까지 35251200초가 지났습니다.
	*/
}
