class NumberEx {
	public int num ;  // ���ǻ� public !!
	public NumberEx(int num) { this.num = num; }
	public int getnum() { return num; }

}
class ArrayForEach2 {
	public static void main(String[] args) {
		NumberEx[] arr = { new NumberEx(2), new NumberEx(4), new NumberEx(8) };
		// NumberEx�� �迭 arr�� �����ϰ� NumberEx�� �ν��Ͻ��� �ʱ�ȭ
		
		int[] arr2 = { 2, 4, 8 };

		int a = 2, b = 4, c = 8;
		int[] arr3 = { a, b, c };

		System.out.println("������");
		System.out.print("�ν��Ͻ� ��� : ");
		for ( NumberEx e : arr ) { System.out.print(e.getnum() + "  "); }	// �ν��Ͻ� ��� : 2  4  8
		System.out.print("\nint ��� : ");
		for ( int e : arr2 ) { System.out.print(e + "  "); }				// int ��� : 2  4  8
		System.out.print("\nint ���� ��� : ");
		for ( int e : arr3 ) { System.out.print(e + "  "); }				// int ���� ��� : 2  4  8

		for ( NumberEx e : arr ) { e.num++; }
		// arr�迭�� ����ִ� NumberEx�� �ν��Ͻ����� ���ʴ�� e�� ��Ƽ� �۾�
		// for-each�� �̿��Ͽ� �迭�� ���� �������� �� �� ���� �ν��Ͻ��̸� ���� ���� ����

		for ( int e : arr2 ) { e++; }
		// arr2�迭�� ����ִ� int�� �ν��Ͻ����� ���ʴ�� e�� ��Ƽ� �۾�
		// for-each�� �̿��Ͽ� �迭�� ���� �������� �� �� ���� �⺻ �ڷ����̸� ���� ���� ������ ����

		for ( int e : arr3 ) { e++; }
		// arr2�迭�� ����ִ� int�� �ν��Ͻ����� ���ʴ�� e�� ��Ƽ� �۾�
		// for-each�� �̿��Ͽ� �迭�� ���� �������� �� �� ���� �⺻ �ڷ����̸� ���� ���� ������ ����
		System.out.println();
 
		System.out.println("������");
		System.out.print("�ν��Ͻ� ��� : ");
		for ( NumberEx e : arr ) { System.out.print(e.getnum() + "  "); }	// �ν��Ͻ� ��� : 3  5  9
		System.out.print("\nint ��� : ");
		for ( int e : arr2 ) { System.out.print(e + "  "); }				// int ��� : 2  4  8
		System.out.print("\nint ���� ��� : ");
		for ( int e : arr3 ) { System.out.print(e + "  "); }				// int ���� ��� : 2  4  8

		System.out.print("\n�ν��Ͻ����� ��: ");
		for (NumberEx e : arr) { 
			e = new NumberEx(7); 
			// e�� ���ο� �ν��Ͻ��� �����ϸ� ������ ������ ���� ����
			System.out.print(e.getnum() + "  ");							// �ν��Ͻ����� ��: 7  7  7
		}
		System.out.print("\n�ν��Ͻ����� �� ���� :  ");
		for (NumberEx e : arr ) { System.out.print(e.getnum() + "  "); }	// �ν��Ͻ����� �� ���� :  3  5  9
	}
}