class PersonInfo {
	private int age;
	private char gender;
	private String name;

	public PersonInfo(int a, char g, String n) {
		age = a;	gender = g;		name = n;
	}

	// 멤버 변수의 값을 참조하기 위한 getter
	public int getAge() { return age; }
	public char getGender() { return gender; }
	public String getName() { return name; }

	// 멤버 변수의 값을 변경하기 위한 setter
	public void setAge(int a) {
		if (a < 0) {
			System.out.println("나이에 음수를 넣을 수 없습니다.");
			return;
		}
		age = a; 
	}
}

class PersonMain {
	public static void main(String[] args) 
	{
		PersonInfo p1 = new PersonInfo(15, 'f', "주리송");
		PersonInfo p2 = new PersonInfo(18, 'm', "상우김");

		System.out.println("나이 : " + p1.getAge() + ", 이름 : " + p1.getName());
		System.out.println("나이 : " + p2.getAge() + ", 이름 : " + p2.getName());

		p1.setAge(25);
		p2.setAge(-10);

		System.out.println("나이 : " + p1.getAge() + ", 이름 : " + p1.getName());
		System.out.println("나이 : " + p2.getAge() + ", 이름 : " + p2.getName());
	}
}
