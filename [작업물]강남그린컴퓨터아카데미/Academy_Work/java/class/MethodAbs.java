class MethodAbs {
	public static void main(String[] args) {
		// �� ���� ������ ���޹޾� �� ���� ���� ���밪���� ������ִ� �޼ҵ� �ۼ�
		printAbs(10, 5);
		printAbs(10, 25);
	}
	
	public static void printAbs(int a, int b) {
		System.out.println(a + "�� " + b + "�� ���� " + ((a-b) > 0 ? (a-b) : -(a-b)));
	}
}
