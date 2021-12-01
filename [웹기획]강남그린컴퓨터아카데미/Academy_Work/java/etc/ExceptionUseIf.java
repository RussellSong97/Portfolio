import java.util.*;

class ExceptionUseIf {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int[] arr = new int[100];

		for (int i = 0 ; i < 3 ; i++ ) {
			System.out.print("나누어 지는 수 : ");
			int n1 = sc.nextInt();

			System.out.print("나누는 수 : ");
			int n2 = sc.nextInt();

			if (n2 == 0) {
				System.out.println("나누는 수는 0이 될 수 없습니다. ");
				i--;
				continue;
			}

			System.out.print("결과를 저장할 인덱스 : ");
			int idx = sc.nextInt();

			if (idx < 0 || idx >= 100) {
				System.out.println("유효하지 않은 인덱스 번호입니다.");
				i--;
				continue;
			}

			arr[idx] = n1 / n2;
			System.out.println("나눗셈 결과 : " + arr[idx]);
			System.out.println("저장된 위치 : " + idx);
			System.out.println();
		}
	} 
}
