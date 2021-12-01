class ArrayForEach {
	public static void main(String[] args) {
		int[] arr = {1, 2, 3, 4, 5};
		int sum = 0;
		for (int e : arr) {
			sum += e;
		}
		System.out.println("배열 요소들의 합 : " + sum);

		sum = 0;

		for (int i = 0; i < arr.length ; i++ ) {
			sum += arr[i];
		}
		System.out.println("배열 요소들의 합 : " + sum);

		for (int e : arr) {
			// arr 배열의 데이터들을 하나씩 차례로 변수 e에 담아 배열의 긑까지 루프를 돌면서 작업함
			e += 1;
			System.out.print(e + " " );	// 2 3 4 5 6
		}
		System.out.println();
		for (int e : arr) { System.out.print(e + " "); }	// 1 2 3 4 5
		System.out.println();

		for (int i = 0; i < arr.length ; i++ ) {
			arr[i] += 1;
			System.out.print(arr[i] + " ");	// 2 3 4 5 6
		}
		System.out.println();
		for (int i = 0; i < arr.length ; i++ ) { System.out.print(arr[i] + " "); } // 2 3 4 5 6
	}
}
