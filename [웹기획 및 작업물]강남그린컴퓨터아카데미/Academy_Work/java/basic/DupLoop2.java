class DupLoop2 {
	public static void main(String[] args) {
		/*
		2 x 1 = 2		3 x 1 = 3	...		9 x 1 = 9
		2 x 2 = 2		3 x 2 = 3	...		9 x 2 = 9
		...				...					...
		2 x 9 = 2		3 x 9 = 27	...		9 x 9 = 81 
		�״�� ������ �� : ���� �Է��ϴ� �κ� : " X " + " = " 
		������ ������ �� : 2~9�� ��, 1~9�� ���� ��
		������� ������ �� : ���ϱ��� ���
		*/

		for (int i = 1; i < 10 ; i++ )  {
			for (int j = 2; j < 10 ; j++ ) {
				System.out.print(j + " X " + i + " = " + (i * j) + ((i * j) < 10  ? "   " :  "  "));
			}
			System.out.println();
		}
		

	}
}
