/*
1. 생년월일을 yyyy-mm-dd형으로 입력받아 Calendar형 인스턴스로 생성한 후 출력
2. Calendar형 인스턴스의 값들을 이용하여 LocalDate형 인스턴스로 생성한 후 출력
3. 입력받은 생일을 기준으로 나이를 계산하여 출력
*/

import java.util.*;				// scanner, calendar
import java.time.*;				// localdate
import java.time.temporal.*;

class BirthTest2 {
	public static void main(String[] args) {
		// 1. 생년월일을 yyyy-mm-dd형으로 입력받아 
		Scanner sc = new Scanner(System.in);
		System.out.print("생년월일을 yyyy-mm-dd 형태로 입력해주세요 : ");
		String birth = sc.nextLine();

		birth = birth.trim();						// 양끝의 공백 제거
		String[] arrBirth = birth.split("-");		// 입력받은 날짜 문자열을 연월일로 쪼개어 배열에 저장
		int yyyy = Integer.parseInt(arrBirth[0]);
		int mm	 = Integer.parseInt(arrBirth[1]);
		int dd	 = Integer.parseInt(arrBirth[2]);
		// Calendar의 set()과 LocalDate의 of() 메소드에서 인수로 사용하기 위해 int형으로 형변환

		// Calendar형 인스턴스로 생성한 후 출력
		Calendar cDate = Calendar.getInstance();	// Calendar 객체를 만들기 위한 인스턴스
		int cYear = cDate.get(Calendar.YEAR);		// 나이를 구하기 위한 올해 연도
		cDate.set(yyyy, mm - 1, dd);				// 사용자가 입력한 값을 이용하여 생일 Calendar 형 인스턴스 생성
		System.out.println("Calendar  생년월일 : " + toString(cDate));

		// 2. Calendar형 인스턴스의 값들을 이용하여 LocalDate형 인스턴스로 생성한 후 출력
		LocalDate ldDate = LocalDate.of(yyyy, mm, dd);	// 사용자가 입력한 값을 이용하여 생일 LocalDate 형 인스턴스 생성
		System.out.println("LocalDate 생년월일 : " + ldDate);

		// 3. 입력받은 생일을 기준으로 나이를 계산하여 출력
		System.out.println("1. 입력받은 생일을 기준으로의 나이는? (걍 year 빼기) : " + (cYear - yyyy) + "살");

	}

	// 그저 format 맞춰 출력
	public static String toString(Calendar date) {
		int yyyy = date.get(Calendar.YEAR);
		int mm   = (date.get(Calendar.MONTH) + 1) ;
		int dd   = date.get(Calendar.DATE);

		return yyyy + "-" + ((mm > 9) ? mm : "0" + mm) + "-" + ((dd > 9) ? dd : "0" + dd);
	}
}
