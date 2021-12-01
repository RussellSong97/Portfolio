package vo;

public class PdtPageInfo {
// 상품 관련 목록에서 페이징을 위해 필요한 데이터들을 저장할 클래스(어드민 & 프론트 공용)
	private int cpage, pcnt, spage, epage, rcnt, psize, bsize;
	// 현재 페이지번호, 페이지수, 시작페이지, 종료페이지, 게시글수, 페이지크기, 블록크기
	private String keyword, bcata, scata, brand, sprice, eprice;
	// 프론트와 어드민에서 공통적으로 사용할 검색관련 정보를 저장할 변수들
	private String isview, sdate, schtype, edate, stock;
	// 어드민에서만 사용할 검색관련 정보를 저장할 변수들
	private String ord;		// 정렬조건

	public int getCpage() {
		return cpage;
	}
	public void setCpage(int cpage) {
		this.cpage = cpage;
	}
	public int getPcnt() {
		return pcnt;
	}
	public void setPcnt(int pcnt) {
		this.pcnt = pcnt;
	}
	public int getSpage() {
		return spage;
	}
	public void setSpage(int spage) {
		this.spage = spage;
	}
	public int getEpage() {
		return epage;
	}
	public void setEpage(int epage) {
		this.epage = epage;
	}
	public int getRcnt() {
		return rcnt;
	}
	public void setRcnt(int rcnt) {
		this.rcnt = rcnt;
	}
	public int getPsize() {
		return psize;
	}
	public void setPsize(int psize) {
		this.psize = psize;
	}
	public int getBsize() {
		return bsize;
	}
	public void setBsize(int bsize) {
		this.bsize = bsize;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getBcata() {
		return bcata;
	}
	public void setBcata(String bcata) {
		this.bcata = bcata;
	}
	public String getScata() {
		return scata;
	}
	public void setScata(String scata) {
		this.scata = scata;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSprice() {
		return sprice;
	}
	public void setSprice(String sprice) {
		this.sprice = sprice;
	}
	public String getEprice() {
		return eprice;
	}
	public void setEprice(String eprice) {
		this.eprice = eprice;
	}
	public String getIsview() {
		return isview;
	}
	public void setIsview(String isview) {
		this.isview = isview;
	}
	
	public String getSchtype() {
		return schtype;
	}
	public void setSchtype(String schtype) {
		this.schtype = schtype;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getOrd() {
		return ord;
	}
	public void setOrd(String ord) {
		this.ord = ord;
	}
}
