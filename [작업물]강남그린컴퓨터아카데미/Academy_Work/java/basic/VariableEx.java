class VariableEx {
	public static void main(String[] args) {
		int num1 = 10; // int형 변수 num1에 10을 저장
		int num2 = 20, num3 = num1 + num2;
		// 같은 자료형의 변수일 경우 두 개 이상을 동시에 선언(생성)할 수 있음(초기화도 가능)

		char c1 = 'a';	// char형이므로 작은 따옴표로 문자를 묶어서 사용
		char c2 = 65;	// char형에는 byte와 short형 정수가 저장될 수 있음
		// 단, 정수가 저장될 경우 char형이므로 들어간 정수에 해당하는 유니코드의 문자로 변환됨

		System.out.println("num1 : " + num1 + ", num2 : " +num2 + ", num3 : " + num3);
		// num1 : 10, num2 : 20, num3 : 30
		System.out.println("c1 : " + c1 + ", c2 : " + c2);
		// c1 : a, c2 : A

		long num4 = 100000000000L;
		// 정수형 리터럴값을 사용시 int의 범위를 벗어나는 경우 접미사로 엘(L)을 붙여서 long 형이라는 표시를 해줘야 함
		// 접미사에서 사용된 엘을 대소문자 구분이 없으나 보통 대문자로 사용함
		System.out.println("num4 : " + num4 );

		double d1 = 3.14;	// 실수의 기본형은 double이므로 잘 들어감
		float f1 = 3.14f;	// double이 float의 두 배크기이므로 float값을 사용하려면 접미사 에프(F)를 붙여야 함
		System.out.println("d1 : " + d1 + ", f1 : " + f1);	// d1 : 3.14, f1 : 3.14
	}
}
