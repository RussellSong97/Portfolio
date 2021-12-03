class BoxingUnBoxing {
	public static void main(String[] args) {

		Integer iValue = new Integer(10);
		Double dValue = new Double(3.14);
		// 기본자료형 데이터를 Wrapper클래스인 Integer와 Double의 인스턴스로 생성 - Boxing

		System.out.println("iValue : " + iValue); // 10
		System.out.println("dValue : " + dValue); // 3.14

		iValue = new Integer(iValue.intValue() + 10);
		dValue = new Double(dValue.doubleValue() + 1.2);
		System.out.println("iValue : " + iValue); // 20
		System.out.println("dValue : " + dValue); // 4.34
		System.out.println();

		int num1 = iValue.intValue();
		double num2 = dValue.doubleValue();
		// Wrapper클래스의 메소드를 이용하여 인스턴스를 기본자료형 데이터로 변환

		System.out.println("num1 : " + num1); // 20
		System.out.println("num2 : " + num2); // 4.34
	}
}