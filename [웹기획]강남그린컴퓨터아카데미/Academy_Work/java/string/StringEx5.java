class StringEx5 {
	public static void main(String[] args) {
		/*
		1~1000 사이의 정수 중 8이 들어있는 숫자의 개수를 출력
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
		System.out.println("1~1000 사이의 정수 중 8이 들어있는 숫자의 개수 : " + cnt); 

	}
}
