class NumberEx {
	int num = 0;	// 멤버변수로 NumberEx클래스내에서 자유롭게 사용가능
	public void addNum(int n) {
		num += n;
	}
	public int getNum() {
		return num;
	}
}

class FirstInstance {
	public static void main(String[] args) {
		NumberEx num = new NumberEx();		
		// NumberEx num = NumberEx 형 인스턴스 num을 선언
		// new NumberEx() : 선언했던 인스턴스 num을 메모리상에서 생성
		// 인수로 인스턴스를 가져갈 경우 인스턴스 자체를 가져감
		System.out.println("메소드 호출 전 : " + num.getNum());	// 0
		// num.getNum() : num 인스턴스 내의 getNum() 메소드를 호출

		numMethod(num);	// 인스턴스도 매개변수로 가져갈 수 있음
		// numMethod() 메소드를 호출하면 인스턴스 num을 인수로 가져감
		System.out.println("메소드 호출 후 : " + num.getNum());	// 12

		int test = 0;
		numMethod2(test);
		// numMethod2() 메소드를 호출하면 변수 test를 인수로 가져감
		// 인수로 변수를 가져갈 경우 변수의 값만을 가져감
		System.out.println("test : " + test);	// 0
	}
	public static void numMethod(NumberEx n) {
		n.addNum(12);
		// 인수로 받아온 인스턴스 n 내의 getNum() 메소드를 호출
	}
	public static void numMethod2(int n) {
		n += 12;
	}
}
