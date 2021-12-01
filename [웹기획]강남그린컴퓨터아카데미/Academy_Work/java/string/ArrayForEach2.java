class NumberEx {
	public int num ;  // 편의상 public !!
	public NumberEx(int num) { this.num = num; }
	public int getnum() { return num; }

}
class ArrayForEach2 {
	public static void main(String[] args) {
		NumberEx[] arr = { new NumberEx(2), new NumberEx(4), new NumberEx(8) };
		// NumberEx형 배열 arr을 선언하고 NumberEx형 인스턴스로 초기화
		
		int[] arr2 = { 2, 4, 8 };

		int a = 2, b = 4, c = 8;
		int[] arr3 = { a, b, c };

		System.out.println("수정전");
		System.out.print("인스턴스 사용 : ");
		for ( NumberEx e : arr ) { System.out.print(e.getnum() + "  "); }	// 인스턴스 사용 : 2  4  8
		System.out.print("\nint 사용 : ");
		for ( int e : arr2 ) { System.out.print(e + "  "); }				// int 사용 : 2  4  8
		System.out.print("\nint 변수 사용 : ");
		for ( int e : arr3 ) { System.out.print(e + "  "); }				// int 변수 사용 : 2  4  8

		for ( NumberEx e : arr ) { e.num++; }
		// arr배열에 들어있는 NumberEx형 인스턴스들을 차례대로 e에 담아서 작업
		// for-each를 이용하여 배열의 값을 변경했을 때 그 값이 인스턴스이면 실제 값이 변함

		for ( int e : arr2 ) { e++; }
		// arr2배열에 들어있는 int형 인스턴스들을 차례대로 e에 담아서 작업
		// for-each를 이용하여 배열의 값을 변경했을 때 그 값이 기본 자료형이면 실제 값이 변하지 않음

		for ( int e : arr3 ) { e++; }
		// arr2배열에 들어있는 int형 인스턴스들을 차례대로 e에 담아서 작업
		// for-each를 이용하여 배열의 값을 변경했을 때 그 값이 기본 자료형이면 실제 값이 변하지 않음
		System.out.println();
 
		System.out.println("수정후");
		System.out.print("인스턴스 사용 : ");
		for ( NumberEx e : arr ) { System.out.print(e.getnum() + "  "); }	// 인스턴스 사용 : 3  5  9
		System.out.print("\nint 사용 : ");
		for ( int e : arr2 ) { System.out.print(e + "  "); }				// int 사용 : 2  4  8
		System.out.print("\nint 변수 사용 : ");
		for ( int e : arr3 ) { System.out.print(e + "  "); }				// int 변수 사용 : 2  4  8

		System.out.print("\n인스턴스삽입 후: ");
		for (NumberEx e : arr) { 
			e = new NumberEx(7); 
			// e에 새로운 인스턴스를 저장하면 원본을 변경할 수는 없음
			System.out.print(e.getnum() + "  ");							// 인스턴스삽입 후: 7  7  7
		}
		System.out.print("\n인스턴스삽입 후 원본 :  ");
		for (NumberEx e : arr ) { System.out.print(e.getnum() + "  "); }	// 인스턴스삽입 후 원본 :  3  5  9
	}
}