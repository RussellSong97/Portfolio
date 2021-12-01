use testMovie;

-- 관리자 관련 테이블 생성 및 기본 데이터 입력
-- 관리자 정보 테이블
create table t_admin (
	a_idx int unique auto_increment, 	-- 관리자 번호
	a_id varchar(20) primary key, 		-- 관리자 아이디
	a_pwd varchar(20) not null, 		-- 비밀번호
	a_name varchar(20) not null, 		-- 이름
	a_isrun char(1) default 'y', 		-- 사용여부(y:사용,n:사용불가)
	a_regdate datetime default now()	-- 등록일자
);
insert into t_admin (a_id, a_pwd, a_name) values ('sa', '1111', '관리자');
select * from t_admin;

-- 회원 관련 테이블 생성 및 기본 데이터 입력
-- 회원 정보 테이블
create table t_member_info (
	mi_id varchar(20) primary key, 		-- 회원아이디
	mi_pwd varchar(20) not null, 		-- 비밀번호
	mi_name varchar(20) not null, 		-- 이름
	mi_birth char(10) not null, 		-- 생년월일
	mi_gender char(1) not null, 		-- 성별
	mi_phone varchar(13) not null, 		-- 전화번호
	mi_email varchar(50) not null, 		-- 이메일
	mi_issns char(1) default 'y', 		-- sns수신여부
	mi_ismail char(1) default 'y', 		-- 메일수신여부
	mi_point int default 0, 			-- 보유포인트
	mi_rebank varchar(50), 				-- 환불은행
	mi_account varchar(50), 			-- 환불계좌
	mi_recommend varchar(20), 			-- 추천인ID
	mi_date datetime default now(), 	-- 가입일
	mi_isactive char(1) default 'y', 	-- 활동여부
	mi_lastlogin datetime 				-- 마지막 로그인일자
);
insert into t_member_info (mi_id, mi_pwd, mi_name, mi_birth, mi_gender, mi_phone, mi_email, mi_point)
values ('test1', '1111', '홍길동', '1988-05-15', 'm', '010-1234-5678', 'hong@gmail.com', 1000);
insert into t_member_info (mi_id, mi_pwd, mi_name, mi_birth, mi_gender, mi_phone, mi_email, mi_point)
values ('test2', '1111', '전우치', '1991-08-05', 'm', '010-2222-5678', 'woochi@nate.com', 1000);
insert into t_member_info (mi_id, mi_pwd, mi_name, mi_birth, mi_gender, mi_phone, mi_email, mi_point)
values ('test3', '1111', '임꺽정', '1998-11-23', 'm', '010-1234-3333', 'lim@naver.com', 1000);
select * from t_member_info;

-- 포인트 사용내역(포인트 사용 및 적립 내역)
create table t_member_point (
	mp_idx int auto_increment primary key, 	-- 일련번호
	mi_id varchar(20), 						-- 회원아이디
	mp_kind char(1) default 's', 			-- 사용/적립 여부(s:적립(예매),a:적립(관리자),u:사용)
	mp_point int default 0, 				-- 사용/적립 포인트
	ti_id char(10), 						-- 적립시 후기 글번호
	mp_content varchar(50) not null, 		-- 사용/적립 내역
	mp_date datetime default now(), 		-- 사용/적립일
	constraint fk_member_point_mi_id foreign key (mi_id) references t_member_info(mi_id)
);
insert into t_member_point (mi_id, mp_point, mp_content) values ('test1', 1000, '가입축하금');
insert into t_member_point (mi_id, mp_point, mp_content) values ('test2', 1000, '가입축하금');
insert into t_member_point (mi_id, mp_point, mp_content) values ('test3', 1000, '가입축하금');


-- 영화 관련 테이블 작성
-- 영화 쟝르 테이블
create table t_film_genre (
	fg_idx int auto_increment primary key, 	-- 쟝르ID
	fg_namekr varchar(20) not null, 		-- 쟝르명(한글)
	fg_nameen varchar(20) not null 			-- 쟝르명(영어)
);
insert into t_film_genre (fg_namekr, fg_nameen) values ('공상과학', 'SF');
select * from t_film_genre;

-- 영화 정보 테이블
create table t_film_info (
	fi_idx int auto_increment primary key, 	-- 영화번호
	fi_namekr varchar(50) not null, 		-- 영화제목-한글
	fi_nameen varchar(50) not null, 		-- 영화제목-영문
	fg_idx int, 							-- 쟝르ID
	fi_country varchar(50) not null, 		-- 국적
	fi_maker varchar(20) not null, 			-- 제작사
	fi_supplier varchar(20) not null, 		-- 배급사
	fi_grade varchar(10) not null, 			-- 관람등급
	fi_rtime int default 0, 				-- 상영시간
	fi_sdate datetime not null, 			-- 개봉일
	fi_edate datetime not null, 			-- 종영일
	fi_sinop varchar(500) not null, 		-- 시놉시스
	fi_poster varchar(50) default '', 		-- 포스터 이미지
	fi_desc varchar(50) default '', 		-- 설명이미지
	fi_notice varchar(50) default '', 		-- 예고편
	fi_rate float default 0.0, 				-- 예매율
	fi_stack int default 0, 				-- 누적관객
	fi_review int default 0, 				-- 관람평 개수
	fi_score float default 0.0, 			-- 평점
	fi_isview char(1) default 'n', 			-- 게시여부
	fi_date datetime default now(), 		-- 등록일
	a_idx int, 								-- 관리자 번호
	fi_last datetime default now(), 		-- 최종수정일
	a_last int, 							-- 최종수정자
	constraint fk_film_info_fg_idx foreign key (fg_idx) references t_film_genre(fg_idx), 
	constraint fk_film_info_a_idx foreign key (a_idx) references t_admin(a_idx)
);

-- 영화 이미지 테이블
create table t_film_pic(
	fp_idx int auto_increment primary key, 	-- 이미지 번호
	fi_idx int, 							-- 영화번호
	fp_img varchar(50) not null, 			-- 이미지명
	fp_isadult char(1) default 'n', 		-- 19금 여부
	fp_isview char(1) default 'n', 			-- 게시여부
	fp_date datetime default now(), 		-- 등록일
	a_idx int, 								-- 관리자 번호
	fp_last datetime, 						-- 최종수정일
	a_last int, 							-- 최종수정자
	constraint fk_film_pic_fi_idx foreign key (fi_idx) references t_film_info(fi_idx), 
	constraint fk_film_pic_a_idx foreign key (a_idx) references t_admin(a_idx)
);

-- 감독 정보 테이블
create table t_director_info (
	di_idx int auto_increment primary key, 	-- 감독 번호
	di_namekr varchar(50) not null, 		-- 감독명(한글)
	di_nameen varchar(50) not null, 		-- 감독명(영문)
	di_desc varchar(500) not null, 			-- 설명
	di_date datetime default now(), 		-- 등록일
	a_idx int, 								-- 관리자 번호
	di_last datetime, 						-- 최종수정일
	a_last int, 							-- 최종수정자
	constraint fk_director_info_a_idx foreign key (a_idx) references t_admin(a_idx)
);
insert into t_director_info (di_namekr, di_nameen, di_desc, a_idx) 
values ('워쇼스키 형제', 'The Wachowskis', '형제같은 자매아닌...', 1);

-- 배우 정보 테이블
create table t_actor_info (
	ai_idx int auto_increment primary key, 	-- 배우 번호
	ai_namekr varchar(50) not null, 		-- 배우명(한글)
	ai_nameen varchar(50) not null, 		-- 배우명(영문)
	ai_desc varchar(500) not null, 			-- 설명
	ai_date datetime default now(), 		-- 등록일
	a_idx int, 								-- 관리자 번호
	ai_last datetime, 						-- 최종수정일
	a_last int, 							-- 최종수정자
	constraint fk_actor_info_a_idx foreign key (a_idx) references t_admin(a_idx)
);
insert into t_actor_info (ai_namekr, ai_nameen, ai_desc, a_idx) 
values ('키아누 리브스', 'Keanu Reeves', '개 건드리면 다 죽임', 1);
insert into t_actor_info (ai_namekr, ai_nameen, ai_desc, a_idx) 
values ('로렌스 피시번', 'Laurence Fishburne', '알약 중독자', 1);

-- 영화 & 감독 테이블
create table t_film_director (
	fd_idx int auto_increment unique, 	-- 일련번호
	fi_idx int, 						-- 영화번호
	di_idx int, 						-- 감독 번호
	fd_date datetime default now(), 	-- 등록일
	a_idx int, 							-- 관리자 번호
	fd_last datetime, 					-- 최종수정일
	a_last int, 						-- 최종수정자
	constraint fk_film_director_fi_idx foreign key (fi_idx) references t_film_info(fi_idx), 
	constraint fk_film_director_di_idx foreign key (di_idx) references t_director_info(di_idx), 
	constraint fk_film_director_a_idx foreign key (a_idx) references t_admin(a_idx), 
    constraint pk_film_director primary key (fi_idx, di_idx)
);

-- 영화 & 배우 테이블
create table t_film_actor (
	fa_idx int auto_increment unique, 	-- 일련번호
	fi_idx int, 						-- 영화번호
	ai_idx int, 						-- 배우 번호
	fa_date datetime default now(), 	-- 등록일
	a_idx int, 							-- 관리자 번호
	fa_last datetime, 					-- 최종수정일
	a_last int, 						-- 최종수정자
	constraint fk_film_actor_fi_idx foreign key (fi_idx) references t_film_info(fi_idx), 
	constraint fk_film_actor_ai_idx foreign key (ai_idx) references t_actor_info(ai_idx), 
	constraint fk_film_actor_a_idx foreign key (a_idx) references t_admin(a_idx), 
    constraint pk_film_actor primary key (fi_idx, ai_idx)
);

-- 영화 정보 등록 프로시저
drop procedure if exists sp_film_insert;
delimiter $$
create procedure sp_film_insert(finamekr varchar(50), finameen varchar(50), fgidx int, 
	ficountry varchar(50), fimaker varchar(20), fisupplier varchar(20), figrade varchar(10), 
	firtime int, fisdate datetime, fiedate datetime, fisinop varchar(500), 
	fiposter varchar(50), fidesc varchar(50), finotice varchar(50), fiisview char(1), aidx int)
begin
	insert into t_film_info (fi_namekr, fi_nameen, fg_idx, fi_country, fi_maker, fi_supplier, 
		fi_grade, fi_rtime, fi_sdate, fi_edate, fi_sinop, fi_poster, fi_desc, fi_notice, 
		fi_isview, fi_date, a_idx) 
	values (finamekr, finameen, fgidx, ficountry, fimaker, fisupplier, figrade, firtime, 
		fisdate, fiedate, fisinop, fiposter, fidesc, finotice, fiisview, fidate, aidx);
end $$
delimiter ;

-- 영화 감독 정보 등록 프로시저
drop procedure if exists sp_film_director_insert;
delimiter $$
create procedure sp_film_director_insert(fiidx int, diidx int, aidx int)
begin
	insert into t_film_director (fi_idx, di_idx, a_idx) values (fiidx, diidx, aidx);
end $$
delimiter ;

-- 영화 배우 정보 등록 프로시저
drop procedure if exists sp_film_actor_insert;
delimiter $$
create procedure sp_film_actor_insert(fiidx int, aiidx int, aidx int)
begin
	insert into t_film_actor (fi_idx, ai_idx, a_idx) values (fiidx, aiidx, aidx);
end $$
delimiter ;











