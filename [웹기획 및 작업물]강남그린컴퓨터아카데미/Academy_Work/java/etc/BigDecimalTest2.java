/*
두 개의 실수를 입력받아 두 실수의 차를 절대값으로 계산하여 출력 : 단, 오차가 존재하면 안됨
*/

import java.util.*;
import java.math.*;

class BigDecimalTest2 {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("실수1 입력: " );
		String num1 = sc.nextLine();
		System.out.println("실수2 입력: " );
		String num2 = sc.nextLine();

		BigDecimal n1 = new BigDecimal(num1);
		BigDecimal n2 = new BigDecimal(num2);

		BigDecimal result = n1.subtract(n2);
		System.out.println("두 실수의 뺄셈 : " + result.abs() );
		System.out.println();
		

//		double d1 = 1.4, d2 = 0.5;
//		BigDecimal d3 = new BigDecimal("1.4");
//		BigDecimal d4 = new BigDecimal("0.5");
//		System.out.println("두 실수의 뺄셈 : " + d3.subtract(d4).abs() );

	}
}
