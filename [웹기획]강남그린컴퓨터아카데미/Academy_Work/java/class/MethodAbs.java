class MethodAbs {
	public static void main(String[] args) {
		// 두 개의 정수를 전달받아 두 수의 차를 절대값으로 출력해주는 메소드 작성
		printAbs(10, 5);
		printAbs(10, 25);
	}
	
	public static void printAbs(int a, int b) {
		System.out.println(a + "과 " + b + "의 차는 " + ((a-b) > 0 ? (a-b) : -(a-b)));
	}
}
