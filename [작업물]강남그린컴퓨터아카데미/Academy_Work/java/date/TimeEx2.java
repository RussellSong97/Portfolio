import java.time.*;
import java.time.temporal.*;
import static java.time.DayOfWeek.*;
import static java.time.temporal.TemporalAdjusters.*;

class DayAfterTomorrow implements TemporalAdjuster {
	@Ovveride	// 오버라이딩을 한다는 표식으로 생략해도 무방
	public Temporal adjustInto(Temporal temporal) {
		return temporal.plus(2, ChronoUnit.DAYS);
	}
}
class TimeEx2 { 
	public static void main(String[] args) {
		LocalDate today = LocalDate.now();
		LocalDate date = today.with(new DayAfterTomorrow());

		p(today);										// 2021-03-30
		p(date);										// 2021-04-01
		p(today.with(firstDayOfNextMonth()));			// 2021-04-01
		p(today.with(firstDayOfMonth()));				// 2021-03-01
		p(today.with(lastDayOfMonth()));				// 2021-03-31
		p(today.with(firstInMonth(TUESDAY)));			// 2021-03-02
		p(today.with(lastInMonth(TUESDAY)));			// 2021-03-30
		p(today.with(previous(TUESDAY)));				// 2021-03-23
		p(today.with(previousOrSame(TUESDAY)));			// 2021-03-30
		p(today.with(next(TUESDAY)));					// 2021-04-06
		p(today.with(nextOrSame(TUESDAY)));				// 2021-03-30
		p(today.with(dayOfWeekInMonth(4, TUESDAY)));	// 2021-03-23
	}

	static void p(Object obj) {
		System.out.println(obj);
	}
}