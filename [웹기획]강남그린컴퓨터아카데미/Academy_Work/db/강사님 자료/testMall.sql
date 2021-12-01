-- 주류 쇼핑몰 DB 모델링 create table 쿼리 모음

use testMall;

-- 관리자 관련 테이블 생성 및 기본 데이터 입력
-- 관리자 정보 테이블
create table t_admin_info (
	ai_idx int unique auto_increment, 	-- 관리자 번호
	ai_id varchar(20) primary key, 		-- 관리자 아이디
	ai_pwd varchar(20) not null, 		-- 비밀번호
	ai_name varchar(20) not null, 		-- 이름
	ai_isrun char(1) default 'y', 		-- 사용여부(y:사용,n:사용불가)
	ai_regdate datetime default now()	-- 등록일자
);
insert into t_admin_info (ai_id, ai_pwd, ai_name) values ('sa', '1111', '관리자');
select * from t_admin_info;

-- 관리자 페이지의 메뉴 정보 테이블
create table t_admin_menu (
	am_idx int primary key, 				-- 메뉴번호(직접생성)
	am_name varchar(20) not null, 			-- 메뉴명
	am_link varchar(100) default 'unlink', 	-- 메뉴URL로 unlink는 상위메뉴
	am_parent int default 0, 				-- 상위메뉴번호로 0이면 상위
	am_seq int default 0, 					-- 메뉴 출력 순서
	am_regdate datetime default now(), 		-- 등록일자
	ai_idx int, 							-- 등록 관리자 번호
	constraint fk_admin_menu_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);

-- 관리자 권한 부여 테이블
create table t_admin_permission (
	ap_idx int unique auto_increment, 	-- 일련번호
	ap_ai_idx int, 						-- 권한을 받을 관리자 번호
	am_idx int, 						-- 메뉴번호
	ap_pms int default 0, 				-- 권한값(0:NONE, 1:VIEW, 2:INSERT, 3:UPDATE, 4:DELETE)
	ap_regdate datetime default now(), 	-- 등록일자
	ai_idx int, 						-- 등록 관리자 번호
	constraint fk_admin_permission_ap_ai_idx foreign key (ap_ai_idx) references t_admin_info(ai_idx), 
	constraint fk_admin_permission_am_idx foreign key (am_idx) references t_admin_menu(am_idx), 
	constraint fk_admin_permission_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx), 
    constraint pk_admin_permission primary key (ap_ai_idx, am_idx)
);

-- 관리자 로그인 로그 테이블
create table t_admin_login_log (
	al_idx int unique auto_increment, 	-- 일련번호
	ai_idx int, 						-- 관리자 번호
	al_ip varchar(15) not null, 		-- 로그인 IP주소
	al_date datetime default now(), 	-- 로그인 일자
	constraint fk_admin_login_log_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);


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

-- 주소록 테이블(모든 회원은 최소 1개 부터 무제한으로 주소를 등록시킬 수 있음)
create table t_member_addr (
	ma_idx int auto_increment primary key, 	-- 일련번호
	mi_id varchar(20), 						-- 회원아이디
	ma_zip char(5) not null, 				-- 우편번호
	ma_addr1 varchar(50) not null, 			-- 주소1
	ma_addr2 varchar(50) not null, 			-- 주소2
	ma_basic char(1) default 'y', 			-- 기본주소여부
	ma_date datetime default now(), 		-- 등록일
	constraint fk_member_addr_mi_id foreign key (mi_id) references t_member_info(mi_id)
);
insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2) values ('test1', '12345', '서울', '123-45');
insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2) values ('test2', '22222', '부산', '222-45');
insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2) values ('test3', '33333', '대전', '333-45');
select * from t_member_addr;

-- 포인트 사용내역(포인트 사용 및 적립 내역)
create table t_member_point (
	mp_idx int auto_increment primary key, 	-- 일련번호
	mi_id varchar(20), 						-- 회원아이디
	mp_kind char(1) default 's', -- 사용/적립 여부(s:적립(주문),r:적립(후기),a:적립(관리자),u:사용)
	mp_point int default 0, 				-- 사용/적립 포인트
	mp_linknum varchar(10) default '', 		-- 주문 or 후기 번호
	mp_content varchar(50) not null, 		-- 사용/적립 내역
	mp_date datetime default now(), 		-- 사용/적립일
	constraint fk_member_point_mi_id foreign key (mi_id) references t_member_info(mi_id)
);
insert into t_member_point (mi_id, mp_point, mp_content) values ('test1', 1000, '가입축하금');
insert into t_member_point (mi_id, mp_point, mp_content) values ('test2', 1000, '가입축하금');
insert into t_member_point (mi_id, mp_point, mp_content) values ('test3', 1000, '가입축하금');

-- 상품 관련 테이블 생성 및 기본 데이터 입력
-- 대분류 테이블 : 발효주, 증류주
create table t_cata_big (
	cb_id char(2) primary key, 		-- 대분류ID
	cb_name varchar(20) not null 	-- 대분류명
);
insert into t_cata_big values ('B1', '발효주');
insert into t_cata_big values ('B2', '증류주');
select * from t_cata_big;

-- 소분류 테이블 : 발효주 - 막걸리, 포도주, 맥주 / 증류주 - 소주, 고량주, 보드카
create table t_cata_small (
	cs_id char(5) primary key, 		-- 소분류ID
	cb_id char(2), 					-- 대분류ID
	cs_name varchar(20) not null,  	-- 소분류명
	constraint fk_cata_small_cb_id foreign key (cb_id) references t_cata_big(cb_id)
);
insert into t_cata_small values ('B1S01', 'B1', '막걸리');
insert into t_cata_small values ('B1S02', 'B1', '포도주');
insert into t_cata_small values ('B1S03', 'B1', '맥주');
insert into t_cata_small values ('B2S01', 'B2', '소주');
insert into t_cata_small values ('B2S02', 'B2', '죽엽청');
insert into t_cata_small values ('B2S03', 'B2', '보드카');
select * from t_cata_small;

-- 브랜드 테이블 : 진로, 장수, 롯데, 한라산, 지평, OB
create table t_brand (
	b_id char(4) primary key, 		-- 브랜드ID
	b_name varchar(20) not null, 	-- 브랜드명
	b_company varchar(20) not null	-- 회사명
);
insert into t_brand values ('BR01', '하이트진로', '진로');
insert into t_brand values ('BR02', '장수', '서울탁주');
insert into t_brand values ('BR03', '처음처럼', '롯데');
insert into t_brand values ('BR04', '한라산', '한라산');
insert into t_brand values ('BR05', '지평', '지평주조');
insert into t_brand values ('BR06', 'OB', '두산');

-- 상품 정보 테이블
create table t_product_info (
	pi_id char(6) primary key, 		-- 상품ID
	pi_name varchar(20) not null, 	-- 상품명
	cs_id char(5), 					-- 소분류ID
	b_id char(4), 					-- 브랜드ID
	pi_origin varchar(20) not null, -- 원산지
	pi_cost int default 0, 			-- 원가
	pi_price int default 0, 		-- 판매가
	pi_discount int default 0, 		-- 할인율
	pi_option varchar(100), 		-- 옵션(도수)
	pi_img1 varchar(50) not null, 	-- 이미지1
	pi_img2 varchar(50), 			-- 이미지2
	pi_img3 varchar(50), 			-- 이미지3
	pi_desc varchar(50) not null, 	-- 설명이미지
	pi_stock int default -1, 		-- 재고량(-1:무제한)
	pi_salecnt int default 0, 		-- 판매개수
	pi_review int default 0, 		-- 후기개수
	pi_star float default 0.0,  	-- 상품별점평균
	pi_isview char(1) default 'n', 	-- 게시여부
	pi_date datetime default now(), -- 등록일
	ai_idx int, 					-- 관리자 번호
	constraint fk_product_info_cs_id foreign key (cs_id) references t_cata_small(cs_id), 
	constraint fk_product_info_b_id foreign key (b_id) references t_brand(b_id), 
	constraint fk_product_info_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, 
	pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt001', '지평막걸리', 'B1S01', 'BR05', '대한민국', 2000, 3000, 0, '14도;18도', 'pdt001_1.jpg', 
'pdt001_d.jpg', 500, 'y', 1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt002', '술01', 'B1S01', 'BR01', '대한민국', 1500,2500,10,'11도;15도', 'pdt002_1.jpg', 'pdt002_d.jpg', 550,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt003', '술02', 'B1S01', 'BR02', '대한민국', 2000,4000,10,'15도;18도', 'pdt003_1.jpg', 'pdt003_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt004', '술03', 'B1S01', 'BR03', '대한민국', 2500,4500,10,'11도;15도', 'pdt004_1.jpg', 'pdt004_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt005', '술04', 'B1S02', 'BR04', '대한민국', 3000,5000,10,'15도;18도', 'pdt005_1.jpg', 'pdt005_d.jpg', 600,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt006', '술05', 'B1S02', 'BR05', '대한민국', 3500,5500,10,'11도;15도', 'pdt006_1.jpg', 'pdt006_d.jpg', 300,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt007', '술06', 'B1S02', 'BR06', '대한민국', 1200,3200,10,'15도;18도', 'pdt007_1.jpg', 'pdt007_d.jpg', 600,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt008', '술07', 'B1S02', 'BR01', '대한민국', 1800,3800,10,'12도;19도;21도', 'pdt008_1.jpg', 'pdt008_d.jpg', 300,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt009', '술08', 'B1S03', 'BR02', '대한민국', 2400,4400,10,'12도;19도;21도', 'pdt009_1.jpg', 'pdt009_d.jpg', 600,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt010', '술09', 'B1S03', 'BR03', '대한민국', 3000,5000,10,'12도;19도;21도', 'pdt010_1.jpg', 'pdt010_d.jpg', 300,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt011', '술10', 'B1S03', 'BR04', '대한민국', 3600,5600,10,'12도;19도;21도', 'pdt011_1.jpg', 'pdt011_d.jpg', 600,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt012', '술11', 'B2S01', 'BR05', '대한민국', 5500,7500,0,'12도;19도;21도', 'pdt012_1.jpg', 'pdt012_d.jpg', 300,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt013', '술12', 'B2S01', 'BR06', '대한민국', 8000,10000,0,'33도;39도;41도', 'pdt013_1.jpg', 'pdt013_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt014', '술13', 'B2S01', 'BR01', '대한민국', 10500,12500,0,'33도;39도;41도', 'pdt014_1.jpg', 'pdt014_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt015', '술14', 'B2S02', 'BR02', '대한민국', 13000,15000,0,'33도;39도;41도', 'pdt015_1.jpg', 'pdt015_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt016', '술15', 'B2S02', 'BR03', '대한민국', 15500,17500,0,'33도;39도;41도', 'pdt016_1.jpg', 'pdt016_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt017', '술16', 'B2S02', 'BR04', '대한민국', 18000,20000,0,'33도;39도;41도', 'pdt017_1.jpg', 'pdt017_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt018', '술17', 'B2S03', 'BR05', '대한민국', 20500,22500,0,'45도;50도;60도', 'pdt018_1.jpg', 'pdt018_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt019', '술18', 'B2S03', 'BR06', '대한민국', 23000,25000,0,'45도;50도;60도', 'pdt019_1.jpg', 'pdt019_d.jpg', -1,'y',1);
insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, pi_price, pi_discount, pi_option, pi_img1, pi_desc, pi_stock, pi_isview, ai_idx) values 
('pdt020', '술19', 'B2S03', 'BR01', '대한민국', 25500,27500,0,'45도;50도;60도', 'pdt020_1.jpg', 'pdt020_d.jpg', -1,'y',1);
select * from t_product_info;


-- 주문 관련 테이블 생성 및 기본 데이터 입력
-- 장바구니 테이블
create table t_order_cart (
	oc_idx int auto_increment primary key, 	-- 일련번호
	mi_id varchar(20), 						-- 회원ID
	pi_id char(6), 							-- 상품ID
	oc_option varchar(10), 					-- 옵션(선택한 옵션)
	oc_cnt int default 1, 					-- 개수
	oc_date datetime default now(), 		-- 일자
	constraint fk_order_cart_mi_id foreign key (mi_id) references t_member_info(mi_id), 
	constraint fk_order_cart_pi_id foreign key (pi_id) references t_product_info(pi_id)
);

-- 주문 정보 테이블
create table t_order_info (
	oi_id char(10) primary key, 	-- 주문번호
	mi_id varchar(20), 				-- 회원ID
	oi_name varchar(20) not null, 	-- 수취인명
	oi_phone varchar(13) not null, 	-- 수취인 전화번호
	oi_zip char(5) not null, 		-- 배송지 우편번호
	oi_addr1 varchar(50) not null, 	-- 배송지 주소1
	oi_addr2 varchar(50) not null, 	-- 배송지 주소2
	oi_payment char(1) default 'a', -- 결제방법
	oi_pay int default 0, 			-- 결제액
	oi_usepnt int default 0, 		-- 사용포인트
	oi_delipay int default 0, 		-- 배송비
	oi_status char(1) default 'b', 	-- 주문상태
	oi_cmt varchar(100), 			-- 요청사항
	oi_invoice varchar(20), 		-- 송장번호
	oi_date datetime default now(), -- 주문일
	constraint fk_order_info_mi_id foreign key (mi_id) references t_member_info(mi_id) 
);

-- 주문 상세 테이블
create table t_order_detail (
	od_idx int auto_increment primary key, 	-- 일련번호
	oi_id char(10), 						-- 주문번호
	pi_id char(6), 							-- 상품ID
	od_pdtname varchar(20) not null, 		-- 상품명
	od_pdtimg varchar(50) not null, 		-- 상품이미지
	od_pdtprice int default 0, 				-- 단가
	od_cnt int default 1, 					-- 개수
	od_option varchar(10), 					-- 옵션(선택한 옵션)
	od_savepnt int default 0, 				-- 적립포인트
	constraint fk_order_detail_oi_id foreign key (oi_id) references t_order_info(oi_id), 
	constraint fk_order_detail_pi_id foreign key (pi_id) references t_product_info(pi_id)
);


-- 고객센터(게시판) 관련 테이블 생성 및 기본 데이터 입력
-- 공지사항 테이블
create table t_brd_notice (
	bn_idx int auto_increment primary key, 	-- 일련번호
	bn_title varchar(100) not null, 		-- 제목
	bn_content text not null, 				-- 내용
	bn_read int default 0, 					-- 조회수
	bn_isnotice char(1) default 'n', 		-- 공지여부
	bn_date datetime default now(), 		-- 작성일
	ai_idx int, 							-- 관리자 번호
	constraint fk_brd_notice_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);

-- FAQ 테이블
create table t_brd_faq (
	bf_idx int auto_increment primary key, 	-- 일련번호
	bf_cata varchar(20) not null, 			-- 분류(회원관련, 상품관련, 주문/배송관련, 결제관련, 후기관련, 기타)
	bf_title varchar(100) not null, 		-- 질문
	bf_content text not null, 				-- 답변
	bf_date datetime default now(), 		-- 작성일
	ai_idx int, 							-- 관리자 번호
	constraint fk_brd_faq_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);

-- QnA 테이블
create table t_brd_qna (
	bq_idx int auto_increment primary key, 	-- 일련번호
	mi_id varchar(20), 						-- 회원ID
	bq_cata varchar(20) not null, 			-- 질문분류(회원관련, 상품관련, 주문/배송관련, 결제관련, 후기관련, 기타)
	bq_title varchar(100) not null, 		-- 질문제목
	bq_content text not null, 				-- 질문내용
	bq_img varchar(50), 					-- 이미지
	bq_qdate datetime default now(), 		-- 질문등록일
	bq_answer text, 						-- 답변내용
	ai_idx int, 							-- 답변관리자
	bq_adate datetime, 						-- 답변등록일
	bq_status char(1) default 'a', 			-- 상태(a:답변전, b:답변완료)
	constraint fk_brd_qna_mi_id foreign key (mi_id) references t_member_info(mi_id), 
	constraint fk_brd_qna_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);

-- 상품후기 테이블
create table t_brd_review (
	br_idx int auto_increment unique, 	-- 일련번호
	mi_id varchar(20), 					-- 회원ID
	oi_id char(10), 					-- 주문번호
	pi_id char(6), 						-- 상품ID
	br_title varchar(100) not null, 	-- 제목
	br_content text not null, 			-- 내용
	br_cnt int default 0, 				-- 댓글수
	br_img1 varchar(50), 				-- 이미지1
	br_img2 varchar(50), 				-- 이미지2
	br_star float default 0.5, 			-- 상품별점
	br_score float default 0, 			-- 별점 평균
	br_isview char(1) default 'y', 		-- 게시여부
	br_date datetime default now(), 	-- 작성일
	constraint fk_brd_review_mi_id foreign key (mi_id) references t_member_info(mi_id), 
	constraint fk_brd_review_oi_id foreign key (oi_id) references t_order_info(oi_id), 
	constraint fk_brd_review_pi_id foreign key (pi_id) references t_product_info(pi_id), 
    constraint pk_brd_review primary key (mi_id, oi_id, pi_id)
);

-- 상품후기 신고 테이블
create table t_brd_review_911 (
	br9_idx int auto_increment primary key, -- 일련번호
	br_idx int, 							-- 후기 글번호
	mi_id varchar(20), 						-- 회원ID
	br9_content varchar(50) not null, 		-- 신고 내용(광고성 후기, 욕설 및 비속어 포함, 선정적 후기, 관련없는 후기, 기타 - 직접 입력받음)
	br9_date datetime default now(), 		-- 신고일
	br9_status char(1) default 'a', 		-- 신고상태(a : 접수중, b : 접수 및 확인작업, c : 처리완료)
	br9_opdate datetime, 					-- 처리일
	ai_idx int, 							-- 관리자 번호
	constraint fk_brd_review_911_br_idx foreign key (br_idx) references t_brd_review(br_idx), 
	constraint fk_brd_review_911_mi_id foreign key (mi_id) references t_member_info(mi_id), 
	constraint fk_brd_review_911_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);

-- 상품후기 평점주기 테이블
create table t_brd_review_star (
	brs_idx int auto_increment unique, 	-- 일련번호
	mi_id varchar(20), 					-- 회원ID
	br_idx int, 						-- 후기 글번호
	brs_score float default 0.5, 		-- 평점
	brs_date datetime default now(), 	-- 작성일
	constraint fk_brd_review_star_br_idx foreign key (br_idx) references t_brd_review(br_idx), 
	constraint fk_brd_review_star_mi_id foreign key (mi_id) references t_member_info(mi_id), 
    constraint pk_brd_review_star primary key (mi_id, br_idx)
);

-- 상품후기 댓글 테이블
create table t_brd_review_reply(
	brr_idx int auto_increment primary key, -- 일련번호
	br_idx int, 							-- 후기 글번호
	mi_id varchar(20), 						-- 회원ID
	brr_content text not null, 				-- 내용
	brr_recc int default 0, 				-- 추천수 평균
	brr_isview char(1) default 'y', 		-- 게시여부
	brr_date datetime default now(), 		-- 작성일
	constraint fk_brd_review_reply_br_idx foreign key (br_idx) references t_brd_review(br_idx), 
	constraint fk_brd_review_reply_mi_id foreign key (mi_id) references t_member_info(mi_id)
);

-- 상품후기 댓글 추천 테이블
create table t_brd_review_reply_recc (
	brrr_idx int auto_increment unique, -- 일련번호
	brr_idx int, 						-- 댓글번호
	mi_id varchar(20), 					-- 회원ID
	brrr_date datetime default now(), 	-- 작성일
	constraint fk_brd_review_reply_recc_brr_idx foreign key (brr_idx) references t_brd_review_reply(brr_idx), 
	constraint fk_brd_review_reply_recc_mi_id foreign key (mi_id) references t_member_info(mi_id), 
    constraint pk_brd_review_reply_recc primary key (mi_id, brr_idx)
);


-- 기타 테이블 생성 및 기본 데이터 입력
-- 설문조사 질문&보기 테이블
create table t_poll_question (
	pq_idx int auto_increment primary key, 	-- 일련번호
	pq_question varchar(200) not null, 		-- 설문내용
	pq_ex1 varchar(100) not null, 			-- 설문보기1
	pq_ex2 varchar(100) not null, 			-- 설문보기2
	pq_ex3 varchar(100), 					-- 설문보기3
	pq_ex4 varchar(100), 					-- 설문보기4
	pq_ex5 varchar(100), 					-- 설문보기5
	pq_sdate varchar(10), 					-- 설문시작일(yyyy-mm-dd)
	pq_edate varchar(10), 					-- 설문종료일(yyyy-mm-dd)
	pq_endmsg varchar(100), 				-- 설문종료메시지
	pq_status char(1) default 'a', 			-- 설문상태(a : 대기중, b : 진행중, c : 종료)
	pq_date datetime default now(), 		-- 등록일
	ai_idx int, 							-- 관리자 번호
	constraint fk_poll_question_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);

-- 설문조사 결과 테이블
create table t_poll_result (
	pr_idx int auto_increment unique, 	-- 일련번호
	pq_idx int, 						-- 설문조사번호
	mi_id varchar(20), 					-- 회원ID
	pr_ex int, 							-- 선택보기번호
	pr_date datetime default now(), 	-- 설문참여일
	constraint fk_poll_result_pq_idx foreign key (pq_idx) references t_poll_question(pq_idx), 
	constraint fk_poll_result_mi_id foreign key (mi_id) references t_member_info(mi_id), 
    constraint pk_poll_result primary key (pq_idx, mi_id)
);

-- 메인화면 배너 이미지 관리 테이블
create table t_main_banner (
	mb_idx int auto_increment primary key, 	-- 일련번호
	mb_img varchar(50) not null, 			-- 이미지파일명
	mb_url varchar(100), 					-- 이동URL
	mb_seq int default 0, 					-- 순서
	mb_isview char(1) default 'n', 			-- 게시여부
	mb_date datetime default now(), 		-- 등록일
	ai_idx int, 							-- 관리자 번호
	constraint fk_main_banner_ai_idx foreign key (ai_idx) references t_admin_info(ai_idx)
);














