import java.util.*;

class CalendarEx4 {
	public static void main(String[] args) {
		Calendar date = Calendar.getInstance();
		date.set(2020, 2, 31);
		System.out.println(toString(date));//2020년 3월31일
		
		System.out.println("== 1일 후 ==");
		date.add(Calendar.DATE, 1);
		System.out.println(toString(date));//2020년 4월1일
		
		
		System.out.println("== 6달 전 ==");
		date.add(Calendar.MONTH, -6);
		System.out.println(toString(date));//2019년 10월1일
		
		System.out.println("== 31일 후(roll) ==");
		date.add(Calendar.DATE, 31);	//지정한 필드의 값만 변함
		System.out.println(toString(date));//2019년 11월1일
		
		System.out.println("== 31일 후(add) ==");
		date.add(Calendar.DATE, 31);
		System.out.println(toString(date));//2019년 12월2일
	}
	public static String toString(Calendar date) {
		return date.get(Calendar.YEAR) + "년 " + (date.get(Calendar.MONTH) + 1) + "월" + date.get(Calendar.DATE) + "일";
	}
}
