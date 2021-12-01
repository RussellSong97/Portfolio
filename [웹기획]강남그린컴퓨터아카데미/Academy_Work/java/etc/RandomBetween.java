/*
사용자에게 최소정수와 최대정수를 받아 그 사이의 난수를 10개 출력 - 최소값과 최대값도 포함되어야 함
*/

import java.util.*;

class RandomBetween {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("최소값 입력 : " );		int num1 = sc.nextInt();
		System.out.print("최대값 입력 : " );		int num2 = sc.nextInt();

		Random rnd = new Random();
		for (int i = 0; i < 10; i++ ){
			int val = rnd.nextInt(num2 - num1 + 1);
			val += num1;
			System.out.print(val + " ");
		}
	}
} 