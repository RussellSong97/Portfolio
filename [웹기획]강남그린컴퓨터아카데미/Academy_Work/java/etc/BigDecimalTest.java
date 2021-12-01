import java.math.*;

class BigDecimalTest {
	public static void main(String[] args) {

		double d1 = 1.6, d2 = 0.1;
		System.out.println("두 실수의 덧셈 : " + (d1 + d2)); // 1.7000000000000002
		System.out.println("두 실수의 곱셈 : " + (d1 * d2)); // 0.16000000000000003
		System.out.println();

		BigDecimal d3 = new BigDecimal(1.6);
		BigDecimal d4 = new BigDecimal(0.1);
		System.out.println("두 실수의 덧셈 : " + d3.add(d4)); // 1.70000000000000009436895709313830593...
		System.out.println("두 실수의 곱셈 : " + d3.multiply(d4)); // 0.160000000000000017763568394002...
		System.out.println();
		// double형 실수를 계산할 때 소수점 아래로 정리되지않는 숫자들이 나오는 경우가 있다.
		// 이런 경우를 대비해서 BigDecimal이라는 클래스를 이용한다.

		BigDecimal d5 = new BigDecimal("1.6");
		BigDecimal d6 = new BigDecimal("0.1");
		// BigDecimal인스턴스 생성시 반드시 문자열로 실수를 입력해야 함
		System.out.println("두 실수의 덧셈 : " + d5.add(d6)); // 1.7
		System.out.println("두 실수의 곱셈 : " + d5.multiply(d6)); // 0.16

		d3 = new BigDecimal(1.4).abs();
		d4 = new BigDecimal(0.5).abs();

		System.out.println("d3, d4 두 실수의 뺄셈 : " + d3.subtract(d4));
	}

}