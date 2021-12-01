class ShowThread extends Thread {
// Thread 클래스를 상속 받음으로써 ShowThread 클래스는 쓰레드로 사용될 수 있음
	String name;
	public ShowThread(String name) {
		this.name = name;
	}
	public void run() {
	// 쓰레드에서 해야할 일들을 정의해 놓는 메소드로 쓰레드 실행시 start()메소드를 호출하면 자동으로 run() 메소드가 실행됨
		for (int i = 0; i < 10 ; i++ ) {
			System.out.println("안녕하세요. " + name + "입니다.");
			try {
				sleep(100);	// 현재 실행되고 있는 쓰레드를 0.1초 동안 멈춤
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ThreadFirst {
	public static void main(String[] args) {
		ShowThread st1 = new ShowThread("1st 쓰레드");
		ShowThread st2 = new ShowThread("2nd 쓰레드");
		// st1와 st2 라는 프로그램 흐름을 가진 두 개의 쓰레드를 생성

		st1.start();	// st1 이라는 쓰레드를 실행시킴(run()메소드 호출)
		st2.start();	// st2 이라는 쓰레드를 실행시킴(run()메소드 호출)
		// 쓰레드는 실행시킨 순서와 상관없이 먼저 cpu를 차지하는 쪽이 실행됨

	}
}
