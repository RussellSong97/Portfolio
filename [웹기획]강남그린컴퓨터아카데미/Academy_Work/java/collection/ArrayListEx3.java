import java.util.*;

class ArrayListEx3 {
	public static void main(String[] args) {
		final int LIMIT = 10;
		String source = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()ZZZ";
		//String source = "0123456789abcdefghijABCDEFGHIJ!@#$%^&*()";
		int length = source.length();	// source ���ڿ��� ����

		/* Q. list�� source�� ���ڿ��� 10�� �� �߶� �����Ͻÿ� */

		int listSize	= length / LIMIT + 10;		// 10���� �߶������� LIST size 
		// ArrayList �ν��Ͻ� ����(ũ��� �˳��ϱ� ����ִ� ���� ����)

		int startIndex	= 0;						// �ڸ� ���� index ��
		int lastIndex	= 0;						// �ڸ� ���� index ��

		List list		= new ArrayList(listSize);	// size ��ŭ LIST ����
		// list�� List������ ����Ǿ��� ������ List�� �޼ҵ�� ArrayList���� �������̵��� �޼ҵ常 ����� �� ����
		// ArrayList�� �������� �ʰ� LIst�� �����ϴ� ������ List�� ��κ��� �޼ҵ尡 �ְ� �� �޼ҵ带 ArrayList���� 
		// �������̵� �س��� ������ ������� ���ϴ� �޼ҵ尡 ���� ��� ���ɻ��� ������ ����
		// List�� ���� �߱� ������ �ٸ� List��ü(LinkedList)���� ����ȯ�� �����ο�
 

		for (int i = 0; i < length ; i += LIMIT ) {
			// ���ڿ��� 10���� �������� ����
			if (i + LIMIT < length) {	// ���ڿ��� 10���� �ڸ��°� �����ϸ�
				list.add(source.substring(i , i + LIMIT));	
			} else {		// �ڸ� ���ڿ��� 10���ڰ� �ȵǸ�
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