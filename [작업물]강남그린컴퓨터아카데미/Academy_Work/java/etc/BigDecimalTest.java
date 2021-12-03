import java.math.*;

class BigDecimalTest {
	public static void main(String[] args) {

		double d1 = 1.6, d2 = 0.1;
		System.out.println("�� �Ǽ��� ���� : " + (d1 + d2)); // 1.7000000000000002
		System.out.println("�� �Ǽ��� ���� : " + (d1 * d2)); // 0.16000000000000003
		System.out.println();

		BigDecimal d3 = new BigDecimal(1.6);
		BigDecimal d4 = new BigDecimal(0.1);
		System.out.println("�� �Ǽ��� ���� : " + d3.add(d4)); // 1.70000000000000009436895709313830593...
		System.out.println("�� �Ǽ��� ���� : " + d3.multiply(d4)); // 0.160000000000000017763568394002...
		System.out.println();
		// double�� �Ǽ��� ����� �� �Ҽ��� �Ʒ��� ���������ʴ� ���ڵ��� ������ ��찡 �ִ�.
		// �̷� ��츦 ����ؼ� BigDecimal�̶�� Ŭ������ �̿��Ѵ�.

		BigDecimal d5 = new BigDecimal("1.6");
		BigDecimal d6 = new BigDecimal("0.1");
		// BigDecimal�ν��Ͻ� ������ �ݵ�� ���ڿ��� �Ǽ��� �Է��ؾ� ��
		System.out.println("�� �Ǽ��� ���� : " + d5.add(d6)); // 1.7
		System.out.println("�� �Ǽ��� ���� : " + d5.multiply(d6)); // 0.16

		d3 = new BigDecimal(1.4).abs();
		d4 = new BigDecimal(0.5).abs();

		System.out.println("d3, d4 �� �Ǽ��� ���� : " + d3.subtract(d4));
	}

}