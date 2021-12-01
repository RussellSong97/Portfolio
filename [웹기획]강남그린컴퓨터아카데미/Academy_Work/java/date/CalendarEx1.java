import java.util.*;


class CalendarEx1 {
	public static void main(String[] args) {
		Calendar today =  Calendar.getInstance();	//오늘 날짜 및 현재 시간을 저장
		//Calendar 클래스는 abstract 클래스이므로 인스턴스를 생성자가 아닌 메소드를 통해 생성
		
		System.out.println("이 해의 년도 :  " + today.get(Calendar.YEAR));					//2021
		System.out.println("월(0~11) :  " + today.get(Calendar.MONTH));					//2
		System.out.println("이 해의 몇 째주 :  " + today.get(Calendar.WEEK_OF_YEAR));		//14 이번년도에 14주차
		System.out.println("이 달의 몇 째주 :  " + today.get(Calendar.WEEK_OF_MONTH));		//5 이번달 들어 5주차
		System.out.println("이 달의 몇 일 :  " + today.get(Calendar.DATE));				//29
		System.out.println("이 달의 몇 일 :  " + today.get(Calendar.DAY_OF_MONTH));		//29
		System.out.println("이 해의 몇 일 :  " + today.get(Calendar.DAY_OF_YEAR));			//88
		System.out.println("요일(1~7, 1 : 일요일) :  " + today.get(Calendar.DAY_OF_WEEK));	//2
		System.out.println("이 달의 몇 째 요일 :  " + today.get(Calendar.DAY_OF_WEEK_IN_MONTH));//5 이번달의 5번쨰 월요일
		System.out.println();
		System.out.println("오전_오후(0:오전, 1:오후) :  " + today.get(Calendar.AM_PM));		//0
		System.out.println("시(0~11) :  " + today.get(Calendar.HOUR));					//10		지금 현재 10시
		System.out.println("시(0~23) :  " + today.get(Calendar.HOUR_OF_DAY));			//10	지금 현재 10시
		System.out.println("분(0~59) :  " + today.get(Calendar.MINUTE));					//4
		System.out.println("초(0~59) :  " + today.get(Calendar.SECOND));					//50
		System.out.println("밀리초(0~59) :  " + today.get(Calendar.SECOND));				//50
		System.out.println("이 달의 말일 :  " + today.getActualMaximum(Calendar.DATE));	//31
	}
}
