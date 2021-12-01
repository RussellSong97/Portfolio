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

class BoundedTypeParam {
	public static void main(String[] args) {
		Aa a = new Aa();		Bb b = new Bb();

		showInstanceAncestor(a);
		showInstanceName(a);
		showInstanceAncestor(b);
		showInstanceName(b);
	}
	public static <T> void showInstanceAncestor(T param) {
		((SimpleInterface)param).showYourName();
		// 인터페이스도 클래스처럼 형변환이 가능하여 하위클래스의 메소드를 실행
	}
	public static <T> void showInstanceName(T param) {
		((UpperClass)param).showYourAncestor();
		// Object 클래스에 없는 메소드이지만 형변환을 통해 하위클래스의 메소드를 사용
	}
}
