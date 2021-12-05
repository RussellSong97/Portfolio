import java.util.*;

class StringTokenizerTest {
	public static void main(String[] args) {

		String str1 = "aa bb cc dd";
		StringTokenizer st = new StringTokenizer(str1);
		// str1의 문자열을 공백문자를 기준으로 잘라냄
		System.out.println("StringTokenizer 사용 - 1");

		while (st.hasMoreTokens()){						// st에 토큰이 더 있으면
			System.out.print(st.nextToken() + "  ");	// 토큰을 가져옴
			// st.nextToken() : st 인스턴스에서 다음 토큰을 리턴
			// aa bb cc dd
		}

		String str22 = "abc:def;ghi jk1-mon:pqr";
		StringTokenizer st2 = new StringTokenizer(str22);
			// str22의 문자열을 띄어쓰기 기준으로 자름
		while (st2.hasMoreTokens()) System.out.print(st2.nextToken() + "  ");
			// abc:def;ghi  jk1-mon:pqr
		System.out.println();
		
		StringTokenizer st3 = new StringTokenizer(str22, ":; -");
			// str22의 ":; -"을 띄어쓰기 기준으로 자름(각각이 구분자로 사용됨)
		while (st3.hasMoreTokens()) System.out.print(st3.nextToken() + "  ");
			// abc  def  ghi  jk1  mon  pqr
		System.out.println();
		
		StringTokenizer st4 = new StringTokenizer(str22, ":; -", true);
			// str22의 ":; -"을 띄어쓰기 기준으로 자름(각각이 구분자로 사용됨)
			// - 구분자로 사용한 문자로 토큰에 포함
		while (st4.hasMoreTokens()) System.out.print(st4.nextToken() + "  ");
			// abc  :  def  ;  ghi     jk1  -  mon  :  pqr
		System.out.println();


	}
}