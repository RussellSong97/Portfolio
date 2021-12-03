/* Seller(판매자) 클래스 */
class FruitSeller {	 
	int numOfApple; 
	int myMoney;
	final int APPLE_PRICE;
	
	/* Seller(판매자) 생성자 */
	public FruitSeller(int num, int money, int price) {
		numOfApple	= num;
		myMoney		= money;
		APPLE_PRICE = price;
	}
	
	/* saleApple() 메소드
		: 사과 값 증감, 사과 갯수 감소 처리 메소드 */
	public int saleApple(int money) { 
		int num		= money / APPLE_PRICE; 
		myMoney		+= money; 
		numOfApple	-= num; 

		return num;		 
	}
	/* showSaleResult() 메소드
		: 남은사과 개수, 판매수익 현황 출력 */
	public void showSaleResult() {
		System.out.println("남은 사과 : " + numOfApple);
		System.out.println("판매 수익 : " + myMoney);
	}
}

/* Buyer(구매자) 클래스 */
class FruitBuyer {	 
	int numOfApple; 
	int myMoney; 
	
	/* Buyer(구매자) 생성자 */
	public FruitBuyer(int money) { 
		numOfApple	= 0;
		myMoney		= money; 
	}

	/* buyApple() 메소드
		: 보유 머니 감소, 사과 보유 갯수 증감 처리 메소드 */
	public void buyApple(FruitSeller seller, int money) {
		numOfApple	+= seller.saleApple(money); 
		myMoney		-= money;
	}
	/* showBuyResult() 메소드
		: 구매사과 개수, 현재잔액 현황 출력 */
	public void showBuyResult() {
		System.out.println("사과 개수 : " + numOfApple);
		System.out.println("현재 잔액 : " + myMoney);
	}
}

/* MAIN */
class FruitSalesMain3 {
	public static void main(String[] args) {
		FruitSeller seller1 = new FruitSeller(30, 0, 1500);	// seller1 인스턴스 생성
		FruitSeller seller2 = new FruitSeller(20, 0, 1000); // seller2 인스턴스 생성

		FruitBuyer buyer = new FruitBuyer(10000);
		buyer.buyApple(seller1, 4500); 
		buyer.buyApple(seller2, 2000); 

		System.out.println("사과판매자1 현재 상황");
		seller1.showSaleResult();

		System.out.println("사과판매자2 현재 상황");
		seller2.showSaleResult();

		System.out.println("사과구매자 현재 상황");
		buyer.showBuyResult();
	}
} 

/*
문제점 
 - Main4

해결점
 - Main5
*/