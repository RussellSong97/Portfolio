class Aa {
	public String toString() { return "class Aa"; }
}

class Bb {
	public String toString() { return "class Bb"; }
}

class InstanceTypeShow2 {
	public <T, U> void showInstType(T inst1, U inst2) {
	// <T, U> : 현 메소드의 매개변수 자료형을 제네릭으로 지정하는 데 두 개를 순서대로 입력하게 함
	// 제네릭 메소드 내에서는 제네릭으로 선언된 참조변수를 통해서 사용할 수 있는 메소드는 Object클래스의 메소드들 밖에 없음
		System.out.println(inst1);
		System.out.println(inst2);
	} 
}
class GenericMethod2 {
	public static void main(String[] args) {
		Aa a = new Aa();		Bb b = new Bb();
		InstanceTypeShow2 show = new InstanceTypeShow2();

		show.<Aa, Bb>showInstType(a, b);	
		// showInstType() 메소드의 인수로 사용할 인스턴스의 자료형을 각각 Aa와 Bb로 지정한 후 호출
		show.showInstType(a, b);
		// 메소드의 제네릭에서는 인수의 자료형을 컴파일러가 파악하여 지정하지 않아도 됨
	}
}
