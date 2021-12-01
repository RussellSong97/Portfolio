/*
1. 생년월일을 yyyy-mm-dd형으로 입력받아 Calendar형 인스턴스로 생성한 후 출력
2. Calendar형 인스턴스의 값들을 이용하여 LocalDate형 인스턴스로 생성한 후 출력
3. 입력받은 생일을 기준으로 나이를 계산하여 출력
*/

import java.util.*;
import java.time.*;
import java.time.temporal.*;

class BirthTest {
	public static void main(String[] args) {
		// 1. 생년월일을 yyyy-mm-dd형으로 입력받아 
		Scanner sc = new Scanner(System.in);
		System.out.print("생년월일을 yyyy-mm-dd 형태로 입력해주세요 : ");
		String birth = sc.nextLine();

		birth = birth.trim();						// 양끝의 공백 제거
		String[] arrBirth = birth.split("-");		// '-' 기준으로 자르기
		int yyyy = Integer.parseInt(arrBirth[0]);
		int mm	 = Integer.parseInt(arrBirth[1]);
		int dd	 = Integer.parseInt(arrBirth[2]);

		// Calendar형 인스턴스로 생성한 후 출력
		Calendar cDate = Calendar.getInstance();
		cDate.set(yyyy, mm - 1, dd); 
		System.out.println("Calendar  생년월일 : " + toString(cDate));

		// 2. Calendar형 인스턴스의 값들을 이용하여 LocalDate형 인스턴스로 생성한 후 출력
		LocalDate ldDate = LocalDate.of(yyyy, mm, dd);
		System.out.println("LocalDate 생년월일 : " + ldDate);

		// 3. 입력받은 생일을 기준으로 나이를 계산하여 출력
		LocalDate today = LocalDate.now();
		System.out.println("1. 입력받은 생일을 기준으로의 나이는? (걍 year 빼기) : " + (today.getYear() - yyyy));

		Period age = Period.between(ldDate, today);
		System.out.println("2. 입력받은 생일을 기준으로의 나이는? (Period ver.) : " + (age.get(ChronoUnit.YEARS) + 1));
		
	}

	// 그저 format 맞춰 출력
	public static String toString(Calendar date) {
		int yyyy = date.get(Calendar.YEAR);
		int mm   = (date.get(Calendar.MONTH) + 1) ;
		int dd   = date.get(Calendar.DATE);

		return yyyy + "-" + ((mm > 9) ? mm : "0" + mm) + "-" + ((dd > 9) ? dd : "0" + dd);
	}
}
