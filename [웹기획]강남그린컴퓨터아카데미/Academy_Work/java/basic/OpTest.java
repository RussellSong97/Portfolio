class OpTest {
	public static void main(String[] args) {
		/*
		A = ((25+5)+(36/4)-72)*5, B = ((25*5)+(36-4)+71)/4, C = (128/4)*2 일 때
		A > B > C 이면 true를 아니면 false를 출력
		*/

		int A = ((25+5)+(36/4)-72)*5;
		int B = ((25*5)+(36-4)+71)/4;
		int C = (128/4)*2;
		boolean result = (A > B) && (B > C); 

		System.out.println("result = " + result );			// result = false
		System.out.println("A = " + A + ", B = " + B + ", C = " + C );	// n1 = 10, n2 = 0
	}
}
