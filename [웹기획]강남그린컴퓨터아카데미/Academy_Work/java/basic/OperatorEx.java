class OperatorEx {
	public static void main(String[] args) {
		int n1 = 7, n2 = 3, n3;
		System.out.println("7 / 3 = " + n1 / n2);	// 7 / 3 =2
		System.out.println("7 % 3 = " + n1 % n2);	// 7 % 3 =1

		double d1 = 3.0;
		System.out.println("7 / 3.0 = " + n1 / d1);	// 7 / 3 = 2.3333333
		System.out.println("7 % 3.0 = " + n1 % d1);	// 7 % 3 = 1.0
		// 산술연산에서 피연산자 중 하나라도 실수일 경우 결과값은 실수로 나옴

		n1 = 5;		n2 = ++n1;		n3 = --n1;  // prefix 방식으로 증감 후 사용
		System.out.println("n1 = " + n1 + ", n2 = " + n2 + ", n3 = " + n3);	// n1 = 5, n2 = 6, n3 = 5
		
		n1 = 5;		n2 = n1++;		n3 = n1--;  // prefix 방식으로 사용 후 증감
		System.out.println("n1 = " + n1 + ", n2 = " + n2 + ", n3 = " + n3);	// n1 = 5, n2 = 5, n3 = 6

		n1 = 0;		n2 = 0;
		boolean result;

		result = (n1 += 10) < 0 && (n2 += 10) > 0;
		// && 연산이므로 앞의 조건이 false이면 뒤의 조건에 상관없이 결과가 false이므로 뒷쪽 조건은 실행하지 않고 넘어감
		System.out.println("result = " + result );			// result = false
		System.out.println("n1 = " + n1 + ", n2 = " + n2 );	// n1 = 10, n2 = 0

		result = (n1 += 10) > 0 || (n2 += 10) > 0;
		// || 연산이므로 앞의 조건이 true이면 뒤의 조건에 상관없이 결과가 true이므로 뒷쪽 조건은 실행하지 않고 넘어감
		System.out.println("result = " + result );			// result = true
		System.out.println("n1 = " + n1 + ", n2 = " + n2 );	// n1 = 20, n2 = 0

		result = (n1 += 10) < 0 || (n2 += 10) > 0; 
		System.out.println("result = " + result );			// result = true
		System.out.println("n1 = " + n1 + ", n2 = " + n2 );	// n1 = 20, n2 = 10

		// 논리식 내부에서는 값이 변경되는 수식을 사용하면 상황에 따라 수식이 동작되지 않고 넘어갈 수도 있음

	}
}
