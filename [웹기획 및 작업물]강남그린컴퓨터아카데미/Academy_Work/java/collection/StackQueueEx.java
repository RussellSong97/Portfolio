import java.util.*;

class StackQueueEx {
	public static void main(String[] args) {
		Stack st = new Stack();
		Queue q = new LinkedList();
		// Queue는 인터페이스이므로 Queue를 구현한 LinkedList를 통해 인스턴스 생성

		st.push("0");		st.push("1");		st.push("2");
		q.offer("0");		q.offer("1");		q.offer("2");

		System.out.println(" = Stack =");
		while (!st.empty()) {
			System.out.println(st.pop());
		}

		System.out.println(" = Queue =");
		while (!q.isEmpty()) {
			System.out.println(q.poll());
		}

	}
}
