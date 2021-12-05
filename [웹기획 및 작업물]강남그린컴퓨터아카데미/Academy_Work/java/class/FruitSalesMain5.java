/* Seller(�Ǹ���) Ŭ���� */
class FruitSeller {	 
	private int numOfApple; 
	private int myMoney;
	private final int APPLE_PRICE;
	
	/* Seller(�Ǹ���) ������ */
	public FruitSeller(int num, int money, int price) {
		numOfApple	= num;
		myMoney		= money;
		APPLE_PRICE = price;
	}
	
	/* saleApple() �޼ҵ�
		: ��� �� ����, ��� ���� ���� ó�� �޼ҵ� */
	public int saleApple(int money) { 
		int num		= money / APPLE_PRICE; 
		myMoney		+= money; 
		numOfApple	-= num; 

		return num;		 
	}
	/* showSaleResult() �޼ҵ�
		: ������� ����, �Ǹż��� ��Ȳ ��� */
	public void showSaleResult() {
		System.out.println("���� ��� : " + numOfApple);
		System.out.println("�Ǹ� ���� : " + myMoney);
	}
}

/* Buyer(������) Ŭ���� */
class FruitBuyer {	 
	private int numOfApple; 
	private int myMoney; 
	
	/* Buyer(������) ������ */
	public FruitBuyer(int money) { 
		numOfApple	= 0;
		myMoney		= money; 
	}

	/* buyApple() �޼ҵ�
		: ���� �Ӵ� ����, ��� ���� ���� ���� ó�� �޼ҵ� */
	public void buyApple(FruitSeller seller, int money) {
		numOfApple	+= seller.saleApple(money); 
		myMoney		-= money;
	}
	/* showBuyResult() �޼ҵ�
		: ���Ż�� ����, �����ܾ� ��Ȳ ��� */
	public void showBuyResult() {
		System.out.println("��� ���� : " + numOfApple);
		System.out.println("���� �ܾ� : " + myMoney);
	}
}

/* MAIN */
class FruitSalesMain5 {
	public static void main(String[] args) {
		FruitSeller seller1 = new FruitSeller(30, 0, 1500);	// seller1 �ν��Ͻ� ����
		FruitSeller seller2 = new FruitSeller(20, 0, 1000); // seller2 �ν��Ͻ� ����
		FruitBuyer buyer = new FruitBuyer(10000);
 
		buyer.buyApple(seller1, 4500);;
		buyer.buyApple(seller2, 2000);;

		System.out.println("����Ǹ���1 ���� ��Ȳ");
		seller1.showSaleResult();

		System.out.println("����Ǹ���2 ���� ��Ȳ");
		seller2.showSaleResult();

		System.out.println("��������� ���� ��Ȳ");
		buyer.showBuyResult();
	}
} 

/*
������ 
 - �ν��Ͻ��� ��������� ���� ������ �� �־ ���� �����Ӱ� ������ �� ����

�ذ���
 - �ν��Ͻ��� ����������� ���������� ������ �� ���� �ؾ� ��(��������) 
*/