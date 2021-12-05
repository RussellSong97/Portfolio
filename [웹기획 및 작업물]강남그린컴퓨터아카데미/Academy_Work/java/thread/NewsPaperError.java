class NewsPaper {
	String todayNews;

	public void setTodayNews(String news) {
		todayNews = news;
	}
	public String getTodayNews() {
		return todayNews;
	}
}

class NewsWriter extends Thread {
	NewsPaper paper;
	public NewsWriter(NewsPaper paper) {
		this.paper = paper;
	}
	public void run() {
		paper.setTodayNews("자바의 열기가 뜨겁습니다.");
	}
}

class NewsReader extends Thread {
	NewsPaper paper;
	public NewsReader(NewsPaper paper) {
		this.paper = paper;
	}
	public void run() {
		System.out.println("오늘의 뉴스 : " + paper.getTodayNews());
		// 뉴스가 없어도 가져와서 실행되는 상황이 발생함(null을 출력)
	}

}

class NewsPaperError {
	public static void main(String[] args) {
		NewsPaper paper = new NewsPaper();
		NewsReader reader = new NewsReader(paper);
		NewsWriter writer = new NewsWriter(paper);
		writer.start();		reader.start();		
		try	{
			writer.join();	reader.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
