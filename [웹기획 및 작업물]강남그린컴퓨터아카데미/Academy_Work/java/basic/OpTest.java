class OpTest {
	public static void main(String[] args) {
		/*
		A = ((25+5)+(36/4)-72)*5, B = ((25*5)+(36-4)+71)/4, C = (128/4)*2 �� ��
		A > B > C �̸� true�� �ƴϸ� false�� ���
		*/

		int A = ((25+5)+(36/4)-72)*5;
		int B = ((25*5)+(36-4)+71)/4;
		int C = (128/4)*2;
		boolean result = (A > B) && (B > C); 

		System.out.println("result = " + result );			// result = false
		System.out.println("A = " + A + ", B = " + B + ", C = " + C );	// n1 = 10, n2 = 0
	}
}
