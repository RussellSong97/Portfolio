package vo;

public class NoticeInfo {
// 하나의 공지사항 게시물을 저장할 클래스
      private int bn_idx, bn_read, ai_idx ;
      private  String bn_title, bn_content, bn_isnotice, bn_date;
      public int getBn_idx() {
         return bn_idx;
      }
      public void setBn_idx(int bn_idx) {
         this.bn_idx = bn_idx;
      }
      public int getBn_read() {
         return bn_read;
      }
      public void setBn_read(int bn_read) {
         this.bn_read = bn_read;
      }
      public int getAi_idx() {
         return ai_idx;
      }
      public void setAi_idx(int ai_idx) {
         this.ai_idx = ai_idx;
      }
      public String getBn_title() {
         return bn_title;
      }
      public void setBn_title(String bn_title) {
         this.bn_title = bn_title;
      }
      public String getBn_content() {
         return bn_content;
      }
      public void setBn_content(String bn_content) {
         this.bn_content = bn_content;
      }
      public String getBn_isnotice() {
         return bn_isnotice;
      }
      public void setBn_isnotice(String bn_isnotice) {
         this.bn_isnotice = bn_isnotice;
      }
      public String getBn_date() {
         return bn_date;
      }
      public void setBn_date(String bn_date) {
         this.bn_date = bn_date;
      }
}