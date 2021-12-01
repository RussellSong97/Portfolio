-- 주류 쇼핑몰 DB 모델링 create procedure 쿼리 모음

use testMall;

-- 관리자 관련 프로시저들
-- 관리자 등록 프로시저
drop procedure if exists sp_ad_admin_insert;
delimiter $$
create procedure sp_ad_admin_insert(
	aiid varchar(20), aipwd varchar(20), ainame varchar(20), aiisrun varchar(20))
begin
	insert into t_admin_info (ai_id, ai_pwd, ai_name, ai_isrun) 
    values (aiid, aipwd, ainame, aiisrun);
end $$
delimiter ;
call sp_ad_admin_insert('admin2', '1111', '임꺽정', 'y');

-- 관리자 수정 프로시저
drop procedure if exists sp_ad_admin_update;
delimiter $$
create procedure sp_ad_admin_update(
	aipwd varchar(20), aiisrun varchar(20), aiid varchar(20))
begin
	update t_admin_info set ai_pwd = aipwd, ai_isrun = aiisrun where ai_id = aiid;
    -- id와 이름은 변경불가로 하고 변경하려는 관리자의 id를 받아와 조건을 사용
end $$
delimiter ;
call sp_ad_admin_update('1111', 'y', 'sa');
select * from t_admin_info;

-- 관리자 로그인 로그 추가 프로시저
drop procedure if exists sp_ad_log_insert;
delimiter $$
create procedure sp_ad_log_insert(aiidx int, alip varchar(15))
begin
	insert into t_admin_login_log (ai_idx, al_ip) values (aiidx, alip);
end $$
delimiter ;
call sp_ad_log_insert(1, '127.0.0.1');
select * from t_admin_login_log;

-- 관리 메뉴 추가 프로시저
drop procedure if exists sp_ad_menu_insert;
delimiter $$
create procedure sp_ad_menu_insert(amidx int, amname varchar(20), 
	amlink varchar(100), amparent int, amseq int, aiidx int)
begin
	insert into t_admin_menu (am_idx, am_name, am_link, am_parent, am_seq, ai_idx) 
    values (amidx, amname, amlink, amparent, amseq, aiidx);
end $$
delimiter ;

-- 관리 메뉴 수정 프로시저
drop procedure if exists sp_ad_menu_update;
delimiter $$
create procedure sp_ad_menu_update(amidx int, amname varchar(20), 
	amlink varchar(100), amparent int, amseq int)
begin
	update t_admin_menu set am_name = amname, am_link = amlink, 
		am_parent = amparent, am_seq = amseq where am_idx = amidx;
end $$
delimiter ;

-- 관리자 권한 등록 프로시저
drop procedure if exists sp_ad_pms_insert;
delimiter $$
create procedure sp_ad_pms_insert(apaiidx int, amidx int, appms int, aiidx int)
begin
	insert into t_admin_permission (ap_ai_idx, am_idx, ap_pms, ai_idx) 
    values (apaiidx, amidx, appms, aiidx);
    -- 한 관리자가 특정 메뉴에 대해 권한을 부여하는 쿼리
end $$
delimiter ;

-- 관리자 권한 수정 프로시저
drop procedure if exists sp_ad_pms_update;
delimiter $$
create procedure sp_ad_pms_update(apidx int, appms int)
begin
	update t_admin_permission set ap_pms = appms where ap_idx = apidx;
end $$
delimiter ;


-- 회원 관련 프로시저들
-- 회원 가입 프로시저
drop procedure if exists sp_member_insert;
delimiter $$
create procedure sp_member_insert(miid varchar(20), mipwd varchar(20), 
	miname varchar(20), mibirth char(10), migender char(1), miphone varchar(13), 
	miemail varchar(50), miissns char(1), miismail char(1), mipoint int, 
    mirebank varchar(50), miaccount varchar(50), mirecommend varchar(20), 
	mazip char(5), maaddr1 varchar(50), maaddr2 varchar(50), 
    mppoint int, mpcontent varchar(50))
begin
	insert into t_member_info (mi_id, mi_pwd, mi_name, mi_birth, mi_gender, mi_phone, 
		mi_email, mi_issns, mi_ismail, mi_point, mi_rebank, mi_account, mi_recommend)
	values (miid, mipwd, miname, mibirth, migender, miphone, miemail, miissns, 
		miismail, mipoint, mirebank, miaccount, mirecommend);
	-- 회원 정보 등록 쿼리

	insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2) 
    values (miid, mazip, maaddr1, maaddr2);	-- 회원 주소 정보 등록 쿼리

	insert into t_member_point (mi_id, mp_point, mp_content) 
    values (miid, mppoint, mpcontent);		-- 회원 가입축하금 수령 쿼리
end $$
delimiter ;
call sp_member_insert('test4', '1111', '둘리', '1995-08-09', 'm', '010-1234-4444', 
'dooly@naver.com', 'y', 'n', 1000, '국민은행', '123-4567-890', 'test1', '11222', 
'경상북도 문경시', '456-456', 1000, '가입축하금');
select * from t_member_info;
select * from t_member_addr;
select * from t_member_point;

-- 회원 정보 수정 프로시저
drop procedure if exists sp_member_update;
delimiter $$
create procedure sp_member_update(miid varchar(20), miphone varchar(13), 
	miemail varchar(50), miissns char(1), miismail char(1), mirebank varchar(50), 
	miaccount varchar(50))
begin
	update t_member_info set mi_phone = miphone, mi_email = miemail, mi_issns = miissns, 
		mi_ismail = miismail, mi_rebank = mirebank, mi_account = miaccount 
	where mi_id = miid;
end $$
delimiter ;
call sp_member_update('test3', '010-1234-3333', 'lim@naver.com', 'y', 'n', 
	'SC은행', '32165-12-123');

-- 회원 암호 수정 프로시저
drop procedure if exists sp_member_password;
delimiter $$
create procedure sp_member_password(miid varchar(20), mipwd varchar(20))
begin
	update t_member_info set mi_pwd = mipwd where mi_id = miid;
end $$
delimiter ;
call sp_member_password('test2', '1234');

-- 회원 탈퇴/복귀 프로시저
drop procedure if exists sp_member_out;
delimiter $$
create procedure sp_member_out(miid varchar(20), miisactive char(1))
begin
	update t_member_info set mi_isactive = miisactive where mi_id = miid;
end $$
delimiter ;

-- 회원 포인트 관리자 직접 입력 프로시저
drop procedure if exists sp_ad_point_insert;
delimiter $$
create procedure sp_ad_point_insert(miid varchar(20), mppoint int, 
	bridx int, mpcontent varchar(50), aiidx int)
begin
	insert into t_member_point (mi_id, mp_kind, mp_point, br_idx, mp_content, ai_idx) 
    values (miid, 'a', mppoint, bridx, mpcontent, aiidx);
    -- 포인트 내역 테이블에 관리자 직접 적립 쿼리 실행

	update t_member_info set mi_point = mi_point + mppoint where mi_id = miid;
    -- 회원 정보 테이블에 보유 포인트 변경
end $$
delimiter ;
call sp_ad_point_insert('test4', 1500, 0, '관리자갑질', 1);

-- 회원 주소 등록 프로시저
drop procedure if exists sp_mem_addr_insert;
delimiter $$
create procedure sp_mem_addr_insert(miid varchar(20), mazip char(5), 
	maaddr1 varchar(50), maaddr2 varchar(50), mabasic char(1))
begin
	if mabasic = 'y' then	-- 추가하려는 주소를 기본주소로 할 경우
		update t_member_addr set ma_basic = 'n' where mi_id = miid and ma_basic = 'y';
        -- 기존의 기본주소를 일반주소로 변경한 후 추가해야 함
    end if;

	insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2, ma_basic) 
    values (miid, mazip, maaddr1, maaddr2, mabasic);
end $$
delimiter ;
call sp_mem_addr_insert('test3', '33333', '울산시 중구', '333-33', 'y');
call sp_mem_addr_insert('test2', '22211', '경상북도 포항시', '오천읍 세계리', 'n');

-- 회원 주소 수정 프로시저
drop procedure if exists sp_mem_addr_update;
delimiter $$
create procedure sp_mem_addr_update(maidx int, miid varchar(20), mazip char(5), 
	maaddr1 varchar(50), maaddr2 varchar(50), mabasic char(1))
begin
	if mabasic = 'y' then	-- 변경하려는 주소를 기본주소로 할 경우
		update t_member_addr set ma_basic = 'n' where mi_id = miid and ma_basic = 'y';
        -- 기존의 기본주소를 일반주소로 변경한 후 추가해야 함
    end if;
	update t_member_addr set ma_zip = mazip, ma_addr1 = maaddr1, ma_addr2 = maaddr2, 
		ma_basic = mabasic where ma_idx = maidx and mi_id = miid;
end $$
delimiter ;

-- 회원 주소 삭제 프로시저
drop procedure if exists sp_mem_addr_delete;
delimiter $$
create procedure sp_mem_addr_delete(maidx int, miid varchar(20))
begin
	delete from t_member_addr where ma_idx = maidx and mi_id = miid and ma_basic = 'n';
	-- 기본주소가 아닐 경우에만 삭제시킴(회원은 무조건 최소 하나 이상의 주소가 남아 있어야 함)
end $$
delimiter ;


-- 상품 관련 프로시저들
-- 상품 분류 등록 프로시저
drop procedure if exists sp_pdt_cata_insert;
delimiter $$
create procedure sp_pdt_cata_insert(kind char(1), 
	cbid char(2), cbname varchar(20), csid char(4), csname varchar(20))
begin
	if kind = 'b' then	-- 대분류 등록이면
		insert into t_cata_big values (cbid, cbname);
    else	-- 소분류 등록이면
		insert into t_cata_small values (csid, cbid, csname);
    end if;
end $$
delimiter ;

-- 상품 분류 수정 프로시저
drop procedure if exists sp_pdt_cata_update;
delimiter $$
create procedure sp_pdt_cata_update(kind char(1), 
	cbid char(2), cbname varchar(20), csid char(4), csname varchar(20))
begin
	if kind = 'b' then	-- 대분류 수정이면
		update t_cata_big set cb_name = cbname where cb_id = cbid;
    else	-- 소분류 수정이면
		update t_cata_small set cb_id = cbid, cs_name = csname where cs_id = csid;
    end if;
end $$
delimiter ;

-- 상품 브랜드 등록 프로시저
drop procedure if exists sp_pdt_brand_insert;
delimiter $$
create procedure sp_pdt_brand_insert(bid char(4), bname varchar(20), bcompany varchar(20))
begin
	insert into t_brand values (bid, bname, bcompany);
end $$
delimiter ;

-- 상품 브랜드 수정 프로시저
drop procedure if exists sp_pdt_brand_update;
delimiter $$
create procedure sp_pdt_brand_update(bid char(4), bname varchar(20), bcompany varchar(20))
begin
	update t_brand set b_name = bname, b_company = bcompany where b_id = bid;
end $$
delimiter ;

-- 상품 등록 프로시저
drop procedure if exists sp_product_insert;
delimiter $$
create procedure sp_product_insert(piid char(6), piname varchar(20), csid char(5), 
	bid char(4), piorigin varchar(20), picost int, piprice int, pidiscount int, 
	pioption varchar(100), piimg1 varchar(50), piimg2 varchar(50), piimg3 varchar(50), 
	pidesc varchar(50), pistock int, piisview char(1), aiidx int)
begin
	insert into t_product_info (pi_id, pi_name, cs_id, b_id, pi_origin, pi_cost, 
		pi_price, pi_discount, pi_option, pi_img1, pi_img2, pi_img3, pi_desc, 
		pi_stock, pi_isview, ai_idx)
	values (piid, piname, csid, bid, piorigin, picost, piprice, pidiscount, pioption, 
		piimg1, piimg2, piimg3, pidesc, pistock, piisview, aiidx);
end $$
delimiter ;
call sp_product_insert('pdt021', '술20', 'B2S03', 'BR01', '대한민국', 33000, 40000, 0, 
	'45도;50도;60도', 'pdt021_1.jpg', '', '', 'pdt021_d.jpg', 1500, 'y', 1);
select * from t_product_info;

-- 상품 수정 프로시저
drop procedure if exists sp_product_update;
delimiter $$
create procedure sp_product_update(piid char(6), piname varchar(20), csid char(5), 
	bid char(4), piorigin varchar(20), picost int, piprice int, pidiscount int, 
	pioption varchar(100), piimg1 varchar(50), piimg2 varchar(50), piimg3 varchar(50), 
	pidesc varchar(50), pistock int, piisview char(1))
begin
	update t_product_info set pi_name = piname, cs_id = csid, b_id = bid, 
		pi_origin = piorigin, pi_cost = picost, pi_price = piprice, 
		pi_discount = pidiscount, pi_option = pioption, pi_img1 = piimg1, 
		pi_img2 = piimg2, pi_img3 = piimg3, pi_desc = pidesc, pi_stock = pistock, 
		pi_isview = piisview where pi_id = piid;
end $$
delimiter ;
call sp_product_update('pdt021', '술21', 'B2S03', 'BR01', '대한민국', 33000, 40000, 0, 
	'45도;50도;60도', 'pdt021_1.jpg', 'pdt021_2.jpg', '', 'pdt021_d.jpg', 1500, 'y');


-- 고객센터 관련 프로시저
-- 공지사항 등록 프로시저
drop procedure if exists sp_ad_notice_insert;
delimiter $$
create procedure sp_ad_notice_insert(
	bntitle varchar(100), bncontent text, bnisnotice char(1), aiidx int)
begin
	declare cnt int; -- 공지글의 개수를 저장할 변수
    declare idx int; -- 가장 오래된 공지글의 글번호를 저장할 변수
    set cnt = 0;
    set idx = 0;
	if bnisnotice = 'y' then -- 현재 등록하려는 글을 공지글로 등록한다면
		select count(*) into cnt from t_brd_notice where bn_isnotice = 'y';
        -- cnt변수에 현재 테이블에 있는 공지글의 개수를 저장
        if cnt >= 3 then -- 현재 공지글의 개수가 3이상 이면(최대 3개이므로 하나를 일반글로 변경해야 함)
			select min(bn_idx) into idx from t_brd_notice where bn_isnotice = 'y';
            -- 현재 공지글들 중 가장 오래된 공지글의 번호를 idx변수에 저장

            update t_brd_notice set bn_isnotice = 'n' where bn_idx = idx;
            -- 가장 오래된 공지글을 일반글로 변경
        end if;
    end if;
	insert into t_brd_notice (bn_title, bn_content, bn_isnotice, ai_idx) 
    values (bntitle, bncontent, bnisnotice, aiidx);
end $$
delimiter ;
call sp_ad_notice_insert('테스트 공지글', '공지 공지 공지', 'y', 1);

-- 공지사항 수정 프로시저
drop procedure if exists sp_ad_notice_update;
delimiter $$
create procedure sp_ad_notice_update(bnidx int, 
	bntitle varchar(100), bncontent text, bnisnotice char(1))
begin
	declare cnt int; -- 공지글의 개수를 저장할 변수
    declare idx int; -- 가장 오래된 공지글의 글번호를 저장할 변수
    set cnt = 0;
    set idx = 0;
	if bnisnotice = 'y' then
		select count(*) into cnt from t_brd_notice 
        where bn_isnotice = 'y' and bn_idx <> bnidx;
        if cnt >= 3 then
			select min(bn_idx) into idx from t_brd_notice where bn_isnotice = 'y';
            update t_brd_notice set bn_isnotice = 'n' where bn_idx = idx;
        end if;
    end if;
	update t_brd_notice set bn_title = bntitle, bn_content = bncontent, 
		bn_isnotice = bnisnotice where bn_idx = bnidx;
end $$
delimiter ;
call sp_ad_notice_update(8, '8번째 공지사항입니다.', '내용888', 'y');
select * from t_brd_notice;

-- 공지사항 삭제 프로시저
drop procedure if exists sp_ad_notice_delete;
delimiter $$
create procedure sp_ad_notice_delete(bnidx int)
begin
	delete from t_brd_notice where bn_idx = bnidx;
end $$
delimiter ;


-- FAQ 등록 프로시저
drop procedure if exists sp_ad_faq_insert;
delimiter $$
create procedure sp_ad_faq_insert(
	bfcata varchar(20), bftitle varchar(100), bfcontent text, aiidx int)
begin
	insert into t_brd_faq (bf_cata, bf_title, bf_content, ai_idx) 
    values (bfcata, bftitle, bfcontent, aiidx);
end $$
delimiter ;

-- FAQ 수정 프로시저
drop procedure if exists sp_ad_faq_update;
delimiter $$
create procedure sp_ad_faq_update(
	bfidx int, bfcata varchar(20), bftitle varchar(100), bfcontent text)
begin
	update t_brd_faq set bf_cata = bfcata, bf_title = bftitle, bf_content = bfcontent 
	where bf_idx = bfidx;
end $$
delimiter ;

-- FAQ 삭제 프로시저
drop procedure if exists sp_ad_faq_delete;
delimiter $$
create procedure sp_ad_faq_delete(bfidx int)
begin
	delete from t_brd_faq where bf_idx = bfidx;
end $$
delimiter ;


-- QnA 등록 프로시저
drop procedure if exists sp_qna_insert;
delimiter $$
create procedure sp_qna_insert(miid varchar(20), 
	bqcata varchar(20), bqtitle varchar(100), bqcontent text, bqimg varchar(50))
begin
	insert into t_brd_qna (mi_id, bq_cata, bq_title, bq_content, bq_img) 
    values (miid, bqcata, bqtitle, bqcontent, bqimg);
end $$
delimiter ;
call sp_qna_insert('test1', '회원 관련', '질문있습니다.', '이게 뭔데??', null);
select * from t_brd_qna;

-- QnA 수정 프로시저(사용자 전용)
drop procedure if exists sp_qna_update;
delimiter $$
create procedure sp_qna_update(bqidx int, 
	bqcata varchar(20), bqtitle varchar(100), bqcontent text, bqimg varchar(50))
begin
	update t_brd_qna set bq_cata = bqcata, bq_title = bqtitle, 
	bq_content = bqcontent, bq_img = bqimg where bq_idx = bqidx and bq_status = 'a';
    -- 질문에 대한 수정으로 관리자가 답변을 달기 전에만 수정이 가능함
end $$
delimiter ;
call sp_qna_update(1, '회원 관련', '질문있습니다.2', '이게 뭔데??', null);

-- QnA 삭제 프로시저(사용자 전용)
drop procedure if exists sp_qna_delete;
delimiter $$
create procedure sp_qna_delete(bqidx int)
begin
	delete from t_brd_qna where bq_idx = bqidx and bq_status = 'a';
    -- 질문에 대한 수정으로 관리자가 답변을 달기 전에만 삭제가 가능함
end $$
delimiter ;

-- QnA 답변/수정 프로시저(관리자 전용)
drop procedure if exists sp_ad_qna_answer;
delimiter $$
create procedure sp_ad_qna_answer(bqidx int, bqanswer text, aiidx int)
begin
	update t_brd_qna set bq_answer = bqanswer, bq_adate = now(), 
		bq_status = 'b', ai_idx = aiidx where bq_idx = bqidx;
	-- 기존의 답변 내용에 추가하는 식으로 계속 답변을 늘릴 수 있음
end $$
delimiter ;
call sp_ad_qna_answer(1, '답변입니다.', 1);
select * from t_brd_qna;

-- QnA 삭제 프로시저(관리자 전용)
drop procedure if exists sp_ad_qna_delete;
delimiter $$
create procedure sp_ad_qna_delete(bqidx int)
begin
	delete from t_brd_qna where bq_idx = bqidx;
end $$
delimiter ;

-- 상품 후기 등록 프로시저(후기 등록시 구매에 대한 포인트 적립(구매금액의 1%)도 같이 이루어져야 함)
drop procedure if exists sp_review_insert;
delimiter $$
create procedure sp_review_insert(miid varchar(20), oiid char(10), piid char(6), 
	bropt varchar(10), brtitle varchar(100), brcontent text, brimg1 varchar(50), brimg2 varchar(50), 
	brstar float, mppoint int, odidx int)
begin
	declare bridx int default 0; -- 새로 입력된 후기 글번호를 저장할 변수
	declare star float default 0.0; -- 별점 평균을 저장할 변수

	insert into t_brd_review (mi_id, oi_id, pi_id, br_title, br_content, br_img1, br_img2, br_star) 
	values (miid, oiid, piid, brtitle, brcontent, brimg1, brimg2, brstar);
	select max(br_idx) into bridx from t_brd_review where mi_id = miid and oi_id = oiid;
    -- 새롭게 등록된 후기의 글번호를 bridx변수에 저장

	insert into t_member_point (mi_id, mp_point, br_idx, mp_content) 
    values (miid, mppoint, bridx, '후기등록 포인트 적립');
    -- 포인트 내역 테이블에 후기등록 적립 쿼리 실행
	update t_member_info set mi_point = mi_point + mppoint where mi_id = miid;
    -- 회원 정보 테이블에 보유 포인트 변경
    update t_order_detail set od_savepnt = mppoint where od_idx = odidx;
    -- 주문 상세 테이블에 구매에 따른 포인트 적립금 변경

	select avg(br_star) into star from t_brd_review where pi_id = piid and br_isview = 'y';
	-- 해당 상품에 대한 리뷰별점의 평균을 구하여 star변수에 저장(볼수있는 리뷰에서만 추출)
	update t_product_info set pi_review = pi_review + 1, pi_star = ifnull(star, 0.0) where pi_id = piid;
    -- 해당 상품의 평점 평균을 변경함(평균값이 null이면 - 리뷰가 없으면 - 0점 처리)
end $$
delimiter ;

-- 상품 후기 수정 프로시저(댓글이 있으면 수정 불가)
drop procedure if exists sp_review_update;
delimiter $$
create procedure sp_review_update(bridx int, brtitle varchar(100), brcontent text, 
	brimg1 varchar(50), brimg2 varchar(50))
begin
	update t_brd_review set br_title = brtitle, br_content = brcontent, br_img1 = brimg1, 
		br_img2 = brimg2 where br_idx = bridx and br_cnt = 0;
end $$
delimiter ;

-- 상품 후기 삭제 프로시저(관리자 전용)
drop procedure if exists sp_review_delete;
delimiter $$
create procedure sp_review_delete(bridx int, piid char(6))
begin
	declare star float; -- 별점 평균을 저장할 변수들
	set star = 0.0; -- 변수 초기화

	update t_brd_review set br_isview = 'n' where br_idx = bridx;
    -- 관리자가 사용자들이 못보게 후기를 수정함(관리자 페이지에서는 보임)

	select avg(br_star) into star from t_brd_review where pi_id = piid and br_isview = 'y';
	-- 해당 상품에 대한 리뷰별점의 평균을 구하여 star변수에 저장(볼수있는 리뷰에서만 추출)

	update t_product_info set pi_review = pi_review - 1, pi_star = ifnull(star, 0.0) where pi_id = piid;
	-- 해당 상품의 리뷰개수를 1줄이고, 평점 평균을 변경함(평균값이 null이면 - 리뷰가 없으면 - 0점 처리)
end $$
delimiter ;

-- 상품 후기 평점 등록 프로시저(따로 수정이나 삭제 기능은 필요 없음)
drop procedure if exists sp_review_star;
delimiter $$
create procedure sp_review_star(miid varchar(20), bridx int, brsscore float)
begin
	declare score float; -- 평점 평균을 저장할 변수들
	set score = 0.0; -- 변수 초기화

	insert into t_brd_review_star (mi_id, br_idx, brs_score) values (miid, bridx, brsscore);
	select avg(brs_score) into score from t_brd_review_star where br_idx = bridx;
	-- 해당 리뷰에 대한 평점의 평균을 구하여 score변수에 저장
	update t_brd_review set br_score = ifnull(score, 0.0) where br_idx = bridx;
    -- 해당 리뷰의 평점 평균을 변경함(평균값이 null이면 0점 처리)
end $$
delimiter ;

-- 상품 후기 댓글 등록 프로시저
drop procedure if exists sp_review_reply_insert;
delimiter $$
create procedure sp_review_reply_insert(bridx int, miid varchar(20), brrcontent text)
begin
	insert into t_brd_review_reply (br_idx, mi_id, brr_content )
	values (bridx, miid, brrcontent );

	update t_brd_review set br_cnt = br_cnt + 1 where br_idx = bridx;
	-- 리뷰 테이블의 댓글 개수를 1증가 시킴
    
end $$
delimiter ;

-- 상품 후기 댓글 수정 프로시저
drop procedure if exists sp_review_reply_update;
delimiter $$
create procedure sp_review_reply_update(brridx int, miid varchar(20), brrcontent text)
begin
	update t_brd_review_reply set brr_content = brrcontent 
	where brr_idx = brridx and mi_id = miid;
end $$
delimiter ;

-- 상품 후기 댓글 삭제 프로시저
drop procedure if exists sp_review_reply_delete;
delimiter $$
create procedure sp_review_reply_delete(brridx int, bridx int, miid varchar(20))
begin
	update t_brd_review_reply set brr_isview = 'n' where brr_idx = brridx and mi_id = miid;

	update t_brd_review set br_cnt = br_cnt - 1 where br_idx = bridx;
	-- 리뷰 테이블의 댓글 개수를 1감소 시킴
end $$
delimiter ;

-- 상품 후기 댓글 추천 프로시저
drop procedure if exists sp_review_reply_recc;
delimiter $$
create procedure sp_review_reply_recc(brridx int, miid varchar(20))
begin
	insert into t_brd_review_reply_recc (brr_idx, mi_id) values (brridx, miid);
    update t_brd_review_reply set brr_recc = brr_recc + 1 where brr_idx = brridx;
end $$
delimiter ;

-- 상품 후기 신고 등록 프로시저(수정이나 삭제 기능은 필요 없음)
drop procedure if exists sp_review_911;
delimiter $$
create procedure sp_review_911(bridx int, miid varchar(20), br9content varchar(50))
begin
	insert into t_brd_review_911 (br_idx, mi_id, br9_content) values (bridx, miid, br9content);
end $$
delimiter ;

-- 상품 후기 신고 처리 프로시저
drop procedure if exists sp_ad_review_911_proc;
delimiter $$
create procedure sp_ad_review_911_proc(br9idx int, br9status char(1), aiidx int)
begin
	update t_brd_review_911 set br9_status = br9status, br9_opdate = now(), ai_idx = aiidx 
	where br9_idx = br9idx;
end $$
delimiter ;


-- 설문조사 등록 프로시저
drop procedure if exists sp_ad_poll_insert;
delimiter $$
create procedure sp_ad_poll_insert(
	pqquestion varchar(200), pqex1 varchar(100), pqex2 varchar(100), pqex3 varchar(100), 
	pqex4 varchar(100), pqex5 varchar(100), pqsdate varchar(10), pqedate varchar(10),
	pqendmsg varchar(100), pqstatus char(1), aiidx int)
begin
	insert into t_poll_question (pq_question, pq_ex1, pq_ex2, pq_ex3, pq_ex4, pq_ex5, 
		pq_sdate, pq_edate, pq_endmsg, pq_status, ai_idx) 
	values (pqquestion, pqex1, pqex2, pqex3, pqex4, pqex5, pqsdate, pqedate, 
		pqendmsg, pqstatus, aiidx);
end $$
delimiter ;
call sp_ad_poll_insert('주로 마시는 술은 어떤 술인가요?', '소주', '맥주', '막걸리', '와인', 
	'데킬라', '2021-04-27', '2021-05-27', '이제 그만', 'a', 1);
select * from t_poll_question;

-- 설문조사 수정 프로시저
drop procedure if exists sp_ad_poll_update;
delimiter $$
create procedure sp_ad_poll_update(
	pqidx int, pqquestion varchar(200), pqex1 varchar(100), pqex2 varchar(100), 
	pqex3 varchar(100), pqex4 varchar(100), pqex5 varchar(100), pqsdate varchar(10), 
	pqedate varchar(10), pqendmsg varchar(100), pqstatus char(1))
begin
	update t_poll_question set pq_question = pqquestion, pq_ex1 = pqex1, pq_ex2 = pqex2, 
		pq_ex3 = pqex3, pq_ex4 = pqex4, pq_ex5 = pqex5, pq_sdate = pqsdate, 
		pq_edate = pqedate, pq_endmsg = pqendmsg, pq_status = pqstatus 
	where pq_idx = pqidx;
end $$
delimiter ;
call sp_ad_poll_update(1, '주로 마시는 술은 어떤 술인가요?', '소주', '맥주', '막걸리', '와인', 
	'데킬라', '2021-04-26', '2021-06-27', '이제 그만', 'b');

-- 설문조사 참여 프로시저
drop procedure if exists sp_poll_result_insert;
delimiter $$
create procedure sp_poll_result_insert(pqidx int, miid varchar(20), prex int)
begin
	-- 현재 진행중인 설문인지 여부를 검사 후 참여시킴
	declare pqstatus char(1);
	select pq_status into pqstatus from t_poll_question where pq_idx = pqidx;
    if pqstatus = 'b' then -- 현재 진행중인 설문조사이면
    	insert into t_poll_result (pq_idx, mi_id, pr_ex) values (pqidx, miid, prex);
	end if;
end $$
delimiter ;
call sp_poll_result_insert(1, 'test1', 1);
call sp_poll_result_insert(1, 'test2', 1);
call sp_poll_result_insert(1, 'test3', 2);
call sp_poll_result_insert(1, 'test4', 3);
select * from t_poll_result;

-- 1번 설문의 결과를 select해오는 쿼리(각 보기와 보기별 개수 추출)
select a.pq_ex1, a.pq_ex2, a.pq_ex3, a.pq_ex4, a.pq_ex5, b.pr_ex, count(b.pr_ex) 
from t_poll_question a, t_poll_result b 
where a.pq_idx = b.pq_idx and a.pq_idx = 1 
group by b.pr_ex;

-- 메인화면 배너 등록 프로시저(들어있는 이미지 개수에 상관없이 계속 넣을 수 있음)
drop procedure if exists sp_ad_banner_insert;
delimiter $$
create procedure sp_ad_banner_insert(
	mbimg varchar(50), mburl varchar(100), mbseq int, mbisview char(1), aiidx int)
begin
	insert into t_main_banner (mb_img, mb_url, mb_seq, mb_isview, ai_idx) 
	values (mbimg, mburl, mbseq, mbisview, aiidx);
end $$
delimiter ;

-- 메인화면 배너 등록 프로시저2(슬라이드될 이미지의 개수가 정해져 있는 경우 - 4개)
-- 정해진 개수를 모두 채운상태에서 보여줄 이미지를 새롭게 추가할 경우 원래 이미지들 중 하나를 안보이게 설정
drop procedure if exists sp_ad_banner_insert2;
delimiter $$
create procedure sp_ad_banner_insert2(oldidx int, -- 기존 이미지중 안보이게 하려는 이미지의 idx
	mbimg varchar(50), mburl varchar(100), mbseq int, mbisview char(1), aiidx int)
begin
	if mbisview = 'y' then -- 새로 추가하는 이미지를 보여주는 경우
		if oldidx > 0 then -- 기존의 이미지들(4개) 중 안보이게 할 이미지가 있을 경우
			update t_main_banner set mb_isview = 'n' where mb_idx = oldidx;
        end if;
    end if;
	insert into t_main_banner (mb_img, mb_url, mb_seq, mb_isview, ai_idx) 
	values (mbimg, mburl, mbseq, mbisview, aiidx);
end $$
delimiter ;
call sp_ad_banner_insert2(0, 'main01.jpg', '/', 1, 'y', 1);
call sp_ad_banner_insert2(0, 'main02.jpg', '/', 1, 'y', 1);
call sp_ad_banner_insert2(0, 'main03.jpg', '/', 1, 'y', 1);
call sp_ad_banner_insert2(0, 'main04.jpg', '/', 1, 'y', 1);
call sp_ad_banner_insert2(0, 'main05.jpg', '/', 5, 'n', 1);
call sp_ad_banner_insert2(3, 'main06.jpg', '/', 3, 'y', 1);
select * from t_main_banner;

-- 메인화면 배너 수정 프로시저(들어있는 이미지 개수에 상관없이 계속 넣을 수 있음)
drop procedure if exists sp_ad_banner_update;
delimiter $$
create procedure sp_ad_banner_update(
	mbidx int, mbimg varchar(50), mburl varchar(100), mbseq int, mbisview char(1))
begin
	update t_main_banner set mb_img = mbimg, mb_url = mburl, mb_seq = mbseq, 
	mb_isview = mbisview where mb_idx = mbidx;
end $$
delimiter ;

-- 메인화면 배너 수정 프로시저2(슬라이드될 이미지의 개수가 정해져 있는 경우 - 4개)
-- 수정하려는 이미지의 mbisview값이 'y'인 경우 기존의 이미지 개수를 확인해야 함
-- 수정하는 이미지 값이 'y'라도 처음부터 'y'였으면 그냥 작업이 가능하나 처음에 'n'이었을 경우 문제가 발생
drop procedure if exists sp_ad_banner_update2;
delimiter $$
create procedure sp_ad_banner_update2(oldidx int, oldview char(1), 
-- oldidx : 새롭게 보여질 이미지를 위해 숨길 이미지번호, oldview : 수정하려는 이미지의 원래 보임여부
	mbidx int, mbimg varchar(50), mburl varchar(100), mbseq int, mbisview char(1))
begin
	if mbisview = 'y' and oldview = 'n' and oldidx > 0 then
    -- 수정하는 이미지가 보임으로 변경되고 기존의 보이는 이미지가 4개일 경우
		update t_main_banner set mb_isview = 'n' where mb_idx = oldidx;
    end if;
	update t_main_banner set mb_img = mbimg, mb_url = mburl, mb_seq = mbseq, 
	mb_isview = mbisview where mb_idx = mbidx;
end $$
delimiter ;
call sp_ad_banner_update2(2, 'n', 3, 'main03.jpg', '/', 2, 'y');
select * from t_main_banner;


-- 주문 관련 프로시저
-- 장바구니 담기 프로시저(기존에 동일상품, 동일옵션의 상품이 들어있으면 추가가 아닌 개수변경으로 작업)
drop procedure if exists sp_cart_insert;
delimiter $$
create procedure sp_cart_insert(
	miid varchar(20), piid char(6), ocoption varchar(10), occnt int)
begin
	declare cnt int; -- 동일상품/옵션의 개수를 저장할 변수
    select count(oc_idx) into cnt from t_order_cart 
	where mi_id = miid and pi_id = piid and oc_option = ocoption;
    -- 동일 회원/상품/옵션을 가진 목록이 카트테이블에 몇 개인지를 추출하는 쿼리

	if cnt = 0 then -- 기존 목록에 동일한 상품이 없을 경우
		insert into t_order_cart (mi_id, pi_id, oc_option, oc_cnt) 
		values (miid, piid, ocoption, occnt);
	else
		update t_order_cart set oc_cnt = oc_cnt + occnt 
		where mi_id = miid and pi_id = piid and oc_option = ocoption;
	end if;
end $$
delimiter ;
call sp_cart_insert('test1', 'pdt001', '14도', 2);
call sp_cart_insert('test1', 'pdt001', '18도', 1);
call sp_cart_insert('test1', 'pdt002', '11도', 3);
select * from t_order_cart;

select oc_idx from t_order_cart where oc_option = '';

-- 장바구니 수정 프로시저(옵션 변경시 기존 상품들 중 동일옵션의 상품이 들어있으면 변경이 아닌 개수변경으로 작업)
drop procedure if exists sp_cart_update;
delimiter $$
create procedure sp_cart_update(
	ocidx int, miid varchar(20), piid char(6), ocoption varchar(10), occnt int)
begin
	declare idx int; -- 동일상품/옵션의 개수를 저장할 변수
    select ifnull(oc_idx, 0) into idx from t_order_cart 
	where mi_id = miid and pi_id = piid and oc_option = ocoption and oc_idx <> ocidx;
    -- 동일 회원/상품/옵션을 가진 목록이 현재 상품이 아닌 것이 카트테이블에 있으면 idx를 추출하는 쿼리

	if idx = 0 then
		update t_order_cart set oc_option = ocoption, oc_cnt = occnt where oc_idx = ocidx;
		-- 옵션/수량을 변경해주는 쿼리
    else
    begin
		update t_order_cart set oc_cnt = oc_cnt + occnt where oc_idx = idx;
		-- 동일한 옵션을 가진 상품이 이미 있으므로 수량만 추가해 주는 쿼리
		delete from t_order_cart where oc_idx = ocidx;
        -- 변경한 상품은 수량을 추가했으므로 변경한 상품을 삭제해야 함
	end;
    end if;
end $$
delimiter ;
call sp_cart_update(1, 'test1', 'pdt001', '14도', 1);
select * from t_order_cart;

-- 장바구니 삭제 프로시저
drop procedure if exists sp_cart_delete;
delimiter $$
create procedure sp_cart_delete(ocidx int)
begin
	delete from t_order_cart where oc_idx = ocidx;
end $$
delimiter ;
call sp_cart_delete(3);

-- 주문 정보 등록 프로시저
-- insert : t_order_info, t_order_detail, t_member_point
-- update : t_member_info, t_product_info / delete : t_order_cart
-- ocidxs : 바로구매(0), 카트구매 - 한가지만 구매(idx) & 여러 가지 구매('idx1','idx2',...,'idxn')
drop procedure if exists sp_order_insert;
delimiter $$
create procedure sp_order_insert(miid varchar(20), oiname varchar(20), oiphone varchar(13), oizip char(5), 
	oiaddr1 varchar(50), oiaddr2 varchar(50), oipayment char(1), oipay int, oiusepnt int, oidelipay int, 
	oistatus char(1), oicmt varchar(100), piid char(6), odpdtname varchar(20), odpdtimg varchar(50), 
	odpdtprice int, odcnt int, odoption varchar(10), ocidxs varchar(50))
begin
	declare oiid char(10);
	declare end_row boolean default false;
	declare cpiid char(6);			-- 쇼핑 카트내 상품id를 저장할 변수
	declare coption varchar(10);	-- 쇼핑 카트내 상품옵션을 저장할 변수
	declare ccnt int;				-- 쇼핑 카트내 상품개수를 저장할 변수
	declare cname varchar(20);		-- 쇼핑 카트내 상품이름을 저장할 변수
	declare cimg varchar(50);		-- 쇼핑 카트내 상품이미지를 저장할 변수
	declare cprice int;				-- 쇼핑 카트내 상품단가를 저장할 변수
	declare c_set cursor for
		select a.pi_id, a.oc_option, a.oc_cnt, b.pi_name, b.pi_img1, b.pi_price 
		from t_order_cart a, t_product_info b where a.pi_id = b.pi_id and a.oc_idx in (ocidxs);
        -- 구매하려는 목록을 카트에서 추출하여 커서로 생성
	declare continue handler for not found set end_row = true;

	-- 새 주문번호 생성
	select if(count(oi_id) = 0, concat(right(replace(date(now()), '-', ''), 6), '1000'), oi_id) + 1 into oiid 
	from t_order_info where date(oi_date) = curdate() order by oi_id desc limit 1;

	-- 주문 정보 등록 쿼리
	insert into t_order_info (oi_id, mi_id, oi_name, oi_phone, oi_zip, oi_addr1, oi_addr2, oi_payment, 
		oi_pay, oi_usepnt, oi_delipay, oi_status, oi_cmt) values (oiid, miid, oiname, oiphone, oizip, 
		oiaddr1, oiaddr2, oipayment, oipay, oiusepnt, oidelipay, oistatus, oicmt);

	-- 포인트 사용내역 등록 및 보유포인트 변경 쿼리
    if oiusepnt > 0 then -- 주문시 포인트를 사용했을 경우
    begin
		insert into t_member_point (mi_id, mp_kind, mp_point, mp_linknum, mp_content) 
		values (miid, 'u', oiusepnt, oiid, '주문시 사용');

		update t_member_info set mi_point = mi_point - oiusepnt where mi_id = miid;
	end;
	end if;

	if ocidxs = '0' then -- 바로 구매로 구매시
	begin
		-- 주문 상세 등록 쿼리
		insert into t_order_detail (oi_id, pi_id, od_pdtname, od_pdtimg, od_pdtprice, od_cnt, od_option) 
		values (oiid, piid, odpdtname, odpdtimg, odpdtprice, odcnt, odoption);
		-- 상품 판매개수 및 재고량 변경 쿼리
		update t_product_info set pi_salecnt = pi_salecnt + odcnt, 
		pi_stock = pi_stock - if(pi_stock = -1, 0, odcnt) where pi_id = piid;
	end;
	else -- 쇼핑카트를 통해 구매시
    begin
        open c_set; -- c_set이라는 커서를 열어줌
		lblLoop:while true do
			fetch c_set into cpiid, coption, ccnt, cname, cimg, cprice; -- 커서내에 있는 데이터들을 차례대로 변수에 저장
			if end_row then
				leave lblLoop;
			end if;
			-- 주문 상세 등록 쿼리
			insert into t_order_detail (oi_id, pi_id, od_pdtname, od_pdtimg, od_pdtprice, od_cnt, od_option) 
			values (oiid, cpiid, cname, cimg, cprice, ccnt, coption);
			-- 상품 판매개수 및 재고량 변경 쿼리
			update t_product_info set pi_salecnt = pi_salecnt + ccnt, 
			pi_stock = pi_stock - if(pi_stock = -1, 0, ccnt) where pi_id = cpiid;
		end while;

		-- 쇼핑카트에서 구매한 상품 삭제 쿼리
		delete from t_order_cart where oc_idx in (ocidxs);
		close c_set;
	end;
	end if;
end $$
delimiter ;

-- 주문 정보 수정 프로시저(사용자와 관리자 공용)
-- 사용자 : 무통장 입금일 경우에만 동작하며, 입금전에만 배송관련 정보수정 가능 및 상태수정-주문취소, 반품요청 등
-- 관리자 : 상황파악 후 정보 수정 및 상태 변경 작업
drop procedure if exists sp_all_order_update;
delimiter $$
create procedure sp_all_order_update(aiidx int, miid varchar(20), oiid char(10), oistatus char(1), 
	oiname varchar(20), oiphone varchar(13), oizip char(5), oiaddr1 varchar(50), oiaddr2 varchar(50))
begin
	if aiidx = 0 then -- 사용자가 변경하는 경우
		update t_order_info set oi_name = oiname, oi_phone = oiphone, oi_zip = oizip, oi_addr1 = oiaddr1, 
			oi_addr2 = oiaddr2, oi_status = oistatus where mi_id = miid and oi_id = oiid;
    else -- 관리자가 변경하는 경우
		update t_order_info set oi_name = oiname, oi_phone = oiphone, oi_zip = oizip, oi_addr1 = oiaddr1, 
			oi_addr2 = oiaddr2, oi_status = oistatus, oi_modify = now(), ai_idx = aiidx where oi_id = oiid;
    end if;
end $$
delimiter ;






















