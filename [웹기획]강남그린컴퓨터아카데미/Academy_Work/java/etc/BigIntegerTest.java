import java.math.*;

class BigIntegerTest {
	public static void main(String[] args) {

		System.out.println("기본자료형 최대 정수 : " + Long.MAX_VALUE);
		System.out.println("기본자료형 최소 정수 : " + Long.MIN_VALUE);

		BigInteger big1 = new BigInteger("100000000000000000000");
		BigInteger big2 = new BigInteger("-99999999999999999999");

		BigInteger addResult = big1.add(big2);
		BigInteger mulResult = big1.multiply(big2);

		System.out.println("BigInteger 덧셈 결과 : " + addResult);
		System.out.println("BigInteger 곱셈 결과 : " + mulResult);

	}
}