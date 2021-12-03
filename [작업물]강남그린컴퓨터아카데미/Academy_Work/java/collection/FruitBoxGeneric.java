class Orange {
	int sugar;
	public Orange(int s) { sugar = s; }
	public void showSugar() { System.out.println("당도 : " + sugar); }
}

class Apple {
	int weight;
	public Apple(int w) { weight = w; }
	public void showWeight() { System.out.println("무게 : " + weight); }

}

class FruitBox<T> {
// FruitBox<T> : FruitBox 클래스의 인스턴스 생성을 위해 선언할 때 <T> 에 해당하는 클래스를 입력하라는 의미
// <T>는 기본적으로 Object를 의미하며 어떠한 종류의 클래스를 넣어도 됨 - T 대신 들어갈 자료형을 지정하는 것
	T item;
	public void store(T item) { this.item = item; }
	public T pullOut() { return item; } 
}

class FruitBoxGeneric {
	public static void main(String[] args) {
		FruitBox<Orange> box1 = new FruitBox<Orange>();
		// FruitBox<Orange> : FruitBox의 인스턴스 생성시 <T> 대신에 Orange클래스를 넣어 반드시 Orange클래스에 해당하는
		// 데이터를 사용해야 한다는 계약조건 성립
		box1.store(new Orange(10));
		Orange org1 = box1.pullOut();
		// box1인스턴스 생성시 Orange로 지정했기 때문에 따로 형변환을 하지 않아도 무관
		org1.showSugar();
		
		FruitBox<Apple> box2 = new FruitBox<Apple>();
		// FruitBox의 인스턴스 생성시 <T> 대신에 Apple클래스를 넣어 Apple 인스턴스만 사용하겠다는 의미
		box2.store(new Apple(10));
		Apple app = box2.pullOut(); 
		app.showWeight();
		
		FruitBox box3 = new FruitBox();
		// FruitBox 의 인스턴스 생성시 제네릭으로 지정하지 않았으므로 <T>는 Object로 dlstlrehla
		box3.store(new Apple(30));
		Apple app2 = (Apple)box3.pullOut(); 
		app2.showWeight();
	}
} 