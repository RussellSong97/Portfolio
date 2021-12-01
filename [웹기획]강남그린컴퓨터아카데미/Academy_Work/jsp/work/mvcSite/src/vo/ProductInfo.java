package vo;

// 하나의 상품 정보를 저장하는 클래스 
public class ProductInfo {
	private String pi_id, pi_name, cs_id, b_id, pi_origin, pi_option;
	private String pi_img1, pi_img2, pi_img3, pi_desc, pi_isview, pi_date;
	private int pi_cost, pi_price, pi_discount, pi_stock, pi_salecnt, pi_review, ai_idx;
	private double pi_star;
	private String cb_id, cb_name, cs_name, b_name; // 조인해서 받아올 데이터들

	public String getB_name() {
		return b_name;
	}

	public void setB_name(String b_name) {
		this.b_name = b_name;
	}

	public String getPi_id() {
		return pi_id;
	}

	public void setPi_id(String pi_id) {
		this.pi_id = pi_id;
	}

	public String getPi_name() {
		return pi_name;
	}

	public void setPi_name(String pi_name) {
		this.pi_name = pi_name;
	}

	public String getCs_id() {
		return cs_id;
	}

	public void setCs_id(String cs_id) {
		this.cs_id = cs_id;
	}

	public String getB_id() {
		return b_id;
	}

	public void setB_id(String b_id) {
		this.b_id = b_id;
	}

	public String getPi_origin() {
		return pi_origin;
	}

	public void setPi_origin(String pi_origin) {
		this.pi_origin = pi_origin;
	}

	public String getPi_option() {
		return pi_option;
	}

	public void setPi_option(String pi_option) {
		this.pi_option = pi_option;
	}

	public String getPi_img1() {
		return pi_img1;
	}

	public void setPi_img1(String pi_img1) {
		this.pi_img1 = pi_img1;
	}

	public String getPi_img2() {
		return pi_img2;
	}

	public void setPi_img2(String pi_img2) {
		this.pi_img2 = pi_img2;
	}

	public String getPi_img3() {
		return pi_img3;
	}

	public void setPi_img3(String pi_img3) {
		this.pi_img3 = pi_img3;
	}

	public String getPi_desc() {
		return pi_desc;
	}

	public void setPi_desc(String pi_desc) {
		this.pi_desc = pi_desc;
	}

	public String getPi_isview() {
		return pi_isview;
	}

	public void setPi_isview(String pi_isview) {
		this.pi_isview = pi_isview;
	}

	public String getPi_date() {
		return pi_date;
	}

	public void setPi_date(String pi_date) {
		this.pi_date = pi_date;
	}

	public int getPi_cost() {
		return pi_cost;
	}

	public void setPi_cost(int pi_cost) {
		this.pi_cost = pi_cost;
	}

	public int getPi_price() {
		return pi_price;
	}

	public void setPi_price(int pi_price) {
		this.pi_price = pi_price;
	}

	public int getPi_discount() {
		return pi_discount;
	}

	public void setPi_discount(int pi_discount) {
		this.pi_discount = pi_discount;
	}

	public int getPi_stock() {
		return pi_stock;
	}

	public void setPi_stock(int pi_stock) {
		this.pi_stock = pi_stock;
	}

	public int getPi_salecnt() {
		return pi_salecnt;
	}

	public void setPi_salecnt(int pi_salecnt) {
		this.pi_salecnt = pi_salecnt;
	}

	public int getPi_review() {
		return pi_review;
	}

	public void setPi_review(int pi_review) {
		this.pi_review = pi_review;
	}

	public int getAi_idx() {
		return ai_idx;
	}

	public void setAi_idx(int ai_idx) {
		this.ai_idx = ai_idx;
	}

	public double getPi_star() {
		return pi_star;
	}

	public void setPi_star(double pi_star) {
		this.pi_star = pi_star;
	}

	public String getCb_id() {
		return cb_id;
	}

	public void setCb_id(String cb_id) {
		this.cb_id = cb_id;
	}

	public String getCb_name() {
		return cb_name;
	}

	public void setCb_name(String cb_name) {
		this.cb_name = cb_name;
	}

	public String getCs_name() {
		return cs_name;
	}

	public void setCs_name(String cs_name) {
		this.cs_name = cs_name;
	}
}