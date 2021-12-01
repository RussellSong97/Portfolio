interface SimpleInterface {
	public void showYourName();
}

class UpperClass {
	public void showYourAncestor() {
		System.out.println("UpperClass");
	}
}

class Aa extends UpperClass implements SimpleInterface {
	public void showYourName() { System.out.println("class Aa"); }
}

class Bb extends UpperClass implements SimpleInterface {
	public void showYourName() { System.out.println("class Bb"); }
}

class BoundedTypeParam2 {
	public static void main(String[] args) {
		Aa a = new Aa();		Bb b = new Bb();

		showInstanceAncestor(a);
		showInstanceName(a);
		showInstanceAncestor(b);
		showInstanceName(b);
	}
	public static <T extends SimpleInterface> void showInstanceAncestor(T param) {
	// <T extends SimpleInterface> : SimpleInterface를 상속받는 인스턴스만 받겠다는 의미
	// 제네릭의 자료형 제한에서는 인터페이스도 extends 키워드를 사용할 수 있음
		(param).showYourName();
	}
	public static <T extends UpperClass> void showInstanceName(T param) {
	// <T extends UpperClass> : UpperClass를 상속받는 인스턴스만 받겠다는 의미
		(param).showYourAncestor();
	}
}
