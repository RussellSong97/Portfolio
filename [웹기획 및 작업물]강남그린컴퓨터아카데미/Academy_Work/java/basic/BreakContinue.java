class BreakContinue {
	public static void main(String[] args) {
		
		System.out.println("100 이하의 자연수에서 5와 7의 최소공배수를 구하여 출력 - while 문 사용" ); 
		int num = 1 ;
		boolean isSearch = false;
		while(num < 100 ) {
			if ( num % 5 == 0 && num % 7 == 0 ) {
				isSearch = true;
				break;
			}

			num++;
		}

		if (isSearch) {
			System.out.println("100 이하의 자연수에서 5와 7의 최소공배수를 구하여 출력 : " + num);
		} else {
			System.out.println("100 이하의 자연수에서 5와 7의 최소공배수를 찾지 못했습니다.");
		}

		System.out.println("------------------------------------------------------------");


		System.out.println("100 이하의 양수중 5와 7의 공배수와 개수를 출력" ); 
		// 100 이하의 양수중 5와 7의 공배수와 개수를 출력
		num = 0;			// 100까지 루프를 돌릴 값을 저장할 변수
		int count = 0;		// 5와 7의 공배수 개수를 저장할 변수
		while (num <= 100) {
			num++;

			if (num % 5 != 0 || num % 7 != 0) {	// 5와 7의 공배수가 아니면
				continue;
			}

			count++;	// 공배수의 개수 증가
			System.out.println(num);	// 공배수 출력
		}
		System.out.println("count : " + count);
		System.out.println("------------------------------------------------------------");


		System.out.println("43과 99의 최소공배수를 구하여 출력" ); 
		num = 1;	// 루프를 돌릴 값을 저장할 변수

		while (true) {	// 조건이 계속 참이므로 무한루프
			// 무한루프 사용시 반드시 무한루프를 빠져나올 조건을 만들어 놔야 함
			if (num % 43 == 0 && num % 99 == 0) {
				break;
			}
			num++;
		}
		System.out.println(num);	
		System.out.println("------------------------------------------------------------");

		/*
		1부터 시작해서 모든 홀수와 짝수 중 3의 배수인 수를 더함
		그 합이 얼마를 더했을 때 1000이 넘는지, 그리고 1000을 넘은 값은 얼만지를 출력  - while, 무한루프 이용
		*/
		System.out.println("1부터 시작해서 모든 홀수와 짝수 중 3의 배수인 수를 더함");
		System.out.println("그 합이 얼마를 더했을 때 1000이 넘는지, 그리고 1000을 넘은 값은 얼만지를 출력  - while, 무한루프 이용" );
 
		num = 1; 
		int sum = 0;
		while (true) { 
			if (num % 2 == 1 || num % 3 == 0) {
				sum += num;
			} 
			
			if (sum > 1000) {
				System.out.println(num + " 을 더할 때 1000을 넘음");	
				System.out.println("1000을 넘은 값 : " + sum);	
				break;
			}

			num++;
		} 
	}
}
