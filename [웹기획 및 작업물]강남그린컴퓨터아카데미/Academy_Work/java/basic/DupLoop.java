class DupLoop {
	public static void main(String[] args) {
		for (int i = 0; i < 3 ; i++ ) {
			System.out.println("���� i : " + i );
			for (int j = 0; j < 3 ; j++ ) {
				System.out.println(j + " " );
			}
			System.out.println();
		}

		
		System.out.println("=========================================");

		/*
		���� i : 0
		0  1  2
		���� i :1 
		0  1  2
		���� i : 2
		0  1  2
		(while������)
		*/
		int i = 0;
		int j = 0;
		while( i < 3 ) {
			
			System.out.println("���� i : " + i );
			while (j < 3) {
				System.out.println(j + " " );
				j++;
			}
			j = 0;

			i++;
		}
	}
}
