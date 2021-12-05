class MessageThread extends Thread {
	String message;
	public MessageThread(String str, int prio) { 
		message = str;
		setPriority(prio);
		// setPriority() : Thread클래스의 메소드로 해당 쓰레드의 우선 순위를 지정함
	}
	public void run() { 
		for (int i = 0; i < 10 ; i++ ) {
			System.out.println(message + "(" + getPriority() + ")"); 
		}
	}
}
class PriorityTest2 {
	public static void main(String[] args) {
		MessageThread tr1 = new MessageThread("1st", Thread.MAX_PRIORITY);	// 10으로 가장 높은 우선순위
		MessageThread tr2 = new MessageThread("2nd", Thread.NORM_PRIORITY);	// 5로 평균 우선순위(default)
		MessageThread tr3 = new MessageThread("3rd", Thread.MIN_PRIORITY);	// 1로 가장 낮은 우선순위

		tr1.start();		tr2.start();		tr3.start();
		// 우선순위를 지정해도 정확하게 높은 우선순위의 쓰레드가 먼저 실행되는 것은 아님
		// 순서가 중요한 프로그램에서는 쓰레드를 사용하면 안됨

	}
}
