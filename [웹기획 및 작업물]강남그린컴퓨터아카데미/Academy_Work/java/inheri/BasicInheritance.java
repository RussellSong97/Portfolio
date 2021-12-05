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
		// 상위클래스인 Man의 생성자를 호출(반드시 하위 클래스 생성자의 첫줄에 있어야 함)
		// 생략시 JVM이 자동으로 super(); 를 실행함
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
		man1.tellYourName();	// 하위클래스의 인스턴스로 상위클래스의 메소드를 호출
		man1.tellYourInfo();

		System.out.println();
		
		System.out.println("2st man info...............");
		man2.tellYourInfo();
	}
}
