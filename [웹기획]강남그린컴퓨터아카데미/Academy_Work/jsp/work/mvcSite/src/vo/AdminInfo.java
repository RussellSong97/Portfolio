package vo;

public class AdminInfo {
// 한 명의 관리자 정보를 저장하는 클래스
	private int ai_idx;
	private String ai_id, ai_pwd, ai_name, ai_pms, ai_isrun, ai_regdate;
	public int getAi_idx() {
		return ai_idx;
	}
	public void setAi_idx(int ai_idx) {
		this.ai_idx = ai_idx;
	}
	public String getAi_id() {
		return ai_id;
	}
	public void setAi_id(String ai_id) {
		this.ai_id = ai_id;
	}
	public String getAi_pwd() {
		return ai_pwd;
	}
	public void setAi_pwd(String ai_pwd) {
		this.ai_pwd = ai_pwd;
	}
	public String getAi_name() {
		return ai_name;
	}
	public void setAi_name(String ai_name) {
		this.ai_name = ai_name;
	}
	public String getAi_pms() {
		return ai_pms;
	}
	public void setAi_pms(String ai_pms) {
		this.ai_pms = ai_pms;
	}
	public String getAi_isrun() {
		return ai_isrun;
	}
	public void setAi_isrun(String ai_isrun) {
		this.ai_isrun = ai_isrun;
	}
	public String getAi_regdate() {
		return ai_regdate;
	}
	public void setAi_regdate(String ai_regdate) {
		this.ai_regdate = ai_regdate;
	}
}
