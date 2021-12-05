create database ksr;

use ksr; -- ksr DB를 사용하겠다는 의미

-- 자유 게시판 테이블
create table brd_free (
	idx int unsigned auto_increment primary key, -- 글번호로 기본키로 지정
    writer varchar(20) not null, 		-- 작성자(로그인 시 회원ID)
    passwd varchar(10), 				-- 비밀번호(회원은 입력안함)
	title varchar(100) not null, 		-- 글제목
    content text not null, 				-- 글내용
    readcnt int unsigned default 0, 	-- 조회수
    recommend int unsigned default 0, 	-- 추천수(정렬등에서 이용가능)
    wdate datetime default now(), 		-- 글 등록일
    ipaddr varchar(15) not null, 		-- 작성자 IP주소
    isview char(1) default 'y'			-- 글 삭제 여부(y:보이는 글, n:삭제된 글)
);

-- 자유게시판 댓글 테이블
create table reply_free (
	idx int unsigned auto_increment primary key, -- 일련번호
	free_idx int unsigned, 			-- 원본글번호
	writer varchar(20) not null, 	-- 작성자
	content text not null, 			-- 내용
	passwd varchar(10), 			-- 비밀번호
	good int unsigned default 0, 	-- 좋아요
	bad int unsigned default 0, 	-- 싫어요
	wdate datetime default now(), 	-- 작성일
	ipaddr varchar(15) not null, 	-- 작성자IP
	isview char(1) default 'y', 	-- 게시여부
	constraint fk_brd_free_free_idx foreign key (free_idx) references brd_free(idx)
);

-- member_info 테이블 생성
create table member_info (
	uid			varchar(20) primary key, 	-- 유저 아이디로 PK로 사용
	pwd			varchar(20)	not null,		-- 비밀번호
	uname		varchar(50)	not null,		-- 유저 이름
	gender		char(1)	default 'f',		-- 성별(f:여자, m:남자)
	birth		char(10)	not null,		-- 생일로 'yyyy-mm-dd'
	phone		varchar(13)	not null,		-- 전화번호
	email		varchar(50)	not null,		-- 메일주소 
	issns		char(1)	default 'y',		-- SNS 서비스를 받을지 여부 (y:받음, n:안받음)
	ismail		char(1)	default 'y',		-- 이메일을 받을지 여부(y:받음, n:안받음)
	raccount	varchar(50),				-- 환불받을 계좌
	rec_urd		varchar(20),				-- 추천인 아이디
	joindate	datetime default now(),		-- 회원 가입일
	isout		char(1) default 'n',		-- 탈퇴여부(n:사용중인 아이디, y:탈퇴한 아이디)
	outdate		datetime					-- 탈퇴일
);

-- member_addr 테이블 생성(FK 연결까지)
create table member_addr (
	idx		int	unsigned auto_increment primary key ,	-- 일련번호
    uid		varchar(20) not null, 		-- 회원아이디
	zip		char(5)	not null,			-- 우편번호
	addr1	varchar(50)	not null,		-- 주소1
	addr2	varchar(50)	not null,		-- 주소2
	isbasic	char(1)	default 'n',		-- 기본주소 여부(y:기본주소, n:일반주소)
	regdate	datetime default now(),
    constraint fk_member_addr_uid FOREIGN KEY(uid) REFERENCES member_info(uid)
    -- fk_member_addr_uid 라는 외래키를 지정
    -- 지정한 외래키는 uid 라는 컬럼으로 member_info 테이블의 uid 컬럼을 참조한다.
    -- 즉 외래키인 uid에 들어가는 값들은 무조건 member_info 테이블의 uid 컬럼에 값 들 중 하나여야 함
);

create table member_point (
	idx			int unsigned auto_increment primary key, -- 일련번호
	uid			varchar(20)	not null, 			-- 회원아이디
	usedate		datetime default now(),  		-- 사용일자
	usepoint 	int not null,  					-- 사용포인트
	rmpoint		int	not null, 					-- 잔여포인트 (사용/적립 후 포인트)
	kind		char(1) default 's',			-- 구분(s:적립, u:사용, a:관리자 직권입력)
	content		varchar(50)	not null, 			-- 사용 및 적립내역(주문번호나 글번호 입력)
	constraint fk_member_point_uid foreign key(uid) references member_info(uid)
); 

ALTER TABLE member_info ADD Column rmpoint int default 0 ;

-- member_info 테이블과 member_addr 테이블에 데이터를 2개 씩 입력
insert into member_info(uid, pwd, uname, gender, birth, phone, email, rmpoint) values ('test1', '1111', '홍길동', 'm', '1999-01-01', '010-8383-2083', 'a001@naver.com', 1000);
insert into member_addr(uid, zip, addr1, addr2, isbasic) values ('test1', '12345', '서울시 강남구', '123-456', 'y');
insert into member_point (uid, usepoint, rmpoint, content) values ('test1', 1000, 1000, '가입 축하금'); 

insert into member_info(uid, pwd, uname, gender, birth, phone, email, rmpoint) values ('test2', '1111', '주리송', 'f', '2009-09-09', '010-9999-9999', 'b99@gmail.com',  1000);
insert into member_addr(uid, zip, addr1, addr2, isbasic) values ('test2', '11223', '울산시 남구', '222-456', 'y');
insert into member_point (uid, usepoint, rmpoint, content) values ('test2', 1000, 1000, '가입 축하금'); 

insert into member_addr (uid, zip, addr1, addr2, isbasic) values ('test1', '98765', '부산시 연제구', '789-123', 'n');
insert into member_addr (uid, zip, addr1, addr2, isbasic) values ('test2', '88995', '인천시 소래포구', '456-123', 'n');
insert into member_addr (uid, zip, addr1, addr2, isbasic) values ('test1', '65487', '수원시 팔달구', '888-22', 'n');
 
select * from member_info; 
select * from member_addr;	
select * from member_point;

-- delete from member_point;
-- delete from member_addr;
-- delete from member_info; 


/*
1. 자유게시판 (brd_free)을 명세서에 입력
2. 자유게시판 (brd_free)에서 사용할 댓글 테이블 reply_free 명세서 제작
3. 댓글 테이블 제작

컬럼명, 데이터 타입은 알아서
*/


-- 상품 관련 테이블들
-- 대분류 테이블
create table product_big (
	big_id		char(2)	primary key,		-- 대분류ID
	big_name	varchar(20)	not null,		-- 대분류명
	big_desc	varchar(200),				-- 설명
	big_date	datetime default now(),		-- 등록일
	admin_id	varchar(20)	not null		-- 등록 관리자 ID
); -- 남성, 여성, 아동
insert into product_big values ('MA', '남성화', '남자꺼', now(), 'sa');
insert into product_big values ('FA', '여성화', '여자꺼', now(), 'sa');
insert into product_big values ('KA', '아동화', '애들꺼', now(), 'sa');

select * from product_big;

-- 쇼핑몰 소분류 테이블
create table product_small (			 
	small_id	char(4)	primary key,	-- 소분류ID
	big_id		char(2)	,				-- 대분류ID
	small_name	varchar(20)	not null,	-- 소분류명
	small_desc	varchar(200) ,			-- 설명
	small_date	datetime default now(),	-- 등록일
	admin_id	varchar(20)	not null,	-- 등록자
    constraint fk_product_big_id foreign key (big_id) references product_big(big_id)
);
-- 남 : 구두, 캐주얼, 등산화 
insert into product_small values('MA01', 'MA', '구두', '', now(), 'sa');
insert into product_small values('MA02', 'MA', '캐쥬얼', '', now(), 'sa');
insert into product_small values('MA03', 'MA', '등산화', '', now(), 'sa');
-- 여 : 구두, 캐주얼, 등산화 
insert into product_small values('FA01', 'FA', '구두', '', now(), 'sa');
insert into product_small values('FA02', 'FA', '캐쥬얼', '', now(), 'sa');
insert into product_small values('FA03', 'FA', '등산화', '', now(), 'sa');
-- 여 : 구두, 운동화
insert into product_small values('KA01', 'KA', '구두', '', now(), 'sa');
insert into product_small values('KA02', 'KA', '운동화', '', now(), 'sa');

select * from product_small;

-- 쇼핑몰 상품 브랜드 테이블
create table product_brand ( 
	br_id		char(2)	primary key,	-- 브랜드ID
	br_name		varchar(20)	not null,	-- 브랜드명
	br_korname	varchar(20)	not null,	-- 브랜드한글명
	br_img		varchar(20) not null,	-- 브랜드 이미지
	br_desc		varchar(200) not null,	-- 설명
	br_date		datetime default now(),	-- 등록일
	admin_id	varchar(20)	not null	-- 등록 관리자 ID
);
-- 마틴, 랜드로바, 버팔로, 크로커다일, 허쉬파피, K2, 몽블랑, 칸투칸, 
insert into product_brand values('AA', 'martin', '마틴', 'aa.jpg', '', now(), 'sa' );
insert into product_brand values('AB', 'Landrover', '랜드로바', 'ab.jpg', '', now(), 'sa' );
insert into product_brand values('AC', 'Buffalo', '버팔로', 'ac.jpg', '', now(), 'sa' );
insert into product_brand values('AD', 'Crocodile', '크로커다일', 'ad.jpg', '', now(), 'sa' );
insert into product_brand values('AE', 'HushPuppies', '허쉬파피', 'ae.jpg', '', now(), 'sa' );
insert into product_brand values('AF', 'K2', '케이투', 'af.jpg', '', now(), 'sa' );
insert into product_brand values('AG', 'Montblanc', '몽블랑', 'ag.jpg', '', now(), 'sa' );
insert into product_brand values('AH', 'KANTUKAN', '칸투칸', 'ah.jpg', '', now(), 'sa' );

select * from product_brand;

-- 쇼핑몰 상품정보 테이블 
create table product_info (
	pd_id		char(6)	primary key,	-- 상품아이디
	small_id	char(4)	,				-- 소분류ID
	br_id		char(2)	,				-- 브랜드ID
	pd_name		varchar(50)	not null,	-- 상품명
	pd_maker	varchar(50)	not null,	-- 제조사
	pd_origin	varchar(50)	not null,	-- 원산지
	pd_cost		int default 0,			-- 원가
	pd_price	int	default	0,			-- 가격
	pd_dc		int	default 0,			-- 할인율
	pd_save		int	default	0,			-- 적립율
	pd_option	varchar(200),			-- 옵션
	pd_img1		varchar(50)	not null,	-- 상품이미지1
	pd_img2		varchar(50),			-- 상품이미지2
	pd_img3		varchar(50),			-- 상품이미지3
	pd_desc		varchar(50) not null,	-- 설명이미지
	pd_stock	int	default	-1,			-- 재고량
	pd_salecnt	int	unsigned default 0,	-- 판매수
    reviewcnt	int	unsigned default 0,	-- 후기개수
	star_avg	float default 0.0,		-- 별점평균
	pd_isview	char(1) default 'n',	-- 게시여부
	pd_date		datetime default now(),	-- 상품등록일
	admin_id	varchar(20)	not null,	-- 등록관리자
	pd_last		datetime default now(),	-- 최종수정일
	last_admin	varchar(20)	not null,	-- 최종수정관리자
    constraint fk_product_info_small_id foreign key (small_id) references product_small(small_id),
    constraint fk_product_info_br_id foreign key (br_id) references product_brand(br_id) 
);

insert into product_info (pd_id, small_id, br_id, pd_name, pd_maker, pd_origin, 
	pd_cost, pd_price, pd_dc, pd_save, pd_option, 
    pd_img1, pd_img2, pd_img3, pd_desc, pd_stock, pd_salecnt, reviewcnt, star_avg, pd_isview, pd_date, 
    admin_id, pd_last, last_admin)
values ('pdt001', 'MA01', 'AA', '좋은 상품', '금강제화', '한국',
	88000, 150000, 0, 1, 'Size:250,255,260,265,270,275', 'pdt001_1.jpg', 'pdt001_2.jpg', 'pdt001_3.jpg', 
    'pdt001_desc.jpg', -1, 0, 0, 0.0, 'n', now(), 'sa', now(), 'sa');

 
select * from product_info;


-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.05 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --
-- 주문관련 테이블
create table order_info	( 
	od_id		char(10)	primary key,	-- 주문아이디
	ismember	char(1)	default 'y',		-- 회원여부
	sender		varchar(20)	not null, 		-- 주문자ID or 이름
	pwd			varchar(20),				-- 비회원용 비밀번호
	e_phone		varchar(13) not null, 		-- 주문자전화번호
	e_mail		varchar(50),				-- 주문자이메일
	receiver	varchar(20)	not null, 		-- 수취인명
	r_phone		varchar(13)	not null, 		-- 수취인전화번호
	r_email		varchar(50),				-- 수취인메일주소
	zip			char(5)	not null, 			-- 배송지 우편번호
	addr1		varchar(50)	not null, 		-- 배송지 주소1
	addr2		varchar(50) not null, 		-- 배송지 주소2
	msg			varchar(100),				-- 메시지
	payment		char(1),					-- 결제방법
	deli_pay	int	default 0,				-- 배송비
	deli_kind	char(1),					-- 배송구분
	pay			int	default 0,				-- 결제액
	use_point	int	default 0, 				-- 사용 포인트
	save_point	int,						-- 적립 포인트
	od_status	char(1)	default 'b',		-- 주문상태
	bank		varchar(20),				-- 환불받을 은행명
	od_account	varchar(20),				-- 환불받을 계좌번호
    acc_name	varchar(20),				-- 환불받을 예금주명
    od_date		datetime default now(),		-- 주문일자
    deli_num	varchar(20)					-- 송장번호
);

-- 장바구니 테이블
create table order_cart (
	idx			int	unsigned auto_increment primary key,	-- 일련번호
    ismember	char(1) default 'y',		-- 회원여부 (y:회원,n:비회원)
	buyer		varchar(50)	not null, 		-- 주문자ID or 세션ID
	pd_id		char(6),					-- 상품아이디
	cnt			int default 1,				-- 수량
	opt			varchar(50)	default '',		-- 옵션
	regdate		datetime default now(),		-- 등록일
	constraint fk_order_cart_pd_id foreign key (pd_id) references product_info(pd_id)
);


-- 쇼핑몰 주문정보 상세 테이블
create table order_detail ( 
	idx			int	unsigned auto_increment primary key,	-- 일련번호
	od_id		char(10),					-- 주문번호
	pd_id		char(6),					-- 상품아이디
	pd_name		varchar(50)	not null, 		-- 품명
	pd_img1		varchar(50)	not null, 		-- 상품이미지1
	pd_price	int	default 0, 				-- 단가
	cnt			int	default 1, 				-- 수량
	opt			varchar(50) default '', 	-- 옵션
	constraint fk_order_detail_od_id foreign key (od_id) references order_info(od_id),
    constraint fk_order_detail_pd_id foreign key (pd_id) references product_info(pd_id)
);

select * from product_brand;
update product_brand set br_desc = '브랜드 설명';
update product_brand set br_desc = '마틴입니다.' where br_id = 'AA';



-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.06 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --
select * from product_brand;
update product_brand set br_desc = '쓸만합니다.' where br_id in('AC', 'AE');

ALTER TABLE `ksr`.`order_info` 
CHANGE COLUMN `e_phone` `s_phone` VARCHAR(13) NOT NULL ,
CHANGE COLUMN `e_mail` `s_mail` VARCHAR(50) NULL DEFAULT NULL ;

-- 주문처리
insert into order_info (od_id, ismember, sender, s_phone, s_mail, receiver, r_phone, r_email, 
zip, addr1, addr2, msg, payment, deli_pay, deli_kind, pay, use_point, save_point, od_status) values 
('2104061001', 'y', 'test1', '010-1234-5678', 'test@test.com', '홍길동', '010-1234-5678', 'test@test.com',
'12345', '서울시 강남구', '123-456', '', 'a', 2000, 'a', 89000, 1000, 890, 'b');
insert into order_detail(od_id, pd_id, pd_name, pd_img1, pd_price, cnt, opt) values
('2104061001', 'pdt001', '좋은 상품', 'pdt001_1.jpg', 88000, 1, '270');
-- update order_info set pay = 89000, use_point = 1000, save_point = 890 where od_id = '2104061001';

-- 포인트 관련 쿼리 - update(member_info), insert(member_point)
insert into member_point(uid, usepoint, rmpoint, kind, content) values ('test1', 1000, 0, 'u', '2104061001');
insert into member_point(uid, usepoint, rmpoint, kind, content) values ('test1', 890, 890, 's', '2104061001');
update member_info set rmpoint = 890 where uid = 'test1';

-- 상품 판매개수 증가 - update(product_info)
update product_info set pd_salecnt = (pd_salecnt + 1) where pd_id = 'pdt001';

select * from product_info;
SELECT * FROM order_info;
SELECT * FROM order_detail;
select * from member_point;
select * from member_info;

select * from product_brand where br_name like '%a%';
-- 영문 브랜드명에 'a'가 들어있는 데이터 출력

insert into product_info values ('pdt002', 'MA01', 'AB', '꽤 좋은 상품', '금강제화', '한국', 
98000, 200000, 0, 1, 'Size:250,255,260,265,270,275', 'pdt002_1.jpg', 'pdt002_2.jpg', 
'pdt002_3.jpg', 'pdt002_desc.jpg', 300, 0, 0, 0.0, 'n', now(), 'sa', now(), 'sa');

insert into product_info values ('pdt003', 'MA02', 'AA', '보통 상품', '그냥제화', '한국', 
66000, 120000, 0, 1, 'Size:250,255,260,265,270,275', 'pdt003_1.jpg', 'pdt003_2.jpg', 
'pdt003_3.jpg', 'pdt003_desc.jpg', 500, 0, 0, 0.0, 'n', now(), 'sa', now(), 'sa');


select small_id, sum(pd_price) from product_info group by small_id;
select small_id, pd_price from product_info group by small_id, pd_price;
select small_id, sum(pd_price), avg(pd_price), count(*)
from product_info 
group by small_id;
-- small_id를 기준으로 그룹화 하여 price컬럼의 값은 더하여 출력

-- bank가 null이면 '국민은행'으로 null이 아니면 해당 값으로 출력
select od_id, sender, ifnull(bank, '국민은행') as bank from order_info;

-- 브랜드별 브랜드ID, 상품개수, 평균가격 출력(컬럼 이름 변경)
select br_id, sum(pd_stock), avg(pd_price) as '평균가격' from product_info group by br_id;

-- 상품테이블에서 재고량을 출력하는 재고량이 -1이면 '무제한'이라고 출력
select pd_id, pd_name, if(pd_stock = -1, '무제한', pd_stock) as '재고량' from product_info;

-- 상품ID, 상품명, 재고량 (컬럼이름 변경)
select pd_id, pd_name, pd_stock as '재고량' from product_info;

-- 연도(cYear)와 월(cMonth), 일(cDay)을 저장할 테이블 birth 제작 
create table birth (
	cYear 	int,
	cMonth 	int,
	cDay 	int
);

insert into birth (cyear, cmonth, cday) values ( 1988, 11, 15 ) ;
insert into birth (cyear, cmonth, cday) values ( 1999,  5, 16 ) ;
insert into birth (cyear, cmonth, cday) values ( 2000,  4,  6 ) ;

select * from birth;

-- 1988-11-15, 1999-05-16, 2000-04-06

select concat(cyear, '-', lpad(cmonth, 2, '0'), '-', lpad(cday, 2, '0')) birthday from birth;

-- 문자열 함수

select ascii('a') ;
select char(97);
select char_length('abcd가나다라');					-- 8
select length('abcd가나다라');						-- 16
select concat('abc', 'def');						-- abcdef
select concat_ws('/', '2021', '04', '06');			-- 2021/04/06
select elt(2, 'aa', 'bb', 'cc', 'dd');				-- bb
select field('cc', 'aa', 'bb', 'cc', 'dd');			-- 3
select find_in_set('ff', 'aa,cc,ff,zz');			-- 3
select instr('abcdefg', 'def');						-- 4
select locate('def', 'abcdefg');					-- 4
select format(3.141592, 3);							-- 3.142
select format(3.141592, 10);						-- 3.1415920000
select bin(31), hex(31), oct(31);					-- 11111	1F	37
select insert('abcdefghij', 3, 4, '@@');			-- ab@@ghij
select left('abcdefghij', 4);						-- abcd
select right('abcdefghij', 4);						-- 'ghij'
select mid('abcdefghij', 4, 3);						-- 'def'
select lcase('abCD가나');							-- abcd가나
select ucase('abCD가나');							-- ABCD가나
select lpad('abcd', 7, '#');						-- ###abcd
select lpad('abcd', 3, '#');						-- abc
select rpad('abcd', 7, '#');						-- abcd###
select rpad('abcd', 3, '#');						-- abc
select ltrim('   ab  cd   ');						-- 'ab  cd   '
select rtrim('   ab  cd   ');						-- '   ab  cd'
select trim('   ab  cd   ');						-- 'ab  cd'
select repeat('abc', 3);							-- abcabcabc
select replace('abcedf', 'cd', 'zz');				-- abzzef
select revsere('abcd');								-- dcba
select concat('::', space(5), '::');				-- ::     ::
select substring('abcdefg', 2, 3);					-- bcd
select substring_index('cafe.naver.com', '.', 2);	-- cafe.naver
select substring_index('cafe.naver.com', '.', -2);	-- naver.com


-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.07 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --

use world;

select * from city;				desc city;
select * from country;			desc country;
select * from countrylanguage;  desc countrylanguage;

-- 나라별 인구가 100만 ~ 900만인 도시 개수 : 나라코드, 도시개수
select CountryCode, count(ID) as cnt 
from city 
-- where population between 1000000 and 9000000
where population >= 1000000 and population <= 9000000
group by CountryCode ;

-- 나라별 인구가 500만 이상인 도시들을 인구 많은 순으로 정렬하여 모든 커럼 출력
select ID, Name, CountryCode, District, population
from city 
where population >= 5000000 
order by population desc;

select floor(1.9), truncate(1.9, 0), floor(-1.9), truncate(-1.9, 0);	-- '1', '1', '-2', '-1'
select ceil(1.9),  truncate(1.9, 0), ceil(-1.9),  truncate(-1.9, 0);	-- '2', '1', '-1', '-1'

select current_date(), current_time(), localtimestamp;
select curdate(), current_date(), current_date;
select curtime(), current_time(), current_time;
select now(), sysdate(), localtime(), localtimestamp();
select hour(now()), minute(now()), second(now()), microsecond(now());
select year(now()), month(now()), day(now());
select date('2021-04-07 11:46:01'),  time('2021-04-07 11:46:01') ;

 select adddate('2021-02-01', interval 31 day), date_add('2021-02-01', interval -31 day);
 select subdate('2021-02-01', interval 31 day), date_sub('2021-02-01', interval -31 day);

select	addtime('2021-01-01 23:59:59', '1:1:1'); -- > 2021-01-02 01:01:00
select	subtime('2021-01-01 23:59:59', '1:1:1'); -- > 2021-01-01 22:58:58

select	addtime('23:59:59', '1:1:1'); -- > 25:01:00
select	subtime('23:59:59', '1:1:1'); -- > 22:58:58

select	addtime('23:59:59', '-1:1:1'); -- > 22:58:58 (음수 갑솓 연산가능)
select	subtime('23:59:59', '-1:1:1'); -- > 25:01:00 (음수 갑솓 연산가능)

select	addtime('23:59:59', '1:1'); -- > 25:00:59 (시와 분으로 연산됨)
select	subtime('23:59:59', '1:1'); -- > 22:58:59 (시와 분으로 연산됨)

select	addtime('23:59:59', '1'); -- > 24:00:00 (하나만 있으면 초로 연산됨)
select  subtime('23:59:59', '1'); -- > 23:59:58 (하나만 있으면 초로 연산됨)

select datediff('2021-07-15', now()), datediff('2021-01-28', now());
select timediff('23:59:59', curtime()), timediff('00:00:00', curtime());
select dayofweek('2021-12-25'), monthname(now()), dayofyear(now());

-- 심심풀이 문제
-- 나라별 나라코드, 도시의 개수, 도시인구 평균(소수점 아래 2자리까지) 출력
select countrycode, count(id), truncate(avg(population), 2)  
from city group by countrycode;

-- 나라별 가장 인구가 많은 나라코드, 도시의 이름, 인구수 출력 
select countrycode, name, max(population)  
from city group by countrycode order by population desc;

select countrycode, name, population  
from city group by countrycode having population = max(population);
 

use ksr;

create table test1 (
	uid varchar(5) primary key, 
    pwd varchar(5) not null
);

insert into test1 values ('test1', '1111');
insert into test1 values ('test2', '1111');
insert into test1 values ('test3', '1111');
delete from test1 where uid = 'test1';
update test1 set uid = 'tes22' where uid = 'test2';
delete from test1 where uid = 'test3';
select * from test1;

create table test2 (
	num int auto_increment primary key,
    uid varchar(5) not null, 
    constraint fk_test2_uid foreign key (uid) references test1(uid) on update cascade on delete cascade 
    -- on update cascade : 참조 컬럼의 값이 바뀌면 같이 바뀐다는 의미
    -- on delete cascade : 참조 컬럼의 값이 삭제되면 같이 삭제된다는 의미
);
insert into test2 (uid) values ('test1');
insert into test2 (uid) values ('test2');
select * from test2;

create table test3 (
	num int auto_increment primary key,
    uid varchar(5) , 	--  on delete set null로 지정하기 위해 not null 사용 안함
    constraint fk_test3_uid foreign key (uid) references test1(uid) on update cascade on delete set null
    -- on delete set null : 참조 컬럼의 값이 삭제되면 외래키의 값에 null을 넣겠다는 의미
    -- set null을 지정하려면 외래키로 지정되는 컬럼이 null을 허용해야 함(not null 불가)
);

insert into test3 (uid) values ('tes22');
insert into test3 (uid) values ('test3');
select * from test3;




-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.08 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --

use ksr;


-- 설문조사 질문 테이블
create table poll_question (
	idx int unsigned auto_increment primary key, 	-- 일련번호
	question varchar(100) not null, -- 질문내용
	q_desc text, 					-- 설문설명
	sdate varchar(12) not null, 	-- 설문시작일(yyyymmddhhmm)
	edate varchar(12), 				-- 설문종료일(yyyymmddhhmm)
	end_msg varchar(50), 			-- 설문종료 메시지(종료일이 없을 경우 입력)
	ex1 varchar(50) not null, 		-- 보기1
	ex2 varchar(50) not null, 		-- 보기2
	ex3 varchar(50), 				-- 보기3
	ex4 varchar(50), 				-- 보기4
	ex5 varchar(50), 				-- 보기5
	regdate datetime default now(), -- 등록일
	q_status char(1) default 'a', 	-- 상태
	admin_id varchar(20) not null 	-- 등록관리자
);

-- 설문조사 결과 테이블
create table poll_result (
	idx	int unsigned auto_increment unique, 	-- 일련번호
	uid varchar(20), 				-- 회원ID
	q_idx int unsigned, 			-- 설문번호
	ex_num int not null, 			-- 선택한 보기번호
	pdate datetime default now(), 	-- 설문참여일자
	constraint fk_poll_result_uid foreign key (uid) references member_info(uid), 
	constraint fk_poll_result_q_idx foreign key (q_idx) references poll_question(idx), 
    constraint pk_poll_result primary key (uid, q_idx)
    -- 한 명의 회원(uid)은 하나의 설문(q_idx)에 한 번만 참여할 수 있게됨
);

-- 학사 관련 테이블들
-- 학생 정보 테이블
create table student_info (
	s_idx int unsigned auto_increment primary key,		-- 일련번호
	s_name varchar(20) not null, 					-- 이름
	s_phone varchar(13)not null, 					-- 전환
	s_regnum char(14) not null, 					-- 민증
	s_regdate datetime default now()				-- 등록일
);

-- 강사 정보 테이블
create table teacher_info (
	t_idx int unsigned auto_increment primary key, 	-- 일련번호
	t_name varchar(20) not null, 			-- 이름
	t_phone varchar(13) not null, 			-- 전화
	t_lecture varchar(100) not null, 		-- 강의가능과목
	t_history text not null, 				-- 이력
	t_status char(1) default 'a'			-- 현 상태
);

-- 강의실 정보 테이블
create table room_info (
	r_idx int unsigned auto_increment primary key, 	-- 일련번호
	r_building varchar(50) not null, 	-- 건물명
	r_floor int not null, 				-- 층수
	r_count int not null 				-- 수용 인원
);

-- 과정 정보 테이블
create table course_info (
	c_idx int unsigned auto_increment primary key, 	-- 일련번호
	c_name varchar(100) not null, 		-- 과정명
	c_sdate	datetime not null, 			-- 과정 시작일
	c_edate	datetime not null, 			-- 과정 종료일
	r_idx int unsigned, 				-- 강의실id
	c_desc text not null, 				-- 과정설명
	c_total	int not null, 				-- 수강가능인원
	c_count	int not null, 				-- 수강인원
	c_ttime	int not null, 				-- 총 강의시간
	c_dtime	int not null, 				-- 일일 강의시간
	c_cost	int default 0, 				-- 수강료
	c_status char(1) default 'a', 		-- 개설여부
	c_regdate datetime default now(), 	-- 등록일
	constraint fk_course_info_r_idx foreign key (r_idx) references room_info(r_idx)
);

-- 과정 강사 테이블
create table course_teacher (
	c_idx int unsigned, -- 과정id
	t_idx int unsigned, -- 강사id
	constraint fk_course_teacher_c_idx foreign key (c_idx) references course_info(c_idx), 
	constraint fk_course_teacher_t_idx foreign key (t_idx) references teacher_info(t_idx), 
    constraint pk_course_teacher primary key (c_idx, t_idx)
);

-- 강의정보 테이블
create table course_lecture (
	s_idx int unsigned, 			-- 학생id
	c_idx int unsigned, 			-- 과정id
	cl_status char(1) default 'a', 	-- 현 상태
	constraint fk_course_lecture_s_idx foreign key (s_idx) references student_info(s_idx), 
	constraint fk_course_lecture_c_idx foreign key (c_idx) references course_info(c_idx), 
    constraint pk_course_lecture primary key (s_idx, c_idx)
);



-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.09 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --

use ksr;

-- 조인 테스트
select * from member_info;
select * from member_addr;
select * from member_point;
select * from order_info;

-- 회원ID, 이름, 전화번호, 주소를 출력하는 데 기본주소만 출력
select i.uid, i.uname, i.phone, a.zip, a.addr1, a.addr2 
from member_info i inner join member_addr a on i.uid = a.uid 
where a.isbasic = 'y';

select i.uid, i.uname, i.phone, a.zip, a.addr1, a.addr2 
from member_info i, member_addr a 
where i.uid = a.uid and a.isbasic = 'y';

-- 회원들 중 한번 이라도 구매를 한 회원의 아이디, 이름, 결제금액을 출력
select m.uid, m.uname, o.pay 
from order_info o, member_info m 
where o.sender = m.uid and o.ismember = 'y';

-- 모든 회원의 구매 횟수를 출력 : 아이디, 이름, 구매횟수, 총 결제금액
-- 구매를 한 번도 안한 회원도 출력되어야 함
select m.uid, m.uname, count(o.od_id) 구매횟수, ifnull(sum(o.pay), 0) 총결제액 
from member_info m left outer join order_info o on o.sender = m.uid 
group by m.uid, m.uname ;

-- 한번도 구매를 하지 않은 회원 검색
select m.* from member_info m left outer join order_info o on o.sender = m.uid 
group by m.uid, m.uname having count(o.od_id) = 0 ;

-- member_addr과 member_point의 레코드들을 하나로 합쳐 출력
select * from member_addr
union
select * from member_point;

-- 대분류명, 소분류명, 브랜드명, 상품ID, 상품명 출력
select * from product_big; -- big_id, big_name
select * from product_small; -- big_id, small_id, small_name
select * from product_brand; -- br_id, br_name
select * from product_info; -- small_id, br_id, pd_name

select a.big_name, b.small_name, c.br_name, d.pd_id, d.pd_name 
from product_big a, product_small b, product_brand c, product_info d 
where a.big_id = b.big_id and b.small_id = d.small_id and c.br_id = d.br_id;

-- 구매내역 : 주문번호, 주문일자, 상품명, 주문금액, 상태, 상품이미지, 수량
select a.od_id, a.od_date, c.pd_name, a.pay, a.od_status, c.pd_img1, b.cnt 
from order_info a, order_detail b, product_info c 
where a.od_id = b.od_id and c.pd_id = b.pd_id;

select a.od_id, a.od_date, b.pd_name, a.pay, a.od_status, b.pd_img1, b.cnt 
from order_info a, order_detail b 
where a.od_id = b.od_id;

create view v_order_list as 
	select a.od_id, a.od_date, b.pd_name, a.pay, a.od_status, b.pd_img1, b.cnt 
	from order_info a, order_detail b where a.od_id = b.od_id;
select * from v_order_list;

-- 회원전용 구매목록 view 생성 : v_order_list_m
create view v_order_list_m as 
	select a.od_id, a.od_date, b.pd_name, a.pay, a.od_status, b.pd_img1, b.cnt 
	from order_info a, order_detail b where a.od_id = b.od_id and a.ismember = 'y';
select * from v_order_list_m;

-- 비회원전용 구매목록 view 생성 : v_order_list_g
create view v_order_list_g as 
	select a.od_id, a.od_date, b.pd_name, a.pay, a.od_status, b.pd_img1, b.cnt 
	from order_info a, order_detail b where a.od_id = b.od_id and a.ismember = 'n';
select * from v_order_list_g;

-- 한 번 이라도 구매를 한 회원의 아이디, 이름, 이메일을 출력
select distinct a.uid, a.uname, a.email 
from member_info a, order_info b where a.uid = b.sender;

show index from order_detail;

select * from course_info;
show index from course_info;
create index idx_course_info_name on course_info(c_name);
drop index idx_course_info_name on course_info;

-- 한번도 구매하지 않은 회원의 아이디와 이름을 출력(서브쿼리 이용)
select uid, uname 
from member_info 
where uid not in (select sender from order_info where ismember = 'y');



-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.12 (월) ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --

use ksr; 

drop procedure if exists sp_if_ex;

delimiter $$
create procedure sp_if_ex()
begin
	declare num int;
    set num = 100;
    
    if num = 100 then
		select '100입니다.';
	else
		select '100이 아닙니다.';
	end if;
    
end $$
delimiter ; 

call sp_if_ex();



drop procedure if exists sp_if_ex2;

delimiter $$
create procedure sp_if_ex2(num int)	-- 정수를 받아와 짝수인지 홀수인지를 출력
begin
    if num % 2 = 0 then
		select concat(num,'는 짝수입니다.');
	else
		select concat(num,'는 홀수입니다.');
	end if;
end $$
delimiter ; 

call sp_if_ex2(15);


-- 시험점수를 입력받아 학점(A, B, C, D, F) 을 출력하는 프로시저 sp_if_ex3()를 생성 및 실행
-- 출력 : 점수 ==> ?? 점  / 학점 ==> ? 학점

drop procedure if exists sp_if_ex3;

delimiter $$
create procedure sp_if_ex3(score int)	-- 정수를 받아와 짝수인지 홀수인지를 출력
begin
	declare grade char(1); -- 학점을 저장할 변수
     
    if score >= 90 then set grade = 'A';
	elseif score >= 80 then set grade = 'B';
	elseif score >= 70 then set grade = 'C'; 
	elseif score >= 60 then set grade = 'D';
    else set grade = 'F';
	end if;
    
    select concat('점수 ==> ', score) 점수, concat('학점 ==> ', grade, '학점') 학점;
    
end $$
delimiter ; 

call sp_if_ex3(75);


 
-- 시험점수를 입력받아 학점(A, B, C, D, F) 을 출력하는 프로시저 sp_case_ex()를 생성 및 실행
-- 출력 : 점수 ==> ?? 점  / 학점 ==> ? 학점

drop procedure if exists sp_case_ex;

delimiter $$
create procedure sp_case_ex(score int)	-- 정수를 받아와 짝수인지 홀수인지를 출력
begin
	declare grade char(1); -- 학점을 저장할 변수
	
    case
		when score >= 90 then set grade = 'A'; 
        when score >= 80 then set grade = 'B'; 
        when score >= 70 then set grade = 'C'; 
        when score >= 60 then set grade = 'D'; 
		else set grade = 'F';
	end case;
    
    select concat('점수 ==> ', score) 점수, concat('학점 ==> ', grade, '학점') 학점;
    
end $$
delimiter ; 

call sp_case_ex(87);


-- 상품아이디를 받아와서 그에 해당하는 상품의 가격을 2000원 인상하는 프로시저
-- sp_upquery_ex() 생성 후 실행

drop procedure if exists sp_upquery_ex;
delimiter $$ 
create procedure sp_upquery_ex(pdid char(6)) 
begin   
   update product_info set pd_price = pd_price + 2000 where pd_id=pdid;
   -- select pd_name 상품이름, pd_price+2000 가격 from product_info where pd_id = pdid;
end $$
delimiter ;
call sp_upquery_ex('pdt003');
select * from product_info;

-- 상품아이디를 받아와서 그에 해당하는 상품의 가격이 15만원 이상이면 5000원 인하, 미만이면 5000원 인상하는 프로시저
-- sp_upquery_ex2() 생성 후 실행

drop procedure if exists sp_upquery_ex2;
delimiter $$ 
create procedure sp_upquery_ex2(pdid char(6)) 
begin   
	declare price int;
    select pd_price into price from product_info where pd_id = pdid;
    -- pdid에 해당하는 상품가격을 price변수에 저장
    if price >= 150000 then
		update product_info set pd_price = pd_price - 5000 where pd_id = pdid; 
	else 
		update product_info set pd_price = pd_price + 5000 where pd_id = pdid; 
	end if;
end $$
delimiter ;
call sp_upquery_ex2('pdt001'); 
select * from product_info;

-- 회원 가입용 프로시저
-- insert into member_info(uid, pwd, uname, gender, birth, phone, email, rmpoint) values ('test1', '1111', '홍길동', 'm', '1999-01-01', '010-8383-2083', 'a001@naver.com', 1000);
-- insert into member_addr(uid, zip, addr1, addr2, isbasic) values ('test1', '12345', '서울시 강남구', '123-456', 'y');
-- insert into member_point (uid, usepoint, rmpoint, content) values ('test1', 1000, 1000, '가입 축하금'); 
drop procedure sp_member_insert;
delimiter $$ 
create procedure sp_member_insert(uid varchar(20), pwd varchar(20), uname varchar(50), gender char(1), birth char(10), phone varchar(13), email varchar(50), rmpoint int, 
								zip char(5), addr1 varchar(50), addr2 varchar(50), isbasic char(1), usepoint int, content varchar(50))
begin
	insert into member_info(uid, pwd, uname, gender, birth, phone, email, rmpoint) values 
		(uid, pwd, uname, gender, birth, phone, email, rmpoint);
	insert into member_addr(uid, zip, addr1, addr2, isbasic) values (uid, zip, addr1, addr2, isbasic);
	insert into member_point (uid, usepoint, rmpoint, content) values (uid, usepoint, rmpoint, content); 
end $$
delimiter ;

call sp_member_insert('test4', '1111', '임꺽정', 'm', '2000-05-05', '010-6541-2356', 'GGeok@gg.com', 1000, '11222', '강원도 원주시', '111-222', 'y', 1000, '가입축하금') ;

select * from member_info;
select * from member_addr;
select * from member_point;


-- 자유게시판(brd_free)에 글 등록하는 프로시저 sp_brd_free_insert() 프로시저 생성 및 실행(비회원글로 등록)
select * from brd_free;

drop procedure if exists sp_brd_free_insert;

delimiter $$ 
create procedure sp_brd_free_insert(writer varchar(10) , passwd varchar(10), title varchar(100), content text, ipaddr varchar(15))
begin
	insert into brd_free (writer, passwd, title, content, ipaddr) values (writer, passwd, title, content, ipaddr);
end $$
delimiter ;
call sp_brd_free_insert('담이언니', '1234', '담이배방구', '포로로롱 배주물주물 너무귀여워', '127.0.0.1');


-- 회원아이디를 받아와서 해당 회원이 가입한지 지금까지 총 몇일이 지났는지 연산하여 출력 : ~ 님은 가입한지 ??일 지났습니다.
select * from member_info;
select joindate from member_info where uid = 'test1';
select uname, datediff(now(), joindate) from member_info;

drop procedure sp_date_ex;

delimiter $$
create procedure if exists sp_date_ex(id varchar(20))
begin
    declare dayCount int;		-- 일수를 저장할 변수
    declare joinDate datetime;	-- 회원 가입일을 저장할 변수
    declare today datetime;		-- 오늘 날짜를 저장할 변수
    
    select joindate into joinDate from member_info where uid = id;
    -- 받아온 uid를 조건으로 가입일을 joinDate에 저장
    
    set today = now();							-- now() 함수로 오늘 날짜를 today변수에 저장
    set dayCount = datediff(today, joinDate);	-- joinDate 부터 today까지의 기간을 일수로 리턴하여 dayCount변수에 저장
    
    select concat(id,'님이 가입한지 ', dayCount, '일이 지났습니다.');
end $$
delimiter ;

call sp_date_ex('test1');

-- 1 부터 100까지의 합을 구하여 출력하는 프로시저 sp_while_ex()를 생성 및 실행 : 1부터 100까지의 합 ==> ??

drop procedure if exists sp_while_ex;
delimiter $$
create procedure sp_while_ex()
begin
	declare i int;
    declare sum int;
    set i = 1;
    set sum = 0;
    
    while (i <= 100) do
		set sum = sum + i;
        set i = i + 1;
	end while;
    
    select concat('1부터 100까지의 합 =>', sum) as 총합;
end $$
delimiter ;
call sp_while_ex();


-- 1부터 100까지 루프를 돌면서 합을 구하여 출력하는 프로시저 sp_while_ex2()제작
-- 단, 7의 배수는 더하지 않고 누적 합계가 1000이 넘으면 연산을 중지하고 출력 : 결과값 : ??
drop procedure if exists sp_while_ex2;
delimiter $$
create procedure sp_while_ex2()
begin
	declare i int;
    declare sum int;
    set i = 0;
    set sum = 0;
    chkLabel:while (i < 100) do
		set i = i + 1;
        if i % 7 = 0 then iterate chkLabel; end if ;   	-- i가 7의 배수이면 더이상 실행하지 않고 chkLabel이 지정된 반복문의 조건으로 이동
                
        set sum = sum + i;
		if sum > 1000 then leave chkLabel; end if ;  	-- 더이상 실행하지 않고 chkLabel이 지정된 반복문을 빠져나감(빤복문 강제종료)
        
	end while;
    select concat('결과값 ==> ', sum) as 총합;
end $$
delimiter ;
call sp_while_ex2();

-- member_addr에 주소를 추가하는 프로시저 sp_member_addr_insert() 생성 및 실행
-- 테이블에 들어갈 모든 데이터를 매개변수를 통해 받아와서 작업을 처리해야 함
-- 단, 추가하는 주소가 기본주소일 경우 기존의 기본주소를 일반주소로 변경 후 추가해야 함
drop procedure if exists sp_member_addr_insert;
delimiter $$
create procedure sp_member_addr_insert(id varchar(20), zip char(5), addr1 varchar(50), addr2 varchar(50), isbasic char(1))
begin
    if isbasic = 'y' then	-- 추가하려는 주소가 '기본주소'이면 ...
		update member_addr set isbasic = 'n' where uid = id and isbasic = 'y';
    end if;
	
    insert into member_addr (uid, zip, addr1, addr2, isbasic) values (id, zip, addr1, addr2, isbasic);
end $$
delimiter ;
call sp_member_addr_insert('test1', '88793', '서울시 중랑구 면목동', '887-20', 'n');

select * from member_addr;
select * from member_addr  where uid = 'test4' and isbasic = 'y';


-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.13 (화) ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --

use ksr;

drop procedure if exists sp_loop_ex;
delimiter $$
create procedure sp_loop_ex()
begin
    -- loop문을 사용하여 100 이하의 양수들 중 5와 7의 배수들의 합을 구하여 출력
	declare i int;
    declare sum int;
    set i = 1;
    set sum = 0;
    LoopLabel:loop
		if i > 100 then
			leave LoopLabel;
		end if;
		if i % 5 = 0 or i % 7 = 0 then
			set sum = sum + i;
        end if;
        set i = i + 1;
	end loop;
    
    select concat("5와 7의 배수들의 합", sum);
end $$
delimiter ;
call sp_loop_ex();




drop procedure if exists sp_repeat_ex;
delimiter $$
create procedure sp_repeat_ex()
begin
    -- repeat문을 사용하여 100 이하의 양수들 중 5와 7의 공배수들의 합을 구하여 출력
	declare i int;
    declare sum int;
    set i = 1;
    set sum = 0;
    
    repeat
		if i % 5 = 0 and i % 7 = 0 then
			set sum = sum + i;
        end if;
        set i = i + 1;
		until i > 100 -- 조건이 참이면 루프 종료(; 없음)
	end repeat;


    select concat("5와 7의 배수들의 합", sum);
end $$
delimiter ;
call sp_repeat_ex();

-- 함수 생성을 위해서는 권한이 있어야 함 
set global log_bin_trust_function_creators = 1;

-- 두 개의 정수를 받아 합을 리턴하는 함수 f_add() 생성 및 실행
drop function if exists f_add;
delimiter $$
create function f_add(n1 int, n2 int) returns int
begin 
	declare result int;
    set result = n1 + n2;
	return result;
end $$
delimiter ;

select f_add(10, 20);

-- 회원아이디를 매개변수로 받아 해당 회원의 나이를 계산하여 리턴하는 함수 f_get_age() 생성 및 실행

drop function if exists f_get_age;
delimiter $$
create function f_get_age(id varchar(20)) returns int
begin 
	declare age int;
    declare byear int;
    -- 문자열이라도 숫자형으로 생겼으면 int형 변수에 자동으로 변환되어 넣을 수 있음(계산도 가능)
    select left(birth,4) into byear from member_info where uid = id;
	set age = year(now()) - bYear;
	return age;
end $$
delimiter ;
select f_get_age('test1');


-- 회원아이디를 매개변수로 받아 해당 회원의 남/여를 가져오는 함수 f_get_gender() 생성 및 실행
drop function if exists f_get_gender;
delimiter $$
create function f_get_gender(id varchar(20)) returns char(1)
begin 
	declare g char(1);
    
    select gender into g from member_info where uid = id;
	if g = 'f' then 
		return '여';
	else
		return '남';
	end if;
end $$
delimiter ; 


-- sp_show_member() 라는 회원정보를 보여주는 프로시저를 생성 후 실행
-- 매개변수로 받아온 회원아이디를 검색하여 해당 회원의 아이디, 이름, 생일, 나이, 성별(남,여) 등을 출력
drop procedure if exists sp_show_member;
delimiter $$
create procedure sp_show_member(id varchar(20)) 
begin
	select id, uname, birth, f_get_age(id) as age, f_get_gender(id) as gender from member_info where uid = id;
end $$
delimiter ;

call sp_show_member('test1');

select uid, uname, birth, f_get_age(uid), if(gender = "m", "남", "여")
from member_info where f_get_age(uid) < 30;
-- select절이나 where절에서도 스토어드 함수를 사용할 수 있음


-- 상품별 판매순위 쿼리 작성(많이 판매된 순서대로 정렬하되 같은 판매량에서는 가격이 높은 순으로 정렬)
-- 상품아이디, 상품명, 소분류명, 브랜드명, 가격, 판매량 순으로 보여주기(판매가 한 번도 없는 상품은 생략) : order테이블과 조인
select a.pd_id, a.pd_name, c.small_name, d.br_name, sum(a.pd_price) price, sum(b.cnt) cnt
from product_info a, order_detail b, product_small c, product_brand d
where a.pd_id = b.pd_id and a.small_id = c.small_id and a.br_id = d.br_id
group by a.pd_id, a.pd_name, c.small_name, d.br_name
order by cnt desc, price desc;


-- 상품별 판매순위 쿼리 작성(많이 판매된 순서대로 정렬하되 같은 판매량에서는 가격이 높은 순으로 정렬)
-- 상품아이디, 상품명, 소분류명, 브랜드명, 가격, 판매량 순으로 보여주기(판매가 한 번도 없는 상품도 포함) : order테이블과 조인
select a.pd_id, a.pd_name, c.small_name, d.br_name, sum(a.pd_price) price, ifnull(sum(b.cnt), 0) cnt
from product_info a left join product_small c on a.small_id = c.small_id
left join product_brand d on a.br_id = d.br_id left join order_detail b on a.pd_id = b.pd_id
group by a.pd_id, a.pd_name, c.small_name, d.br_name
order by cnt desc, price desc;


create table trTest1 (
	c1 int auto_increment primary key,
    c2 varchar(10), c3 varchar(10)
);
insert into trTest1 (c2, c3) values ('asdf1', 'fdsa1');
insert into trTest1 (c2, c3) values ('asdf2', 'fdsa2');
insert into trTest1 (c2, c3) values ('asdf3', 'fdsa3');
select * from trTest1;
select * from trTest2;

create table trTest2 (
	c1 int auto_increment primary key,
    c2 varchar(10), c3 varchar(10)
);

drop trigger if exists tr1;
delimiter $$
create trigger tr1 after update on trTest1 for each row
-- trTest1테이블에서 update기능이 실행되는 순간 처리할 트리거 tr1을 생성
begin
	insert into trTest2 (c2, c3) values ('asdf4', 'fdsa4');
	-- 트리거에는 해당 테이블에 대한 쿼리는 사용할 수 없으므로 다른 테이블에 대한 쿼리를 사용해야 함
end $$
delimiter ;

update trTest1 set c2 = '222' where c1 = 1;


-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.13 (화) ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --

use world;

-- gnp가 예전 gnp보다 높은 나라들의 이름과 코드, 도시개수와 도시의 평균 인구수를 보여주는 쿼리를 이용하여 view를 생성
-- 뷰의 이름은 v_develop_country , 현재 gnp가 20만 이상, 예전 gnp가 null이 아니어야 함
select * from country;
select * from city;
 
drop view if exists v_develop_country ;
create view v_develop_country as 
select b.code, b.name, count(a.ID), avg(a.population)
from city a, country b
where a.countrycode = b.code and b.gnp >= 200000 and b.gnp > b.gnpold and b.gnpold is not null
group by b.code, b.name ;
  
select * from v_develop_country; 

use ksr;
-- 이번달 판매된 상품들을 많이 판매된 순서대로 상위 10개를 출력하는 쿼리 작성
-- 상품ID, 상품명, 판매개수

select a.pd_id, a.pd_name, sum(b.cnt) cnt
from product_info a, order_detail b, order_info c
where a.pd_id = b.pd_id and b.od_id = c.od_id and od_status = 'd' 
and left(c.od_date, 7) = left(now(), 7) 
group by a.pd_id, a.pd_name
limit 10;


-- brd_free 테이블에 아무 데이터나 98개를 등록시키는 프로시저 sp_brd_insert_tmp() 작성 후 실행
-- 제목 : ?번째 제목입니다. | 내용 : ?번째 내용입니다. 
drop procedure if exists sp_brd_insert_tmp;

delimiter $$
create procedure sp_brd_insert_tmp()
begin
	declare i int;
    declare d_writer varchar(20);
    declare d_title varchar(100);
    declare d_content text;
    set i = 1; 
    /*
	label:loop
	if i > 98 then
		leave label;
	end if;
	end loop;
    */
    while (i <= 98) do 
		set d_writer = concat(i, '작성자');
		set d_title = concat(i, '번째 제목입니다.');
		set d_content = concat(i, '번째 내용입니다.');
		insert into brd_free (writer, passwd, title, content, ipaddr) values(d_writer, '1234', d_title, d_content, '127.0.0.1');
		set i = i + 1;
    end while;
end $$
delimiter ;

call sp_brd_insert_tmp();

select * from brd_free;

-- 게시글ID, 작성자, 제목, 조회수, 작성일 들을 1 페이지에 해당하는 만큼 추출 (1 페이지 : 10개의 게시글)
select idx, writer, title, readcnt, wdate from brd_free where isview='y' order by idx desc limit  0, 10;
select idx, writer, title, readcnt, wdate from brd_free where isview='y' order by idx desc limit 10, 10;

-- 페이지번호와 페이지크기를 매개변수로 받아와 해당 페이지의 게시글을 select하는 프로시저 sp_brd_free_list()
drop procedure if exists sp_brd_free_list;

delimiter $$
create procedure sp_brd_free_list(pge int , size int) 
begin 
	declare startidx int;
    set startidx = (pge - 1) * size;
	select idx, writer, title, readcnt, wdate from brd_free where isview = 'y' order by idx desc limit startidx, size; 
end $$
delimiter ;

call sp_brd_free_list(2, 10);
 



-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.15 (목) ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --

use ksr;

-- 페이지번호와 페이지크기를 매개변수로 받아와 해당 페이지의 게시글을 select하는 프로시저 sp_brd_free_list()
drop procedure if exists sp_brd_free_list;

delimiter $$
create procedure sp_brd_free_list(cpage int , psize int) 
begin 
	declare s_idx int;
    set s_idx = (cpage - 1) * psize;
	select idx, writer, title, readcnt, wdate from brd_free where isview = 'y' order by idx desc limit s_idx, psize; 
end $$
delimiter ;

call sp_brd_free_list(2, 10);


-- 페이지번호와 페이지크기를 매개변수로 받아와 해당 페이지의 게시글을 select하는 프로시저 sp_brd_free_list()
-- 검색기능(제목, 내용, 제목+내용, 작성자)
drop procedure if exists sp_brd_free_list;
delimiter $$
create procedure sp_brd_free_list(cpage int , psize int, schkind char(1), keyword varchar(10)) 
-- cpage : 현재 페이지번호, psize : 페이지 크기, chkkind : 검색조건, keyword : 검색어
begin 
	declare s_idx int; 
    set s_idx = (cpage - 1) * psize;
	if schkind = 'a' then 		-- 검색조건이 '제목'이면 
		select idx, writer, title, readcnt, wdate from brd_free where isview = 'y' 
        and title like concat('%',keyword,'%') order by idx desc limit s_idx, psize; 
	elseif schkind = 'b' then 	-- 검색조건이 '내용' 이면
		select idx, writer, title, readcnt, wdate  from brd_free where isview = 'y' 
        and content like concat('%',keyword,'%') order by idx desc limit s_idx, psize; 
	elseif schkind = 'c' then  	-- 검색조건이 '제목+내용' 이면 
		select idx, writer, title, readcnt, wdate  from brd_free where isview = 'y' 
        and (title like concat('%',keyword,'%') or content like concat('%',keyword,'%')) 
        order by idx desc limit s_idx, psize; 
	elseif schkind = 'd' then  	-- 검색조건이 '작성자' 이면
		select idx, writer, title, readcnt, wdate  from brd_free where isview  = 'y' 
        and writer like concat('%',keyword,'%') order by idx desc limit s_idx, psize; 
	else						-- 검색을 하지 않는 경우
		select idx, writer, title, readcnt, wdate  from brd_free where isview = 'y' 
        order by idx desc limit s_idx, psize; 
	end if;	
end $$
delimiter ;
call sp_brd_free_list(1, 10, 'd', '8');


select * from reply_free;
-- 댓글 테이블을 이용하여 댓글 목록을 가져오는 프로시저 sp_replay_free_list 생성
-- 매개변수로는 게시글번호, 정렬조건(입력시간순, 좋아요순, 싫어요순)
drop procedure if exists sp_replay_free_list;
delimiter $$
create procedure sp_replay_free_list(f_idx int , o_kind char(1)) 
-- cpage : 현재 페이지번호, psize : 페이지 크기, chkkind : 검색조건, keyword : 검색어
begin  
	if o_kind = 'a' then 		-- '좋아요' 순으로 정렬시
		select * from reply_free where free_idx = f_idx and isview = 'y' order by good desc;
	elseif o_kind = 'b' then  	-- '싫어요' 순으로 정렬시
		select * from reply_free where free_idx = f_idx and isview = 'y' order by bad desc;
	else						-- 정렬조건을 선택하지 않는 경우
		select * from reply_free where free_idx = f_idx and isview = 'y' order by idx asc;
	end if;	
end $$
delimiter ;
call sp_replay_free_list(1, 'a');

-- 댓글을 삭제하는 프로시저 sp_replay_free_del() 생성
-- 매개변수로는 댓글 번호, 비밀번호 
drop procedure if exists sp_replay_free_del;
delimiter $$
create procedure sp_replay_free_del(r_idx int, pwd varchar(10))
begin
	update reply_free set isview = 'n' where idx = r_idx and passwd = pwd;
end $$
delimiter ;
call sp_replay_free_del(1, '1234');

 -- insert into reply_free(free_idx, writer, content, passwd, ip) values(1, '댓글작성자', 'ddd', '1234', '127.0.0.1') ;


