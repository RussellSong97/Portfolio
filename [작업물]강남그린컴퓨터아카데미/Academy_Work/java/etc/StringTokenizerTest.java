import java.util.*;

class StringTokenizerTest {
	public static void main(String[] args) {

		String str1 = "aa bb cc dd";
		StringTokenizer st = new StringTokenizer(str1);
		// str1�� ���ڿ��� ���鹮�ڸ� �������� �߶�
		System.out.println("StringTokenizer ��� - 1");

		while (st.hasMoreTokens()){						// st�� ��ū�� �� ������
			System.out.print(st.nextToken() + "  ");	// ��ū�� ������
			// st.nextToken() : st �ν��Ͻ����� ���� ��ū�� ����
			// aa bb cc dd
		}

		String str22 = "abc:def;ghi jk1-mon:pqr";
		StringTokenizer st2 = new StringTokenizer(str22);
			// str22�� ���ڿ��� ���� �������� �ڸ�
		while (st2.hasMoreTokens()) System.out.print(st2.nextToken() + "  ");
			// abc:def;ghi  jk1-mon:pqr
		System.out.println();
		
		StringTokenizer st3 = new StringTokenizer(str22, ":; -");
			// str22�� ":; -"�� ���� �������� �ڸ�(������ �����ڷ� ����)
		while (st3.hasMoreTokens()) System.out.print(st3.nextToken() + "  ");
			// abc  def  ghi  jk1  mon  pqr
		System.out.println();
		
		StringTokenizer st4 = new StringTokenizer(str22, ":; -", true);
			// str22�� ":; -"�� ���� �������� �ڸ�(������ �����ڷ� ����)
			// - �����ڷ� ����� ���ڷ� ��ū�� ����
		while (st4.hasMoreTokens()) System.out.print(st4.nextToken() + "  ");
			// abc  :  def  ;  ghi     jk1  -  mon  :  pqr
		System.out.println();


	}
}