class NewsPaper {
	String todayNews;
	boolean isTodayNews = false;

	public void setTodayNews(String news) {
		todayNews	= news;
		isTodayNews = true;
		synchronized(this) {
			notifyAll();	// wait() 상태의 모든 쓰레드를 깨움
		}

	}
	public String getTodayNews() {
		if (!isTodayNews) {	// 기사가 없을 때
			try {
				synchronized(this) {
					wait();	// 쓰레드를 대기상태로 만듦 - notify로 깨워야 함
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	}

}

class NewsPaperSync {
	public static void main(String[] args) {
		NewsPaper paper		= new NewsPaper();
		NewsWriter writer	= new NewsWriter(paper);
		NewsReader reader1	= new NewsReader(paper);
		NewsReader reader2	= new NewsReader(paper);
		try	{
			reader1.start();	
			reader2.start();	
			Thread.sleep(1000);
			writer.start();		
			
			reader1.join();
			reader2.join();
			writer.join();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
