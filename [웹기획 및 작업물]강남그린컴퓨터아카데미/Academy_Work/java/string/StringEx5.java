class StringEx5 {
	public static void main(String[] args) {
		/*
		1~1000 ������ ���� �� 8�� ����ִ� ������ ������ ���
		*/
		
		int cnt = 0;
		String keyword = "8";
		for (int i = 1; i <= 1000 ; i++ ) {
			if ((i + "").indexOf(keyword) > -1) {
				System.out.print(i + "    "); 
				cnt++;
			}
		}
		System.out.println();
		System.out.println("1~1000 ������ ���� �� 8�� ����ִ� ������ ���� : " + cnt); 

	}
}
