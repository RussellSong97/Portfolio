class LoopTest {
	public static void main(String[] args) {
		// 1~100������ ���� ���Ͽ� ���
		int result = 0;
		for (int i = 1; i <= 100 ; i++) {
			result += i;
		}
		System.out.println("1~100������ �� : " + result);

		// 100 ������ ����� �� 2�� 7�� ������� ���� ���
		result = 0 ;
		for (int i = 1; i <= 100 ; i++) {
			if ((i % 2 == 0) && (i % 7 == 0)) {
				result += i;
			}
		}
		System.out.println("1~100 ������ ����� �� 2�� 7�� ����� : " + result);

	}
}
