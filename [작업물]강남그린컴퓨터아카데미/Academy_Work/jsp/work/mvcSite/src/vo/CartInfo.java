package vo;

public class CartInfo {
// 장바구니에 담긴 상품정보를 저장할 클래스로 장바구니 화면에서 보여줄 데이터들도 저장할 수 있게 했음
	private int oc_idx, oc_cnt;
	private String mi_id, pi_id, oc_option, oc_date;
	private String pi_name, pi_img1, b_name, pi_option;
	private int pi_price, pi_stock;

	public int getOc_idx() {
		return oc_idx;
	}
	public void setOc_idx(int oc_idx) {
		this.oc_idx = oc_idx;
	}
	public int getOc_cnt() {
		return oc_cnt;
	}
	public void setOc_cnt(int oc_cnt) {
		this.oc_cnt = oc_cnt;
	}
	public String getMi_id() {
		return mi_id;
	}
	public void setMi_id(String mi_id) {
		this.mi_id = mi_id;
	}
	public String getPi_id() {
		return pi_id;
	}
	public void setPi_id(String pi_id) {
		this.pi_id = pi_id;
	}
	public String getOc_option() {
		return oc_option;
	}
	public void setOc_option(String oc_option) {
		this.oc_option = oc_option;
	}
	public String getOc_date() {
		return oc_date;
	}
	public void setOc_date(String oc_date) {
		this.oc_date = oc_date;
	}
	public String getPi_name() {
		return pi_name;
	}
	public void setPi_name(String pi_name) {
		this.pi_name = pi_name;
	}
	public String getPi_img1() {
		return pi_img1;
	}
	public void setPi_img1(String pi_img1) {
		this.pi_img1 = pi_img1;
	}
	public String getB_name() {
		return b_name;
	}
	public void setB_name(String b_name) {
		this.b_name = b_name;
	}
	public String getPi_option() {
		return pi_option;
	}
	public void setPi_option(String pi_option) {
		this.pi_option = pi_option;
	}
	public int getPi_price() {
		return pi_price;
	}
	public void setPi_price(int pi_price) {
		this.pi_price = pi_price;
	}
	public int getPi_stock() {
		return pi_stock;
	}
	public void setPi_stock(int pi_stock) {
		this.pi_stock = pi_stock;
	}
}
