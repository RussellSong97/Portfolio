package com.cuv.admin.domain.calendar;


import com.cuv.admin.domain.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CalendarRepository extends JpaRepository<Calendar, Long>,
        CalendarRepositoryCustom,
        QuerydslPredicateExecutor<Calendar> {


}
