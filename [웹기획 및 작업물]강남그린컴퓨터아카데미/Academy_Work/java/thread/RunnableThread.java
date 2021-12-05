class Sum {
	int num;
	public Sum() { num = 0; }
	public void addNum(int n) { num += n; }
	public int getNum() { return num; }
}

class AdderThread extends Sum implements Runnable {
	// Thread를 상속받을 수 없는 상황에서는 Runnable 인터페이스를 이용하여 쓰레드 작업을 해야 함
	int start, end;
	public AdderThread(int s, int e) {
		start	= s;
		end		= e; 
	}
	public void run() {
	// Runnable 인터페이스의 abstract 메소드로 반드시 구현해야 함
		for (int i = start; i <= end ; i++ ) {
			addNum(i);
			// Sum 클래스에서 상속받은 메소드
		}
	}
}

class RunnableThread {
	public static void main(String[] args) {
		AdderThread at1 = new AdderThread(1, 50);
		AdderThread at2 = new AdderThread(51, 100);

		Thread tr1 = new Thread(at1);
		Thread tr2 = new Thread(at2);
		// Thread 클래스의 생성자를 이용하여 Runnable 의 인스턴스를 쓰레드로 만듦

		tr1.start();
		tr2.start();
		// 생성된 두 개의 쓰레드를 실행 시킴(run() 메소드 호출)

		try {
			// join() : 해당 쓰레드 모든 작업이 끝날 때 까지 다른 작업을 대기시키는 기능
			// join() 메소드는 메소드 선언시 throws InterruptedException을 지정했기 때문에 예외처리를 하면서 호출해야 함
			tr1.join();
			tr2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("1~100 까지의 합 : " + (at1.getNum() + at2.getNum()) );
	}
}
