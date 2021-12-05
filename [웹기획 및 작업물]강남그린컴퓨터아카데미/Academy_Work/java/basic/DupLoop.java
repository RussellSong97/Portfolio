class DupLoop {
	public static void main(String[] args) {
		for (int i = 0; i < 3 ; i++ ) {
			System.out.println("변수 i : " + i );
			for (int j = 0; j < 3 ; j++ ) {
				System.out.println(j + " " );
			}
			System.out.println();
		}

		
		System.out.println("=========================================");

		/*
		변수 i : 0
		0  1  2
		변수 i :1 
		0  1  2
		변수 i : 2
		0  1  2
		(while문으로)
		*/
		int i = 0;
		int j = 0;
		while( i < 3 ) {
			
			System.out.println("변수 i : " + i );
			while (j < 3) {
				System.out.println(j + " " );
				j++;
			}
			j = 0;

			i++;
		}
	}
}
