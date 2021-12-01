class MessageThread extends Thread {
	String message;
	public MessageThread(String str) { message = str; }
	public void run() { 
		for (int i = 0; i < 10 ; i++ ) {
			System.out.println(message + "(" + getPriority() + ")");
			// getPriority() : Thread클래스의 메소드로 해당 쓰레드의 우선 순위를 리턴함(기본값은 5)
		}
	}
}
class PriorityTest1 {
	public static void main(String[] args) {
		MessageThread tr1 = new MessageThread("1st");
		MessageThread tr2 = new MessageThread("2nd");
		MessageThread tr3 = new MessageThread("3rd");

		tr1.start();		tr2.start();		tr3.start();
		// 쓰레드 호출 순서와는 무관하게 실행됨
	}
}
