class MethodPrimeNumber {
	public static void main(String[] args) {
		/*
		1~100 ������ ������ �� �Ҽ��� ���Ͽ� ���
		��, ���޵� ���� �Ҽ����� ���θ� �Ǵ��� �ִ� �޼ҵ带 �����Ͽ� �۾�
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
