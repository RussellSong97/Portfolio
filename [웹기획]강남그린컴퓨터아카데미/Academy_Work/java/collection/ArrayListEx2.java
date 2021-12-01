import java.util.*;

class ArrayListEx2 {
	public static void main(String[] args) {
		final int LIMIT = 10;
		String source = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()ZZZ";
		//String source = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()";
		int length = source.length();	// source 문자열의 개수

		/* Q. list에 source의 문자열을 10개 씩 잘라 저장하시오 */

		int listSize	= length / LIMIT + 1;		// 10개씩 잘랐을때의 LIST size 
		int startIndex	= 0;						// 자를 시작 index 값
		int lastIndex	= 0;						// 자를 종료 index 값

		List list		= new ArrayList(listSize);	// size 만큼 LIST 생성
		
		for (int i = 0; i < listSize ; i++ ) {
			startIndex	= i * LIMIT;
			lastIndex	= (startIndex + LIMIT > length) ? length : startIndex + LIMIT;
			list.add(source.substring(startIndex , lastIndex));
 
		}
 
		System.out.println(list);	
		// [0123456789, abcdefghij, ABCDEFGHIJ, !@#$%^&*(), ZZZ]
	}
}