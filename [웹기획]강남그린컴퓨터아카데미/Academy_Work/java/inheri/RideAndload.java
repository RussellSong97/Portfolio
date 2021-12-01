class Aa {
	public void testA() {System.out.println("Aa's test"); }			// 일반 메소드
	public void rideMethod() { System.out.println("Aa's Method"); }	// 오버라이딩      
	public void laodMethod() { System.out.println("Void Method"); }	// 오버로딩       
}

class Bb extends Aa {
	public void testB() {System.out.println("Bb's test"); }				// 일반 메소드
	public void rideMethod() { System.out.println("Bb's Method"); }		// 오버라이딩
	public void laodMethod(int n) { System.out.println("int Method"); }	// 오버로딩
}

class Cc extends Bb {	
	public void testC() {System.out.println("Cc's test"); }						// 일반 메소드
	public void rideMethod() { System.out.println("Cc's Method"); }				// 오버라이딩      
	public void laodMethod(double n) { System.out.println("double Method"); }	// 오버로딩       
}

class RideAndload {
	public static void main(String[] args) {
		Aa a = new Cc();
		Bb b = new Cc();
		Cc c = new Cc();
		// 상위클래스로 선언한 후 하위클래스의 인스턴스로 생성
		// JVM에서는 실제 인스턴스의 자료형 대신 선언한 자료형을 사용하기 때문에 상위클래스의 메소드들만 사용가능

		/*
		a.testA() : 실행가능
		a.testB() 와 a.testC() : 실행불가능(a가 Aa클래스형으로 인식되기 때문에 Aa클래스의 메소드만 실행가능)

		b.testA() 와 b.testB() : 실행가능
		b.testC() : 실행불가능(b가 Bb클래스형으로 인식되기 때문에 Bb클래스의 메소드와 Bb의 상위클래스인 Aa만 실행가능)
		*/
		

		a.rideMethod();
		b.rideMethod();
		c.rideMethod();
		// Cc's Method
		// 상위클래스로 선언한 후 하위클래스의 인스턴스로 생성한 인스턴스로는 상위클래스의 메소드에만 접근이 가능하나 
		// 오버라이딩된 메소드에는 예외적으로 최하위클래스의 오버라이딩된 메소드가 실행됨

		a.laodMethod();		// Void Method      
		b.laodMethod(1);	// int Method       
		c.laodMethod(1.2);	// double Method  
		// 오버로딩된 메소드들을 매개변수의 자료형과 개수로 구분하여 호출

	}
}
