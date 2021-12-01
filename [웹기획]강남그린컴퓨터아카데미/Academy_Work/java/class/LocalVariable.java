class LocalVariable {
	public static void main(String[] args) {
		boolean scope = true;
		if (scope) {
			int num = 1;
			num++;
			System.out.println("num : " + num);
		} else {
			int num = 1;
			System.out.println("num : " + num);
		}
		testMethod();
	} 

	public static void testMethod() {
		int num = 3; 
		System.out.println("num : " + num);
	}
}
