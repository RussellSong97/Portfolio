import java.math.*;

class BigIntegerTest {
	public static void main(String[] args) {

		System.out.println("�⺻�ڷ��� �ִ� ���� : " + Long.MAX_VALUE);
		System.out.println("�⺻�ڷ��� �ּ� ���� : " + Long.MIN_VALUE);

		BigInteger big1 = new BigInteger("100000000000000000000");
		BigInteger big2 = new BigInteger("-99999999999999999999");

		BigInteger addResult = big1.add(big2);
		BigInteger mulResult = big1.multiply(big2);

		System.out.println("BigInteger ���� ��� : " + addResult);
		System.out.println("BigInteger ���� ��� : " + mulResult);

	}
}