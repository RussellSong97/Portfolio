use world;


select * from city where CountryCode = 'kor' order by CountryCode;
select * from country;
select * from countrylanguage;

-- 나라별 나라코드, 도시의 개수, 도시인구 평균(소수점 아래 2자리까지) 출력
select CountryCode, count(ID), truncate(avg(Population), 2) 
from city group by CountryCode;

-- 나라별 가장 인구가 많은 나라코드, 도시의 이름, 인구수 출력
select CountryCode, Name, Population 
from city group by CountryCode having Population = max(Population);

 
 






-- 나라별 인구가 100만 ~ 900만인 도시 개수 : 나라코드, 도시개수
select CountryCode, count(ID) from city 
-- where Population between 1000000 and 9000000 
where Population >= 1000000 and Population <= 9000000 
group by CountryCode;

-- 나라별 인구가 500만이상인 도시들을 인구 많은 순으로 정렬하여 모든 컬럼 출력
select * from city where Population >= 5000000 order by Population desc;

select floor(1.5), truncate(1.5, 0), floor(-1.5), truncate(-1.5, 0);
select ceil(1.5), truncate(1.5, 0), ceil(-1.5), truncate(-1.5, 0);

select adddate('2020-02-01', interval 31 day), addtime('23:59:59', '1:1:1');
select datediff('2021-07-15', now()), datediff('2021-01-28', now());
select timediff('23:59:59', curtime()), timediff('00:00:00', curtime());
select dayofweek('2021-12-25'), monthname(now()), dayofyear(now()), last_day('2020-02-15');
select makedate(2021, 100), makedate(2021, -1), maketime(12, 34, 56);
select period_add(202111, 6), period_diff(202205, 202111), quarter(now()), time_to_sec('12:34:56');
select database(), schema(), found_rows(), version();




