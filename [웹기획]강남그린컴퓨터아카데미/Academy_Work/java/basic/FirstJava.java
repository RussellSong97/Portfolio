/*
자바 프로그램 실행 순서
C:\Users\Administrator>E:								// E드라이브로 이동
E:\>cd ksr\greenart_study\java\basic					// .java파일이 있는 곳으로 이동
E:\ksr\greenart_study\java\basic>javac FirstJava.java	// FirstJava.java 파일을 컴파일하여 .class파일 생성
E:\ksr\greenart_study\java\basic>java FirstJava			// 컴파일하여 생성한 클래스파일을 이용하여 실행시킴 
Java를 자바라
*/

class FirstJava {
	public static void main(String[] args) {
		System.out.println("java를 자바라!!");			// java를 자바라!!
		System.out.println("2 + 5 = " + 2 + 5);		// 2 + 5 = 25
		System.out.println("2 + 5 = " + (2 + 5));	// 2 + 5 = 7
		System.out.println( 2 + 5 + "2 + 5 ");		// 72 + 5
	}
}
