/* 문자열 "aa,bb,cc,dd"를 StringTokenizer와 String클래스의 split메소드를 이용하여 각각 출력 */
import java.util.*;

class SplitTokenizer {
	public static void main(String[] args) {
		String str = "aa,bb,cc,dd";

		StringTokenizer st = new StringTokenizer(str, ",");
		// StringTokenizer 구분자 1개만 사용 가능
		while (st.hasMoreTokens()) {
			System.out.print(st.nextToken() + " " );	// aa bb cc dd
		}	// StringTokenizer 는 한번 루프를 돌면 끝남 - 다시 사용하려면 다시 StringTokenizer 인스턴스로 생성해야 함

		System.out.println();

		String[] arr = str.split(",");
		for (int i = 0; i < arr.length ; i++ ) {
			System.out.print(arr[i] + " " );			// aa bb cc dd
		}
	}
}
