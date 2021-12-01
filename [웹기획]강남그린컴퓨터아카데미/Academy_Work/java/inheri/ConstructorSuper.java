class Car {
	private int gasoline; 
	// 원래는 protected 로 써야 하지만 getter/setter 가 있으면 사용 가능

	public Car(int gasoline) { 
		this.gasoline = gasoline;
	}
	protected int getGasoline() { return gasoline; }
}

class HybridCar extends Car {
	private int electric;  

	public HybridCar(int gasoline, int electric) {
		super(gasoline);
		this.electric = electric;
	}
	protected int getElectric() { return electric; }
}

class HybridWaterCar extends HybridCar {
	private int water;

	public HybridWaterCar(int gasoline, int electric, int water) {
		super(gasoline, electric);
		this.water = water;
	}

	public void showCurrentGauge() { 
		System.out.println("잔여 가솔린 : " + getGasoline());
		System.out.println("잔여 전기량 : " + getElectric());
		System.out.println("잔여 워터량 : " + water);
	}
}

class ConstructorSuper {
	public static void main(String[] args) {
		HybridWaterCar car1 = new HybridWaterCar(4, 2, 3);
		car1.showCurrentGauge();

		System.out.println();

		HybridWaterCar car2 = new HybridWaterCar(9, 5, 7);
		car2.showCurrentGauge(); 
	}
}
