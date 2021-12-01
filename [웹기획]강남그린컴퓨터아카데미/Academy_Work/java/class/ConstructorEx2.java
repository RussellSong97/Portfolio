class NumberTest {
	int num;
	public NumberTest(int n) {	// 생성자
		num = n;
		System.out.println("전달된 인수의 값 : " + n);
	}

	public int getNumber() {
		return num;
	}
}
class  ConstructorEx2 {
	public static void main(String[] args) 
	{
		NumberTest num1 = new NumberTest(10);
		// NumberTest 형 인스턴스 num1을 선언 및 생성( () → 생성자 호출) 
		System.out.println("메소드 반환값 : " + num1.getNumber());
		
		NumberTest num2 = new NumberTest(20);
		System.out.println("메소드 반환값 : " + num2.getNumber());
	}
}
