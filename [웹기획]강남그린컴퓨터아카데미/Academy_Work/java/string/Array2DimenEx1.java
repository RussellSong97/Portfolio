class Array2DimenEx1 {
	public static void main(String[] args) {
		int[][] arr = new int[3][4];

		for (int i = 0; i < arr.length ; i++ ) {
			// arr.length : arr배열의 행수 - 3
			for (int j = 0; j < arr[i].length ; j++ ) {
				// arr[i].length : i인덱스에 해당하는 1차원 배열의 열수 - 4
				arr[i][j] = i + j ;
			}
		}
		
		for (int i = 0; i < arr.length ; i++ ) {
			for (int j = 0; j < arr[i].length ; j++ ) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}
}
