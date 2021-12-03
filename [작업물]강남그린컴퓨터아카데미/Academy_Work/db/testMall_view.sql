-- ------------------------------------------------------- --
-- ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 2021.04.19 (월) ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  -- 
-- ------------------------------------------------------- --

-- 주류 쇼핑몰 DB 모델링 create view 쿼리 모음

use testMall;

-- 상품 후기 게시판 목록 화면용 뷰 : v_review_list
-- 작성자 제목 작성일 상품명 이미지 후기ID 선택옵션 상품별점
create view v_review_list as 
	select 	a.br_idx, a.mi_id,  concat(a.br_title, ' - ' , c.od_option ), a.br_date, a.br_star,
			a.br_img1, b.pi_name
	from t_brd_review a, t_product_info b, t_order_detail c 
	where a.pi_id = b.pi_id and a.oi_id = c.oi_id ;

-- 마이 페이지 내 구매목록 및 어드민 구매목록 화면용 뷰 : v_order_list
drop view v_order_list;
create view v_order_list as 
	select 	a.oi_id, a.mi_id, b.pi_id, b.od_pdtimg, b.od_pdtname, b.od_option, 
			b.od_cnt, a.oi_pay, a.oi_date, a.oi_status, c.pi_isview, c.pi_stock,
            d.cb_id, d.cb_name, c.cs_id, e.cs_name, c.b_id, f.b_name
	from t_order_info a, t_order_detail b, t_product_info c, t_cata_big d, t_cata_small e, t_brand f
	where a.oi_id = b.oi_id and b.pi_id = c.pi_id and d.cb_id = e.cb_id and e.cs_id = c.cs_id and f.b_id = c.b_id;
select * from v_order_list;


-- 장바구니 목록 화면용 뷰 : v_cart_list 
drop view v_cart_list;
create view v_cart_list as 
	select a.pi_id, a.pi_price, a.pi_discount, b.oc_cnt, a.pi_option, b.oc_option, a.pi_name, a.pi_img1
	from t_product_info a, t_order_cart b
	where a.pi_id = b.pi_id;


