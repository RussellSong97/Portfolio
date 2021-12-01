/*
1. ��������� yyyy-mm-dd������ �Է¹޾� Calendar�� �ν��Ͻ��� ������ �� ���
2. Calendar�� �ν��Ͻ��� ������ �̿��Ͽ� LocalDate�� �ν��Ͻ��� ������ �� ���
3. �Է¹��� ������ �������� ���̸� ����Ͽ� ���
*/

import java.util.*;
import java.time.*;
import java.time.temporal.*;

class BirthTest {
	public static void main(String[] args) {
		// 1. ��������� yyyy-mm-dd������ �Է¹޾� 
		Scanner sc = new Scanner(System.in);
		System.out.print("��������� yyyy-mm-dd ���·� �Է����ּ��� : ");
		String birth = sc.nextLine();

		birth = birth.trim();						// �糡�� ���� ����
		String[] arrBirth = birth.split("-");		// '-' �������� �ڸ���
		int yyyy = Integer.parseInt(arrBirth[0]);
		int mm	 = Integer.parseInt(arrBirth[1]);
		int dd	 = Integer.parseInt(arrBirth[2]);

		// Calendar�� �ν��Ͻ��� ������ �� ���
		Calendar cDate = Calendar.getInstance();
		cDate.set(yyyy, mm - 1, dd); 
		System.out.println("Calendar  ������� : " + toString(cDate));

		// 2. Calendar�� �ν��Ͻ��� ������ �̿��Ͽ� LocalDate�� �ν��Ͻ��� ������ �� ���
		LocalDate ldDate = LocalDate.of(yyyy, mm, dd);
		System.out.println("LocalDate ������� : " + ldDate);

		// 3. �Է¹��� ������ �������� ���̸� ����Ͽ� ���
		LocalDate today = LocalDate.now();
		System.out.println("1. �Է¹��� ������ ���������� ���̴�? (�� year ����) : " + (today.getYear() - yyyy));

		Period age = Period.between(ldDate, today);
		System.out.println("2. �Է¹��� ������ ���������� ���̴�? (Period ver.) : " + (age.get(ChronoUnit.YEARS) + 1));
		
	}

	// ���� format ���� ���
	public static String toString(Calendar date) {
		int yyyy = date.get(Calendar.YEAR);
		int mm   = (date.get(Calendar.MONTH) + 1) ;
		int dd   = date.get(Calendar.DATE);

		return yyyy + "-" + ((mm > 9) ? mm : "0" + mm) + "-" + ((dd > 9) ? dd : "0" + dd);
	}
}
