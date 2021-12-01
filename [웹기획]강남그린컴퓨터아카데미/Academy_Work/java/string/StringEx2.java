class StringEx2 {
	public static void main(String[] args) {
		String str1 = "test@abc.com";
		String str2 = "testabc.com";
		String str3 = "test@abccom";
		String str4 = "test.abc@com";

		emailTest(str1);
		emailTest(str2);
		emailTest(str3);
		emailTest(str4);
	}

	public static void emailTest(String str) {	// 메일주소를 받아 검사한 후 결과를 출력하는 메소드
	/* 검사조건
		1. 반드시 '@'가 있어야 함 
		2. 반드시 '.'이 있어야 함		
		3. '@'가 '.' 보다 앞에 있어야 함
		이메일이 맞으면 이메일주소 + "는 이메일 주소가 맞습니다."
		이메일이 맞으면 틀리면 + "는 이메일 주소가 아닙니다."
	*/
		int at = str.indexOf("@");
		int dot = str.indexOf(".");

		if (at == -1 || dot == -1 || at > dot)	System.out.println(str + "는 이메일 주소가 아닙니다.");
		else									System.out.println(str + "는 이메일 주소가 맞습니다.");

	}
}
