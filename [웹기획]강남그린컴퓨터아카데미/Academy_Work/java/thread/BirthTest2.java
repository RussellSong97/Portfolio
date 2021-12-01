/*
1. ��������� yyyy-mm-dd������ �Է¹޾� Calendar�� �ν��Ͻ��� ������ �� ���
2. Calendar�� �ν��Ͻ��� ������ �̿��Ͽ� LocalDate�� �ν��Ͻ��� ������ �� ���
3. �Է¹��� ������ �������� ���̸� ����Ͽ� ���
*/

import java.util.*;				// scanner, calendar
import java.time.*;				// localdate
import java.time.temporal.*;

class BirthTest2 {
	public static void main(String[] args) {
		// 1. ��������� yyyy-mm-dd������ �Է¹޾� 
		Scanner sc = new Scanner(System.in);
		System.out.print("��������� yyyy-mm-dd ���·� �Է����ּ��� : ");
		String birth = sc.nextLine();

		birth = birth.trim();						// �糡�� ���� ����
		String[] arrBirth = birth.split("-");		// �Է¹��� ��¥ ���ڿ��� �����Ϸ� �ɰ��� �迭�� ����
		int yyyy = Integer.parseInt(arrBirth[0]);
		int mm	 = Integer.parseInt(arrBirth[1]);
		int dd	 = Integer.parseInt(arrBirth[2]);
		// Calendar�� set()�� LocalDate�� of() �޼ҵ忡�� �μ��� ����ϱ� ���� int������ ����ȯ

		// Calendar�� �ν��Ͻ��� ������ �� ���
		Calendar cDate = Calendar.getInstance();	// Calendar ��ü�� ����� ���� �ν��Ͻ�
		int cYear = cDate.get(Calendar.YEAR);		// ���̸� ���ϱ� ���� ���� ����
		cDate.set(yyyy, mm - 1, dd);				// ����ڰ� �Է��� ���� �̿��Ͽ� ���� Calendar �� �ν��Ͻ� ����
		System.out.println("Calendar  ������� : " + toString(cDate));

		// 2. Calendar�� �ν��Ͻ��� ������ �̿��Ͽ� LocalDate�� �ν��Ͻ��� ������ �� ���
		LocalDate ldDate = LocalDate.of(yyyy, mm, dd);	// ����ڰ� �Է��� ���� �̿��Ͽ� ���� LocalDate �� �ν��Ͻ� ����
		System.out.println("LocalDate ������� : " + ldDate);

		// 3. �Է¹��� ������ �������� ���̸� ����Ͽ� ���
		System.out.println("1. �Է¹��� ������ ���������� ���̴�? (�� year ����) : " + (cYear - yyyy) + "��");

	}

	// ���� format ���� ���
	public static String toString(Calendar date) {
		int yyyy = date.get(Calendar.YEAR);
		int mm   = (date.get(Calendar.MONTH) + 1) ;
		int dd   = date.get(Calendar.DATE);

		return yyyy + "-" + ((mm > 9) ? mm : "0" + mm) + "-" + ((dd > 9) ? dd : "0" + dd);
	}
}
