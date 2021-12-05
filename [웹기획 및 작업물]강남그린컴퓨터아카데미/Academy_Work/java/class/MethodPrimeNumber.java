class MethodPrimeNumber {
	public static void main(String[] args) {
		/*
		1~100 사이의 점수들 중 소수를 구하여 출력
		단, 전달된 값이 소수인지 여부를 판단해 주는 메소드를 제작하여 작업
		*/

		for (int i = 1; i < 100 ; i++){
			if (isPrime(i)) {
				System.out.println(i);
			}
		}
	}

	public static void isPrime(int num) {
		if (i == 1 || (i != 2 && i % 2 == 0)) {
			return false;
		}
		for (int i = 2; i < num / 2 ; i++) {
			if (n % i == 0)  return false; 
		}
	}
}
