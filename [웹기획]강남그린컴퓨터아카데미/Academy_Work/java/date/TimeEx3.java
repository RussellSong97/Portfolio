import java.time.*;
import java.time.temporal.*;


class TimeEx3 {
	public static void main(String[] args) {
		LocalDate date1 = LocalDate.of(2020,  1,  1);
		LocalDate date2 = LocalDate.of(2021, 12, 31);

		Period pe = Period.between(date1, date2);

		System.out.println("date1 : " + date1);						// date1 : 2020-01-01          
		System.out.println("date2 : " + date2);						// date2 : 2021-12-31          
		System.out.println("pe    : " + pe);						// pe    : P1Y11M30D           
		System.out.println();										                               
		System.out.println("YEAR  : " + pe.get(ChronoUnit.YEARS));	// YEAR  : 1                   
		System.out.println("MONTH : " + pe.get(ChronoUnit.MONTHS));	// MONTH : 11                  
		System.out.println("DAY   : " + pe.get(ChronoUnit.DAYS));	// DAY   : 30                  
		System.out.println();										                               
		
		LocalTime time1 = LocalTime.of( 0,  0,  0);					       
		LocalTime time2 = LocalTime.of(12, 34, 56);					       
		
		Duration du = Duration.between(time1, time2);

		System.out.println("time1 : " + time1);						// time1 : 00:00        
		System.out.println("time2 : " + time2);						// time2 : 12:34:56     
		System.out.println("du    : " + du);						// du    : PT12H34M56S  
		System.out.println();

		LocalTime tmp = LocalTime.of(0, 0).plusSeconds(du.getSeconds());
		System.out.println("HOUR   : " + tmp.getHour());			// HOUR   : 12   	
		System.out.println("MINUTE : " + tmp.getMinute());			// MINUTE : 34   
		System.out.println("SECOND : " + tmp.getSecond());			// SECOND : 56   
		System.out.println("NANO   : " + tmp.getNano());			// NANO   : 0    

	}
}
