class FinallyTest {
	public static void main(String[] args) {
		boolean divOK = divider(4, 2);
		if (divOK)	System.out.println("연산 성공");
		else		System.out.println("연산 실패");

		divOK = divider(4, 0);
		if (divOK)	System.out.println("연산 성공");
		else		System.out.println("연산 실패");
	} 


	
	public static boolean divider(int n1, int n2) { 
		try { 
			int tmp = n1 / n2;
			System.out.println("나눗셈 결과 : " + tmp);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			// 예외 발생여부와 상관없이 항상 실행되는 영역
			System.out.println("finally 영역 실행");
		}
	}
}
