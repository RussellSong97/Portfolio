class FruitSeller {	 
	int numOfApple; 
	int myMoney;
	int APPLE_PRICE;	// 상수로 지정하면 오류 발생
	// 일반 메소드에서 상수에 값을 지정할 수는 없으므로 APPLE_PRICE를 상수로 선언할 수 없음
	// 일반 메소드는 다시 호출할 수 있으므로 상수의 값을 변경할까바 오류를 발생시킴
	
	public void initMember(int num, int money, int price) {
		numOfApple = num;
		myMoney = money;
		APPLE_PRICE = price;
	}

	public int saleApple(int money) { 
		int num = money / APPLE_PRICE; 
		myMoney += money; 
		numOfApple -= num; 

		return num;		 
	}
	public void showSaleResult() {
		System.out.println("남은 사과 : " + numOfApple);
		System.out.println("판매 수익 : " + myMoney);
	}
}

class FruitBuyer {	 
	int numOfApple = 0; 
	int myMoney = 10000; 

	public void buyApple(FruitSeller seller, int money) {
		numOfApple += seller.saleApple(money); 
		myMoney -= money;
	}
	public void showBuyResult() {
		System.out.println("사과 개수 : " + numOfApple);
		System.out.println("현재 잔액 : " + myMoney);
	}
}

class FruitSalesMain2 {
	public static void main(String[] args) {
		FruitSeller seller1 = new FruitSeller(); 
		// FruitSeller 형 인스턴스 seller1 을 선언 및 생성
		seller1.initMember(30, 0, 1500);
		// seller1 인스턴스 안의 initMember() 메소드를 실행

		FruitSeller seller2 = new FruitSeller(); 
		// FruitSeller 형 인스턴스 seller2 을 선언 및 생성
		seller2.initMember(20, 0, 1000);
		// seller2 인스턴스 안의 initMember() 메소드를 실행

		FruitBuyer buyer = new FruitBuyer();
		// FruitBuyer 형 인스턴스 buyer를 선언 및 생성
		buyer.buyApple(seller1, 4500); 
		buyer.buyApple(seller2, 2000); 
		// 각기 다른 판매자에게 가서 사과를 구매함

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
 - APPLE_PRICE가 상수가 아닌 변수로 지정됨
 - 인스턴스 생성 시 두 개의 명령이 필요
 - initMember() 메소드를 언제든지 호출할 수 있음(다시 호출되면 판매기록이 모두 리셋됨)

해결점
 - APPLE_PRICE를 상수로 지정
 - 인스턴스 생성시 하나의 명령으로 처리
 - 초기화 메소드는 한 번만 호출되게 함 - APPLE_PRICE를 상수로 지정할 수 있게 됨
 - 모든 해결책은 생성자를 사용하면 됨
*/