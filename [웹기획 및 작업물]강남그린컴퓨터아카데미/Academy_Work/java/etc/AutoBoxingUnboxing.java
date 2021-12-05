class AutoBoxingUnBoxing {
	public static void main(String[] args) {

		Integer iValue = 10;
		Double dValue = 3.14;

		System.out.println("iValue : " + iValue); // 10
		System.out.println("dValue : " + dValue); // 3.14

		
		int n1 = iValue;
		double n2 = dValue;

		System.out.println("n1 : " + n1); // 20
		System.out.println("n2 : " + n2); // 4.34

	}
}