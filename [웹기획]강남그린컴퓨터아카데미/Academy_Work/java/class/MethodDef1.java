class MethodDef1 {
	public static void main(String[] args) {
		System.out.println("프로그램 시작");

		testMethod(33);
		testMethod(18);

		System.out.println("프로그램 종료");
	}

	public static void testMethod(int age) {
		System.out.println("제 나이는 " + age + "세입니다.");
	}
}
