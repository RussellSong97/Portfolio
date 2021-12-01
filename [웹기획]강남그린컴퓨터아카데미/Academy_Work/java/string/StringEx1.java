class StringEx1 {
	public static void main(String[] args) {

		String str1 = "test";	// "test" 인스턴스 생성
		String str2 = str1;		// "test"가 이미 있으므로 기존의 인스턴스를 사용
		String str3 = new String("test");	// 새로운 인스턴스 생성
		String str4 = new String("test");	// 새로운 인스턴스 생성
		
		if (str1 == str2)	System.out.println("동일 인스턴스 참조");	// O
		else				System.out.println("다른 인스턴스 참조");
		if (str2 == str3)	System.out.println("동일 인스턴스 참조");
		else				System.out.println("다른 인스턴스 참조");	// O
		if (str1 == str3)	System.out.println("동일 인스턴스 참조");
		else				System.out.println("다른 인스턴스 참조");	// O
		if (str3 == str4)	System.out.println("동일 인스턴스 참조");
		else				System.out.println("다른 인스턴스 참조");	// O

		// 인스턴스는 '=='로 비교할 경우 안에 들어있는 데이터가 아닌 참조하는 주소로 비교하므로 정확한 비교가 어려움
		// 그러므로 문자열의 비교는 compareTo()나 equals()로 비교해야 함
		if (str1.compareTo(str2) == 0)	System.out.println("동일한 문자열");	// O
		else							System.out.println("다른 문자열");
		if (str2.compareTo(str3) == 0)	System.out.println("동일한 문자열");	// O
		else							System.out.println("다른 문자열");
		if (str1.compareTo(str3) == 0)	System.out.println("동일한 문자열");	// O
		else							System.out.println("다른 문자열");
		if (str3.compareTo(str4) == 0)	System.out.println("동일한 문자열");	// O
		else							System.out.println("다른 문자열");
		
		if (str1.equals(str2))	System.out.println("동일한 문자열");	// O
		else					System.out.println("다른 문자열");
		if (str2.equals(str3))	System.out.println("동일한 문자열");	// O
		else					System.out.println("다른 문자열");
		if (str1.equals(str3))	System.out.println("동일한 문자열");	// O
		else					System.out.println("다른 문자열");
		if (str3.equals(str4))	System.out.println("동일한 문자열");	// O
		else					System.out.println("다른 문자열");

		str2 = "abcd";
		// 기존의 str2를 변경하는 것이 아닌 새로운 인스턴스를 생성하는 것임
		// 기존의 str2 데이터를 메모리 상에서 방치됨

		String s1 = "abc", s2 = "czc", s3 = "abc";
		System.out.println(s1.compareTo(s2));	// -2	(원본이 비교대상보다 사전적으로 2칸 앞에 있다는 의미)
		System.out.println(s1.compareTo(s3));	// 0	(원본과 비교대상의 문자열이 동일하다는 의미)
		System.out.println(s2.compareTo(s1));	// 2	(원본이 비교대상보다 사전적으로 2칸 뒤에 있다는 의미)

	}
}
