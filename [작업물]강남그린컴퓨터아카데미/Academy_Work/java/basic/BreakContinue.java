class BreakContinue {
	public static void main(String[] args) {
		
		System.out.println("100 ������ �ڿ������� 5�� 7�� �ּҰ������ ���Ͽ� ��� - while �� ���" ); 
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
			System.out.println("100 ������ �ڿ������� 5�� 7�� �ּҰ������ ���Ͽ� ��� : " + num);
		} else {
			System.out.println("100 ������ �ڿ������� 5�� 7�� �ּҰ������ ã�� ���߽��ϴ�.");
		}

		System.out.println("------------------------------------------------------------");


		System.out.println("100 ������ ����� 5�� 7�� ������� ������ ���" ); 
		// 100 ������ ����� 5�� 7�� ������� ������ ���
		num = 0;			// 100���� ������ ���� ���� ������ ����
		int count = 0;		// 5�� 7�� ����� ������ ������ ����
		while (num <= 100) {
			num++;

			if (num % 5 != 0 || num % 7 != 0) {	// 5�� 7�� ������� �ƴϸ�
				continue;
			}

			count++;	// ������� ���� ����
			System.out.println(num);	// ����� ���
		}
		System.out.println("count : " + count);
		System.out.println("------------------------------------------------------------");


		System.out.println("43�� 99�� �ּҰ������ ���Ͽ� ���" ); 
		num = 1;	// ������ ���� ���� ������ ����

		while (true) {	// ������ ��� ���̹Ƿ� ���ѷ���
			// ���ѷ��� ���� �ݵ�� ���ѷ����� �������� ������ ����� ���� ��
			if (num % 43 == 0 && num % 99 == 0) {
				break;
			}
			num++;
		}
		System.out.println(num);	
		System.out.println("------------------------------------------------------------");

		/*
		1���� �����ؼ� ��� Ȧ���� ¦�� �� 3�� ����� ���� ����
		�� ���� �󸶸� ������ �� 1000�� �Ѵ���, �׸��� 1000�� ���� ���� ������ ���  - while, ���ѷ��� �̿�
		*/
		System.out.println("1���� �����ؼ� ��� Ȧ���� ¦�� �� 3�� ����� ���� ����");
		System.out.println("�� ���� �󸶸� ������ �� 1000�� �Ѵ���, �׸��� 1000�� ���� ���� ������ ���  - while, ���ѷ��� �̿�" );
 
		num = 1; 
		int sum = 0;
		while (true) { 
			if (num % 2 == 1 || num % 3 == 0) {
				sum += num;
			} 
			
			if (sum > 1000) {
				System.out.println(num + " �� ���� �� 1000�� ����");	
				System.out.println("1000�� ���� �� : " + sum);	
				break;
			}

			num++;
		} 
	}
}
