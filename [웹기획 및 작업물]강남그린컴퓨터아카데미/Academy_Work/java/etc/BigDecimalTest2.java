/*
�� ���� �Ǽ��� �Է¹޾� �� �Ǽ��� ���� ���밪���� ����Ͽ� ��� : ��, ������ �����ϸ� �ȵ�
*/

import java.util.*;
import java.math.*;

class BigDecimalTest2 {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("�Ǽ�1 �Է�: " );
		String num1 = sc.nextLine();
		System.out.println("�Ǽ�2 �Է�: " );
		String num2 = sc.nextLine();

		BigDecimal n1 = new BigDecimal(num1);
		BigDecimal n2 = new BigDecimal(num2);

		BigDecimal result = n1.subtract(n2);
		System.out.println("�� �Ǽ��� ���� : " + result.abs() );
		System.out.println();
		

//		double d1 = 1.4, d2 = 0.5;
//		BigDecimal d3 = new BigDecimal("1.4");
//		BigDecimal d4 = new BigDecimal("0.5");
//		System.out.println("�� �Ǽ��� ���� : " + d3.subtract(d4).abs() );

	}
}
