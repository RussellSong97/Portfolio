/*
����Ǹ� �� ���� ���α׷� 1
����Ǹ��� : FruitSeller
 - �����������, �����, �������
 - �޼ҵ� : �Ǹ�(), ����Ȳ����()

��������� : FruitBuyer
 - ������� : ��������� ��, ������ ����
 - �޼ҵ� : ����(), ����Ȳ����()

���α׷� ���� : FruitSalesMain1
 - FruitSeller�� FruitBuyer�� �ν��Ͻ��� �����Ͽ� ��� �Ǹ� �� ���Ÿ� �۵���Ŵ
*/

class FruitSeller {		// ��� �Ǹ��� Ŭ����
	int numOfApple = 20;	// �����������
	int myMoney = 0;		// �����
	final int APPLE_PRICE = 1000;	// �������
	
	public int saleApple(int money) {	// ��� �Ǹ� �޼ҵ�
		int num = money / APPLE_PRICE;	// �Ǹ��� ������� (�����ڰ� �� ����ŭ�� ����� ���� )
		myMoney += money;		// �Ǹ��� ����� ���� ����� ���� (�����ڰ� �� ��ŭ ����� ����)
		numOfApple -= num;		// �Ǹ��� ����� ���� ����������� ���� ()

		return num;				// �����ڿ��� ����� �ѱ�
	}
	public void showSaleResult() {
		System.out.println("���� ��� : " + numOfApple);
		System.out.println("�Ǹ� ���� : " + myMoney);
	}
}

class FruitBuyer {		// ��� ������
	int numOfApple = 0;	// ������Ű���
	int myMoney = 5000;		// ���ž�

	public void buyApple(FruitSeller seller, int money) {
		numOfApple += seller.saleApple(money);
		// ����� ������ ���� ������ seller �ν��Ͻ��� saleApple() �޼ҵ带 ȣ��
		myMoney -= money;
	}
	public void showBuyResult() {
		System.out.println("��� ���� : " + numOfApple);
		System.out.println("���� �ܾ� : " + myMoney);
	}
}

class FruitSalesMain1 {
	public static void main(String[] args) {
		FruitSeller seller = new FruitSeller();	// ����Ǹ��� ��ü ����
		// FruitSeller �� �ν��Ͻ� seller �� ���� �� ����
		FruitBuyer buyer = new FruitBuyer();	// ��������� ��ü ����
		// FruitBuyer �� �ν��Ͻ� buyer �� ���� �� ����

		buyer.buyApple(seller, 2000);
		// buyer�ν��Ͻ� ���� buyApple() �޼ҵ� ����
		// seller : ����� �����Ϸ��� �Ǹ��ڰ� �ʿ��ϱ� ������ seller�ν��Ͻ��� �μ��� ������
		// 2000 : ��� ������ ��

		System.out.println("����Ǹ��� ���� ��Ȳ");
		seller.showSaleResult();
		System.out.println("��������� ���� ��Ȳ");
		buyer.showBuyResult();
	}
}

/*
������
 - �Ǹ��ڳ� ������ �ν��Ͻ��� ���� �����ص� ��� ������ ��(�������)�� ������ ����

�ذ�å
 - �ν��Ͻ� ���� ��������� ���� �ٸ��� ������ �� �־�� ��
*/