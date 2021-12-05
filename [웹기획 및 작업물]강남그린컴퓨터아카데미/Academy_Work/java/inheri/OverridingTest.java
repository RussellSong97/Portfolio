class Aa {
	public void test1() { System.out.println("Aa's test1()"); }
	public void test2() { System.out.println("Aa's test2()"); }
}

class Bb extends Aa {
	public void test1() { System.out.println("Bb's test1()"); }
	public void test2() { System.out.println("Bb's test2()"); }
}

class Cc extends Bb {
	public void test2() { System.out.println("Cc's test1()"); }
}


class OverridingTest {
	public static void main(String[] args) {
		Bb b = new Bb();	Cc c = new Cc();

		b.test1();		b.test2();
		c.test1();		c.test2();
		// 오버라이딩된 메소드는 항상 가장 나중에 오버라이딩된 메소드가 실행됨
	}
}
