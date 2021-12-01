class ArrayEx1 {
	public static void main(String[] args) {
		int[] arrInt = new int[3];	// int형 데이터 3개를 저장할 수 있는 배열 arrInt를 선언 및 생성
		arrInt[0] = 10;		// arrInt배열의 0번 인덱스 칸에 10의 값을 저장
		arrInt[1] = 20;
		arrInt[2] = 30;

		int sum = arrInt[0] + arrInt[1] + arrInt[2];

		System.out.println("sum : " + sum);

		String[] arrStr = new String[3];
		arrStr[0] = "Java";
		arrStr[1] = "JSP";
		arrStr[2] = "Servlet";

		for (int i = 0; i < arrStr.length ; i++ ) {
			// length : 메소드가 아닌 배열이 가지는 속성(field)으로 배열의 크기를 의미
			System.out.println(arrStr[i]);
		}
	}
}
