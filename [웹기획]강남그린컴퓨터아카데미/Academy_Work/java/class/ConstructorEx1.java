class NumberTest {
	int num;
	public NumberTest() {
		num = 10;
		System.out.println("생성자 호출!!");
	}
	/*
	public NumberTest() { }
	// 디폴트 생성자 : 생성자가 없을 시 JVM이 자동으로 마들어주는 생성자로 인스턴스 생성 작업만 처리
	*/
	public int getNumber() {
		return num;
	}
}
class  ConstructorEx1 {
	public static void main(String[] args) 
	{
		NumberTest num1 = new NumberTest();
		// NumberTest 형 인스턴스 num1을 선언 및 생성( () → 생성자 호출) 
		System.out.println(num1.getNumber());
		
		NumberTest num2 = new NumberTest();
		System.out.println(num2.getNumber());
	}
}
