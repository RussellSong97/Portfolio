class FinallyTest {
	public static void main(String[] args) {
		boolean divOK = divider(4, 2);
		if (divOK)	System.out.println("���� ����");
		else		System.out.println("���� ����");

		divOK = divider(4, 0);
		if (divOK)	System.out.println("���� ����");
		else		System.out.println("���� ����");
	} 


	
	public static boolean divider(int n1, int n2) { 
		try { 
			int tmp = n1 / n2;
			System.out.println("������ ��� : " + tmp);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			// ���� �߻����ο� ������� �׻� ����Ǵ� ����
			System.out.println("finally ���� ����");
		}
	}
}
