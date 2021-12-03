import java.util.*;

class ArrayListEx3 {
	public static void main(String[] args) {
		final int LIMIT = 10;
		String source = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()ZZZ";
		//String source = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()";
		int length = source.length();	// source 문자열의 개수

		/* Q. list에 source의 문자열을 10개 씩 잘라 저장하시오 */

		int listSize	= length / LIMIT + 10;		// 10개씩 잘랐을때의 LIST size 
		// ArrayList 인스턴스 생성(크기는 넉넉하기 잡아주는 것이 좋다)

		int startIndex	= 0;						// 자를 시작 index 값
		int lastIndex	= 0;						// 자를 종료 index 값

		List list		= new ArrayList(listSize);	// size 만큼 LIST 생성
		// list는 List형으로 선언되었기 때문에 List의 메소드와 ArrayList에서 오버라이딩한 메소드만 사용할 수 있음
		// ArrayList로 선언하지 않고 LIst로 선언하는 이유는 List에 대부분의 메소드가 있고 그 메소드를 ArrayList에서 
		// 오버라이딩 해놨기 때문에 사용하지 못하는 메소드가 거의 없어서 성능상의 문제는 없음
		// List로 선언 했기 때문에 다른 List객체(LinkedList)로의 형변환이 자유로움
 

		for (int i = 0; i < length ; i += LIMIT ) {
			// 문자열을 10글자 가능한지 여부
			if (i + LIMIT < length) {	// 문자열을 10글자 자르는게 가능하면
				list.add(source.substring(i , i + LIMIT));	
			} else {		// 자를 문자열이 10글자가 안되면
				list.add(source.substring(i));	
			}
		}
 
		System.out.println(list);	
 		for (int i = 0; i < list.size() ; i++ ) {
			System.out.println(list.get(i));	
		}
		// [0123456789, abcdefghij, ABCDEFGHIJ, !@#$%^&*(), ZZZ]
		// 0123456789
		// abcdefghij
		// ABCDEFGHIJ
		// !@#$%^&*()
		// ZZZ
	}
}