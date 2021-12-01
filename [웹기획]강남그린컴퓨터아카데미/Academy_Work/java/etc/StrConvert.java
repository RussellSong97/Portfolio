class StrConvert {
	public static void main(String[] args) {
		Integer n1 = new Integer("123");
		Double n2 = new Double("3.14");
		// Wrapper클래스를 이용하면 문자열을 기본 자료형으로 변환할 수 있음

		n1 += 10;	n2 += 1.2;

		System.out.println("n1 : " + n1);	// 133
		System.out.println("n2 : " + n2);	// 4.34

		Integer n3 = Integer.valueOf("123");
		Double n4 = Double.valueOf("3.14");
		// 모든 Wrapper클래스에 있는 valueOf() static 메소드를 이용하여 기본 자료형 인스턴스로 변환
		n3 -= 10;		n4 -= 1.2;
		System.out.println("n3 : " + n3);	// 113
		System.out.println("n4 : " + n4);	// 1.9400000000000002

		int n5 = Integer.parseInt("123");
		double n6 = Double.parseDouble("3.14");
		// 모든 Wrapper클래스에 있는 parse자료형() static 메소드를 이용하여 기본 자료형 데이터로 변환
		n5 *= 2;		n6 *= 2.0;
		System.out.println("n5 : " + n5);	// 246
		System.out.println("n6 : " + n6);	// 6.28

	}
}
