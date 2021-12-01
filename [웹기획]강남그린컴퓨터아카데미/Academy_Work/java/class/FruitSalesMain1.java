/*
사과판매 및 구매 프로그램 1
사과판매자 : FruitSeller
 - 사과보유개수, 매출액, 사과가격
 - 메소드 : 판매(), 현상황보기()

사과구매자 : FruitBuyer
 - 멤버변수 : 사과구매할 돈, 구매한 개수
 - 메소드 : 구매(), 현상황보기()

프로그램 제어 : FruitSalesMain1
 - FruitSeller와 FruitBuyer의 인스턴스를 생성하여 사과 판매 및 구매를 작동시킴
*/

class FruitSeller {		// 사과 판매자 클래스
	int numOfApple = 20;	// 사과보유개수
	int myMoney = 0;		// 매출액
	final int APPLE_PRICE = 1000;	// 사과가격
	
	public int saleApple(int money) {	// 사과 판매 메소드
		int num = money / APPLE_PRICE;	// 판매할 사과개수 (구매자가 준 돈만큼의 사과를 구함 )
		myMoney += money;		// 판매한 사과로 인한 매출액 증가 (구매자가 준 만큼 매출액 증가)
		numOfApple -= num;		// 판매한 사과로 인한 사과보유개수 감소 ()

		return num;				// 구매자에게 사과를 넘김
	}
	public void showSaleResult() {
		System.out.println("남은 사과 : " + numOfApple);
		System.out.println("판매 수익 : " + myMoney);
	}
}

class FruitBuyer {		// 사과 구매자
	int numOfApple = 0;	// 사과구매개수
	int myMoney = 5000;		// 구매액

	public void buyApple(FruitSeller seller, int money) {
		numOfApple += seller.saleApple(money);
		// 사과를 구매할 돈을 가지고 seller 인스턴스의 saleApple() 메소드를 호출
		myMoney -= money;
	}
	public void showBuyResult() {
		System.out.println("사과 개수 : " + numOfApple);
		System.out.println("현재 잔액 : " + myMoney);
	}
}

class FruitSalesMain1 {
	public static void main(String[] args) {
		FruitSeller seller = new FruitSeller();	// 사과판매자 객체 생성
		// FruitSeller 형 인스턴스 seller 를 선언 및 생성
		FruitBuyer buyer = new FruitBuyer();	// 사과구매자 객체 생성
		// FruitBuyer 형 인스턴스 buyer 를 선언 및 생성

		buyer.buyApple(seller, 2000);
		// buyer인스턴스 내의 buyApple() 메소드 실행
		// seller : 사과를 구매하려면 판매자가 필요하기 때문에 seller인스턴스를 인수로 가져감
		// 2000 : 사과 구매할 돈

		System.out.println("사과판매자 현재 상황");
		seller.showSaleResult();
		System.out.println("사과구매자 현재 상황");
		buyer.showBuyResult();
	}
}

/*
문제점
 - 판매자나 구매자 인스턴스를 여럿 생성해도 모두 동일한 값(멤버변수)을 가지고 있음

해결책
 - 인스턴스 별로 멤버변수의 값을 다르게 지정할 수 있어야 함
*/