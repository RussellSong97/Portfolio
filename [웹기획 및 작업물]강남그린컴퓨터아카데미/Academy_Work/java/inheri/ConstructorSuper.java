class Car {
	private int gasoline; 
	// ������ protected �� ��� ������ getter/setter �� ������ ��� ����

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
		System.out.println("�ܿ� ���ָ� : " + getGasoline());
		System.out.println("�ܿ� ���ⷮ : " + getElectric());
		System.out.println("�ܿ� ���ͷ� : " + water);
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
