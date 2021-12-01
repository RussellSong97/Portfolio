import java.time.*;
import java.time.temporal.*;

class TimeEx1 {
	public static void main(String[] args) {
		LocalDate today = LocalDate.now();	// 오늘 날짜로 LocalDate 인스턴스 today 생성
		LocalTime now	= LocalTime.now();	// 현재 시간으로 LocaTime 인스턴스 now 생성

		LocalDate bDate = LocalDate.of(1999, 12, 31);	// 특정 날짜를 지정하여 LocalDate 인스턴스 bDate 생성
		LocalTime bTime = LocalTime.of(23, 59, 59);		// 특정 시간을 지정하여 LocalTime 인스턴스 bTime 생성

		System.out.println("today : "	+ today);		// today : 2021-03-30                           
		System.out.println("now : "		+ now);			// now : 10:21:10.664255900                     
		System.out.println("bDate : "	+ bDate);		// bDate : 1999-12-31                           
		System.out.println("bTime : "	+ bTime);		// bTime : 23:59:59                             
		System.out.println("bTime : "	+ bTime);		// bTime : 23:59:59                             
		System.out.println();																					                                                
		System.out.println("bDate.withYear(2000) : " + bDate.withYear(2000) );			// bDate.withYear(2000) : 2000-12-31            
		// bDate.withYear(2000) : bDate의 날짜에서 년도를 2000으로 변경(bDate는 변경 안됨)
		System.out.println("bDate.plusDays(1) : " + bDate.plusDays(1) );				// bDate.plusDays(1) : 2000-01-01               
		// bDate.plusDays(1) : bDate의 날짜에서 일(day)부분에 1을 더한 날짜 리턴
		System.out.println("bDate.plus(1, ChronoUnit.DAYS) : " + bDate.plus(1, ChronoUnit.DAYS) );				// bDate.plus(1, ChronoUnit.DAYS) : 2000-01-01  
		System.out.println();																					                                                
		System.out.println("bTime.truncatedTo(ChronoUnit.HOURS) : " + bTime.truncatedTo(ChronoUnit.HOURS));		// bTime.truncatedTo(ChronoUnit.HOURS) : 23:00  
		// bTime.truncatedTo(ChronoUnit.HOURS) : bTime의 시간에서 지정한 시(hour)보다 작은 유닛들은 0으로 변경
	}
}
