class Man {
	private String name;
	public Man(String name) { 
		this.name = name; 
	}
	public void tellYourName() {
		System.out.println("My name is " + name);
	}
}

class BusinessMan extends Man {
	private String company, position;
	public BusinessMan(String name, String company, String position) {
		super(name);
		// ����Ŭ������ Man�� �����ڸ� ȣ��(�ݵ�� ���� Ŭ���� �������� ù�ٿ� �־�� ��)
		// ������ JVM�� �ڵ����� super(); �� ������
		this.company = company;
		this.position = position;
	}
	public void tellYourInfo() {
		System.out.println("My company is " + company);
		System.out.println("My position is " + position);
		tellYourName();
	}
}

class BasicInheritance {
	public static void main(String[] args) {
		BusinessMan man1 = new BusinessMan("Mr.Lee", "Samsung", "CEO");
		BusinessMan man2 = new BusinessMan("Mr.Kim", "Samsung", "CFO");

		System.out.println("1st man info...............");
		man1.tellYourName();	// ����Ŭ������ �ν��Ͻ��� ����Ŭ������ �޼ҵ带 ȣ��
		man1.tellYourInfo();

		System.out.println();
		
		System.out.println("2st man info...............");
		man2.tellYourInfo();
	}
}
