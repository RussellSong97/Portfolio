import java.util.*;

class ArrayListEx2 {
	public static void main(String[] args) {
		final int LIMIT = 10;
		String source = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()ZZZ";
		//String source = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()";
		int length = source.length();	// source ���ڿ��� ����

		/* Q. list�� source�� ���ڿ��� 10�� �� �߶� �����Ͻÿ� */

		int listSize	= length / LIMIT + 1;		// 10���� �߶������� LIST size 
		int startIndex	= 0;						// �ڸ� ���� index ��
		int lastIndex	= 0;						// �ڸ� ���� index ��

		List list		= new ArrayList(listSize);	// size ��ŭ LIST ����
		
		for (int i = 0; i < listSize ; i++ ) {
			startIndex	= i * LIMIT;
			lastIndex	= (startIndex + LIMIT > length) ? length : startIndex + LIMIT;
			list.add(source.substring(startIndex , lastIndex));
 
		}
 
		System.out.println(list);	
		// [0123456789, abcdefghij, ABCDEFGHIJ, !@#$%^&*(), ZZZ]
	}
}