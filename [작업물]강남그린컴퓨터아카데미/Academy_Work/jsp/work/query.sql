use testmall;

-- 자유게시판 테이블: t_brd_free            
create table t_brd_free(            
	bf_idx   	int   primary key,  	-- 글번호
	bf_ismem   	char(1) default 'n',   	-- 회원여부
	bf_pwd   	varchar(20),   			-- 비밀번호
	bf_writer   varchar(20) not null,   -- 작성자
	bf_title   	varchar(100) not null,  -- 제목
	bf_content  text not null,   		-- 내용
	bf_reply   	int default 0,   		-- 댓글 수 
	bf_read   	int default 0,   		-- 조회수
	bf_date   	datetime default now()  -- 작성일
);    
select * from t_brd_free;
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (1, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (2, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (3, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (4, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (5, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (6, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (7, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (8, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (9, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (10, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (11, 'y', '1234', 'test2', '담이', '언니조아' '제일조앙!!!');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (12, 'y', '1234', 'tholakim', '문의사항 있습니다.', '왜 삼성전자가 안오르는거죠? 분명 9만원 간다면서요? 미쳤나요 ?');
insert into t_brd_free(bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, bf_content) values (13, 'y', '1234', 'tholakim', '나라망했나요', '쎄트렉아이 한국항공우주 상처도 내 평단안옴!');


create table t_brd_free_reply (
   bfr_idx int auto_increment primary key,          -- 댓글번호
   bf_idx int not null,                         -- 게시글번호
   mi_id varchar(20) not null,                     -- 회원ID
   bfr_content text not null,                      -- 댓글내용
   bfr_date datetime default now(),                -- 작성일
   constraint fk_brd_free_reply_bf_idx foreign key (bf_idx) references t_brd_free(bf_idx), 
   constraint fk_brd_free_reply_mi_id foreign key (mi_id) references t_member_info(mi_id)
);
select * from t_brd_free_reply ;
insert into t_brd_free_reply (bf_idx, mi_id, bfr_content) values (1,'test1', 'fsdfcsdrrewf');
update t_brd_free set bf_reply = 3 where bf_idx = 15;

-- 일정관리 테이블
create table t_schedule_info (
	si_idx int auto_increment primary key, 	-- 일정번호
	mi_id varchar(20) not null, 		-- 회원ID
	si_stime char(16) not null, 		-- 시행일시(yyyy-mm-dd hh:mm)
	si_content varchar(1000) not null, 	-- 내용
	si_date datetime default now(), 	-- 일정등록일
	constraint fk_si_mi_id foreign key (mi_id) references t_member_info(mi_id) 
);

insert into t_schedule_info (mi_id, si_stime, si_content) values ('', '', '');
select * from t_schedule_info where mi_id = 'test2' and si_stime like '2021-05-27%' order by si_stime ; 
select * from t_schedule_info where si_idx = 3 and mi_id = '';
update t_schedule_info set si_stime = '', si_content = '' where si_idx = ?;

select a.*, b.ma_idx, b.ma_zip, b.ma_addr1, b.ma_addr2
from t_member_info a, t_member_addr b
where a.mi_id = b.mi_id and a.mi_id = 'test1' and a.mi_pwd = '1111'  ;  

use testmall;
select count(*) from t_brd_notice;

drop table t_brd_notice;
create table t_brd_notice(				
bn_idx int auto_increment primary key,		-- 일련번호
bn_title varchar(100) not null,				-- 제목
bn_content text	not null,					-- 내용
bn_read	int	 default 0,						-- 조회수
bn_isnotice	char(1) default 'n',			-- 공지여부
bn_date	datetime default now(),				-- 작성일
ai_idx	int,								-- 관리자번호
constraint fk_t_brd_notice_ai_idx foreign key(ai_idx) references t_admin_info(ai_idx)				
);

use testmall;
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);
insert into t_brd_notice(bn_title, bn_content, ai_idx) values("타이틀", "내용", 1);

select * from t_brd_notice where bn_idx =  15;

insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('1번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('2번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('3번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('4번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('5번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('6번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('7번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('8번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('9번공지','내용111','n',1);
insert into t_brd_notice(bn_title, bn_content, bn_isnotice,ai_idx) values('10번공지','내용111','n',1);


select a.*, b.cb_id, b.cb_name, c.cs_name, d.b_name
from t_product_info a, t_cata_big b, t_cata_small c, t_brand d
where a.cs_id = c.cs_id and b.cb_id = c.cb_id and a.b_id = d.b_id;

select a.*, b.cb_id, b.cb_name, c.cs_name, d.b_name  
from t_product_Info a, t_cata_big b, t_cata_small c, t_brand d  
where a.cs_id = c.cs_id and b.cb_id = c.cb_id and a.b_id = d.b_id 
and a.pi_isview = 'y'  
order by a.pi_id desc limit 0, 12;

select * from t_product_info; 
select * from t_cata_big; 
select * from t_cata_small; 

select a.*, b.cb_id, b.cb_name, c.cs_name, d.b_name  
from t_product_Info a, t_cata_big b, t_cata_small c, t_brand d  
where a.cs_id = c.cs_id and b.cb_id = c.cb_id and a.b_id = d.b_id 
and a.pi_isview = 'y'  and a.pi_price >= '4000' order by a.pi_id desc limit 0, 12;


select * from t_admin_info;
-- t_admin_info 테이블에 ai_pms 컬럼 추가해요
-- 자료형은 char(1) , 기본값은 'a'
-- ALTER TABLE `testmall`.`t_admin_info` ADD COLUMN `ai_pms` CHAR(1) NULL DEFAULT 'a' AFTER `ai_regdate`;

-- admin & user 같이 쓸 수 있는 상품리스트 쿼리
create view v_pdt_list as 
	select a.*, b.cb_id, b.cb_name, c.cs_name, d.b_name
	from t_product_info a, t_cata_big b, t_cata_small c, t_brand d
	where a.cs_id = c.cs_id and b.cb_id = c.cb_id and a.b_id = d.b_id;

select * from v_pdt_list;
select * from v_pdt_list  where 1=1  order by pi_name asc limit 0, 15;

select * from t_cata_small; 

select * from t_product_Info; 

select a.*, b.cb_id, b.cb_name, c.cs_name, d.b_name  
from t_product_Info a, t_cata_big b, t_cata_small c, t_brand d  where a.cs_id = c.cs_id and b.cb_id = c.cb_id and a.b_id = d.b_id and a.pi_isview = 'y' order by a.pi_id desc limit 0, 15;
 
select max(right(pi_id, 3)) from t_product_info; 

 
select * from t_member_info where mi_id = '' ;

select * from t_order_cart; 

select a.oc_idx, a.oc_cnt, b.pi_stock
from t_order_cart a, t_product_info b
where a.pi_id = b.pi_id and a.mi_id = '' and a.pi_id = '' and oc_option = ''; 

update t_order_cart set oc_cnt = oc_cnt + ? where oc_idx = ? and (oc_cnt + cnt) < stock;


use testmall;
